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

import konantech.ai.aikwc.entity.Collector;
import konantech.ai.aikwc.entity.Site;
import konantech.ai.aikwc.repository.mapping.CollectorMapping;


@Repository
public interface CollectorRepository extends JpaRepository<Collector, Integer>{
	
	public List<Collector> findByUseyn(@Param("useyn") String useyn);
	
	@Query(value="select * from kwc_collector"
			+ " where useyn='Y' and site = :site" 
			, nativeQuery = true)
	public List<Collector> findAllInSite(@Param("site") String site);
	
	public List<Collector> findBySite(@Param("site") String site);
	
//	@Query(value="select pk,class_name,view_name,code,name,ctrt_start,ctrt_end,useyn,status,dtype,site,dtype,param1,param2 "
//			+ "from kwc_collector kr where site in ("
//			+ "select pk from kwc_site where grp in ( "
//			+ "select pk from kwc_group kg where agency = :agency))"
//			, nativeQuery = true)
	@Query(value="select v.pk, v.code, v.name, v.status, v.ctrt_start, v.ctrt_end, v.useyn, v.class_name, v.site"
			+ ", s.name as sitename ,s.grp ,  g.name as grpname "
			+ "from kwc.kwc_collector v , kwc.kwc_site s , kwc.kwc_group g "
			+ "where v.site = s.pk and s.grp = g.pk and g.agency = :agency"
			, nativeQuery = true)
	public List<CollectorMapping> findInAgency(@Param("agency") int agency);
	
	@Query(value="select v.pk, v.code, v.name, v.status, v.ctrt_start, v.ctrt_end, v.useyn, v.class_name, v.site"
			+ ", s.name as sitename ,s.grp ,  g.name as grpname "
			+ "from kwc.kwc_collector v , kwc.kwc_site s , kwc.kwc_group g "
			+ "where v.site = s.pk and s.grp = g.pk"
			, nativeQuery = true)
	public List<CollectorMapping> findAllList();
	
	
	
	@Modifying
	@Transactional
	@Query(value = " update kwc_collector set status = :stat where pk = :pk ", nativeQuery = true)
	public void updateStatus(@Param("pk") int pk, @Param("stat") String status);
	
	
	@Modifying
	@Transactional
	@Query(value = " update kwc_collector set dtype = :dtype where pk = :pk ", nativeQuery = true)
	public void updateDtype(@Param("pk") int pk, @Param("dtype") String dtype);
	
	
	
	public List<Collector> findByClassName(@Param("className") String className);
	
}