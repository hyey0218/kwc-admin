package konantech.ai.aikwc.service.impl;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.common.config.StatusWebSocketHandler;
import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.collectors.Collector;
import konantech.ai.aikwc.entity.Crawl;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.selenium.BasicCollectorKWC;
import konantech.ai.aikwc.selenium.KWCSelenium;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CommonService;
import konantech.ai.aikwc.service.CrawlService;

@Service("CrawlService")
public class CrawlServiceImpl implements CrawlService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${chrome.web.driver.path}")
	String driverPath;
	
	@Resource(name = "collectorService")
	CollectorService<Collector> collectorService;
	@Resource(name = "BasicCollectorService")
	CollectorService<BasicCollector> basicCollectorService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	CrawlRepository crawlRepository;
	
	
	@Async("kwcExecutor")
	public CompletableFuture webCrawlThread(Class collector, int pk, String start, String end) throws Exception {
		logger.info("start task");
		int result = 1;
		if(collector.isInstance(new BasicCollector())) {
			BasicCollector c = basicCollectorService.getCollectorInfo(pk);
			c.setStartPage(start);
			c.setEndPage(end);
			result = basicWeb(c);
		}else {
			
		}
		return CompletableFuture.completedFuture(result);
	}

	public int webCrawlDefault(Class collector, int pk, String start, String end) throws Exception {
		logger.info("start task");
		int result = 1;
		if(collector.isInstance(new BasicCollector())) {
			BasicCollector c = basicCollectorService.getCollectorInfo(pk);
			c.setStartPage(start);
			c.setEndPage(end);
			result = basicWeb(c);
		}else {
			
		}
		return result;
	}
	
	public void preworkForCrawling(BasicCollector selectedCollector) {
		Agency Agency = collectorService.getAgencyNameForCollector(selectedCollector.getToSite().getGroup().getAgency());
		String agencyName = Agency.getName();
		selectedCollector.getToSite().getGroup().setAgencyName(agencyName);
		selectedCollector.setChannel("기관");
	}
	
	public int basicWeb(BasicCollector collector) {
		preworkForCrawling(collector);
		logger.info("start task");
		StringBuffer logBuffer = new StringBuffer();
		String threadName = Thread.currentThread().getName();
		String colInfo = collector.getToSite().getName() + "/" + collector.getName();
		logBuffer.append("["+CommonUtil.getCurrentTimeStr("")+"] START TASK " + threadName ).append(" : " + colInfo +"\n");
		
		BasicCollectorKWC kwc = new BasicCollectorKWC(driverPath, collector);
		//2. crawling 페이지별로  insert하는 크롤링
//		int result = kwc.work(crawlRepository);
		int result = kwc.crawlWeb(crawlRepository);
		
		String endTime = "["+CommonUtil.getCurrentTimeStr("")+"] ";
		String sePage = collector.getStartPage() + " ~ " + collector.getEndPage();
		//3. DB status update Success+Wait
		if(result == 0) {
			collectorService.updateStatus(collector.getPk(), "SW");
			logBuffer.append(endTime+ Thread.currentThread().getName() +" : " +  colInfo +" [SUCCESS] : "+sePage+" page \n");
			kwc.log.setComment(colInfo + " 수집 완료 [SUCCESS]");
		}
		else {
			collectorService.updateStatus(collector.getPk(), "FW");
			logBuffer.append(endTime+ Thread.currentThread().getName() +" : " +  colInfo +" [FAIL] : "+sePage+" page \n");
			kwc.log.setComment(colInfo + " 수집 완료 : [FAIL]");
		}
		logBuffer.append("["+CommonUtil.getCurrentTimeStr("")+"] END TASK " + threadName ).append(" : " + colInfo);
		kwc.log.setLogCont(logBuffer.toString());
		kwc.insertLog(commonService);
		return result;
	}
	
}
