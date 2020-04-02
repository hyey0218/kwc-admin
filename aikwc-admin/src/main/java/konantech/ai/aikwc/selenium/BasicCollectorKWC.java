package konantech.ai.aikwc.selenium;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.springframework.data.jpa.repository.JpaRepository;

import konantech.ai.aikwc.common.utils.CommonUtil;
import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Crawl;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.service.CommonService;

public class BasicCollectorKWC extends KWCSelenium {
	/***** 데이터 테이블에 적재될 엘리먼트 *****/
	private String startUrl;
	private By titleLink;
	private By title;
	private By writer;
	private By writeDate;
	private By content;
	
	private BasicCollector c;

	public BasicCollectorKWC(String driverPath, BasicCollector c) {
		super(driverPath, c);
	}

	@Override
	public void prework() {
	}

	@Override
	public void afterwork() {
	}

	@Override
	public int work(JpaRepository repository) {
//		BasicCollector c = null;
		CrawlRepository repo = null;
		List<Crawl> list = new ArrayList<Crawl>();
		try {
//			if(collector instanceof BasicCollector)
//				this.c = (BasicCollector) collector;
//			else
//				throw new Exception("Collector 형변환 오류");
//			if(repository instanceof CrawlRepository)
//				repo = (CrawlRepository) repository;
//			else
//				throw new Exception("CrawlRepository 형변환 오류");
			
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
	
	
	public void crawlPerPage(int startPage, int endPage,BasicCollector c, JpaRepository repo) throws Exception {
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
			
//		}catch(org.openqa.selenium.NoSuchElementException ex) { //
//							webDriver.close();
//							webDriver.switchTo().window(listWin);
//							continue;
//					throw ex;
		}catch(Exception e) {
			throw e; // 이외 모든 예외에는 크롤링 중단
		}finally {
		}
	}

}
