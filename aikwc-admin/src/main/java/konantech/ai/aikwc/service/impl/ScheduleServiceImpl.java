package konantech.ai.aikwc.service.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.entity.KTask;
import konantech.ai.aikwc.service.ScheduleService;

@Service("ScheduleService")
public class ScheduleServiceImpl implements ScheduleService{

	@Autowired
	ThreadPoolTaskScheduler tpts;
	
	private Map<String, ScheduledFuture<?>> scheduleMap = new ConcurrentHashMap<>();
	
	public void registerSchedule(KTask task, String cronEps) {
		ScheduledFuture<?> future = this.tpts.schedule(()->{
			System.out.println(">>>>>>>>>>>>>>>>>>>>> schdule!!!");
		},new CronTrigger(cronEps));
		
		scheduleMap.put(task.getTaskNo(), future);
	}

	@Override
	public void stopSchedule(KTask task) {
		scheduleMap.get(task.getTaskNo()).cancel(true);
		scheduleMap.remove(task.getTaskNo());
	}
}
