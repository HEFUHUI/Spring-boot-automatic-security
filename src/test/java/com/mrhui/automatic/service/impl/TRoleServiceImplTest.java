package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TRoleService;
import com.mrhui.automatic.utils.IdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TRoleServiceImplTest {

    @Autowired
    TRoleService tRoleService;
    @Autowired
    IdWorker idWorker;

    @Test
    void findByUserId() {

    }

    @Test
    void findAllByPage() {
    }

    @Test
    void findAll() {
        System.out.println(tRoleService.findAll(new Page<>()));
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
        TRole tRole = new TRole();
        tRole.setRoleId(idWorker.nextId());
        tRole.setName("设备");
        tRole.setComment("设备");
        TPermissions tPermissions = new TPermissions();
        tPermissions.setPermissionsId("1394179513037881344");
        TPermissions tPermissions1 = new TPermissions();
        tPermissions1.setPermissionsId("666");
        tRole.getPermissions().add(tPermissions);
        tRole.getPermissions().add(tPermissions1);
        System.out.println(tRole);
        tRoleService.add(tRole);
    }
}