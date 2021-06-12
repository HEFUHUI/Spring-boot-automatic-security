package com.mrhui.automatic.mapper;

import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.utils.IdWorker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TPermissionsMapperTest {

    @Autowired
    TPermissionsMapper permissionsMapper;
    @Autowired
    IdWorker idWorker;

    @Test
    void getAllByRoleId() {
        permissionsMapper.getAllByRoleId("111");
    }

    @Test
    void getAllByPage(){
        System.out.println(permissionsMapper.findAllByPage(10,0));
    }

    @Test
    void addPermissions(){
        final TPermissions tPermissions = new TPermissions();
        tPermissions.setPermissionsId(idWorker.nextId());
        tPermissions.setTitle("权限添加");
        tPermissions.setCode("permission:add");
        permissionsMapper.add(tPermissions);
    }
}