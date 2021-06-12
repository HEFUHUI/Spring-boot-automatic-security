package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.Logging;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.LoggingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoggingServiceImplTest {


    @Autowired
    LoggingService loggingService;

    @Test
    void findAll() {
    }

    @Test
    void findByQuery() {
        Map<String,Object> map = new HashMap<>();
        map.put("log_outcome","true");
        map.put("log_type","1001");
        System.out.println(loggingService.findByQuery(map, new Page<>()));
    }

    @Test
    void findById() {
        System.out.println(loggingService.findById("2"));
    }

    @Test
    void update() {
    }

    @Test
    void remove() {
    }

    @Test
    void testRemove() {
    }

    @Test
    void add() {
        Logging logging = new Logging();
        logging.setLogType(1000);
        logging.setLogUser("1399546696370749441");
        logging.setLogOutcome(true);
        loggingService.add(logging);
    }
}