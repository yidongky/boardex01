package org.zerock.controller;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)/* - @RunWith는 jUnit 프레임워크의 테스트 실행방법을 확장할 때 사용하는 어노테이션이다.
- SpringJUnit4ClassRunner라는 클래스를 지정해주면 jUnit이 테스트를 진행하는 중에 ApplicationContext를 만들고 관리하는 작업을 진행해준다*/
@ContextConfiguration( //- 스프링 빈(Bean) 설정 파일의 위치를 지정할 때 사용되는 어노테이션이다.
		locations = { "file:src/main/webapp/WEB-INF/spring/**/root-context.xml" })
public class DataSourceTest {

	@Inject
	private DataSource ds;
	
	@Test  /*@Test가 선언된 메서드는 테스트를 수행하는 메소드가 된다.
             jUnit은 각각의 테스트가 서로 영향을 주지 않고 독립적으로 실행됨을 원칙으로 @Test마다 객체를 생성한다.*/
	public void testConection()throws Exception{
		
		try(Connection con = ds.getConnection()){
			System.out.println(con);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
}

/*
@Ignore
- @Ignore가 선언된 메서드는 테스트를 실행하지 않게 한다.
@Before
- @Before가 선언된 메서드는 @Test 메서드가 실행되기 전에 반드시 실행되어진다.
- @Test메서드에서 공통으로 사용하는 코드를 @Before 메서드에 선언하여 사용하면 된다.

@After
- @After가 선언된 메서드는 @Test 메소드가 실행된 후 실행된다.

@BeforeClass
- @BeforeClass 어노테이션은 @Test 메소드보다 먼저 한번만 수행되어야 할 경우에 사용하면 된다.

@AfterClass
- @AfterClass 어노테이션은 @Test 메소드 보다 나중에 한번만 수행되어야 할 경우에 사용하면 된다.


assertEquals(a,b); 

 객체 a,b의 값이 일치함을 확인한다.

 assertArrayEquals(a,b);

 배열 a,b의 값이 일치함을 확인한다.

 assertSame(a,b);

 객체 a,b가 같은 객체임을 확인한다.

 두 객체의 레퍼런스가 동일한가를 확인한다.

 assertTrue(a);

 조건 a가 참인가 확인한다.

 assertNotNull(a);

 객체 a가 null이 아님을 확인한다.
*/