package mysqltest.test1.repository;

import mysqltest.test1.entity.CarefulEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarefulRepository extends JpaRepository<CarefulEntity, Long> {

    List<CarefulEntity> findBy제품명AContainingIgnoreCase(String keyword);
    List<CarefulEntity> findBy제품명BContainingIgnoreCase(String keyword);


    boolean existsBy제품명AAnd제품명B(String 제품명A, String 제품명B);
    boolean existsBy제품명BAnd제품명A(String 제품명B, String 제품명A);


}
