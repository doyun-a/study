package mysqltest.test1.service;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class PillEntity {

    @Id
    @GeneratedValue
    private int id;

    private String itemName;
    private String itemSeq;
}
