package com.legaoyi.data.service;

import java.util.Map;

public interface CarReportService {

    public void updateCarReport(String date, Map<String, Object> device, Map<String, Object> data);

    public void incCarReport(String date, Map<String, Object> device, Map<String, Object> data);
}
