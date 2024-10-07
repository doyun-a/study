package mysqltest.test1.repository;

import mysqltest.test1.entity.PillEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PillRepository extends JpaRepository<PillEntity, Long> {

}
