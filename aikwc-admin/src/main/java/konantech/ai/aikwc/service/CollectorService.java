package konantech.ai.aikwc.service;

import java.util.List;
import java.util.Map;

import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.Site;

public interface CollectorService<T extends Collector> {
	public Group saveGroup(Group group);
	public void updateGroup(Group group);
	public void deleteGroup(Group group);
	
	public List<Site> getSiteList();
	public List<Site> getSiteListInAgency(int grp);
	
	public List<T> getCollectorList();
	
	public T saveCollector(T collector);
	
	public List<T> getCollectorListInSite(String site);
	public List<T> getCollectorListInSiteInUse(String site);
	
	public List<T> getCollectorListInAgency(int agency);
	
	public T getCollectorInfo(int pk) ;
	
	
	public void saveCollectorDetail(T collector);
	
	public void updateStatus(int pk, String status);
	
	public Agency getAgencyNameForCollector(String pk);
	
	public List<Site> getSiteInGroup(String group);
	public Site saveSite(Site site);
}
