package cn.edu.xmu.oomall.jtexpress.util;

import jakarta.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * @author 徐森彬
 * 2023-dgn3-02
 */
public class BeanValidator {

    private static final Logger logger = LoggerFactory.getLogger(BeanValidator.class);

    /**
     * 验证某个 Bean 对象的参数
     *
     * @param object 被校验的对象
     * @param <T>    对象的类型
     * @throws ValidationException 如果参数校验不成功则抛出此异常
     */
    public static <T> void validate(T object) throws ValidationException {
        // 获取验证器
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        // 执行验证
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);

        // 如果有验证信息，则将第一个取出来包装成异常返回
        ConstraintViolation<T> constraintViolation = getFirst(constraintViolations);
        if (constraintViolation != null) {
            logger.debug("ERROR:{}类中{} {}", constraintViolation.getRootBeanClass().getName(), constraintViolation.getPropertyPath(), constraintViolation.getMessage());
            throw new ValidationException(constraintViolation.getMessage());
        }
    }

    // 获取集合中的第一个元素，如果集合为空则返回默认值
    private static <T> T getFirst(Set<T> set) {
        return set.isEmpty() ? null : set.iterator().next();
    }
}