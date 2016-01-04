package rwtest;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import rwtest.aspect.Master;
import rwtest.aspect.Slave;
import rwtest.domain.SomeData;
import rwtest.mapper.SomeDataMapper;
import rwtest.routing.DbContextHolder;

@Component
public class Main {
	public static Log logger = LogFactory.getLog("MAIN");
	@Autowired
	private SomeDataMapper someDataMapper;

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring.xml");
		Main main = context.getBean(Main.class);
		main.testDefaultFindOne();
		main.testAddData();
		main.testFindOne();
		main.testFindTwo();
		main.testDefaultFindTwo();
		
		main.testFindALL();
	}
	@Transactional
	public void testDefaultFindOne(){
		logger.info(String.format(">>> DbContextHolder.getDbType %s ",DbContextHolder.getDbType()));
		SomeData someData = someDataMapper.findOneSomeData(1);
		logger.info(someData.toString());
	}

	@Master
	@Transactional
	public void testAddData() {
		logger.info(String.format(">>> DbContextHolder.getDbType %s ",DbContextHolder.getDbType()));
		SomeData t1 = new SomeData();
		t1.setId(2);
		t1.setName("lvpei");
		t1.setVal("val1");
		someDataMapper.addSomeData(t1);
	}
	@Slave
	@Transactional
	public void testFindOne() {
		logger.info(String.format(">>> DbContextHolder.getDbType %s ",DbContextHolder.getDbType()));
		SomeData someData = someDataMapper.findOneSomeData(1);
		logger.info(someData.toString());
	}
	@Master
	@Transactional
	public void testFindTwo() {
		logger.info(String.format(">>> DbContextHolder.getDbType %s ",DbContextHolder.getDbType()));
		SomeData someData = someDataMapper.findOneSomeData(2);
		logger.info(someData!=null?someData.toString():null);
	}
	@Transactional
	public void testDefaultFindTwo() {
		logger.info(String.format(">>> DbContextHolder.getDbType %s ",DbContextHolder.getDbType()));
		SomeData someData = someDataMapper.findOneSomeData(2);
		logger.info(someData!=null?someData.toString():null);
	}
	
	public void testFindALL(){
		List<SomeData> result = someDataMapper.findALL();
		logger.info(result.get(0));
	}

}
