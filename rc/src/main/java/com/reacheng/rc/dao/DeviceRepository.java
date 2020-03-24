package com.reacheng.rc.dao;

import com.reacheng.rc.entity.Device;
import com.reacheng.rc.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends BaseRepository<Device, Integer> {

    @Query(value = "FROM Device d where d.imei =:imei")
    Device findByImei(@Param("imei") String imei);

    @Query(value = "FROM Device d where d.imei =:imei and  d.project =:project")
    Device findByImeiAndProject(@Param("imei") String imei,@Param("project") Project project);


    @Query(value="select * from t_device s where s.project_id = :proId" ,nativeQuery = true)
    Page<Device> findByProId(@Param("proId") Integer proId, Pageable pageable);

    //查询项目下所有设备
    @Query(value = "FROM Device d where d.project =:project")
    List<Device> findAllDevice(@Param("project") Project project);

}
