package com.example.sys;

import com.example.sys.service.AsyncTaskService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SysApplicationTests {

    @Autowired
    AsyncTaskService asyncTaskService;

    @Test
    public void contextLoads() {
        for (int i = 0; i < 5; i++) {
           // asyncTaskService.executeAsyncTask(i);
        }
    }

}
