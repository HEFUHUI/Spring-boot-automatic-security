package com.mrhui.automatic.service.impl;

import com.mrhui.automatic.entity.TImage;
import com.mrhui.automatic.service.TImageService;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TImageServiceImplTest {

    @Autowired
    TImageService imageService;

    @Test
    void add() {
        final val image = new TImage();
        image.setAlias("456fc4a557528edf9f1");
        image.setUrl("v2-456fc4a557528edf9f1c5fd4c768482e_1440w.jpg");
        imageService.add(image);
    }
}