package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.SettingsRequest;
import edu.spirinigor.blogengine.api.response.SettingResponse;
import edu.spirinigor.blogengine.model.GlobalSetting;
import edu.spirinigor.blogengine.repository.GlobalSettingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Transactional
    public void putGlobalSetting(SettingsRequest settingsRequest) {
        List<GlobalSetting> all = globalSettingRepository.findAll();
        ArrayList<GlobalSetting> globalSettings = new ArrayList<>();
        if (!settingsRequest.getMultiUserMode()){
            GlobalSetting multiUser = all.stream()
                    .filter(globalSetting -> globalSetting.getCode().equals("MULTIUSER_MODE"))
                    .peek(globalSetting -> globalSetting.setValue("YES")).findFirst().get();
            globalSettings.add(multiUser);
        }
        if (!settingsRequest.getPostPreModeration()){
            GlobalSetting preModeration = all.stream()
                    .filter(globalSetting -> globalSetting.getCode().equals("POST_PREMODERATION"))
                    .peek(globalSetting -> globalSetting.setValue("YES")).findFirst().get();
            globalSettings.add(preModeration);
        }
        if (!settingsRequest.getStatisticsIsPublic()){
            GlobalSetting statistics = all.stream()
                    .filter(globalSetting -> globalSetting.getCode().equals("STATISTICS_IS_PUBLIC"))
                    .peek(globalSetting -> globalSetting.setValue("YES")).findFirst().get();
            globalSettings.add(statistics);
        }
        globalSettingRepository.saveAll(globalSettings);
    }
}
