package konantech.ai.aikwc.service;

import java.util.Map;

import org.springframework.ui.Model;

import konantech.ai.aikwc.entity.KLog;

public interface CommonService {

	public Map<String,Object> commInfo(int agencyNum);
	
	public void saveLog(KLog log);
}
