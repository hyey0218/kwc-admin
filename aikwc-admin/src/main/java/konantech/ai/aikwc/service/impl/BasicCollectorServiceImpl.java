package konantech.ai.aikwc.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.CollectorRepository;
import konantech.ai.aikwc.service.CollectorService;

@Service("BasicCollectorService")
public class BasicCollectorServiceImpl extends CollectorService<BasicCollector> {

	@Autowired
	private CollectorRepository<BasicCollector> basicCollectorRepository;

	@Override
	public BasicCollector saveCollector(BasicCollector collector) {
		return basicCollectorRepository.save(collector);
	}

	@Override
	public void saveCollectorDetail(BasicCollector collector) {
		Optional<BasicCollector> op = basicCollectorRepository.findById(collector.getPk());
		
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
			basicCollectorRepository.save(newer);
		});
	}

	@Override
	public BasicCollector getCollectorInfo(int pk) {
		return basicCollectorRepository.findById(pk).get();
	}

	@Override
	public List<BasicCollector> getAllCollectorList() {
		return basicCollectorRepository.findAll();
	}

	
}

