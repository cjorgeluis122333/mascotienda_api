package com.jorgeluis.mascotionda.common.utili;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerMessage {

    public static void showMessage(Class<?> clazz, String msg) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.info("\n======================================  MESSAGE   ======================================\n:{}", msg);
    }

    public static void showError(Class<?> clazz, String msg) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.error("\n====================================== ERROR    ======================================\n:{}", msg);
    }


}
