package konantech.ai.aikwc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import konantech.ai.aikwc.entity.collectors.BasicCollector;

public interface BasicCollectorRepository extends ECollectorRepository<BasicCollector>, JpaRepository<BasicCollector, Integer> {

}
