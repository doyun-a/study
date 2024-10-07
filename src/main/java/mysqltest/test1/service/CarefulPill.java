package mysqltest.test1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import mysqltest.test1.dto.CarefulDTO;
import mysqltest.test1.entity.CarefulEntity;
import mysqltest.test1.repository.CarefulRepository;
import mysqltest.test1.repository.PillRepository1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
public class CarefulPill {

    @Autowired
    private PillRepository1 pillRepository;

    @Autowired
    private CarefulRepository carefulRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager em;

    private static final String BASE_URL = "https://api.odcloud.kr/api/15089525/v1/uddi:3f2efdac-942b-494e-919f-8bdc583f65ea";
    private static final String SERVICE_KEY = "fYHpGdZ%2FNFBUjUBE2UX2QdKVJBrO7aHRwAVFryPGIfJEK8W3%2BMbFyUauY7UECLk2iNpQYLqPkLJxCx2uRfxykg%3D%3D";
    private static final int ITEMS_PER_PAGE = 100;

    public void fetchcarefulDataAndSave() throws JsonProcessingException, URISyntaxException, ExecutionException, InterruptedException {
        int totalCount = getTotalCount();

        Long countdb = (Long) em.createQuery("SELECT COUNT(c) FROM CarefulEntity c").getSingleResult();
        if (countdb == 0) {
            int totalPages = (int) Math.ceil((double) totalCount / ITEMS_PER_PAGE);
            List<CompletableFuture<Void>> futures = new ArrayList<>();

            for (int page = 1; page <= totalPages; page++) {
                final int currentPage = page; // final로 선언하여 람다식에서 사용
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                    try {
                        fetchAndSavePage(currentPage);
                    } catch (Exception e) {
                        e.printStackTrace(); // 에러 처리
                    }
                });
                futures.add(future);
            }

            // 모든 작업이 완료될 때까지 대기
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } else {
            System.out.print("Already up to date.");
        }
    }

    private void fetchAndSavePage(int page) throws JsonProcessingException, URISyntaxException {
        String url = String.format("%s?page=%d&perPage=%d&serviceKey=%s", BASE_URL, page, ITEMS_PER_PAGE, SERVICE_KEY);
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String jsonData = response.getBody();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        JsonNode itemsNode = rootNode.path("data");
        List<CarefulDTO> carefulDTOList = objectMapper.readValue(itemsNode.toString(), new TypeReference<List<CarefulDTO>>() {});

        List<CarefulEntity> entitiesToSave = new ArrayList<>();
        for (CarefulDTO carefulDto : carefulDTOList) {
            CarefulEntity entity = new CarefulEntity();
            entity.set제품명A(carefulDto.get제품명A());
            entity.set제품명B(carefulDto.get제품명B());
            entity.set제품코드A(carefulDto.get제품코드A());
            entity.set제품코드B(carefulDto.get제품코드B());
            entitiesToSave.add(entity);
        }

        // 일괄 저장
        if (!entitiesToSave.isEmpty()) {
            carefulRepository.saveAll(entitiesToSave);
        }
    }

    // 전체 데이터 수 가져오는 메서드
    private int getTotalCount() throws JsonProcessingException, URISyntaxException {
        String url = String.format("%s?page=%d&perPage=%d&serviceKey=%s", BASE_URL, 1, ITEMS_PER_PAGE, SERVICE_KEY);
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String jsonData = response.getBody();

        JsonNode rootNode = objectMapper.readTree(jsonData);
        return rootNode.path("totalCount").asInt();
    }
}
