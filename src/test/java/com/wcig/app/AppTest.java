package com.wcig.app;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AppTest {
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }

    @Test
    public void test() throws Exception {
        long start = System.currentTimeMillis();
        Thread.sleep(1000);
        long end = System.currentTimeMillis();
        System.out.println("time cost:" + (end - start));
    }
}
