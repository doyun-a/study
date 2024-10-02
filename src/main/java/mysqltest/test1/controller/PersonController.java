package mysqltest.test1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import mysqltest.test1.entity.Person;
import mysqltest.test1.entity.PillEntity;
import mysqltest.test1.service.Pill;
import mysqltest.test1.service.PillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PersonController {

    @Autowired
    private PillService pillService;

    @Autowired
    private Pill pill;


    @ResponseBody
    @GetMapping("/api/exchange-rate")
    public String getpill() throws JsonProcessingException, URISyntaxException {
        pill.fetchDataAndSave();
        return "Test successful!";
    }

    @GetMapping("/search")
    public String searchDrugs(@RequestParam("name") String name, Model model) {
        List<PillEntity> pills = pillService.searchPillsByName(name);
        model.addAttribute("pills", pills); // 검색 결과를 모델에 추가
        return "home"; // search.html로 리턴
    }

    @GetMapping("/pill/{id}")
    public String getPillDetails(@PathVariable("id") Long id, Model model) {
        PillEntity pill = pillService.PillById(id); // DB에서 해당 약 정보를 가져옴
        model.addAttribute("pill", pill); // 약 정보를 모델에 추가
        return "pilldetails"; // pillDetails.html로 리턴
    }





}
