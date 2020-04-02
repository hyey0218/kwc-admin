package konantech.ai.aikwc.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;

import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.collectors.BasicCollector;

public interface CrawlService {

	@Async("kwcExecutor")
	public CompletableFuture webCrawlThread(Class collector, int pk, String start, String end) throws Exception;
	
	public int webCrawlDefault(Class collector, int pk, String start, String end) throws Exception;
}
