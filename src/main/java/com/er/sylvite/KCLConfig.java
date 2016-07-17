package com.er.sylvite;


import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.bind.RelaxedNames;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Abstraction around the KCL configuration.
 *
 * This will use Spring Boot to pull the configuration from
 * any valid property source (environment vars, program args, from a properties file), so you are not
 * locked into having to setup a properties file.
 *
 * However, if you want to use a config file,
 * this will read values from a --config=/path/to/some/file.properties file as well.
 *
 */
@Component
public class KCLConfig {

    @Autowired
    public Environment environment;

    // extra properties not defined supported as config, but not discoverable via withX methods
    // in KinesisClientLibConfiguration.  These will be combined with the withX methods to
    // create the full list of available config elements.
    public static final String[] VALID_PROPERTIES = new String[] {
            "applicationName",
            "executableName",
            "processingLanguage",
            "maxActiveThreads",
            "awsCredentialsProvider",  // two of these due to strange case
            "AWSCredentialsProvider",
            "workerId",
            "streamName",
    };


    public static Properties defaults() {
        Properties p = new Properties();
        p.setProperty("AWSCredentialsProvider", "DefaultAWSCredentialsProviderChain");
        p.setProperty("initialPositionInStream", "TRIM_HORIZON");
        return p;
    }

    public static List<String> enumerateValidProperties() {
        Set<String> props = new HashSet<>();
        for (String prop : VALID_PROPERTIES) {
            props.add(prop);
        }

        // inspired by KinesistClientLibConfigurator
        for (Method method : KinesisClientLibConfiguration.class.getMethods()) {
            // convention
            if (method.getName().startsWith("with")) {
                String propName = method.getName().substring(4);
                propName = Character.toLowerCase(propName.charAt(0)) + propName.substring(1);
                props.add(propName);
            }
        }
        return new ArrayList<>(props);
    }

    public Properties asProperties() {

        Properties p = defaults();

        // if there's a config param, read props from that
        if (environment.containsProperty("config")) {
            String configFile = environment.getProperty("config");
            FileSystemResource fsResource = new FileSystemResource(configFile);
            p = new Properties();
            p.putAll(defaults());
            try {
                p.load(fsResource.getInputStream());
            } catch (FileNotFoundException e) {
                throw new RuntimeException("File " + configFile + " not found!");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (String name : enumerateValidProperties()) {
            RelaxedNames names = new RelaxedNames(name);
            for (String relaxedName : names) {
                if (environment.containsProperty(relaxedName)) {
                    p.setProperty(name, environment.getProperty(relaxedName));
                }
            }
        }

        // special handling due to case issues
        if (p.containsKey("awsCredentialsProvider")) {
            Object val = p.remove("awsCredentialsProvider");
            p.setProperty("AWSCredentialsProvider", (String) val);
        }

        return p;
    }
}
