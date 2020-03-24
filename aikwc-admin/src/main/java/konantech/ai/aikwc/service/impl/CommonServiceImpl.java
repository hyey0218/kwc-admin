package konantech.ai.aikwc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.repository.AgencyRepository;
import konantech.ai.aikwc.service.CommonService;

@Service("CommonService")
public class CommonServiceImpl implements CommonService {

	@Autowired
	AgencyRepository agencyRepository;
	
	
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
}
