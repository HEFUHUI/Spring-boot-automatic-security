package com.mrhui.automatic;

import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.pojo.ProjectConfig;
import com.mrhui.automatic.service.TUserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.junit.jupiter.api.Test;
import org.opencv.highgui.HighGui;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static org.opencv.highgui.HighGui.imshow;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutomaticApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(new Md5Hash("123", "admin", 2));
        //88212f91e2e9cf36981a91b6c518af5c
    }

    @Autowired
    TUserService userService;
    @Autowired
    ProjectConfig config;


    @Test
    void setUserService(){
        final Page<TUser> all = userService.findAll(new Page<>(1, 1));
        System.out.print(all);
    }

    @Test
    void openCV() throws Exception {
        System.load(config.getDllPath());
    }

    @Test
    void testReg(){
        String queryString = "ws://192.168.2.185:8081/ws?name=miru&token=fbd136ee-8206-4bd2-a6bb-a3de5ef11a77";
        System.out.println(Arrays.toString(queryString.split("token=")));
    }
}
