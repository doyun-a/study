package mysqltest.test1.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import mysqltest.test1.entity.CarefulEntity;
import mysqltest.test1.entity.PillEntity;
import mysqltest.test1.repository.CarefulRepository;
import mysqltest.test1.repository.PillRepository1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PillService {

    @Autowired
    private PillRepository1 pillRepository;

    @Autowired
    private CarefulRepository carefulRepository;
    @PersistenceContext
    private EntityManager em;


    @Transactional
    public void savePill(PillEntity pill) {
        pillRepository.save(pill);
    }

    @Transactional
    public void savecarefulPill(CarefulEntity carefulEntity) {
        pillRepository.savecareful(carefulEntity);

    }


    public List<PillEntity> searchPillsByName(String name) {
        String jpql = "SELECT p FROM PillEntity p WHERE p.itemName LIKE :name"; // 'name'을 'itemName'으로 변경
        TypedQuery<PillEntity> query = em.createQuery(jpql, PillEntity.class);
        query.setParameter("name", name + "%");
        return query.getResultList();
    }


    public List<String> searchByKeyword(String keyword) {
        List<CarefulEntity> resultsA = carefulRepository.findBy제품명AContainingIgnoreCase(keyword);
        List<CarefulEntity> resultsB = carefulRepository.findBy제품명BContainingIgnoreCase(keyword);

        Set<String> uniqueResults = new HashSet<>();


        for (CarefulEntity entity : resultsA) {
            uniqueResults.add(entity.get제품명A());
        }

        // 제품명B에서 중복 제거
        for (CarefulEntity entity : resultsB) {
            uniqueResults.add(entity.get제품명B());
        }

        return new ArrayList<>(uniqueResults);
    }







    public PillEntity PillById(Long id) {
       return pillRepository.findPillById(id);
    }
}
