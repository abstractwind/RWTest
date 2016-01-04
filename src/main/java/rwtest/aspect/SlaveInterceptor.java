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
public class SlaveInterceptor {

    @Pointcut(value="execution(public * *(..))")
    public void anyPublicMethod() { }

	@Around("@annotation(slave)")
	public Object proceed(ProceedingJoinPoint pjp,
			Slave slave) throws Throwable {

		try {
            DbContextHolder.setDbType(DbType.SLAVE);
            Object result = pjp.proceed();
            DbContextHolder.clearDbType();
			return result;
		} finally {
            DbContextHolder.clearDbType();
		}
	}
}
