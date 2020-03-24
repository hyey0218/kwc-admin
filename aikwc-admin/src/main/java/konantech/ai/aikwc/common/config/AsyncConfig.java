package konantech.ai.aikwc.common.config;

import java.util.concurrent.Executor;

import javax.annotation.Resource;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
	
	@Resource(name="kwcExecutor")
	private ThreadPoolTaskExecutor kwcExecutor;
	
	
	@Bean(name="kwcExecutor")
	public Executor kwcExecutor() {
		ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
		tpte.setCorePoolSize(2); // Thread 기본사이즈
		tpte.setMaxPoolSize(3); // 최대 사이즈
		tpte.setQueueCapacity(10); // Max Thread가 도작하는 경우 대기하는 queue 사이즈
		//thread 와 queue 가 전부 찼을 경우 Exception 
		tpte.setThreadNamePrefix("KWC"); // KWC-1 ,KWC-2 ...
//		tpte.initialize();
		return tpte;
	}

	public int getTaskCount() {
		return this.kwcExecutor.getActiveCount();
	}
	
}
