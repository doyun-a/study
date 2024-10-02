package mysqltest.test1.service;

import jakarta.persistence.Access;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import mysqltest.test1.entity.PillEntity;
import mysqltest.test1.repository.PillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PillService {

    @Autowired
    private PillRepository pillRepository;

    @PersistenceContext
    private EntityManager em;


    @Transactional
    public void savePill(PillEntity pill) {
        pillRepository.save(pill);
    }


    public List<PillEntity> searchPillsByName(String name) {
        String jpql = "SELECT p FROM PillEntity p WHERE p.itemName LIKE :name"; // 'name'을 'itemName'으로 변경
        TypedQuery<PillEntity> query = em.createQuery(jpql, PillEntity.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }


    public PillEntity PillById(Long id) {
       return pillRepository.findPillById(id);
    }
}
