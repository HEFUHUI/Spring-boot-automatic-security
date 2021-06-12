package com.mrhui.automatic.interceptor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestTest {

    @Test
    void run() {
        final Thread hello = new Thread(() -> {
            System.out.println("hello");
        });
        hello.start();
    }
}