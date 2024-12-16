package cn.edu.xmu.javaee.core.aop;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

import javax.annotation.processing.SupportedAnnotationTypes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huang zhong
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes({"cn.edu.xmu.javaee.core.aop.CopyFrom","cn.edu.xmu.javaee.core.aop.CopyFromOf","cn.edu.xmu.javaee.core.aop.CopyFromExclude"})
// 声明一个注解处理器，支持CopyFrom、CopyFromOf、CopyFromExclude注解
public class CopyFromProcessor extends AbstractProcessor {
    private Messager messager;
    private Set<String> daoPaths = new HashSet<>();

    @Override
    // 返回支持的Java源代码版本
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    // 初始化处理环境
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
    }


    @Override
    // 处理注解
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (annotations.isEmpty()) {
            return false;
        }
        TypeSpec.Builder typeSpecBuilder = TypeSpec.classBuilder("CloneFactory")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        messager.printMessage(Diagnostic.Kind.NOTE, "CopyFromProcessor start");
        Set<TypeElement> elementsToProcess = new HashSet<>();
        //0.找出所有Dao层路径
        roundEnv.getRootElements().stream()
                .map(element -> processingEnv.getElementUtils().getPackageOf(element).getQualifiedName().toString())
                .filter(pkg -> pkg.contains(".dao."))
                .forEach(daoPaths::add);

        // 1. 添加所有带@CopyFrom注解的类
        Set<TypeElement> annotatedElements = roundEnv.getElementsAnnotatedWith(CopyFrom.class).stream()
                .filter(element -> element.getKind() == ElementKind.CLASS)
                .map(element -> (TypeElement)element)
                .collect(Collectors.toSet());
        elementsToProcess.addAll(annotatedElements);

        // 2. 添加所有未被@CopyFrom注解且以Po结尾的类
        roundEnv.getRootElements().stream()
                .filter(element -> element.getKind() == ElementKind.CLASS)
                .map(element -> (TypeElement)element)
                .filter(element -> element.getSimpleName().toString().endsWith("Po"))
                .filter(element -> element.getAnnotation(CopyFrom.class) == null)
                .forEach(elementsToProcess::add);



        elementsToProcess.forEach(element -> {
            messager.printMessage(Diagnostic.Kind.NOTE, new StringBuilder("Processing: ").append(element.getSimpleName()).toString());

            //获取Target类所有可以Set的字段
            List<Element> copyableFields = getAllSetableFields(element);
            List<DeclaredType> sourceClasses;
            if (element.getAnnotation(CopyFrom.class) != null) {
                // 使用注解中指定的源类
                sourceClasses = getSourceClass(element, "value", CopyFrom.class);
            } else {
                // 对于Po类，假设有@CopyFrom(xxx.class)注解
                TypeElement sourceElement = findSourceClassForPo(element);
                if (sourceElement != null) {
                    sourceClasses = List.of((DeclaredType) sourceElement.asType());
                } else {
                    messager.printMessage(Diagnostic.Kind.WARNING,
                            new StringBuilder("Cannot find source class for ") .append(element.getSimpleName()).toString());
                    sourceClasses = List.of();
                }
            }

            sourceClasses.forEach(sourceClass -> {
                //获取源类所有可以Get的字段
                List<Element> sourceFields = getAllGetableFields((TypeElement)sourceClass.asElement());
                messager.printMessage(Diagnostic.Kind.NOTE,
                        new StringBuilder("Source fields: ")
                                .append(sourceFields)
                                .append(sourceClass)
                                .toString());
                messager.printMessage(Diagnostic.Kind.NOTE,
                        new StringBuilder("Source element: ")
                                .append(element)
                                .toString());

                // 创建一个名为copy的方法
                MethodSpec.Builder copyMethodBuilder = MethodSpec.methodBuilder("copy")
                        .addJavadoc("Copy all fields from source to target\n")
                        .addJavadoc("@param target the target object\n")
                        .addJavadoc("@param source the source object\n")
                        .addJavadoc("@return the copied target object\n")
                        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                        .returns(TypeName.get(element.asType()))
                        .addParameter(TypeName.get(element.asType()), "target")
                        .addParameter(TypeName.get(sourceClass), "source");

                // 遍历目标类的可复制字段
                copyableFields.stream()
                        .filter(field -> {
                            String fieldName = field.getSimpleName().toString();
                            // 检查源类是否有对应的字段
                            boolean hasMatchingField = sourceFields.stream()
                                    .anyMatch(sourceField ->
                                            sourceField.getSimpleName().toString().equals(fieldName));
                            // 检查是否应该被排除
                            return hasMatchingField && !shouldExcludeField(field, sourceClass);
                        })
                        .forEach(field -> {
                            String fieldName = field.getSimpleName().toString();
                            String setterName = getSetterName(fieldName);
                            String getterName = getGetterName(fieldName);
                            copyMethodBuilder.addStatement("target.$L(source.$L())",
                                    setterName, getterName);
                        });

                // 返回目标对象
                copyMethodBuilder.addStatement("return target");

                // 将copy方法添加到类中
                typeSpecBuilder.addMethod(copyMethodBuilder.build());
            });
        });

        // 创建Java文件
        JavaFile javaFile = JavaFile.builder("cn.edu.xmu.javaee.core.util", typeSpecBuilder.build()).build();

        // 将Java文件写入到文件中
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (Exception e) {
            // 如果出现异常，打印错误信息
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
        }

        return true;
    }


    // 获取类的所有方法
    private List<ExecutableElement> getAllMethods(TypeElement type) {
        List<ExecutableElement> methods = new ArrayList<>();
        // 获取当前类的所有方法
        methods.addAll(ElementFilter.methodsIn(type.getEnclosedElements()));
        // 获取父类的类型
        TypeElement superClass = (TypeElement) ((DeclaredType) type.getSuperclass()).asElement();
        // 递归获取所有父类的方法，直到到达Object类
        while (superClass != null && !superClass.getQualifiedName().toString().equals("java.lang.Object")) {
            methods.addAll(ElementFilter.methodsIn(superClass.getEnclosedElements()));
            TypeMirror superClassType = superClass.getSuperclass();
            if (superClassType.getKind() == TypeKind.DECLARED) {
                superClass = (TypeElement) ((DeclaredType) superClassType).asElement();
            } else {
                break;
            }
        }
        return methods;
    }

    // 获取元素的注解
    private Optional<AnnotationMirror> getAnnotationMirror(Element element, Class<?> clazz) {
        String clazzName = TypeName.get(clazz).toString();
        for(AnnotationMirror m : element.getAnnotationMirrors()) {
            if(m.getAnnotationType().toString().equals(clazzName)) {
                return Optional.ofNullable(m);
            }
        }
        return Optional.empty();
    }

    // 获取注解的值
    private Optional<AnnotationValue> getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        for(Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet() ) {
            if(entry.getKey().getSimpleName().toString().equals(key)) {
                messager.printMessage(Diagnostic.Kind.NOTE, String.format("Entry: %s, value: %s", entry.getKey().getSimpleName().toString(), entry.getValue().toString()));
                return Optional.ofNullable(entry.getValue());
            }
        }
        return Optional.empty();
    }

    // 获取注解的??，并转换为类型列表
    private List<DeclaredType> getSourceClass(Element clazz, String key,Class clas) {
        return getAnnotationMirror(clazz, clas)
                .flatMap(annotation -> getAnnotationValue(annotation, key))
                // ^ note that annotation value here corresponds to Class[],
                .map(annotation -> (List<AnnotationValue>)annotation.getValue())
                .map(fromClasses -> fromClasses.stream()
                        .map(fromClass -> (DeclaredType)fromClass.getValue())
                        .collect(Collectors.toList()))
                .orElse(List.of());
    }

    /**
     * 检查元素（字段或类）是否有Lombok的Getter/Data注解
     * 支持@Getter, @Data, @Value注解
     */
    private boolean hasGetter(Element element) {
        return element.getAnnotationMirrors().stream()
                .anyMatch(am -> {
                    String annotationName = am.getAnnotationType().toString();
                    return annotationName.equals("lombok.Getter") ||
                            annotationName.equals("lombok.Data") ||
                            annotationName.equals("lombok.Value");
                });
    }

    /**
     * 检查元素（字段或类）是否有Lombok的Setter/Data注解
     * 支持@Setter, @Data注解
     */
    private boolean hasSetter(Element element) {
        return element.getAnnotationMirrors().stream()
                .anyMatch(am -> {
                    String annotationName = am.getAnnotationType().toString();
                    return annotationName.equals("lombok.Setter") ||
                            annotationName.equals("lombok.Data");
                });
    }

    /**
     * 根据字段名生成标准的getter方法名
     * 例如：name -> getName, age -> getAge
     */
    private String getGetterName(String fieldName) {
        StringBuilder getterName=new StringBuilder();
        return  getterName.append("get").append(fieldName.substring(0,1).toUpperCase()).append(fieldName.substring(1)).toString();
    }

    /**
     * 根据字段名生成标准的setter方法名
     * 例如：name -> setName, age -> setAge
     */
    private String getSetterName(String fieldName) {
        StringBuilder setterName=new StringBuilder();
        return  setterName.append("set").append(fieldName.substring(0,1).toUpperCase()).append(fieldName.substring(1)).toString();

    }

    /**
     * 获取所有该类字段(包括父类字段)
     */
    private List<Element> getAllFields(TypeElement type) {
        List<Element> allFields=new ArrayList<>();
        // 获取当前类的所有字段（包括父类字段）
        TypeElement currentType = type;
        while (currentType != null && !currentType.getQualifiedName().toString().equals("java.lang.Object")) {
            allFields.addAll(ElementFilter.fieldsIn(currentType.getEnclosedElements()));
            TypeMirror superclass = currentType.getSuperclass();
            if (superclass.getKind() == TypeKind.DECLARED) {
                currentType = (TypeElement) ((DeclaredType) superclass).asElement();
            } else {
                currentType = null;
            }
        }
        return allFields;
    }

    /**
     * 获取所有可以Get的字段(包括父类字段)
     */
    private List<Element> getAllGetableFields(TypeElement type) {
        // 获取所有字段和方法
        List<Element> allFields = getAllFields(type);
        List<ExecutableElement> methods = getAllMethods(type);

        // 使用流处理字段，过滤掉static和final字段，并构建recordFields映射
        Map<Element, Boolean> recordFields = allFields.stream()
                .filter(field -> !(field.getKind() == ElementKind.FIELD &&
                        (field.getModifiers().contains(Modifier.STATIC) ||
                                field.getModifiers().contains(Modifier.FINAL))))
                .collect(Collectors.toMap(
                        field -> field,
                        field -> {
                            String getterName = getGetterName(field.getSimpleName().toString());
                            return hasGetter(field) || methods.stream()
                                    .anyMatch(method -> method.getSimpleName().toString().equals(getterName));
                        }
                ));

        // 获取类的层次结构
        List<TypeElement> hierarchy = Stream.iterate(type,
                        currentType -> {
                            TypeMirror superclass = currentType.getSuperclass();
                            return superclass.getKind() == TypeKind.DECLARED ?
                                    (TypeElement) ((DeclaredType) superclass).asElement() : null;
                        })
                .takeWhile(currentType -> currentType != null &&
                        !currentType.getQualifiedName().toString().equals("java.lang.Object"))
                .collect(Collectors.toList());
        Collections.reverse(hierarchy);

        // 处理类级别的Lombok注解
        hierarchy.stream()
                .filter(this::hasGetter)
                .flatMap(temp -> getClassFields(temp).stream())
                .forEach(classField -> {
                    if (recordFields.containsKey(classField)) {
                        recordFields.put(classField, true);
                    }
                });

        // 返回所有可获取的字段
        return recordFields.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有获取的字段
     * 包括：
     * 1. 有getter方法的字段
     * 2. 有Lombok注解的字段
     * 3. 父类中符合条件的字段
     */
    private List<Element> getAllSetableFields(TypeElement type) {
        // 获取所有字段和方法
        List<Element> allFields = getAllFields(type);
        List<ExecutableElement> methods = getAllMethods(type);

        // 使用流处理字段，过滤掉static和final字段，并构建recordFields映射
        Map<Element, Boolean> recordFields = allFields.stream()
                .filter(field -> !(field.getKind() == ElementKind.FIELD &&
                        (field.getModifiers().contains(Modifier.STATIC) ||
                                field.getModifiers().contains(Modifier.FINAL))))
                .collect(Collectors.toMap(
                        field -> field,
                        field -> {
                            String setterName = getSetterName(field.getSimpleName().toString());
                            return hasSetter(field) || methods.stream()
                                    .anyMatch(method -> method.getSimpleName().toString().equals(setterName));
                        }
                ));

        // 获取类的层次结构
        List<TypeElement> hierarchy = Stream.iterate(type,
                        currentType -> {
                            TypeMirror superclass = currentType.getSuperclass();
                            return superclass.getKind() == TypeKind.DECLARED ?
                                    (TypeElement) ((DeclaredType) superclass).asElement() : null;
                        })
                .takeWhile(currentType -> currentType != null &&
                        !currentType.getQualifiedName().toString().equals("java.lang.Object"))
                .collect(Collectors.toList());
        Collections.reverse(hierarchy);

        // 处理类级别的Lombok注解
        hierarchy.stream()
                .filter(this::hasSetter)
                .flatMap(temp -> getClassFields(temp).stream())
                .forEach(classField -> {
                    if (recordFields.containsKey(classField)) {
                        recordFields.put(classField, true);
                    }
                });

        // 返回所有可设置的字段
        return recordFields.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    /**
     * 检查字段是否应该被排除
     * 处理@CopyFrom.Exclude和@CopyFrom.Of注解的逻辑
     */
    private boolean shouldExcludeField(Element field, DeclaredType sourceClass) {
        // 获取字段上的注解
        List<DeclaredType> fieldExcludeList = getSourceClass(field, "value", CopyFrom.Exclude.class);
        List<DeclaredType> fieldOfList = getSourceClass(field, "value", CopyFrom.Of.class);

        // 获取对应getter/setter方法
        String fieldName = field.getSimpleName().toString();
        String getterName = getGetterName(fieldName);
        String setterName = getSetterName(fieldName);

        // 在当前类和父类中查找对应的getter/setter方法
        TypeElement classElement = (TypeElement) field.getEnclosingElement();
        List<ExecutableElement> methods = getAllMethods(classElement);

        // 查找getter和setter方法上的注解
        List<DeclaredType> methodExcludeList = methods.stream()
                .filter(method -> method.getSimpleName().toString().equals(getterName) ||
                        method.getSimpleName().toString().equals(setterName))
                .flatMap(method -> getSourceClass(method, "value", CopyFrom.Exclude.class).stream())
                .collect(Collectors.toList());

        List<DeclaredType> methodOfList = methods.stream()
                .filter(method -> method.getSimpleName().toString().equals(getterName) ||
                        method.getSimpleName().toString().equals(setterName))
                .flatMap(method -> getSourceClass(method, "value", CopyFrom.Of.class).stream())
                .collect(Collectors.toList());

        // 合并字段和方法上的注解列表
        List<DeclaredType> excludeList = Stream.concat(fieldExcludeList.stream(), methodExcludeList.stream())
                .distinct()
                .collect(Collectors.toList());
        List<DeclaredType> ofList = Stream.concat(fieldOfList.stream(), methodOfList.stream())
                .distinct()
                .collect(Collectors.toList());

        // 检查是否应该排除
        if (!excludeList.isEmpty() && !ofList.isEmpty()) {
            return (excludeList.stream().anyMatch(hasClass ->
                    TypeName.get(hasClass).toString().equals(TypeName.get(sourceClass).toString()))) ||
                    ofList.stream().noneMatch(hasClass ->
                            TypeName.get(hasClass).toString().equals(TypeName.get(sourceClass).toString()));
        }
        else if (excludeList.isEmpty() && !ofList.isEmpty()) {
            return ofList.stream().noneMatch(hasClass ->
                    TypeName.get(hasClass).toString().equals(TypeName.get(sourceClass).toString()));
        }
        else if (!excludeList.isEmpty() && ofList.isEmpty()) {
            return excludeList.stream().anyMatch(hasClass ->
                    TypeName.get(hasClass).toString().equals(TypeName.get(sourceClass).toString()));
        }
        return false;
    }


    /**
     * 获取所有该类定义的字段
     */
    private List<Element>getClassFields(TypeElement type) {
        List<Element> allFields=new ArrayList<>();
        allFields.addAll(type.getEnclosedElements());
        return allFields;
    }



    // 查找Po类对应的源类
    private TypeElement findSourceClassForPo(TypeElement poElement) {
        String poClassName = poElement.getSimpleName().toString();
        String baseClassName = poClassName.substring(0, poClassName.length() - 2);
        String packageName = processingEnv.getElementUtils().getPackageOf(poElement).getQualifiedName().toString();
        // 获取mapper的上一层目录
        int lastDot = packageName.lastIndexOf("mapper");
        if (lastDot == -1) {
            messager.printMessage(Diagnostic.Kind.WARNING,
                    new StringBuilder("Not in a mapper package: ").append(packageName).toString());
            return null;
        }
        String basePackage = packageName.substring(0, lastDot);
        // 在所有dao路径下查找
        for (String daoPath : daoPaths) {
            System.out.println(daoPath);
            if (daoPath.startsWith(basePackage)) {
                String location = new StringBuilder(daoPath)
                        .append(".")
                        .append(baseClassName)
                        .toString();
                TypeElement sourceElement = processingEnv.getElementUtils().getTypeElement(location);
                if (sourceElement != null) {
                    messager.printMessage(Diagnostic.Kind.NOTE,
                            new StringBuilder("Found source class ")
                                    .append(location)
                                    .append(" for ")
                                    .append(poClassName)
                                    .toString());
                    return sourceElement;
                }
            }
        }
        messager.printMessage(Diagnostic.Kind.WARNING,
                new StringBuilder("Cannot find source class for ")
                        .append(poClassName)
                        .toString());
        return null;
    }
}
