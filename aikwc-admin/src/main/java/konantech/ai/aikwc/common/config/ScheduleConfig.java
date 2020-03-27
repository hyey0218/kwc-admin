package konantech.ai.aikwc.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;


@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer{

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		// 기본 scheduled default는 스레드 1개로 동작 -> 설정 변경하여 스레딩함.
		
		ThreadPoolTaskScheduler tpts = new ThreadPoolTaskScheduler();
		tpts.setPoolSize(10);
		tpts.setThreadNamePrefix("KWC-Schedule");
		tpts.initialize();
		
		taskRegistrar.setTaskScheduler(tpts);
	}

//	@Scheduled(cron = "*/10 * * * * *")  // 매 30초
//	public void fixedTask() {
//		System.out.println(">>>>>>>>>>>>>>> Scheduled Task");
//		System.out.println("Current Thread : " + Thread.currentThread().getName());
////		Thread.sleep(30000);
//	}
}
