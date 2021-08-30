package com.yf.aspect;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect    // 进行切面操作
@Component // 主键扫描
public class LogAspect {

    // 日志记录器
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 拦截任何的方法(public private protect) com.yf.web这个包下的所有的类中的所有方法
    @Pointcut("execution(* com.yf.web.*.*(..))")    // 注解这是一个切面,execution的内容用于拦截该内容的类
    public void log() {}

    @Before("log()")    // 该方法会在切面之前执行
    public void doBefore(JoinPoint joinPoint)
    {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName()+"."+ joinPoint.getSignature().getName(); // 请求的方法名
        Object[] args = joinPoint.getArgs();

        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("Request: {}", requestLog);

    }

    @After("log()")     // 该方法会在切面之后执行
    public void doAfter()
    {
        logger.info("-----doAfter------");
    }

    @AfterReturning(returning = "result", pointcut = "log()")
    public void doAfterReturn(Object result)
    {
        logger.info("Result : {}" , result);
    }

    private static class RequestLog
    {
        private final String url;
        private final String ip;
        private final String classMethod;
        private final Object[] args;

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return "{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }
}
