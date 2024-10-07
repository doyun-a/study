package mysqltest.test1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import mysqltest.test1.entity.PillEntity;
import mysqltest.test1.service.CarefulPill;
import mysqltest.test1.repository.CarefulRepository;
import mysqltest.test1.service.Pill;
import mysqltest.test1.service.PillService;
import mysqltest.test1.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
@RequiredArgsConstructor
public class PersonController {

    @Autowired
    private PillService pillService;

    @Autowired
    private Pill pill;

    @Autowired
    private CarefulPill carefulPill;

    @Autowired
    private  CarefulRepository carefulRepository;

    @Autowired
    private RegisterService registerService;

    @ResponseBody
    @GetMapping("/api/exchange-rate")
    public String getpill() throws JsonProcessingException, URISyntaxException {
        pill.fetchDataAndSave();
        return "Test successful!";
    }

    @ResponseBody
    @GetMapping("/api/careful")
    public String getcarefulpill() throws JsonProcessingException, URISyntaxException, ExecutionException, InterruptedException {
        carefulPill.fetchcarefulDataAndSave();
        return "Test successful!";
    }

//    @GetMapping("/search")
//    public String searchDrugs(@RequestParam("name") String name, Model model) {
//        List<PillEntity> pills = pillService.searchPillsByName(name);
//        model.addAttribute("pills", pills); // 검색 결과를 모델에 추가
//        return "home"; // search.html로 리턴
//    }

    @GetMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        // 검색 결과 가져오기
        List<String> searchResults = pillService.searchByKeyword(keyword);

        // 검색 결과를 Model에 추가
        model.addAttribute("keyword", keyword);
        model.addAttribute("results", searchResults);

        return "home"; // home.html 템플릿으로 데이터 전달
    }


    @GetMapping("/register")
    public String registerDrug(@RequestParam("drugName") String drugName, Model model) {

        List<String> registeredDrugs = registerService.getRegisteredDrugs();

        for (String registeredDrug : registeredDrugs) {
            if (carefulRepository.existsBy제품명AAnd제품명B(drugName, registeredDrug) ||
                    carefulRepository.existsBy제품명AAnd제품명B(registeredDrug, drugName)) {
                model.addAttribute("warningMessage", "병용금기입니다: " + drugName + "와(과) " + registeredDrug + "은(는) 함께 사용할 수 없습니다.");
                return "home";
            }
        }

        registerService.registerDrug(drugName);
        model.addAttribute("Message", drugName+"이 등록되었습니다");
        return "home";
    }



    @GetMapping("/pill/{id}")
    public String getPillDetails(@PathVariable("id") Long id, Model model) {
        PillEntity pill = pillService.PillById(id); // DB에서 해당 약 정보를 가져옴
        model.addAttribute("pill", pill); // 약 정보를 모델에 추가
        return "pilldetails"; // pillDetails.html로 리턴
    }





}
