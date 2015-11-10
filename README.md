# Sylvite

This library is a wrapper around the Kinesis Client Library's multi-lang daemon.  The goal is to make it feel a bit more natural to use the KCL with non-Java processing logic.

## Usage

Sylvite needs to know a few things about the Kinesis stream you are trying to process.

### Required Parameters

`streamName (STREAM_NAME)`: The Kinesis stream to process.

`applicationName (APPLICATION_NAME)`: The name of the application.  A DynamoDB table with this name will be used (or created) for checkpointing.

`executableName (EXECUTABLE_NAME)`: The child process to spawn.  This process must conform to the KCL multi-lang daemon protocol.


### Quick Start
Download the executable jar (or build it yourself), set a few environment vars, and go:

```sh
> export STREAM_NAME=my-kinesis-stream
> export APPLICATION_NAME=MyKinesisApp
> export EXECUTABLE_NAME="/opt/venv/bin/python /app/my_module.py"
> java -jar sylvite.jar
```

Or pass these via command line args:

```sh
> java -jar sylvite.jar --streamName=my-kinesis-stream --applicationName=MyKinesisApp --executableName="/opt/venv/bin/python /app/my_module.py"
```

Or use a properties file:

```sh
> cat <<EOF > /opt/myapp.properties
streamName=my-kinesis-stream
applicationName=MyKinesisApp
executableName=/opt/venv/bin/python /app/my_module.py
EOF
> java -jar sylvite.jar --config=/opt/myapp.properties
```

You can also mix and match the config types, to say store a few things in a properties file and some in environment variables.

## Config

The config is based on the original KCL format.  All of the KCL parameters are supported by Sylvite, including reading them directly from a properties file (the default, documented Kinesis multi-lang deamon way).

Sylvite will also read configuration from the command line and environment variables, so you can use whatever is most natural in your environment.

## Logging

By default, all logging from the Sylvite java process will be sent to STDOUT.  All logs from the spawned processes' STDERR will be sent to STDERR.

## IAM Policy

To fully use Sylvite, you need to grant access to Kinesis, CloudWatch and DynamoDB.  Here's a sample policy that gives blanket access to these resources:

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "kinesis:Get*",
                "kinesis:DescribeStream"
            ],
            "Resource": [
                "arn:aws:kinesis:us-east-1:*:stream/MyKinesisStream"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "cloudwatch:PutMetricData"
            ],
            "Resource": [
                "*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "dynamodb:*"
            ],
            "Resource": [
                "arn:aws:dynamodb:us-east-1:*:table/MyAppName"
            ]
        }
    ]
}
```
