package konantech.ai.aikwc.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;


public interface CrawlService {

	@Async("kwcExecutor")
	public CompletableFuture webCrawlThread(int pk, String start, String end) throws Exception;
	
	public int webCrawlDefault( int pk, String start, String end) throws Exception;
}
