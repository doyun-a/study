package mysqltest.test1.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import mysqltest.test1.entity.Person;
import mysqltest.test1.service.Pill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
public class PersonController {


    @GetMapping("/test")
    public String test() {

        return "Test successful!";

    }

    @Autowired
    private Pill pill;

    @GetMapping("/api/exchange-rate")
    public String getpill() throws JsonProcessingException, URISyntaxException {
        pill.fetchDataAndSave();
        return "Test successful!";
    }
}
