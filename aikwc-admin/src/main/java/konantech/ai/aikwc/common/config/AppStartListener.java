package konantech.ai.aikwc.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import konantech.ai.aikwc.entity.KTask;
import konantech.ai.aikwc.service.CrawlService;
import konantech.ai.aikwc.service.ScheduleService;
import konantech.ai.aikwc.service.TaskService;

@Component
public class AppStartListener implements ApplicationListener<ApplicationStartedEvent> {

	@Autowired
	TaskService taskService;
	@Autowired
	ScheduleService scheduleService;
	@Autowired
	CrawlService crawlService;
	@Override
	public void onApplicationEvent(ApplicationStartedEvent event) {
		System.out.println("###########Start Event - AppStartListener#############");
//		taskCheck();
	}
	
	public void taskCheck() {
		List<KTask> taskList = taskService.getAllTaskByUsable();
		if(taskList.size() > 0) {
			taskList.forEach((task)->{
				scheduleService.registerSchedule(task);
			});
		}
	}

}
