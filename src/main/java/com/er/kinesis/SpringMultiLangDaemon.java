package com.er.kinesis;

import com.amazonaws.services.kinesis.clientlibrary.config.KinesisClientLibConfigurator;
import com.amazonaws.services.kinesis.multilang.MultiLangDaemon;
import com.amazonaws.services.kinesis.multilang.MultiLangDaemonConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Spring boot command line runner that will spawn the MultiLangDaemon.
 */
@ComponentScan
@EnableAutoConfiguration
@EnableConfigurationProperties
public class SpringMultiLangDaemon implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {

        String executableName = args[0];
        String propertiesPath = args[1];

        Properties p = new Properties();

        // some default values, these can all be overridden in the .properties file
        p.setProperty("executableName", executableName);
        p.setProperty("AWSCredentialsProvider", "DefaultAWSCredentialsProviderChain");
        p.setProperty("initialPositionInStream", "TRIM_HORIZON");

        p.load(new FileInputStream(new File(propertiesPath)));

        MultiLangDaemonConfig config = new MultiLangDaemonConfig(p, new KinesisClientLibConfigurator());
        MultiLangDaemon.start(config);
    }


}
