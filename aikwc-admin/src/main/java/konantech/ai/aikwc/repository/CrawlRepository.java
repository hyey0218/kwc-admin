package konantech.ai.aikwc.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import konantech.ai.aikwc.entity.Crawl;

@Repository
public interface CrawlRepository extends JpaRepository<Crawl, Integer> {
	
//	public Crawl findByHashed(String hashed);
	public Optional<Crawl> findByHashed(String hashed);
}