package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TClassService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TClassServiceImplTest {

    @Autowired
    TClassService classService;

    @Test
    void findAll() {
        System.out.println(classService.findAll(new Page<>()));
    }

    @Test
    void findByQuery() {
    }

    @Test
    void findById() {
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
    }

    @Test
    void updateState() {
        classService.updateWorking(true, "1402084099253862400");
    }
}