package konantech.ai.aikwc.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.KTask;
import konantech.ai.aikwc.service.CrawlService;
import konantech.ai.aikwc.service.ScheduleService;

@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService{

	@Autowired
	ThreadPoolTaskScheduler tpts;
	
	@Autowired
	CrawlService crawlService;
	
	
	private Map<String, ScheduledFuture<?>> scheduleMap = new ConcurrentHashMap<>();
	
	public void registerSchedule(KTask task, Collector collector) {
		ScheduledFuture<?> future = this.tpts.schedule(()->{
			System.out.println(">>>>>>>>>>>>>>>>>>>>> schdule register");
			try {
				crawlService.webCrawl(collector);
			} catch (Exception e) {
				e.printStackTrace();
			}
		},new CronTrigger(task.getCycleCron()));
		
		scheduleMap.put(task.getTaskNo(), future);
	}

	@Override
	public void stopSchedule(KTask task) {
		scheduleMap.get(task.getTaskNo()).cancel(true);
		scheduleMap.remove(task.getTaskNo());
	}
	
}
