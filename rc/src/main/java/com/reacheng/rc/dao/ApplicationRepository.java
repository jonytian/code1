package com.reacheng.rc.dao;

import com.reacheng.rc.entity.Application;
import com.reacheng.rc.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ApplicationRepository extends BaseRepository<Application,Integer> {


    @Query(value = "FROM Application  a where a.project.projectName =:projectName")
    List<Application>  findByProjectName(@Param("projectName") String projectName );

    @Query(value = "FROM Application  a where a.project.projectName =:projectName and a.handleStatus =:handleStatus")
    List<Application>  findByProjectNameAndStatus(@Param("projectName") String projectName ,@Param("handleStatus") String handleStatus );

    @Query(value = "FROM Application  a where a.packageName =:packageName and a.project.projectName =:projectName and a.testImei =:imei and a.status =:status ")
    List<Application>  findByAll(@Param("packageName") String packageName, @Param("projectName") String projectName , @Param("imei") String imei ,@Param("status") String status );

    @Query(value = "FROM Application  a where  a.project.projectName =:projectName and a.testImei =:imei and a.status =:status")
    List<Application>  findByAll( @Param("projectName") String projectName , @Param("imei") String imei ,@Param("status") String status);

    @Query(value = "FROM Application  a where  a.packageName =:packageName and a.project.id =:projectId and a.version=:version")
    Application findByPackageNameAndProjectNameAndVersion(@Param("packageName") String packageName, @Param("projectId") Integer projectId, @Param("version") String version);

}
