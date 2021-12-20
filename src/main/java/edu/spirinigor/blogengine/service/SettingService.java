package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.response.SettingResponse;
import edu.spirinigor.blogengine.model.GlobalSetting;
import edu.spirinigor.blogengine.repositories.GlobalSettingRepository;
import org.apache.maven.lifecycle.internal.LifecycleStarter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingService {
    private final GlobalSettingRepository globalSettingRepository;

    public SettingService(GlobalSettingRepository globalSettingRepository) {
        this.globalSettingRepository = globalSettingRepository;
    }

    public SettingResponse getGlobalSetting(){
       List<GlobalSetting> globalSettingList = globalSettingRepository.findAll();
       SettingResponse settingResponse = new SettingResponse();
       globalSettingList.stream().forEach(globalSetting -> {
           if (globalSetting.getCode().equals("MULTIUSER_MODE")) {
               settingResponse.setMultiuserMode(!globalSetting.getValue().equals("NO"));
           }else if(globalSetting.getCode().equals("POST_PREMODERATION")){
               settingResponse.setPostPremoderation(!globalSetting.getValue().equals("NO"));
           }else{
               settingResponse.setStatisticsIsPublic(!globalSetting.getValue().equals("NO"));
           }
       });
       return settingResponse;
    }
}
