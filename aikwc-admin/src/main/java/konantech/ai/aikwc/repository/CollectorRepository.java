package konantech.ai.aikwc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import konantech.ai.aikwc.entity.collectors.*;
import konantech.ai.aikwc.entity.ECollector;
import konantech.ai.aikwc.entity.Site;
import konantech.ai.aikwc.repository.mapping.CollectorMapping;


@Repository
public interface CollectorRepository extends ECollectorRepository<Collector>, JpaRepository<Collector, Integer>{

}