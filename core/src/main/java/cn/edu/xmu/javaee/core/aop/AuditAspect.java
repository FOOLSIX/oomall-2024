//School of Informatics Xiamen University, GPL-3.0 license
package cn.edu.xmu.javaee.core.aop;

import cn.edu.xmu.javaee.core.exception.BusinessException;
import cn.edu.xmu.javaee.core.model.dto.UserDto;
import cn.edu.xmu.javaee.core.model.UserToken;
import cn.edu.xmu.javaee.core.util.JwtHelper;
import cn.edu.xmu.javaee.core.model.ReturnNo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @auther mingqiu
 * @date 2020/6/26 下午2:16
 *      modifiedBy Ming Qiu 2020/11/3 22:59
 *
 */
@Aspect
@Component
@Order(20)
@Slf4j
public class AuditAspect {

    /**
     * @Audit的Around Advice
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("cn.edu.xmu.javaee.core.aop.CommonPointCuts.auditAnnotation()")
    public Object aroundAudit(JoinPoint joinPoint) throws  Throwable{

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        Method method = ms.getMethod();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        String token = request.getHeader(JwtHelper.LOGIN_TOKEN_KEY);

        UserToken decryptToken = decryptToken(token);

        checkDepartId(request, method, decryptToken);

        Object[] args = joinPoint.getArgs();
        Annotation[][] annotations = method.getParameterAnnotations();
        putMethodParameter(decryptToken, args, annotations);

        Object obj = null;
        try {
            obj = ((ProceedingJoinPoint) joinPoint).proceed(args);
        } catch (Throwable e) {
            throw e;
        }
        return obj;
    }

    /**
     * 检查路径上的departId是否与token里一致
     * @param request
     * @param method
     * @param decryptToken
     * @throws BusinessException
     */
    private void checkDepartId(HttpServletRequest request, Method method, UserToken decryptToken) throws BusinessException{
        //检验/shop的api中传入token是否和departId一致
        String pathInfo = request.getRequestURI();
        log.debug("checkDepartId : the api path is {}", pathInfo);
        if(Objects.isNull(pathInfo)) {
            log.info("checkDepartId : the api path is null");
            throw new BusinessException(ReturnNo.RESOURCE_ID_NOTEXIST);
        }

        String departName=null;
        Audit auditAnnotation = method.getAnnotation(Audit.class);
        if(Objects.nonNull(auditAnnotation)){
            departName = ((Audit) auditAnnotation).departName();
        }

        boolean flag=false;
        if(!"".equals(departName)) {
            log.debug("checkDepartId: getPathInfo = {}", pathInfo);
            String paths[] = pathInfo.split("/");
            for (int i = 0; i < paths.length; i++) {
                //如果departId为0,可以操作所有的depart
                if (0 == decryptToken.getDepartId()) {
                    flag = true;
                    break;
                }
                if (paths[i].equals(departName)) {
                    if (i + 1 < paths.length) {
                        //找到路径上对应id 将其与string类型的departId比较
                        String pathId = paths[i + 1];
                        log.debug("checkDepartId : did = {}" , pathId);
                        if (!pathId.equals(decryptToken.getDepartId().toString())) {
                            log.info("checkDepartId : 不匹配departId = {}", decryptToken.getDepartId());
                            throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE);
                        } else {
                            flag = true;
                            log.debug("checkDepartId : success match Id!");
                        }
                    }
                    else {
                        flag = true;//这是没有departId的情况
                        break;
                    }
                }
            }
            if (!flag) {
                log.info("checkDepartId : 不匹配departId = {}", decryptToken.getDepartId());
                throw new BusinessException(ReturnNo.RESOURCE_ID_OUTSCOPE);
            }
        }
        else {
            decryptToken.setDepartId(null);
        }
    }

    /**
     * 将token的值设置到jp的参数上
     * @param token
     * @param args
     * @param annotations
     */
    private void putMethodParameter(UserToken token, Object[] args, Annotation[][] annotations) {
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] paramAnn = annotations[i];
            if (paramAnn.length == 0){
                continue;
            }

            for (Annotation annotation : paramAnn) {
                //这里判断当前注解是否为LoginUser.class
                if (annotation.annotationType().equals(LoginUser.class)) {
                    //校验该参数，验证一次退出该注解
                    UserDto user = new UserDto();
                    user.setName(token.getUserName());
                    user.setId(token.getUserId());
                    user.setUserLevel(token.getUserLevel());
                    user.setDepartId(token.getDepartId());
                    args[i] = user;
                    log.debug("putMethodParameter: token = {}, user = {}",token,user);
                } else if (annotation.annotationType().equals(UserLevel.class)) {
                    args[i] = token.getUserLevel();
                }
            }
        }
    }

    /**
     * 解密token
     * @param tokenString token字符串
     * @return 解密的JWTHelper.Token对象
     * @throws BusinessException
     */
    private UserToken decryptToken(String tokenString)  throws BusinessException{

        if (Objects.isNull(tokenString)){
            log.debug("decryptToken : no token..");
            throw new BusinessException(ReturnNo.AUTH_NEED_LOGIN);
        }

        UserToken token = new JwtHelper().verifyTokenAndGetClaims(tokenString);

        if (Objects.isNull(token)) {
            log.debug("decryptToken : invalid token..");
            throw new BusinessException(ReturnNo.AUTH_INVALID_JWT);
        }

        if (Objects.isNull(token.getUserId())) {
            log.debug("decryptToken : userId is null");
            throw new BusinessException(ReturnNo.AUTH_NEED_LOGIN);
        }
        log.debug("decryptToken : token = {}", token);
        return  token;
    }
}
