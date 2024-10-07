package mysqltest.test1.service;

import mysqltest.test1.entity.RegisterEntity;
import mysqltest.test1.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterService {

    @Autowired
    private RegisterRepository registerRepository; // 등록 DB에 대한 Repository

    public void registerDrug(String drugName) {
        // 약물 등록 기능
        RegisterEntity registerEntity = new RegisterEntity();
        registerEntity.setDrugName(drugName);
        registerRepository.save(registerEntity);
    }

    public List<String> getRegisteredDrugs() {
        // 등록된 약물 리스트 가져오기
        return registerRepository.findAllDrugNames(); // 등록된 약물 이름 리스트 반환
    }
}
