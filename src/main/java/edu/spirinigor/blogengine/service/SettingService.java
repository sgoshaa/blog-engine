package edu.spirinigor.blogengine.service;

import edu.spirinigor.blogengine.api.request.SettingsRequest;
import edu.spirinigor.blogengine.api.response.SettingResponse;
import edu.spirinigor.blogengine.exception.UserRegistrationException;
import edu.spirinigor.blogengine.model.GlobalSetting;
import edu.spirinigor.blogengine.model.enums.GlobalSettingEnum;
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

    public SettingResponse getGlobalSetting() {
        List<GlobalSetting> globalSettingList = globalSettingRepository.findAll();
        SettingResponse settingResponse = new SettingResponse();
        globalSettingList.stream().forEach(globalSetting -> {
            if (globalSetting.getCode().equals("MULTIUSER_MODE")) {
                settingResponse.setMultiuserMode(!globalSetting.getValue().equals("NO"));
            } else if (globalSetting.getCode().equals("POST_PREMODERATION")) {
                settingResponse.setPostPremoderation(!globalSetting.getValue().equals("NO"));
            } else {
                settingResponse.setStatisticsIsPublic(!globalSetting.getValue().equals("NO"));
            }
        });
        return settingResponse;
    }

    @Transactional
    public void putGlobalSetting(SettingsRequest settingsRequest) {
        List<GlobalSetting> all = globalSettingRepository.findAll();
        ArrayList<GlobalSetting> globalSettings = new ArrayList<>();
        all.stream().forEach(globalSetting -> {
            if (globalSetting.getCode().equals("MULTIUSER_MODE")) {
                globalSetting.setValue(settingsRequest.getMultiUserMode() ? "YES" : "NO");
            }
            if (globalSetting.getCode().equals("POST_PREMODERATION")) {
                globalSetting.setValue(settingsRequest.getPostPreModeration() ? "YES" : "NO");
            }
            if (globalSetting.getCode().equals("STATISTICS_IS_PUBLIC")) {
                globalSetting.setValue(settingsRequest.getStatisticsIsPublic() ? "YES" : "NO");
            }
        });
        globalSettingRepository.saveAll(all);
    }

    public void checkingPossibilityRegistration() {
        GlobalSetting byCode = globalSettingRepository.findByCode(GlobalSettingEnum.MULTIUSER_MODE.toString());
        if (byCode.getValue().equals("NO")) {
            throw new UserRegistrationException("Регистрация закрыта");
        }
    }

    public Boolean isPreModeration(){
        GlobalSetting byCode = globalSettingRepository.findByCode(GlobalSettingEnum.POST_PREMODERATION.toString());
        if (byCode.getValue().equals("NO")) {
            return false;
        }
        return true;
    }

    public Boolean isStatisticsIsPublic(){
        GlobalSetting statisticsIsPublic = globalSettingRepository.findByCode(GlobalSettingEnum.STATISTICS_IS_PUBLIC.toString());
        if(statisticsIsPublic.getValue().equals("NO")){
            return false;
        }
        return true;
    }

}
