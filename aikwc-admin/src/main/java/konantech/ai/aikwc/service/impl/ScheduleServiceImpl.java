package konantech.ai.aikwc.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.KTask;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CrawlService;
import konantech.ai.aikwc.service.ScheduleService;

@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService{

	@Autowired
	ThreadPoolTaskScheduler tpts;
	
	@Autowired
	CrawlService crawlService;
	
	@Resource(name = "CollectorService")
	CollectorService collectorService;
	
	private Map<String, ScheduledFuture<?>> scheduleMap = new ConcurrentHashMap<>();
	
	public void registerSchedule(KTask task) {
		ScheduledFuture<?> future = this.tpts.schedule(()->{
			System.out.println(">>>>>>>>>>>>>>>>>>>>> schdule register :" + getTaskCount() +"/"+ getUsableTaskCount());
			try {
				int pk = Integer.parseInt(task.getCollector());
				crawlService.webCrawlDefault(pk, task.getStart(), task.getEnd());
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
	@Override
	public int getTaskCount() {
		return this.tpts.getActiveCount();
	}
	@Override
	public int getSchedulingTaskCount() {
		return scheduleMap.size();
	}
	
	public int getUsableTaskCount() {
		return this.tpts.getScheduledThreadPoolExecutor().getMaximumPoolSize();
	}
}
