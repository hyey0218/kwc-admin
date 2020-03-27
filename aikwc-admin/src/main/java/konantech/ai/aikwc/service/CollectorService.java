package konantech.ai.aikwc.service;

import java.util.List;
import java.util.Map;

import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.Site;

public interface CollectorService {
	public Group saveGroup(Group group);
	public void updateGroup(Group group);
	public void deleteGroup(Group group);
	
	public List<Site> getSiteList();
	public List<Site> getSiteListInAgency(int grp);
	
	public List<Collector> getCollectorList();
	
	public Collector saveCollector(Collector collector);
	
	public List<Collector> getCollectorListInSite(String site);
	public List<Collector> getCollectorListInSiteInUse(String site);
	
	public List<Collector> getCollectorListInAgency(int agency);
	
	public Collector getCollectorInfo(int pk) ;
	
	
	public void saveCollectorDetail(Collector collector);
	
	public void updateStatus(int pk, String status);
	
	public Agency getAgencyNameForCollector(String pk);
	
	public List<Site> getSiteInGroup(String group);
	public Site saveSite(Site site);
}
