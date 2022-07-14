package edu.spirinigor.blogengine.repository;

import edu.spirinigor.blogengine.model.GlobalSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalSettingRepository extends JpaRepository<GlobalSetting, Integer> {

    GlobalSetting findByCode(String setting);

}
