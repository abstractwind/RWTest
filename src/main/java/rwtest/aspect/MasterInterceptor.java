package rwtest.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import rwtest.routing.DbContextHolder;
import rwtest.routing.DbType;

@Aspect
@Order(20)
@Component
public class MasterInterceptor{

    @Pointcut(value="execution(public * * (..))")
    public void anyPublicMethod() { }

	@Around("@annotation(master)")
	public Object proceed(ProceedingJoinPoint pjp,
			Master master) throws Throwable {
		try {
            DbContextHolder.setDbType(DbType.MASTER);
            Object result = pjp.proceed();
            DbContextHolder.clearDbType();
			return result;
		} finally {
            DbContextHolder.clearDbType();
		}
	}
}
