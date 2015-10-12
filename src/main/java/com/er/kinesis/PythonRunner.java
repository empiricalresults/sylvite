package com.er.kinesis;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import org.springframework.boot.SpringApplication;

import java.io.File;
import java.nio.file.Paths;

/**
 * Entry point to launch a python KCL MultiLangDaemon client.  This will build the appropriate
 * path and launch the MultiLangDaemon for you.
 */
public class PythonRunner {


    private static class Args {

        @Parameter(
                names = "--python-binary",
                description = "The python binary to run.  This should be the one" +
                    " in your virtual environment, or the global python interpreter if you have the requirements" +
                    " installed globally.",
                required = true
        )
        private String pythonBinary;

        @Parameter(
                names = "--python-path",
                description = "The base python directory that contains the modules to run.",
                required = true
        )
        private String pythonPath;

        @Parameter(
                names = "--config",
                description = "The kcl config to run.  There must be a config file in <python_path>/config/<config>.properties",
                required = true
        )
        private String pythonConfig;

        @Parameter(
                names = "--python-module",
                description = "The python module to run.  This is only required if running a module different than the config" +
                        " name.",
                required = false
        )
        private String pythonModule;

        @Parameter(names = "--help", help = true)
        private boolean help;

    }

    public static void main(String[] args) {

        Args parsedArgs = new Args();
        JCommander jc = new JCommander(parsedArgs);
        try {
            jc .parse(args);
        } catch (ParameterException e) {
            System.out.println(e.getMessage());
            jc.usage();
            System.exit(1);
        }

        String pythonBinary = parsedArgs.pythonBinary;
        String pythonBasePath = parsedArgs.pythonPath;
        String pythonModule = parsedArgs.pythonModule;
        String config = parsedArgs.pythonConfig;
        if (pythonModule == null) {
            pythonModule = config;
        }

        String modulePath = Paths.get(pythonBasePath, pythonModule + ".py").toAbsolutePath().toString();
        if (!new File(modulePath).exists()) {
            throw new IllegalArgumentException(modulePath + " does not exist!");
        }

        // we can pick out the configurations based on the app name
        String propertiesPath = Paths.get(pythonBasePath, "config", config + ".properties").toAbsolutePath().toString();
        if (!new File(propertiesPath).exists()) {
            throw new IllegalArgumentException(propertiesPath + " does not exist!");
        }

        String executableName = pythonBinary + " " + modulePath;
        SpringApplication.run(SpringMultiLangDaemon.class, executableName, propertiesPath);
    }
}
