package com.library;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = NaverClientTest.TestConfig.class)
@ActiveProfiles("test")
class NaverClientTest {

    @ComponentScan(basePackageClasses = NaverClient.class)
    static class TestConfig{}

    @Autowired
    NaverClient naverClient;

    @Test
    void callNaver() {
        String result = naverClient.search("HTTP");
        System.out.println("result = " + result);
    }
}