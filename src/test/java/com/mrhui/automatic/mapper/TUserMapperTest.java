package com.mrhui.automatic.mapper;

import com.mrhui.automatic.pojo.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TUserMapperTest {

    @Autowired
    TUserMapper userMapper;

    @Test
    void findByQuery() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("user_id","1394483121159667712");
        System.out.println(userMapper.findByQuery(map,10,0));
    }

    @Test
    void findUsers(){
    }

    @Test
    void findByUserName(){
        System.out.println(userMapper.findByUserName("fuhui"));
    }
}