package com.sample.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * TODO
 *
 * @author JueYi
 */

@Slf4j
public class LogTest {

    @Test
    public void levelTest() {

        log.info("info");
        log.warn("warn");
        log.debug("debug");
        log.error("error");
    }



}
