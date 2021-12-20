package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.response.InitResponse;
import edu.spirinigor.blogengine.api.response.SettingResponse;
import edu.spirinigor.blogengine.service.SettingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService settingService;

    public ApiGeneralController(InitResponse initResponse, SettingService settingService) {
        this.initResponse = initResponse;
        this.settingService = settingService;
    }

    @GetMapping("/api/init")
    public ResponseEntity<InitResponse> getInit(){
        return new ResponseEntity<>(initResponse, HttpStatus.OK);
    }

    @GetMapping("/api/settings")
    public ResponseEntity<SettingResponse> getSettings(){
        return new ResponseEntity<>(settingService.getGlobalSetting(),HttpStatus.OK);
    }

}
