package konantech.ai.aikwc.service;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import konantech.ai.aikwc.entity.KLog;

public interface CommonService {

	public Map<String,Object> commInfo(int agencyNum);
	
	public void saveLog(KLog log);
	public void readAllLog();
	
	public List<KLog> getAgencyLogList(String agency);
}
