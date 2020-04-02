package konantech.ai.aikwc.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.common.config.StatusWebSocketHandler;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.Site;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.AgencyRepository;
import konantech.ai.aikwc.repository.BasicCollectorRepository;
import konantech.ai.aikwc.repository.GroupRepository;
import konantech.ai.aikwc.repository.SiteRepository;
import konantech.ai.aikwc.service.CollectorService;

@Service("BasicCollectorServiceImpl")
public class BasicCollectorServiceImpl implements CollectorService<BasicCollector> {

	@Autowired
	private BasicCollectorRepository BasicCollectorRepository;

	@Override
	public Group saveGroup(Group group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateGroup(Group group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGroup(Group group) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Site> getSiteList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getSiteListInAgency(int grp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BasicCollector> getCollectorList() {
		return BasicCollectorRepository.findAll();
	}

	@Override
	public BasicCollector saveCollector(BasicCollector collector) {
		
		return BasicCollectorRepository.save(collector);
	}

	@Override
	public List<BasicCollector> getCollectorListInSite(String site) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BasicCollector> getCollectorListInSiteInUse(String site) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BasicCollector> getCollectorListInAgency(int agency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BasicCollector getCollectorInfo(int pk) {
		return BasicCollectorRepository.findById(pk).get();
	}

	@Override
	public void saveCollectorDetail(BasicCollector collector) {
		Optional<BasicCollector> op = BasicCollectorRepository.findById(collector.getPk());
		
		op.ifPresent(newer -> {
			newer.setStartUrl(collector.getStartUrl());
			newer.setTitleLink(collector.getTitleLink());
			newer.setTitle(collector.getTitle());
			newer.setContent(collector.getContent());
			newer.setWriter(collector.getWriter());
			newer.setWdatePattern(collector.getWdatePattern());
			newer.setWriteDate(collector.getWriteDate());
			newer.setPageUrl(collector.getPageUrl());
			newer.setContId(collector.getContId());
			BasicCollectorRepository.save(newer);
		});
	}

	@Override
	public void updateStatus(int pk, String status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Agency getAgencyNameForCollector(String pk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Site> getSiteInGroup(String group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Site saveSite(Site site) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

