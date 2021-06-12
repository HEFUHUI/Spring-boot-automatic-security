package com.mrhui.automatic.mapper;

import com.mrhui.automatic.entity.TPermissions;
import com.mrhui.automatic.entity.TRole;
import com.mrhui.automatic.utils.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class TRoleMapperTest {

    @Autowired
    TRoleMapper tRoleMapper;

    @Autowired
    IdWorker idWorker;

    @Test
    void findByUserId() {
        System.out.println(tRoleMapper.findByUserId("321"));
    }
    @Test
    void getCount(){
        System.out.println(tRoleMapper.getCount());
    }

    @Test
    void findAll(){
        System.out.println(tRoleMapper.findAll(10,0));
    }

    @Test
    void add(){
        final TRole tRole = new TRole();
        tRole.setRoleId(idWorker.nextId());
        tRole.setName("学生");
        tRole.setComment("fdskfds");
        tRoleMapper.add(tRole);
    }

    @Test
    void addPermissions(){
        tRoleMapper.addPermissions("666","111");
    }
}