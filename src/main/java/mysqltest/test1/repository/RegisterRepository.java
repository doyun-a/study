package mysqltest.test1.repository;


import mysqltest.test1.entity.RegisterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RegisterRepository extends JpaRepository<RegisterEntity, Long> {


    @Query("SELECT r.drugName FROM RegisterEntity r")  // JPQL 사용
    List<String> findAllDrugNames();
}
