package konantech.ai.aikwc.service.impl;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

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
import konantech.ai.aikwc.common.utils.KWCSelenium;
import konantech.ai.aikwc.entity.Agency;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Crawl;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CommonService;
import konantech.ai.aikwc.service.CrawlService;

@Service("CrawlService")
public class CrawlServiceImpl implements CrawlService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Value("${chrome.web.driver.path}")
	String driverPath;
	
	@Autowired
	CollectorService collectorService;
	
	@Autowired
	CommonService commonService;
	
	@Autowired
	CrawlRepository crawlRepository;
	
	private KWCSelenium getKWCBot() {
		KWCSelenium kwc = new KWCSelenium(driverPath) {
			@Override
			public int crawlWeb(Object collector ,JpaRepository repository) {
				Collector c = null;
				CrawlRepository repo = null;
				List<Crawl> list = new ArrayList<Crawl>();
				try {
					if(collector instanceof Collector)
						c = (Collector) collector;
					else
						throw new Exception("Collector 형변환 오류");
					if(repository instanceof CrawlRepository)
						repo = (CrawlRepository) repository;
					else
						throw new Exception("CrawlRepository 형변환 오류");
					
					this.startUrl = c.getPageUrl();
					this.titleLink = By.xpath(c.getTitleLink());
					this.title = By.xpath(c.getTitle());
					this.content = By.xpath(c.getContent());
					this.writer = By.xpath(c.getWriter());
					this.writeDate = By.xpath(c.getWriteDate());
					this.log.setAgency(c.getToSite().getGroup().getAgency());
					
					int startPage = Integer.parseInt(c.getStartPage());
					int endPage = Integer.parseInt(c.getEndPage());
					c.setEndPage("");
					openBrowser();
					
					crawlPerPage(startPage, endPage, c, repo);
				}catch(Exception e) {
					e.printStackTrace();
					return 1;
				}finally {
					closeBrowser();
				}
				return 0;
			}
			
			public void crawlPerPage(int startPage, int endPage,Collector c, CrawlRepository repo) throws Exception {
				boolean isTimePattern = StringUtils.containsAny(c.getWdatePattern(), "Hms");
				boolean idIsXpath = StringUtils.startsWith(c.getContId(), "//");
				try {
					for(int page =startPage ; page <= endPage; page++) {
						webDriver.get(startUrl+page);
						int len = webDriver.findElements(titleLink).size();
						List<Crawl> dataList = new ArrayList<Crawl>();
	//					String listWin = webDriver.getWindowHandle();
						for(int i=0;i<len;i++) {// page back() 하면 pageSession이 달라서 titleLink List를 새롭게 구해야함 
							List<WebElement> titleLinks = webDriver.findElements(titleLink);
							titleLinks.get(i).sendKeys(Keys.ENTER);
							Thread.sleep(3000);
						
							Crawl obj = new Crawl();
							
								//사이트내용
								obj.setChannel(c.getChannel());
								obj.setSiteName(c.getToSite().getGroup().getAgencyName());
								obj.setBoardName(c.getToSite().getName()+"/"+c.getName());
								obj.setUrl(webDriver.getCurrentUrl());
								obj.setCrawledTime(LocalDateTime.now());
								if(idIsXpath)
									obj.setUniqkey(webDriver.findElement(By.xpath(c.getContId())).getAttribute("value"));
								else
									obj.setUniqkey(CommonUtil.getUriParamValue(webDriver.getCurrentUrl(), c.getContId()));
								//본문내용
								obj.setTitle(webDriver.findElement(title).getText());
								obj.setDoc(webDriver.findElement(content).getText());
								obj.setWriteId(webDriver.findElement(writer).getText());
								try {
									obj.setWriteTime(CommonUtil.stringToLocalDateTime(webDriver.findElement(writeDate).getText(), c.getWdatePattern(),isTimePattern) );
								}catch(java.time.format.DateTimeParseException de){
	//								System.out.println("****************** DateTimeParseException ********************");
									obj.setWtimeStr(webDriver.findElement(writeDate).getText());
								}
								dataList.add(obj);
								webDriver.navigate().back();
								Thread.sleep(3000);
						}
						repo.saveAll(dataList);
						c.setEndPage(String.valueOf(page));
					}
					
//				}catch(org.openqa.selenium.NoSuchElementException ex) { //
//									webDriver.close();
//									webDriver.switchTo().window(listWin);
//									continue;
//							throw ex;
				}catch(Exception e) {
					throw e; // 이외 모든 예외에는 크롤링 중단
				}finally {
				}
			}
		};
		return kwc;
	}
	
	@Async("kwcExecutor")
	public CompletableFuture webCrawlThread(Collector collector) throws Exception {
		logger.info("start task");
		StringBuffer logBuffer = new StringBuffer();
		String threadName = Thread.currentThread().getName();
		String colInfo = collector.getToSite().getName() + "/" + collector.getName();
		logBuffer.append("["+CommonUtil.getCurrentTimeStr("")+"] START TASK " + threadName ).append(" : " + colInfo +"\n");
		
		KWCSelenium kwc = getKWCBot();
		//2. crawling 페이지별로  insert하는 크롤링
		int result = kwc.crawlWeb(collector, crawlRepository);
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
		return CompletableFuture.completedFuture(result);
	}

	public int webCrawlDefault(Collector collector) throws Exception {
		logger.info("start task");
		StringBuffer logBuffer = new StringBuffer();
		String threadName = Thread.currentThread().getName();
		String colInfo = collector.getToSite().getName() + "/" + collector.getName();
		logBuffer.append("["+CommonUtil.getCurrentTimeStr("")+"] START TASK " + threadName ).append(" : " + colInfo +"\n");
		
		KWCSelenium kwc = getKWCBot();
		//2. crawling 페이지별로  insert하는 크롤링
		int result = kwc.crawlWeb(collector, crawlRepository);
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
	
	public void preworkForCrawling(Collector selectedCollector) {
		Agency Agency = collectorService.getAgencyNameForCollector(selectedCollector.getToSite().getGroup().getAgency());
		String agencyName = Agency.getName();
		selectedCollector.getToSite().getGroup().setAgencyName(agencyName);
		selectedCollector.setChannel("기관");
	}
	
}
