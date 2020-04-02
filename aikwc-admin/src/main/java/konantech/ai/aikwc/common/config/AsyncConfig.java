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
public class AsyncConfig implements AsyncConfigurer{
	
	@Resource(name="kwcExecutor")
	private ThreadPoolTaskExecutor kwcExecutor;
	
	private static int TASK_CORE_POOL_SIZE = 50;
    private static int TASK_MAX_POOL_SIZE = 50;
    private static int TASK_QUEUE_CAPACITY = 5;
    private static String EXECUTOR_BEAN_NAME = "KWC-";
    

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		// TODO Auto-generated method stub
		return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
	}


	@Bean(name="kwcExecutor")
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
		tpte.setCorePoolSize(TASK_CORE_POOL_SIZE); // Thread 기본사이즈
		tpte.setMaxPoolSize(TASK_MAX_POOL_SIZE); // 최대 사이즈
		tpte.setQueueCapacity(TASK_QUEUE_CAPACITY); // Max Thread가 도작하는 경우 대기하는 queue 사이즈
		//thread 와 queue 가 전부 찼을 경우 Exception 
		tpte.setThreadNamePrefix(EXECUTOR_BEAN_NAME); // KWC-1 ,KWC-2 ...
		tpte.initialize();
		return tpte;
	}

	
	public int getTaskCount() {
		return this.kwcExecutor.getActiveCount();
	}
	
	public int getAfterTaskCount() {
		return this.kwcExecutor.getActiveCount()-1;
	}
	
	public boolean isTaskExecute() {
        boolean rtn = true;
        if (kwcExecutor.getActiveCount() >= (TASK_MAX_POOL_SIZE + TASK_QUEUE_CAPACITY)) {
            rtn = false;
        }
        return rtn;
    }
	
	public int getTaskCorePoolCount() {
		return this.kwcExecutor.getCorePoolSize();
	}

}
