/*
 * Copyright 2012-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
package com.er.sylvite;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.util.StringUtils;

/**
 * {@link com.amazonaws.auth.AWSCredentialsProvider} implementation that provides credentials
 * by looking at the: <code>CUSTOM_AWS_ACCESS_KEY_ID</code> and <code>CUSTOM_AWS_SECRET_ACCESS_KEY</code>)
 * environment variables.
 *
 */
public class CustomEnvironmentVariableCredentialsProvider implements AWSCredentialsProvider {

    public static String ACCESS_KEY_ENV_VAR = "AWS_ACCESS_KEY_ID";
    public static String SECRET_KEY_ENV_VAR = "AWS_SECRET_ACCESS_KEY";
    public static String PREFIX_ENV_VAR = "CUSTOM_PREFIX";

    public static String DEFAULT_PREFIX = "CUSTOM";

    @Override
    public AWSCredentials getCredentials() {

        String prefix = System.getProperty(PREFIX_ENV_VAR, DEFAULT_PREFIX);

        String accessKeyEnvVar = prefix + "_" +  ACCESS_KEY_ENV_VAR;
        String secretKeyEnvVar = prefix + "_" + SECRET_KEY_ENV_VAR;

        String accessKey = System.getenv(accessKeyEnvVar);
        String secretKey = System.getenv(secretKeyEnvVar);

        accessKey = StringUtils.trim(accessKey);
        secretKey = StringUtils.trim(secretKey);

        if (StringUtils.isNullOrEmpty(accessKey)
                || StringUtils.isNullOrEmpty(secretKey)) {

            throw new AmazonClientException(
                    "Unable to load AWS credentials from environment variables " +
                            "(" + accessKeyEnvVar + " and " +
                            secretKeyEnvVar + ")");
        }

        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Override
    public void refresh() {}

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}