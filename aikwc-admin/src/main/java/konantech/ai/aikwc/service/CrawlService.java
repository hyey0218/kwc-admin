package konantech.ai.aikwc.service;

import org.springframework.scheduling.annotation.Async;

import konantech.ai.aikwc.entity.Collector;

public interface CrawlService {

	@Async("kwcExecutor")
	public void webCrawl(Collector collector);
	
	@Async("kwcExecutor")
	public void webCrawl2(Collector collector);
}
