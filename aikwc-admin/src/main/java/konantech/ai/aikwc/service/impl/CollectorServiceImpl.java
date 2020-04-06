package konantech.ai.aikwc.service.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.repository.CollectorRepository;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.selenium.KWCSelenium;
import konantech.ai.aikwc.service.CollectorService;

@Service("CollectorService")
public class CollectorServiceImpl extends CollectorService{
	
	@Autowired
	CollectorRepository collectorRepository;
	@Autowired
	CrawlRepository crawlRepository;
	

	@Override
	public int webCrawl(Collector collector,Object k) {
		KWCSelenium kwc = (KWCSelenium) k;
		preworkForCrawling(collector);
		int result = kwc.crawlWeb(crawlRepository);
		if(result == 0) {
			super.updateStatus(collector.getPk(), "SW");
		}else {
			super.updateStatus(collector.getPk(), "FW");
		}
		return result;
	}
	
	public void preworkForCrawling(Collector selectedCollector) {
		Agency Agency = super.getAgencyNameForCollector(selectedCollector.getToSite().getGroup().getAgency());
		String agencyName = Agency.getName();
		selectedCollector.getToSite().getGroup().setAgencyName(agencyName);
		selectedCollector.setChannel("기관");
	}

}
