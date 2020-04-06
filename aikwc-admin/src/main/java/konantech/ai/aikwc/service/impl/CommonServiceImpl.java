package konantech.ai.aikwc.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import konantech.ai.aikwc.common.config.MsgWebSocketHandler;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.KLog;
import konantech.ai.aikwc.repository.AgencyRepository;
import konantech.ai.aikwc.repository.GroupRepository;
import konantech.ai.aikwc.repository.KLogRepository;
import konantech.ai.aikwc.service.CommonService;

@Service("CommonService")
public class CommonServiceImpl implements CommonService {

	@Autowired
	AgencyRepository agencyRepository;
	
	@Autowired
	KLogRepository logRepository;
	
	@Autowired
	GroupRepository groupRepository;
	
	@Autowired
	MsgWebSocketHandler msgHandler;
	
	public Map<String,Object> commInfo(int agencyNum) {

		HashMap<String, Object> map = new HashMap<String,Object>();
//		int agency = Integer.parseInt(agencyNum);
		List<Agency> agencyList = agencyRepository.findAll();
		if(agencyNum == 0 && agencyList!= null &&  agencyList.size()>0) {
			agencyNum = agencyList.get(0).getPk();
		}
		Agency selAgency = agencyRepository.findById(agencyNum).get();
		map.put("selAgency", selAgency);
		if(selAgency != null)
			map.put("groupList", selAgency.getGroup());
		map.put("agencyList", agencyList);
		return map;
	}
	
	public void saveLog(KLog log) {
		logRepository.saveAndFlush(log);
		
		try {
			msgHandler.sendLogMassage();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void readAllLog() {
		logRepository.updateRead();
	}
	public List<KLog> getAllLog(){
		return logRepository.findAllByOrderByCreateDateDesc();
	}
	
	public List<Agency> getAgencyAll(){
		return agencyRepository.findAll();
	}
	
	public List<Group> getGroupInAgency(String agency){
		return groupRepository.findAllByAgency(agency);
	}
}
