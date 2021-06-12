package com.mrhui.automatic.service;

import com.mrhui.automatic.pojo.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CrudServiceTest {

    @Autowired
    TPermissionsService permissionsService;

    @Test
    void findAll() {
        System.out.println(permissionsService.findAllByPage(new Page<>()));
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
}