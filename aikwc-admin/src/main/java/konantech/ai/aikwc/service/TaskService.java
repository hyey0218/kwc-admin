package konantech.ai.aikwc.service;

import java.util.List;
import java.util.Map;

import konantech.ai.aikwc.entity.KTask;

public interface TaskService {
	public KTask getTaskByPk(String pk);
	
	public List<KTask> getTaskByCollector(String collector);
	
	public Long getTaskByCollectorCount(String collector);
	
	public void updateTaskNo(int pk, String taskNo);
	
	public void saveTask(KTask task);
	
	public List<KTask> getAllTask();
	
	public List<Map<String, String>> getAllTaskWithCollectorName();
	
	public void deleteTask(KTask task);
}
