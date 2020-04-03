package konantech.ai.aikwc.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.common.config.StatusWebSocketHandler;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.ECollector;
import konantech.ai.aikwc.entity.collectors.*;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.Site;
import konantech.ai.aikwc.repository.AgencyRepository;
import konantech.ai.aikwc.repository.CollectorRepository;
import konantech.ai.aikwc.repository.GroupRepository;
import konantech.ai.aikwc.repository.SiteRepository;
import konantech.ai.aikwc.repository.mapping.CollectorMapping;


public abstract class CollectorService<T extends ECollector> {
	@Autowired
	private GroupRepository groupRepository;
	@Autowired
	private SiteRepository siteRepository;
	@Autowired
	private AgencyRepository agencyRepository;
	@Autowired
	private CollectorRepository<konantech.ai.aikwc.entity.collectors.Collector> collectorRepository;
	
	@Autowired
	StatusWebSocketHandler statusHandler;
	
	
	public List<konantech.ai.aikwc.entity.collectors.Collector> getCollectorList(){
		return collectorRepository.findAllWithJoin();
	}
	public List<konantech.ai.aikwc.entity.collectors.Collector> getCollectorListInSiteInUse(String site){
		return collectorRepository.findAllInSite(site);
	}
	public List<konantech.ai.aikwc.entity.collectors.Collector> getCollectorListInSite(String site){
		return collectorRepository.findBySite(site);
	}
	public List<CollectorMapping> getCollectorListInAgency(int agency){
		return collectorRepository.findInAgency(agency);
	}
	public void updateStatus(int pk, String status) {
		Optional<konantech.ai.aikwc.entity.collectors.Collector> op = collectorRepository.findById(pk);
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
	
	/**
	 * Collector 관련 공통 메소드
	 */
	public abstract T saveCollector(T collector);
	public abstract void saveCollectorDetail(T collector);
	public abstract T getCollectorInfo(int pk);
	public abstract List<T> getAllCollectorList();
	
}
