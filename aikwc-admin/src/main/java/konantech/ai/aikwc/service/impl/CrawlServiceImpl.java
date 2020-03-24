package konantech.ai.aikwc.service.impl;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import konantech.ai.aikwc.common.config.AsyncConfig;
import konantech.ai.aikwc.common.config.CheckStatusHandler;
import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.common.utils.KWCSelenium;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Crawl;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.service.CollectorService;
import konantech.ai.aikwc.service.CrawlService;

@Service("CrawlService")
public class CrawlServiceImpl implements CrawlService {

	@Value("${chrome.web.driver.path}")
	String driverPath;
	
	@Autowired
	AsyncConfig asyncConfig;
	
	@Autowired
	CollectorService collectorService;
	
	@Autowired
	CrawlRepository crawlRepository;
	
	@Autowired
	CheckStatusHandler statusHandler;
	
	@Async("kwcExecutor")
	public void webCrawl(Collector collector ) {
		KWCSelenium kwc = new KWCSelenium(driverPath) {
			@Override
			public int crawlWeb(Object collector ,JpaRepository repository) {
				System.out.println(">>>>>>>>>>>>crawlWeb");
//				this.startUrl = collector.getStartUrl();
				try {
					Collector c ;
					if(collector instanceof Collector)
						c = (Collector) collector;
					else
						throw new Exception("Collector 형변환 오류");
					CrawlRepository repo;
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
					
					int startPage = Integer.parseInt(c.getStartPage());
					int endPage = Integer.parseInt(c.getEndPage());
					openBrowser();
					
					crawlPerPage(startPage, endPage, c, repo);
				}catch(Exception e) {
					System.out.println(">>>>>>>>>>>crawlWeb() : error");
					e.printStackTrace();
					return 1;
				}finally {
					closeBrowser();
				}
				return 0;
			}
			
			public void crawlPerPage(int startPage, int endPage,Collector c, CrawlRepository repo ) throws Exception {
				boolean isTimePattern = StringUtils.containsAny(c.getWdatePattern(), "Hms");
				boolean idIsXpath = StringUtils.startsWith(c.getContId(), "//");
				for(int page =startPage ; page <= endPage; page++) {
					List<Crawl> dataList = new ArrayList<Crawl>();
					webDriver.get(startUrl+page);
					
					List<WebElement> titleLinks = webDriver.findElements(titleLink);
					String listWin = webDriver.getWindowHandle();
					System.out.println(listWin);
					for(WebElement titleLink : titleLinks) {
						Actions action = new Actions(webDriver);
						action.keyDown(Keys.CONTROL).click(titleLink).perform();
						Thread.sleep(300);
					
						Set<String> windows = webDriver.getWindowHandles();
						for(String win : windows) {
							if(!listWin.equals(win)) {
								Crawl obj = new Crawl();
								try {
//									System.out.println(win);
									webDriver.switchTo().window(win);
									//사이트내용
									obj.setChannel(c.getToSite().getGroup().getName());
									obj.setSiteName(c.getToSite().getGroup().getAgencyName()+"/"+c.getToSite().getName());
									obj.setBoardName(c.getName());
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
										System.out.println("****************** DateTimeParseException ********************");
										obj.setWtimeStr(webDriver.findElement(writeDate).getText());
									}
									dataList.add(obj);
									
									webDriver.close();
									webDriver.switchTo().window(listWin);
								}catch(org.openqa.selenium.NoSuchElementException ex) { //
									ex.printStackTrace();
//									webDriver.close();
//									webDriver.switchTo().window(listWin);
//									continue;
									throw ex;
								}catch(Exception e) {
									throw e; // 이외 모든 예외에는 크롤링 중단
								}
							}
							
						}
					}
					//data insert
					repo.saveAll(dataList);
				}
			}
		};
		
		
		System.out.println(">>>>>>>>>>>>>> cnt: "+asyncConfig.getTaskCount());
		// crawling 시작
		
		try {
			//1. DB status update Running
			collectorService.updateStatus(collector.getPk(), "R");
			//2. crawling 페이지별로  insert하는 크롤링
			int result = kwc.crawlWeb(collector, crawlRepository);
			
			//3. DB status update Success+Wait
			if(result == 0)
				collectorService.updateStatus(collector.getPk(), "SW");
			else
				collectorService.updateStatus(collector.getPk(), "FW");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Async("kwcExecutor")
	public void webCrawl2(Collector collector ) {
		KWCSelenium kwc = new KWCSelenium(driverPath) {
			@Override
			public int crawlWeb(Object collector ,JpaRepository repository) {
				System.out.println(">>>>>>>>>>>>crawlWeb");
//				this.startUrl = collector.getStartUrl();
				try {
					Collector c ;
					if(collector instanceof Collector)
						c = (Collector) collector;
					else
						throw new Exception("Collector 형변환 오류");
					CrawlRepository repo;
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
					
					int startPage = Integer.parseInt(c.getStartPage());
					int endPage = Integer.parseInt(c.getEndPage());
					openBrowser();
					
					crawlPerPage(startPage, endPage, c, repo);
				}catch(Exception e) {
					System.out.println(">>>>>>>>>>>crawlWeb() : error");
					e.printStackTrace();
					return 1;
				}finally {
					closeBrowser();
				}
				return 0;
			}
			
			public void crawlPerPage(int startPage, int endPage,Collector c, CrawlRepository repo ) throws Exception {
				boolean isTimePattern = StringUtils.containsAny(c.getWdatePattern(), "Hms");
				boolean idIsXpath = StringUtils.startsWith(c.getContId(), "//");
				for(int page =startPage ; page <= endPage; page++) {
					List<Crawl> dataList = new ArrayList<Crawl>();
					webDriver.get(startUrl+page);
					
					int len = webDriver.findElements(titleLink).size();
//					String listWin = webDriver.getWindowHandle();
					for(int i=0;i<len;i++) {// page back() 하면 pageSession이 달라서 titleLink List를 새롭게 구해야함 
						List<WebElement> titleLinks = webDriver.findElements(titleLink);
						titleLinks.get(i).sendKeys(Keys.ENTER);
						Thread.sleep(3000);
					
						Crawl obj = new Crawl();
						try {
							//사이트내용
							obj.setChannel(c.getToSite().getGroup().getName());
							obj.setSiteName(c.getToSite().getGroup().getAgencyName()+"/"+c.getToSite().getName());
							obj.setBoardName(c.getName());
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
								System.out.println("****************** DateTimeParseException ********************");
								obj.setWtimeStr(webDriver.findElement(writeDate).getText());
							}
							dataList.add(obj);
							webDriver.navigate().back();
							Thread.sleep(5000);
						}catch(org.openqa.selenium.NoSuchElementException ex) { //
							ex.printStackTrace();
//									webDriver.close();
//									webDriver.switchTo().window(listWin);
//									continue;
							throw ex;
						}catch(Exception e) {
							throw e; // 이외 모든 예외에는 크롤링 중단
						}
					}
					//data insert
					repo.saveAll(dataList);
				}
			}
		};
		
		
		System.out.println(">>>>>>>>>>>>>> cnt: "+asyncConfig.getTaskCount());
		// crawling 시작
		
		try {
			//1. DB status update Running
//			collectorService.updateStatus(collector.getPk(), "R");
//			//2. crawling 페이지별로  insert하는 크롤링
//			int result = kwc.crawlWeb(collector, crawlRepository);
//			
//			//3. DB status update Success+Wait
//			if(result == 0)
//				collectorService.updateStatus(collector.getPk(), "SW");
//			else
//				collectorService.updateStatus(collector.getPk(), "FW");
			
			try {
				statusHandler.sendCollectorStatus();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
