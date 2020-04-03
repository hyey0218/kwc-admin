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
import konantech.ai.aikwc.entity.collectors.Collector;
import konantech.ai.aikwc.entity.Group;
import konantech.ai.aikwc.entity.Site;
import konantech.ai.aikwc.repository.AgencyRepository;
import konantech.ai.aikwc.repository.CollectorRepository;
import konantech.ai.aikwc.repository.GroupRepository;
import konantech.ai.aikwc.repository.SiteRepository;
import konantech.ai.aikwc.repository.mapping.CollectorMapping;
import konantech.ai.aikwc.service.CollectorService;


@Service("collectorService")
public class CollectorServiceImpl extends CollectorService<Collector> {
	@Autowired
	CollectorRepository<Collector> collectorRepository;
	
	@Override
	public Collector saveCollector(Collector collector){
		return collectorRepository.save(collector);
	}
	@Override
	public void saveCollectorDetail(Collector collector){
		collectorRepository.save(collector);
	}
	@Override
	public Collector getCollectorInfo(int pk) {
		return (Collector)collectorRepository.findById(pk).get();
	}
	@Override
	public List<Collector> getAllCollectorList() {
		return collectorRepository.findAll();
	}
	
	
	
}
