package konantech.ai.aikwc.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.common.config.StatusWebSocketHandler;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.Site;
import konantech.ai.aikwc.repository.AgencyRepository;
import konantech.ai.aikwc.repository.CollectorRepository;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.repository.GroupRepository;
import konantech.ai.aikwc.repository.SiteRepository;
import konantech.ai.aikwc.repository.mapping.CollectorMapping;
import konantech.ai.aikwc.selenium.KWCSelenium;
import konantech.ai.aikwc.service.CollectorService;

@Service("CollectorService")
public class CollectorServiceImpl implements CollectorService{
	
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private CollectorRepository collectorRepository;
	@Autowired
	StatusWebSocketHandler statusHandler;
	
	public void deleteCollector(Collector collector) {
		collectorRepository.delete(collector);
	}
	
	public void saveCollectorDetail(Collector collector) {
		Optional<Collector> op = collectorRepository.findById(collector.getPk());
		op.ifPresent(newer -> {
			newer.setDetail(collector.getDetail());
			collectorRepository.save(newer);
		});
	}
	public Collector getCollector(int pk) {
		return collectorRepository.findById(pk).get();
	}
	public Collector updateDtype(int pk, String type) {
		collectorRepository.updateDtype(pk, type);
		return collectorRepository.findById(pk).get();
	}
	public List<Collector> getCollectorListInSiteInUse(String site){
		return collectorRepository.findAllInSite(site);
	}
	public List<Collector> getCollectorListInSite(String site){
		return collectorRepository.findBySite(site);
	}
	public List<CollectorMapping> getCollectorListInAgency(int agency){
		return collectorRepository.findInAgency(agency);
	}
	public List<CollectorMapping> getCollectorList(){
		return collectorRepository.findAllList();
	}
	public void updateStatus(int pk, String status) {
		Optional<Collector> op = collectorRepository.findById(pk);
		op.ifPresent(newer -> {
			newer.setStatus(status);
			collectorRepository.saveAndFlush(newer);
		});
		
		try {
			statusHandler.sendCollectorStatus();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Group saveGroup(Group group) {
		return groupRepository.save(group);
	}
	public void updateGroup(Group group) {
		groupRepository.updateGroup(group.getPk(), group.getCode(), group.getName());
	}
	public void deleteGroup(Group group) {
		groupRepository.deleteById(group.getPk());
	}
	public List<Site> getSiteList(){
		return siteRepository.findAll();
	}
	public List<Site> getSiteListInAgency(int agency) {
		return siteRepository.getSiteListInAgency(agency);
	}
	public Agency getAgencyNameForCollector(String pk) {
		return agencyRepository.findOneByPk(pk);
	}
	
	public List<Site> getSiteInGroup(String group){
		return siteRepository.findByGrp(group);
	}
	public Site saveSite(Site site) {
		return siteRepository.save(site);
	}
	public void saveCollector(Collector collector) {
		collectorRepository.save(collector);
	}
	

}
