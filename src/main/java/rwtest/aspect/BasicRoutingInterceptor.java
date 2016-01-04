package rwtest.aspect;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import rwtest.routing.DbContextHolder;
import rwtest.routing.DbType;

@Aspect
@Order(20)
@Component
public class BasicRoutingInterceptor{
	
	@Pointcut(value="execution(* rwtest.mapper.*.*(..))")
    public void anyMapperMethod() { }
	
	@Around("anyMapperMethod()")
	public Object proceed(ProceedingJoinPoint pjp) throws Throwable {
		MethodSignature sign = (MethodSignature) pjp.getSignature();
		try {
			if(DbContextHolder.getDbType() != null){
				return pjp.proceed();
			}
			Select select = sign.getMethod().getAnnotation(Select.class);
			SelectProvider selectProvider = sign.getMethod().getAnnotation(SelectProvider.class);
			if (select != null || selectProvider != null) {
				DbContextHolder.setDbType(DbType.SLAVE);
			} else {
				DbContextHolder.setDbType(DbType.MASTER);
			}
            Object result = pjp.proceed();
			return result;
		} finally {
            DbContextHolder.clearDbType();
		}
	}

	

}
