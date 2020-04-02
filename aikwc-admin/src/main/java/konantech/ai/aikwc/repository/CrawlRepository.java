package konantech.ai.aikwc.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import konantech.ai.aikwc.entity.Crawl;

@Repository
public interface CrawlRepository extends JpaRepository<Crawl, Integer> {
	

}