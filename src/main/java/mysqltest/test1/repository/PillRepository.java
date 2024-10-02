package mysqltest.test1.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mysqltest.test1.entity.PillEntity;
import org.springframework.stereotype.Repository;

@Repository
public class PillRepository {

    @PersistenceContext
    private EntityManager em;  // EntityManager 주입

    @Transactional
    public void save(PillEntity entity)
    {
        em.persist(entity);  // Person 객체를 영속성 컨텍스트에 저장
    }

    public PillEntity findPillById(Long id)
    {
        return em.find(PillEntity.class, id);
    }




}