package com.airvcov.util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class TestLog {
    private static Logger logger = Logger.getLogger(TestLog.class);

    public static void main(String[] args){
        BasicConfigurator.configure();
        logger.debug("this is message");
    }
}
