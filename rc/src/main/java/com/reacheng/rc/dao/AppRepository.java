package com.reacheng.rc.dao;


import com.reacheng.rc.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppRepository extends JpaRepository<Application, Integer> {

    @Query(value="SELECT a.* FROM t_device_app d INNER JOIN t_application a ON d.app_id = a.id  WHERE d.imei =:imei AND d.project_name = :projectName ",nativeQuery=true)
    List<Application> selectAppList(@Param("imei") String imei, @Param("projectName") String projectName);

    @Query(value="SELECT a.* FROM t_device_app d INNER JOIN t_application a ON d.app_id = a.id  WHERE d.imei =:imei AND d.project_name = :projectName AND a.package_name = :packageName  ",nativeQuery=true)
    List<Application> selectAppList(@Param("imei") String imei, @Param("projectName") String projectName, @Param("packageName") String packageName);



}
