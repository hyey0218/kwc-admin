package konantech.ai.aikwc.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

@Repository
public class DataCustomRepository {
	@PersistenceContext
	EntityManager entityManager;
}
