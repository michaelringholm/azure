package com.opusmagus.democonsole;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.SimpleLog;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    private Logger logger = new SimpleLog("name");
}