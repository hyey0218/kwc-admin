package konantech.ai.aikwc.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.service.CollectorService;

@Service("CollectorService")
public class CollectorServiceImpl extends CollectorService<Collector>{
	@Resource(name = "BasicCollectorService")
	BasicCollectorServiceImpl collectorService;
	
	@Override
	public void saveCollectorDetail(int pk, Collector collector) {
	}

	@Override
	public Collector getCollectorDetailInfo(int pk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Collector> getAllCollectorList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int webCrawl(Collector collector, String start, String end) {
		// TODO Auto-generated method stub
		return 0;
	}

}
