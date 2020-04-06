package konantech.ai.aikwc.selenium;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

public class BasicCollectorKWC extends KWCSelenium<BasicCollector> {
	/***** 데이터 테이블에 적재될 엘리먼트 *****/
	private String startUrl;
	private By titleLink;
	private By title;
	private By writer;
	private By writeDate;
	private By content;
	
	private CrawlRepository repository;

	public BasicCollectorKWC() {}
	public BasicCollectorKWC(String driverPath, Collector collector) {
		super(driverPath, collector);
	}
	public void setMyCollector(String jsonStr) {
		BasicCollector my = (BasicCollector) CommonUtil.stringToJsonClass(jsonStr, BasicCollector.class);
		super.c = my;
	}

	@Override
	public void prework() throws Exception {
		System.out.println("BasicCollectorKWC START");
		logger.info("BasicCollectorKWC START");
		this.startUrl = c.getPageUrl();
		this.titleLink = By.xpath(c.getTitleLink());
		this.title = By.xpath(c.getTitle());
		this.content = By.xpath(c.getContent());
		this.writer = By.xpath(c.getWriter());
		this.writeDate = By.xpath(c.getWriteDate());
		openBrowser();
		logger.info(" PK : "+ c.getPk()+ ", page : " + collector.getStartPage() +" ~ " + collector.getEndPage());
	}

	@Override
	public void afterwork() {
		System.out.println("BasicCollectorKWC END");
		logger.info("BasicCollectorKWC END");
	}

	@Override
	public int work(CrawlRepository repository) throws Exception {
		this.repository = repository;
		int startPage = Integer.parseInt(collector.getStartPage());
		int endPage = Integer.parseInt(collector.getEndPage());
		boolean isTimePattern = StringUtils.containsAny(c.getWdatePattern(), "Hms");
		boolean idIsXpath = StringUtils.startsWith(c.getContId(), "//");
		collector.setEndPage(""); // 성공 페이지 다시 담을꺼임
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
					obj.setChannel(collector.getChannel());
					obj.setSiteName(collector.getToSite().getGroup().getAgencyName());
					obj.setBoardName(collector.getToSite().getName()+"/"+collector.getName());
					obj.setUrl(webDriver.getCurrentUrl());
					obj.setCrawledTime(LocalDateTime.now());
					if(idIsXpath)
						obj.setUniqkey(webDriver.findElement(By.xpath(c.getContId())).getAttribute("value"));
					else
						obj.setUniqkey(CommonUtil.getUriParamValue(webDriver.getCurrentUrl(), c.getContId()));
					
					String hashed = webDriver.getCurrentUrl()+obj.getUniqkey();
					obj.setHashed(CommonUtil.getEncMd5(hashed));
					//본문내용
					obj.setTitle(webDriver.findElement(title).getText());
					obj.setDoc(webDriver.findElement(content).getText());
					obj.setWriteId(webDriver.findElement(writer).getText());
					try {
						obj.setWriteTime(CommonUtil.stringToLocalDateTime(webDriver.findElement(writeDate).getText(), c.getWdatePattern(),isTimePattern) );
					}catch(java.time.format.DateTimeParseException de){
						obj.setWtimeStr(webDriver.findElement(writeDate).getText());
					}
					dataList.add(obj);
					webDriver.navigate().back();
					Thread.sleep(3000);
				}
				insertData(dataList);
				collector.setEndPage(String.valueOf(page));
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
		return 0;
	}
	
	
	public void insertData(List<Crawl> dataList) {
		dataList.forEach((data) -> {
			Optional<Crawl> op = repository.findByHashed(data.getHashed());
			if(op.isPresent()) {
				data.setIdx(op.get().getIdx());
			}
			repository.save(data);
		});
	}
}
