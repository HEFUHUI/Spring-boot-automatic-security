package com.mrhui.automatic.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TPermissionsServiceTest {

    @Autowired
    TPermissionsService permissionsService;

    @Test
    void findByRoleId() {
        permissionsService.findByRoleId("111");
    }
}