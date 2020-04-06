package konantech.ai.aikwc.selenium;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ThreadGuard;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.KLog;
import konantech.ai.aikwc.entity.collectors.BasicCollector;
import konantech.ai.aikwc.repository.CrawlRepository;
import konantech.ai.aikwc.repository.KLogRepository;
import konantech.ai.aikwc.service.CommonService;

public abstract class KWCSelenium<T>{
	
	private String driverPath;
//	private ChromeDriver webDriver;
	protected WebDriver webDriver;
	protected Collector collector;
	protected T c;
	
	public KLog log;
	
	public KWCSelenium() {}
	public KWCSelenium(String driverPath, Collector collector) {
		this.driverPath = driverPath;
		this.log = new KLog();
		this.collector = collector;
	}
	public abstract void setMyCollector(String jsonStr);
	
	public void openBrowser() throws Exception{
		System.setProperty("webdriver.chrome.driver", driverPath); 
		ChromeOptions options = new ChromeOptions();
		options.setPageLoadStrategy(PageLoadStrategy.EAGER); // only HTML document loading, (discards loding of css/image...)
//        options.setHeadless(true); // 특정 엘리먼트를 못찾음...; don't use
        options.setProxy(null);
        
        ChromeDriver webDriver = new ChromeDriver(options);
		webDriver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS); // implicit wait
//		WebDriver wd = (ChromeDriver)ThreadGuard.protect(webDriver);
		this.webDriver = ThreadGuard.protect(webDriver);
	}
	public void closeBrowser() {
		
		if( this.webDriver != null) {
			this.webDriver.quit();
		}
	}
	/**
	 * 기관 별로 크롤링 내용을 만들어서 사용합니다.
	 * @param collector
	 * @param repository
	 * @return 0: SUCCESS , 1: FAIL
	 * @throws Exception
	 */
	public abstract void prework() throws Exception;
	public abstract int work(CrawlRepository repository) throws Exception;
	public abstract void afterwork() throws Exception;
	
	/**
	 * 크롤링 작업 콜 메서드
	 * @param collector
	 * @param repository
	 */
	public int crawlWeb(CrawlRepository repository) {
		int ret = 1;
		try {
			prework();
			ret = work(repository);
			afterwork();
		}catch(Exception e) {
			e.printStackTrace();
			return 1;
		}finally {
			closeBrowser();
		}
		return ret;
	}
	/**
	 * 로그 테이블 적재
	 */
	public void insertLog(CommonService service) {
		if(!StringUtils.isEmpty(log.getAgency()) ) {
			service.saveLog(log);
		}
	}
	
}
