package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/*
 * Aspect : 공통 관심사에 대한 추상적인 명칭. 예를 들어 로깅이나 보안, 트랜잭션과 같은기능 자체에 대한 용어
 * Advice : 실제로 기능을 구현한 객체
 *          실제로 적용시키고 싶은 코드 자체, 개발자가 만드는 것은 Aspect가 아닌 클래스를 제작하고 @Advice를 적용하는 것임, 예를 들어 로그 출력 기능,
 *          파라미터 체크 기능 자체는 Aspect라는 용어로 부르지만, 실제 구현 시에는 Advice를 제작한다고 표현
 *          
 * join Points :  공통 관심사를 적용할수 있는 대상, Spring AOP에서는 각 객체의 메소드가 이에 해당
 * Pointcuts : 여러 메소드 중 실제 Adivce가 적용 될 대상 메소드
 *             여러 join points 중에서 advice를 적용할 대상을 선택하는 정보, 이를통해서 특성 메소드는 advice가 적용된 형태로 동작함.
 * target : 대상 메소드를 가지는 객체
 * Proxy : Advice각 적용되었을때 만들어지는 객체
 * Introduction : target에 없는 새로운 메소드나 인스턴스 변수를 추가하는 기능
 * Weaving : Advice 와 target이 결합되어서 프록시 객체를 만드는 과정
 */

/*
 * Advice의 종류
 * Advice는 실제 구현된 클래스로 생각할 수 있는데 ,advice의 타입은 다시 아래 와 같이 분류 됩니다.
 * Before Advice target의 메소드 호출전에 적용
 * After returning target의 메소드 호출 이후에 적용
 * After throwing target의 예외 발생 후 적용
 * After target의 메소드 호출 후 예외의 발생에 관계없이 적용
 * Around taget의 메소드 호출 이전과 이후 모두 적용(가장 광범위하게 사용됨)
 */

/*
 * org.aspectj.lang.JoinPoint 의 기능들
 * Object[] getArgs() : 전달되는 모든 파라미터들을 Object의 배열로 가져온다.
 * String getKind() : 해당 Advice의 타입을 알아낸다.
 * Signature getSignature() 실행하는 대상 객체의 메소드에 대한 정보를 알아낼 때 사용
 * Object getTarget() target객체를 알아낼 때 사용
 * Object getThis() Advice를 행하는 객체를 알아낼 때 사용
 */

@Component   // 스프링 빈으로 인식되기 위해서 설정
@Aspect  // AOP 기능을 하는 클래스의 선언에 추가
public class SampleAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(SampleAdvice.class);
	// org.zerock.service.MessageService 를 실행하기전에 실행
	
/*	
	@Before("execution(* org.zerock.service.MessageService*.*(..))") //execution Pointcut을 지정하느 문법으로 ASpectJ 언어의 문법을 사용 org.zerock.service.MessageService 시작하는 모든 클래스의 (모든)메소드를 지정
	public void startLog() {
		logger.info("----------------------------------------------");
		logger.info("----------------------------------------------");
	}
*/
	
	@Before("execution(* org.zerock.service.MessageService*.*(..))") //execution Pointcut을 지정하느 문법으로 ASpectJ 언어의 문법을 사용 org.zerock.service.MessageService 시작하는 모든 클래스의 (모든)메소드를 지정
	public void startLog(JoinPoint jp) {
		logger.info("----------------------------------------------");
		logger.info("----------------------------------------------");
		logger.info(Arrays.toString(jp.getArgs())); //전달 되는 모든 파라미터들을 Object의 배열로 가져온다.
		//INFO : org.zerock.aop.SampleAdvice - [MessageVO [mid=null, targetid=user01, sender=user02, message=Good Luck!, opendate=null, senddate=null]]
		//JoinPoint 인터페이스는 호출되는 대상 객체, 메서드 그리고 전달되는 파라미터 목록에 접근할 수 있는 메서드를 제공
	}
	
	@Around("execution(* org.zerock.service.MessageService*.*(..))") //ProceedingJoinPoint 는 Exception 보다 상위의 Throwable 을  처리해야 하므로 시간을  체크하는 기능을 다음과 같이 작성할 수 있습니다.
	public Object timeLog(ProceedingJoinPoint pjp)throws Throwable{
		//@Around를 이용하는 경우에는 반드시 메소드이 리턴 타입은 Object로 선언해야 합니다. 
		// ProceedingJoinPoint는 JoinPoint의 하위 인터페이스로 joinPoint의 모든 메소드를 가지면서도, 직접 target 객체의 메소드를 실행할수 있는 기능이 추가된 형태입니다.
		//Object proceed() : 다음 Advice를 실행하거나, 실제 target 객체의 메소드를 실행하는 기능  
		
		/*
		 * ●Around Advice의 경우 org.aspectj.lang.ProceedingJoinPoint를 첫 번째 파라미터로 전달받는데 해당 인터페이스는 프록시 대상 객체를 호출할 수있는 proceed() 메서드를 제공
		 * ●ProceedingJoinPoint는 JoinPoint 인터페이스를 상속받았기 때문에 Signature를 이용하여 대상 객체, 메서드 및 전달되는 파라미터에 대한 정보를 구할 수 있음
		 * @Around는 다른 Advice와 달리 @Around는 메소드를 직접 호출하고 결과를 반환해야만 정상적인 처리가 됩니다.
		 * @Around는 반환타입을 만드시 Object로 해주어야한다.
		 */
		
		
		
		
		
		
		/*
		 * java.lang.Throwable 클래스
			모든 예외 클래스의 최상위 클래스:물론 Throwable도 Object를 상속한다
			
			Throwable 클래스의 메소드 둘
			public String getMessage():예외의 원인을 담고 있는 문자열을 반환
			public void printStackTrace():예외가 발생한 위치와 호출된 메소드의 정보를 출력
		 */
		
		long startTime = System.currentTimeMillis();
		logger.info(Arrays.toString(pjp.getArgs())); //전
		
		Object result = pjp.proceed(); //proceed()를 이용해서 실제 메소드를 호출합니다.
		
		long endTime = System.currentTimeMillis();  //후
		logger.info(pjp.getSignature().getName()+ " : " + (endTime - startTime) );
		logger.info("==============================================================");
		
		return result;
	}

}
