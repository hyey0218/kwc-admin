package konantech.ai.aikwc.service;

import org.springframework.scheduling.Trigger;

import konantech.ai.aikwc.entity.KTask;
import konantech.ai.aikwc.entity.collectors.BasicCollector;

public interface ScheduleService {
	
	public void registerSchedule(KTask task) ;
	public void stopSchedule(KTask task);
	public int getTaskCount();
	public int getSchedulingTaskCount();
}
