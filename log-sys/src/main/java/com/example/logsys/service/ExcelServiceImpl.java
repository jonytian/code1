package com.example.logsys.service;


import com.example.logsys.entity.BusClick;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ExcelServiceImpl implements ExcelService {
    @Override
    public List<BusClick> getBusClick() {
        return null;
    }
}
