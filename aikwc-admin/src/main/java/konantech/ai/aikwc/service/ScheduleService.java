package konantech.ai.aikwc.service;

import org.springframework.scheduling.Trigger;

import konantech.ai.aikwc.entity.KTask;

public interface ScheduleService {
	
	public void registerSchedule(KTask task) ;
	public void stopSchedule(KTask task);
	public int getTaskCount();
	public int getSchedulingTaskCount();
}
