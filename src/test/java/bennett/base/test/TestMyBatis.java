package bennett.base.test;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;

import bennett.base.domain.User;
import bennett.base.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类  
@ContextConfiguration(locations = {"classpath:spring-mybatis.xml"}) 
public class TestMyBatis {
	private static Logger logger = Logger.getLogger(TestMyBatis.class);
	@Resource
	private IUserService userService = null;

	@Test
	public void test() {
		User user = userService.getUserById(1);  
        logger.info(JSON.toJSONString(user)); 
	}
	public static void main(String[] args) {
		System.out.println("..");
	}
}
