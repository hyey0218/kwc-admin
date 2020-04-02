package konantech.ai.aikwc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import konantech.ai.aikwc.entity.collectors.BasicCollector;

@Repository
public interface BasicCollectorRepository extends JpaRepository<BasicCollector, Integer> {

}
