package mysqltest.test1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mysqltest.test1.dto.MyDTO;
import mysqltest.test1.entity.PillEntity;
import mysqltest.test1.repository.PillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Transactional
public class Pill {

    @Autowired
    private PillRepository pillRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager em;

    private static final String BASE_URL = "https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList";
    private static final String SERVICE_KEY = "fYHpGdZ%2FNFBUjUBE2UX2QdKVJBrO7aHRwAVFryPGIfJEK8W3%2BMbFyUauY7UECLk2iNpQYLqPkLJxCx2uRfxykg%3D%3D";
    private static final int ITEMS_PER_PAGE = 100;

    public void fetchDataAndSave() throws JsonProcessingException, URISyntaxException {
        // 1. 첫 페이지 호출하여 전체 데이터 수 가져오기
        int totalCount = getTotalCount();

        Long countdb = (Long) em.createQuery("SELECT COUNT(p) FROM PillEntity p").getSingleResult();
        if (countdb == 0) {

            // 2. 전체 페이지 수 계산
            int totalPages = (int) Math.ceil((double) totalCount / ITEMS_PER_PAGE);
            List<PillEntity> entitiesToSave = new ArrayList<>();

            // 3. 모든 페이지에서 데이터 가져오기
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int page = 1; page <= totalPages; page++) {
                final int currentPage = page; // 람다 표현식에서 사용할 수 있도록 final로 선언
                futures.add(CompletableFuture.runAsync(() -> {
                    try {
                        List<PillEntity> pageEntities = fetchPageData(currentPage);
                        synchronized (entitiesToSave) {
                            entitiesToSave.addAll(pageEntities);
                            // 1000개 단위로 저장
                            if (entitiesToSave.size() >= 1000) {
                                pillRepository.saveAll(entitiesToSave);
                                entitiesToSave.clear(); // 리스트 초기화
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // 예외 처리
                    }
                }));
            }

            // 4. 모든 비동기 작업 완료 기다리기
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // 5. 남은 데이터 저장
            if (!entitiesToSave.isEmpty()) {
                pillRepository.saveAll(entitiesToSave);
            }
        } else {
            System.out.print("Already up to date.");
        }
    }

    // 페이지별 데이터 가져오기
    private List<PillEntity> fetchPageData(int page) throws JsonProcessingException, URISyntaxException {
        String url = String.format("%s?serviceKey=%s&numOfRows=%d&pageNo=%d&type=json", BASE_URL, SERVICE_KEY, ITEMS_PER_PAGE, page);
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String jsonData = response.getBody();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode itemsNode = rootNode.path("body").path("items");

        List<MyDTO> myDtoList = objectMapper.readValue(itemsNode.toString(), new TypeReference<List<MyDTO>>() {});

        List<PillEntity> entities = new ArrayList<>();
        for (MyDTO myDto : myDtoList) {
            PillEntity entity = new PillEntity();
            entity.setItemName(myDto.getItemName());
            entity.setItemSeq(myDto.getItemSeq());
            entity.setEfcyQesitm(myDto.getEfcyQesitm());
            entity.setUseMethodQesitm(myDto.getUseMethodQesitm());
            entity.setAtpnWarnQesitm(myDto.getAtpnWarnQesitm());
            entity.setAtpnQesitm(myDto.getAtpnQesitm());
            entity.setIntrcQesitm(myDto.getIntrcQesitm());
            entity.setSeQesitm(myDto.getSeQesitm());
            entity.setDepositMethodQesitm(myDto.getDepositMethodQesitm());

            if (myDto.getItemName().length() <= 500) {
                entities.add(entity);
            }
        }
        return entities;
    }

    // 전체 데이터 수를 가져오는 메서드
    private int getTotalCount() throws JsonProcessingException, URISyntaxException {
        String url = String.format("%s?serviceKey=%s&numOfRows=%d&pageNo=%d&type=json", BASE_URL, SERVICE_KEY, ITEMS_PER_PAGE, 1);
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String jsonData = response.getBody();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        return rootNode.path("body").path("totalCount").asInt(); // totalCount 필드 가져오기
    }
}
