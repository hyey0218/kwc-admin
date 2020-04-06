package konantech.ai.aikwc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.common.config.StatusWebSocketHandler;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.collectors.*;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.Site;
import konantech.ai.aikwc.repository.AgencyRepository;
import konantech.ai.aikwc.repository.CollectorRepository;
import konantech.ai.aikwc.repository.GroupRepository;
import konantech.ai.aikwc.repository.SiteRepository;
import konantech.ai.aikwc.repository.mapping.CollectorMapping;
import konantech.ai.aikwc.selenium.BasicCollectorKWC;
import konantech.ai.aikwc.selenium.KWCSelenium;


public interface CollectorService {
	public void deleteCollector(Collector collector);
	
	public void saveCollectorDetail(Collector collector);
	public Collector getCollector(int pk);
	public Collector updateDtype(int pk, String type) ;
	public List<Collector> getCollectorListInSiteInUse(String site);
	public List<Collector> getCollectorListInSite(String site);
	public List<CollectorMapping> getCollectorListInAgency(int agency);
	public List<CollectorMapping> getCollectorList();
	public void updateStatus(int pk, String status) ;
	
	public Group saveGroup(Group group) ;
	public void updateGroup(Group group) ;
	public void deleteGroup(Group group) ;
	public List<Site> getSiteList();
	public List<Site> getSiteListInAgency(int agency) ;
	public Agency getAgencyNameForCollector(String pk) ;
	public List<Site> getSiteInGroup(String group);
	public Site saveSite(Site site) ;
	public void saveCollector(Collector collector);
}
