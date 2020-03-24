package com.reacheng.rc.dao;

import com.reacheng.rc.entity.Device;
import com.reacheng.rc.entity.Whitelist;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WhitelistRepository extends BaseRepository<Whitelist, Integer> {

    @Query(value = "FROM Whitelist  w where w.projectId=:projectId and w.application.id=:appId ")
    List<Whitelist> findByProjectIdAndAppId(@Param("projectId") Integer projectId,@Param("appId") Integer appId);

    @Query(value = "FROM Whitelist  w where w.imei=:imei and w.application.id=:appId")
    Whitelist findByImeiAndAppId(@Param("imei") String imei,@Param("appId") Integer appId);

    @Query(value = "FROM Whitelist  w where w.imei=:imei and w.application.packageName=:packageName")
    List<Whitelist> findByImeiAndPackageName(@Param("imei") String imei,@Param("packageName") String packageName);

    @Query(value = "FROM Whitelist  w where w.imei=:imei")
    List<Whitelist> findByImei(@Param("imei") String imei);


    @Query(value = "FROM Whitelist  w where w.imei=:imei and  w.projectId=:projectId")
    List<Whitelist> findByImeiAndProjectId(@Param("imei") String imei,@Param("projectId") Integer projectId);
    @Query(value = "FROM Whitelist  w where w.imei=:imei and  w.projectId=:projectId and w.application.packageName=:packageName")
    List<Whitelist> findByImeiAndPackageNameAndProjectId(@Param("imei") String imei,@Param("packageName") String packageName,@Param("projectId") Integer projectId);



    @Query(value="FROM Whitelist  w where w.projectId=:projectId and w.application.id=:appId ")
    Page<Whitelist> findWhiteByProId(@Param("projectId") Integer projectId,@Param("appId") Integer appId, Pageable pageable);

    @Query(value = "FROM Whitelist  w where w.projectId=:projectId and w.status =:status and w.application.id=:appId ")
    List<Whitelist> findByProjectIdAndStatus(@Param("projectId") Integer projectId ,@Param("status") String status,@Param("appId") Integer appId);
}
