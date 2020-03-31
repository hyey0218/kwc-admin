package konantech.ai.aikwc.service;

import org.springframework.scheduling.Trigger;

import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.KTask;

public interface ScheduleService {
	
	public void registerSchedule(KTask task, Collector collector) ;
	public void stopSchedule(KTask task);
	public int getTaskCount();
	public int getSchedulingTaskCount();
}
