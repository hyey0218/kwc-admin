package konantech.ai.aikwc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.CollectorRepository;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.selenium.BasicCollectorKWC;
import konantech.ai.aikwc.selenium.KWCSelenium;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CommonService;

@Service("BasicCollectorService")
public class BasicCollectorServiceImpl extends CollectorService<BasicCollector> {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${chrome.web.driver.path}")
	String driverPath;
	
	@Autowired
	private CollectorRepository basicCollectorRepository;
	
	@Autowired
	CrawlRepository crawlRepository;
	
	@Autowired
	CommonService commonService;

	@Override
	public void saveCollectorDetail(int pk, BasicCollector collector) {
		Optional<Collector> op = basicCollectorRepository.findById(pk);
		
		op.ifPresent(newer -> {
			newer.setDetail(CommonUtil.objectToString(collector));
			basicCollectorRepository.save(newer);
		});
	}
	@Override
	public BasicCollector getCollectorDetailInfo(int pk) {
		Collector c =  basicCollectorRepository.findById(pk).get();
		BasicCollector b = CommonUtil.stringToObject(c.getDetail(), BasicCollector.class);
		b.setPk(String.valueOf(c.getPk()));
		return b;
	}
	@Override
	public List<BasicCollector> getAllCollectorList() {
		List<Collector> clist = basicCollectorRepository.findByClassName("BasicCollector");
		
		List<BasicCollector> list = new ArrayList<BasicCollector>();
		clist.forEach((o)->{
			list.add((BasicCollector) CommonUtil.stringToJsonClass(o.getDetail(), BasicCollector.class));
		});
		return list;
	}

	public int webCrawl(Collector collector, String start, String end) {
		preworkForCrawling(collector);
		BasicCollector bc = CommonUtil.stringToObject(collector.getDetail(), BasicCollector.class);
		bc.setStartPage(start);
		bc.setEndPage(end);
		logger.info("start task");
		StringBuffer logBuffer = new StringBuffer();
		String threadName = Thread.currentThread().getName();
		String colInfo = collector.getToSite().getName() + "/" + collector.getName();
		logBuffer.append("["+CommonUtil.getCurrentTimeStr("")+"] START TASK " + threadName ).append(" : " + colInfo +"\n");
		
		BasicCollectorKWC kwc = new BasicCollectorKWC(driverPath, collector, bc);
		int result = kwc.crawlWeb(crawlRepository);
		
		String endTime = "["+CommonUtil.getCurrentTimeStr("")+"] ";
		String sePage = bc.getStartPage() + " ~ " + bc.getEndPage();
		//3. DB status update Success+Wait
		if(result == 0) {
			super.updateStatus(collector.getPk(), "SW");
			logBuffer.append(endTime+ Thread.currentThread().getName() +" : " +  colInfo +" [SUCCESS] : "+sePage+" page \n");
			kwc.log.setComment(colInfo + " 수집 완료 [SUCCESS]");
		}
		else {
			super.updateStatus(collector.getPk(), "FW");
			logBuffer.append(endTime+ Thread.currentThread().getName() +" : " +  colInfo +" [FAIL] : "+sePage+" page \n");
			kwc.log.setComment(colInfo + " 수집 완료 : [FAIL]");
		}
		logBuffer.append("["+CommonUtil.getCurrentTimeStr("")+"] END TASK " + threadName ).append(" : " + colInfo);
		kwc.log.setLogCont(logBuffer.toString());
		kwc.insertLog(commonService);
		
		
		return result;
	}
	public void preworkForCrawling(Collector selectedCollector) {
		Agency Agency = super.getAgencyNameForCollector(selectedCollector.getToSite().getGroup().getAgency());
		String agencyName = Agency.getName();
		selectedCollector.getToSite().getGroup().setAgencyName(agencyName);
		selectedCollector.setChannel("기관");
	}
	
}

