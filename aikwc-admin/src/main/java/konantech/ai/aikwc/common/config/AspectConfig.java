package konantech.ai.aikwc.common.config;

import java.io.IOException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectConfig {

	@Autowired
	CheckStatusHandler statusHandler;
	
//	@Around("execution(* konantech.ai.aikwc.service.CrawlService.webCrawl(..)) ")
//	public Object crawlAround(ProceedingJoinPoint pjp) throws Throwable, IOException {
//		long begin = System.currentTimeMillis();
//		statusHandler.sendTaskCnt();
//		try {
//			Object ret = pjp.proceed();
//			return ret;
//		}finally {
//			System.out.println(System.currentTimeMillis() - begin );
//			statusHandler.sendTaskCnt();
//		}
//	}
	
	@Before("execution(* konantech.ai.aikwc.service.CrawlService.webCrawl(..)) ")
	public void crawlBefore() throws IOException{
		statusHandler.sendTaskCnt();
	}

	@After("execution(* konantech.ai.aikwc.service.CrawlService.webCrawl(..)) ")
	public void crawlAfter() throws IOException{
		statusHandler.sendTaskCnt();
	}
}