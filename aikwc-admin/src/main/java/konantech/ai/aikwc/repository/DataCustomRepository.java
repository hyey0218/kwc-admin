package konantech.ai.aikwc.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import konantech.ai.aikwc.entity.KTask;

@Repository
public class DataCustomRepository {
	@PersistenceContext
	EntityManager em;
	
	@Transactional
	public void saveTest(String table) {
//		Query query = em.createNativeQuery("select * from "+table,KTask.class);
//		KTask k = (KTask) query.getSingleResult();
//		System.out.println(k.getPk());
		
		Query query = em.createNativeQuery("insert into " + table + "( pk )" + "values ( 33 )");
		query.executeUpdate();
		
	}
}
