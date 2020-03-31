package konantech.ai.aikwc.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.entity.KTask;
import konantech.ai.aikwc.repository.KTaskRepository;
import konantech.ai.aikwc.service.TaskService;

@Service("TaskService")
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	KTaskRepository taskRepository;
	
	public KTask getTaskByPk(String pk) {
		return taskRepository.findById(Integer.parseInt(pk)).get();
	}
	public List<KTask> getTaskByCollector(String collector) {
		return taskRepository.findAllByCollector(collector);
	}
	public Long getTaskByCollectorCount(String collector) {
		return taskRepository.countByCollector(collector);
	}
	
	public void updateTaskNo(int pk, String taskNo) {
		Optional<KTask> op = taskRepository.findById(pk);
		op.ifPresent(newer -> {
			newer.setTaskNo(taskNo);
			taskRepository.saveAndFlush(newer);
		});
		
	}
	
	public void saveTask(KTask task) {
		taskRepository.saveAndFlush(task);
	}
	
	public List<KTask> getAllTaskByUsable(){
		return taskRepository.findByUseyn("Y");
	}
	
	public List<Map<String, String>> getAllTaskWithCollectorName(){
		return taskRepository.findByUseynWithCollName();
	}
	
	public void deleteTask(KTask task) {
		taskRepository.delete(task);
	}
}
