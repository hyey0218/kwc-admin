package konantech.ai.aikwc.service;

import java.util.List;

import konantech.ai.aikwc.entity.KTask;

public interface TaskService {
	public List<KTask> getTaskByCollector(String collector);
	
	public Long getTaskByCollectorCount(String collector);
	
	public void updateTaskNo(int pk, String taskNo);
	
	public void saveTask(KTask task);
}
