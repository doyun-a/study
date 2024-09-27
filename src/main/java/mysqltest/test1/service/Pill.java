package mysqltest.test1.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import mysqltest.test1.repository.PillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Service
@Transactional
public class Pill {

    @Autowired
    private PillRepository pillRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String BASE_URL = "https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList";
    private static final String SERVICE_KEY = "fYHpGdZ%2FNFBUjUBE2UX2QdKVJBrO7aHRwAVFryPGIfJEK8W3%2BMbFyUauY7UECLk2iNpQYLqPkLJxCx2uRfxykg%3D%3D";
    private static final int ITEMS_PER_PAGE = 100;

    public void fetchDataAndSave() throws JsonProcessingException, URISyntaxException {
        // 1. 첫 페이지 호출하여 전체 데이터 수 가져오기
        int totalCount = getTotalCount();


        // 2. 전체 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / ITEMS_PER_PAGE);


        // 3. 모든 페이지에서 데이터 가져오기
        for (int page = 1; page <= totalPages; page++) {
            String url = String.format("%s?serviceKey=%s&numOfRows=%d&pageNo=%d&type=json", BASE_URL, SERVICE_KEY, ITEMS_PER_PAGE, page);
            URI uri = new URI(url);
            ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
            String jsonData = response.getBody();

            // 4. 응답 데이터 로깅
            //System.out.println("API 응답 데이터: " + jsonData);

            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode itemsNode = rootNode.path("body").path("items");

            // 5. JSON 데이터를 필요한 필드만 포함한 DTO로 변환
            List<MyDTO> myDtoList = objectMapper.readValue(itemsNode.toString(), new TypeReference<List<MyDTO>>() {});

            // 6. MyDTO를 MyEntity로 변환하여 데이터베이스에 저장
            for (MyDTO myDto : myDtoList) {
                PillEntity entity = new PillEntity();
                entity.setItemName(myDto.getItemName());
                entity.setItemSeq(myDto.getItemSeq());
                if (myDto.getItemName().length() > 500) {
                    continue;
                }
                pillRepository.save(entity);

            }
        }
    }

    // 전체 데이터 수를 가져오는 메서드
    private int getTotalCount() throws JsonProcessingException, URISyntaxException {
        String url = String.format("%s?serviceKey=%s&numOfRows=%d&pageNo=%d&type=json", BASE_URL, SERVICE_KEY, ITEMS_PER_PAGE, 1);
        URI uri = new URI(url);
        ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class);
        String jsonData = response.getBody();

        // 응답 데이터 파싱하여 전체 데이터 수를 반환
        JsonNode rootNode = objectMapper.readTree(jsonData);
        return rootNode.path("body").path("totalCount").asInt(); // totalCount 필드 가져오기
    }
}
