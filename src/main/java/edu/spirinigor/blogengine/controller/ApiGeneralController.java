package edu.spirinigor.blogengine.controller;

import edu.spirinigor.blogengine.api.request.SettingsRequest;
import edu.spirinigor.blogengine.api.response.InitResponse;
import edu.spirinigor.blogengine.api.response.SettingResponse;
import edu.spirinigor.blogengine.api.response.StatisticResponse;
import edu.spirinigor.blogengine.service.SettingService;
import edu.spirinigor.blogengine.service.StatisticService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final InitResponse initResponse;
    private final SettingService settingService;
    private final StatisticService statisticService;

    public ApiGeneralController(InitResponse initResponse, SettingService settingService, StatisticService statisticService) {
        this.initResponse = initResponse;
        this.settingService = settingService;
        this.statisticService = statisticService;
    }

    @GetMapping("/init")
    public ResponseEntity<InitResponse> getInit(){
        return new ResponseEntity<>(initResponse, HttpStatus.OK);
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingResponse> getSettings(){
        return new ResponseEntity<>(settingService.getGlobalSetting(),HttpStatus.OK);
    }

    @PutMapping("/settings")
    @PreAuthorize("hasAuthority('user:write')")
    public void putSettings(@RequestBody SettingsRequest settingsRequest){
        settingService.putGlobalSetting(settingsRequest);
    }

    @GetMapping("/statistics/my")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<StatisticResponse> getMyStatistics(){
        return new ResponseEntity<>(statisticService.getMyStatistic(),HttpStatus.OK);
    }

    @GetMapping("/statistics/all")
    public ResponseEntity<StatisticResponse> getAllStatistics(){
        return new ResponseEntity<>(statisticService.getAllStatistic(),HttpStatus.OK);
    }
}
