package com.reacheng.rc.dao;

import com.reacheng.rc.entity.AppLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AppLogRepository extends BaseRepository<AppLog,Integer> {

    @Query(value = "FROM AppLog a where a.imei =:imei and a.projectName =:projectName and a.packageName =:packageName and a.versionCode =:versionCode")
    AppLog  findAppInfo (@Param("imei") String imei,@Param("projectName") String projectName ,@Param("packageName") String packageName,@Param("versionCode") String versionCode );

}
