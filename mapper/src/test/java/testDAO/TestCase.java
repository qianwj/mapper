package testDAO;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.UserDAO;
import entity.User;

public class TestCase {

	@Test
	public void testMapper() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("spring-mybatis.xml");
		UserDAO dao = ac.getBean(UserDAO.class);
		User u1 = dao.find("hehe");
		User u2 = dao.login("hehe", "123");
		System.out.println(u1.getName().equals(u2.getName()));
	}
}
