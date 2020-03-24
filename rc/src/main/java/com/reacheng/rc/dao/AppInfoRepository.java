package com.reacheng.rc.dao;

import com.reacheng.rc.entity.AppInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppInfoRepository extends BaseRepository<AppInfo, Integer> {
    @Query(value = "FROM AppInfo a where a.appName =:appName")
    AppInfo findByName(@Param("appName") String appName);

    @Query(value = "FROM AppInfo a where a.packageName =:packageName")
    AppInfo findByPackageName(@Param("packageName") String packageName);
}
