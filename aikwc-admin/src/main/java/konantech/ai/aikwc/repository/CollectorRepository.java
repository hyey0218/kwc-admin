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
public interface CollectorRepository<T extends ECollector> extends BaseRepository<T> {

	@Query(value="select kc.*, "
			+ " kg.code as g_code, kg.name as g_name, ks.code as s_code, ks.name as s_name"
			+ " from v_collector kc, kwc_group kg, kwc_site ks"
			+ " where kc.site = ks.pk and ks.grp = kg.pk"
			, nativeQuery = true)
	public List<konantech.ai.aikwc.entity.collectors.Collector> findAllWithJoin();
	
	public List<konantech.ai.aikwc.entity.collectors.Collector> findByUseyn(@Param("useyn") String useyn);
	
	
	@Query(value="select * from v_collector"
			+ " where useyn='Y' and site = :site" 
			, nativeQuery = true)
	public List<konantech.ai.aikwc.entity.collectors.Collector> findAllInSite(@Param("site") String site);
	
	public List<konantech.ai.aikwc.entity.collectors.Collector> findBySite(@Param("site") String site);
	
	@Query(value="select v.pk, v.code, v.name, v.status, v.ctrt_start, v.ctrt_end, v.useyn, v.class_name, v.hashed, v.site"
			+ ", s.name as sitename ,s.grp ,  g.name as grpname "
			+ "from kwc.v_collector v , kwc.kwc_site s , kwc.kwc_group g "
			+ "where v.site = s.pk and s.grp = g.pk"
			, nativeQuery = true)
	public List<CollectorMapping> findInAgency(@Param("agency") int agency);
	
	
	@Modifying
	@Transactional
	@Query(value = " update v_collector set status = :stat where pk = :pk ", nativeQuery = true)
	public void updateStatus(@Param("pk") int pk, @Param("stat") String status);
	
}

@NoRepositoryBean
interface BaseRepository<T> extends JpaRepository<T, Integer>{
}