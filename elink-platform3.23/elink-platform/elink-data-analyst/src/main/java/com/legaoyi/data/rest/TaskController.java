package com.legaoyi.data.rest;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.legaoyi.data.analyst.task.AccTimeDayReportTask;
import com.legaoyi.data.analyst.task.AlarmDayReportTask;
import com.legaoyi.data.analyst.task.AlarmNotifyTask;
import com.legaoyi.data.analyst.task.AttendanceDayReportTask;
import com.legaoyi.data.analyst.task.DataCountReportTask;
import com.legaoyi.data.analyst.task.GpsInfoIndexTask;
import com.legaoyi.data.analyst.task.HistoryDataCleanTask;
import com.legaoyi.data.analyst.task.MileageAndOilmassDayReportTask;
import com.legaoyi.data.analyst.task.OnlineTimeDayReportTask;
import com.legaoyi.data.analyst.task.ParkingTimeDayReportTask;

@RestController("taskController")
@RequestMapping(produces = {"application/json"})
public class TaskController extends BaseController {

    @Autowired
    private AlarmDayReportTask alarmDayReportTask;

    @Autowired
    private GpsInfoIndexTask gpsInfoIndexTask;

    @Autowired
    private HistoryDataCleanTask historyDataCleanTask;

    @Autowired
    private MileageAndOilmassDayReportTask mileageAndOilmassDayReportTask;

    @Autowired
    private OnlineTimeDayReportTask onlineTimeDayReportTask;

    @Autowired
    private AccTimeDayReportTask accTimeDayReportTask;

    @Autowired
    private DataCountReportTask dataCountReportTask;

    @Autowired
    private AttendanceDayReportTask attendanceDayReportTask;

    @Autowired
    private ParkingTimeDayReportTask parkingTimeDayReportTask;

    @Autowired
    private AlarmNotifyTask alarmNotifyTask;

    @RequestMapping(value = "/task/alarmDayReportTask/{date}", method = RequestMethod.GET)
    public Result executeAlarmDayReportTask(@PathVariable String date) throws Exception {
        java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyyMMdd");
        Date d = df.parse(date);
        long startTime = d.getTime();
        long endTime = startTime + 24 * 60 * 60 * 1000;
        alarmDayReportTask.executeTask(startTime, endTime);
        return new Result();
    }

    @RequestMapping(value = "/task/gpsInfoIndexTask/{date}", method = RequestMethod.GET)
    public Result executeGpsInfoIndexTask(@PathVariable String date) throws Exception {
        gpsInfoIndexTask.executeTask(date);
        return new Result();
    }

    @RequestMapping(value = "/task/historyDataCleanTask/{date}", method = RequestMethod.GET)
    public Result executeHistoryDataCleanTask(@PathVariable String date) throws Exception {
        historyDataCleanTask.executeTask(date);
        return new Result();
    }

    @RequestMapping(value = "/task/mileageAndOilmassDayReportTask/{date}", method = RequestMethod.GET)
    public Result executeMileageAndOilmassDayReportTask(@PathVariable String date) throws Exception {
        mileageAndOilmassDayReportTask.executeTask(date);
        return new Result();
    }

    @RequestMapping(value = "/task/onlineTimeDayReportTask/{date}", method = RequestMethod.GET)
    public Result executeOnlineTimeDayReportTask(@PathVariable String date) throws Exception {
        onlineTimeDayReportTask.executeTask(date);
        return new Result();
    }

    @RequestMapping(value = "/task/accTimeDayReportTask/{date}", method = RequestMethod.GET)
    public Result executeAccTimeDayReportTask(@PathVariable String date) throws Exception {
        accTimeDayReportTask.executeTask(date);
        return new Result();
    }

    @RequestMapping(value = "/task/dataCountReportTask/{date}", method = RequestMethod.GET)
    public Result executeDataCountReportTask(@PathVariable String date) throws Exception {
        dataCountReportTask.executeTask(date);
        return new Result();
    }

    @RequestMapping(value = "/task/attendanceDayReportTask/{date}", method = RequestMethod.GET)
    public Result executeAttendanceDayReportTask(@PathVariable String date) throws Exception {
        attendanceDayReportTask.executeTask(date);
        return new Result();
    }

    @RequestMapping(value = "/task/parkingTimeDayReportTask/{date}", method = RequestMethod.GET)
    public Result executeParkingTimeDayReportTask(@PathVariable String date) throws Exception {
        parkingTimeDayReportTask.executeTask(date);
        return new Result();
    }

    @RequestMapping(value = "/task/alarmNotifyTask", method = RequestMethod.GET)
    public Result executeAlarmNotifyTask() throws Exception {
        alarmNotifyTask.executeTask();
        return new Result();
    }
}
