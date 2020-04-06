package konantech.ai.aikwc.service.impl;


import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import konantech.ai.aikwc.common.CommonConstants;
import konantech.ai.aikwc.common.config.StatusWebSocketHandler;
import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Crawl;
import konantech.ai.aikwc.entity.KLog;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.repository.KLogRepository;
import konantech.ai.aikwc.selenium.BasicCollectorKWC;
import konantech.ai.aikwc.selenium.KWCSelenium;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CommonService;
import konantech.ai.aikwc.service.CrawlService;

@Service("CrawlService")
public class CrawlServiceImpl implements CrawlService {
	
	@Resource(name = "CollectorService")
	CollectorService collectorService;
	
	@Autowired
	CommonService commonService;
	
	@Value("${chrome.web.driver.path}")
	String driverPath;
	
	@Autowired
	CrawlRepository crawlRepository;
	
	
	@Async("kwcExecutor")
	public CompletableFuture webCrawlThread(int pk, String start, String end) throws Exception {
		int result = 1;
		callCollectorScrap( pk, start, end);
		return CompletableFuture.completedFuture(result);
	}
	public int webCrawlDefault(int pk, String start, String end) throws Exception {
		int result = 1;
		callCollectorScrap( pk, start, end);
		return result;
	}
	
		
	public int callCollectorScrap(int pk, String start, String end) throws Exception {
		int result = 1;
		Collector collector = collectorService.getCollector(pk);
		Class[] constructorTypes = {String.class, Collector.class};
		Object[] constructorParams = {driverPath, collector};
		Constructor constructor = Class.forName(CommonConstants.SELENIUM_PACKAGE+collector.getClassName()+"KWC").getConstructor(constructorTypes);
		KWCSelenium kwc = (KWCSelenium) constructor.newInstance(constructorParams);
		kwc.setMyCollector(collector.getDetail());
		collector.setStartPage(start);
		collector.setEndPage(end);
		/////////////////////////////////////////////////////////////
//		KWCSelenium kwc = (KWCSelenium) cls;
		Agency Agency = collectorService.getAgencyNameForCollector(collector.getToSite().getGroup().getAgency());
		String agencyName = Agency.getName();
		collector.getToSite().getGroup().setAgencyName(agencyName);
		collector.setChannel("기관");
		
		String info = agencyName +" " + collector.getToSite().getName() +"/" + collector.getName();
		StringBuffer logBuffer = new StringBuffer();
		logBuffer.append("["+CommonUtil.getCurrentTimeStr("")+"]" + info + " START : PAGE("+start+"~"+end+") \n");
		
		//시작
		result = kwc.crawlWeb(crawlRepository);
		String status = "";
		if(result == 0) {
			collectorService.updateStatus(collector.getPk(), "SW");
			status = "[SUCCESS]";
		}else {
			collectorService.updateStatus(collector.getPk(), "FW");
			status = "[FAIL]";
		}
		logBuffer.append("["+CommonUtil.getCurrentTimeStr("")+"]" + info + " END " + status + " : PAGE("+start+"~"+kwc.getEndPage()+")");
		KLog log = new KLog();
		log.setComment(info+ " 수집 완료 : " + status);
		log.setLogCont(logBuffer.toString());
		commonService.saveLog(log);
		
		return result;
	}
	
}
