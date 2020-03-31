package konantech.ai.aikwc.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;

import konantech.ai.aikwc.entity.Collector;

public interface CrawlService {

	@Async("kwcExecutor")
	public CompletableFuture webCrawlThread(Collector collector) throws Exception;
	
	public int webCrawlDefault(Collector collector) throws Exception;
}
