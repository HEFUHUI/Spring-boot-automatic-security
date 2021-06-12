package com.mrhui.automatic.controller;

import com.mrhui.automatic.pojo.ProjectConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class WebSocketServerTest {

    @Autowired
    ProjectConfig projectConfig;

    @Test
    void encodeBase64ToImage() {
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            FileInputStream fileInputStream = new FileInputStream(projectConfig.getLocation()+ "/2021-06-01/43g816.jpg");
            final byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            byte[] encode = encoder.encode(b);
            System.out.println(new String(encode));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void decodeBase64yToImage() throws IOException {
        final Base64.Decoder decoder = Base64.getDecoder();
        FileInputStream inputStream = new FileInputStream(projectConfig.getLocation()+"/img.txt");
        String base64Img = new String(inputStream.readAllBytes());
        final byte[] decode = decoder.decode(base64Img);
        final FileOutputStream fileOutputStream = new FileOutputStream(projectConfig.getLocation() + "/img.jpg");
        fileOutputStream.write(decode);
        fileOutputStream.close();
        inputStream.close();
    }
}