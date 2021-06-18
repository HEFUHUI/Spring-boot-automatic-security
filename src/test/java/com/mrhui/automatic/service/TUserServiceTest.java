package com.mrhui.automatic.service;

import com.mrhui.automatic.entity.TImage;
import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.pojo.Common;
import com.mrhui.automatic.pojo.Page;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TUserServiceTest {

    @Autowired
    TUserService userService;
    @Autowired
    TImageService imageService;

    @Test
    void findByUserName() {
        TImage image = new TImage();
        image.setAlias("https://img-blog.csdnimg.cn/20210317114147258.jpg#pic_center");
        image.setUrl("https://img-blog.csdnimg.cn/20210317114147258.jpg#pic_center");
        System.out.println(imageService.add(image));
        TUser user = new TUser();
        user.setAvatar(image);
        user.setUserType(0);
        user.setLoginName("root");
        user.setNickName("伏地龟");
        user.setComment("新用户-管理员");
        user.setPassword("123");
        System.out.println(userService.add(user));
        System.out.println(user);
    }

    @Test
    void getAllUser(){
        System.out.println(userService.findAll(new Page<>()));
    }

    @Test
    void setRole(){
        userService.setRole(new String[]{"1394462389755183104"},"1393473801844424704");
    }

    @Test
    void findAll(){
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("nick_name","管");
        System.out.println(userService.findAll(
                new Page<>()));
    }
    @Test
    void findByQuery(){
        final val hashMap = new HashMap<String, Object>();
        hashMap.put("user_type", Common.TYPE_HARDWARE);
        System.out.println(userService.findByQuery(hashMap, new Page<>()));
    }

    @Test
    void update(){
    }

    @Test
    void findById(){
        System.out.println(userService.findById("1404344556895338496"));
    }
}