package com.mrhui.automatic.utils;

import com.mrhui.automatic.entity.TUser;
import com.mrhui.automatic.pojo.Page;
import com.mrhui.automatic.service.TUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtUtilsTest {

    @Autowired
    TUserService userService;

    @Test
    void getToken() {
        final TUser user = userService.findById("1406051339007426561");
        System.out.println(JwtUtils.getToken(user));
//eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4ODIxMmY5MWUyZTljZjM2OTgxYTkxYjZjNTE4YWY1YyIsImxvZ2luTmFtZSI6ImFkbWluIiwiZXhwIjoxNjI0NDU1NTI1LCJ1c2VySWQiOiIxNDA2MDUxMzM5MDA3NDI2NTYxIn0.zr3jmwOaf60L3GUUotz4seXTWQjtPSWLHIXZiaBQbDI
    }

    @Test
    void sign() {
        System.out.println(JwtUtils.sign("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4ODIxMmY5MWUyZTljZjM2OTgxYTkxYjZjNTE4YWY1YyIsImxvZ2luTmFtZSI6ImFkbWluIiwiZXhwIjoxNjI0NDU1NTI1LCJ1c2VySWQiOiIxNDA2MDUxMzM5MDA3NDI2NTYxIn0.zr3jmwOaf60L3GUUotz4seXTWQjtPSWLHIXZiaBQbDI"));
    }
}