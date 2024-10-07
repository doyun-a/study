package mysqltest.test1.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class CarefulEntity {

    @Id
    @GeneratedValue
    private int id;

    private String 제품명A;
    private String 제품명B;
    private String 제품코드A;
    private String 제품코드B;
}
