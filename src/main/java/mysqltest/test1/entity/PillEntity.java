package mysqltest.test1.entity;


import jakarta.persistence.*;
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

    @Lob  // 긴 텍스트 데이터 저장
    @Column(columnDefinition = "TEXT")
    private String efcyQesitm;

    @Lob  // 긴 텍스트 데이터 저장
    @Column(columnDefinition = "TEXT")
    private String useMethodQesitm;

    @Lob  // 긴 텍스트 데이터 저장
    @Column(columnDefinition = "TEXT")
    private String atpnWarnQesitm;

    @Lob  // 긴 텍스트 데이터 저장
    @Column(columnDefinition = "TEXT")
    private String atpnQesitm;

    @Lob  // 긴 텍스트 데이터 저장
    @Column(columnDefinition = "TEXT")
    private String intrcQesitm;

    @Lob  // 긴 텍스트 데이터 저장
    @Column(columnDefinition = "TEXT")
    private String seQesitm;

    @Lob  // 긴 텍스트 데이터 저장
    @Column(columnDefinition = "TEXT")
    private String depositMethodQesitm;
}
