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

import static org.opencv.highgui.HighGui.imshow;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutomaticApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(new Md5Hash("123", "admin", 2));
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
}
