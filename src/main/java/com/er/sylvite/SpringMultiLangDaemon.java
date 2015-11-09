package com.er.sylvite;

import com.amazonaws.services.kinesis.clientlibrary.config.KinesisClientLibConfigurator;
import com.amazonaws.services.kinesis.multilang.MultiLangDaemon;
import com.amazonaws.services.kinesis.multilang.MultiLangDaemonConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

/**
 * Spring boot command line runner that will spawn the MultiLangDaemon.
 */
@SpringBootApplication
public class SpringMultiLangDaemon implements CommandLineRunner {

    private static final Log log = LogFactory.getLog(SpringMultiLangDaemon.class);

    @Autowired
    private KCLConfig config;

    @Override
    public void run(String... args) throws Exception {

        Properties p = config.asProperties();
        MultiLangDaemonConfig config = new MultiLangDaemonConfig(p, new KinesisClientLibConfigurator());
        MultiLangDaemon.start(config);
    }

    public static void main(String[] args) {
        SpringApplication.run(new Object[]{SpringMultiLangDaemon.class}, args);
    }
}
