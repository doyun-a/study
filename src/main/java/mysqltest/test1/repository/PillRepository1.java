package mysqltest.test1.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mysqltest.test1.entity.CarefulEntity;
import mysqltest.test1.entity.PillEntity;
import org.springframework.stereotype.Repository;

@Repository
public class PillRepository1 {

    @PersistenceContext
    private EntityManager em;  // EntityManager 주입

    @Transactional
    public void save(PillEntity entity)
    {
        em.persist(entity);  // Person 객체를 영속성 컨텍스트에 저장
    }


    @Transactional
    public void savecareful(CarefulEntity carefulEntity)
    {
        em.persist(carefulEntity);
    }

    public PillEntity findPillById(Long id)
    {
        return em.find(PillEntity.class, id);
    }

    @Transactional
    public void save(CarefulEntity entity) {
        em.persist(entity);

    }

}