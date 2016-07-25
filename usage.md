---
layout: page
title: Usage
---

#### Quick Start
{: .home-subtitle}

Sylvite is packaged as a fat executable jar.  To use it, simply [download](http://abc.com) the latest version
and execute the jar:

{% highlight sh %}
% export STREAM_NAME=my-kinesis-stream
% export APPLICATION_NAME=my-app
% export EXECUTABLE_NAME=my-multilang-script.sh
% java -Xmx200m -jar sylvite.jar
{% endhighlight %}

**Note:** Java 7 or greater is required.

Configuration can be set via environment variables or a config file, here's
an equivalent config file version:

{% highlight sh %}
% touch config.properties
% echo "streamName=my-kinesis-stream" >> config.properties
% echo "applicationName=my-app" >> config.properties
% echo "executableName=my-multilang-script.sh" >> config.properties
% java -Xmx200m -jar sylvite.jar --config config.properties
{% endhighlight %}

See the [configuration](http://configuration) page for a description of all the available options.

#### IAM Policy
{: .home-subtitle}

The KCL requires the following IAM policy similar to the following (this is for the us-east-1 aws region),
restricting dynamodb access to the table MyAppName and kinesis access to the stream MyKinesisStream.

{% highlight json %}
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
{% endhighlight %}

#### Example: Python Kinesis Processor
{: .home-subtitle}

The example above references the *my-multilang-script.sh* script, which must conform
to the multi-lang protocol in order to process records.  As a concrete example, here's
how to create a python kinesis processor.

1. Install the kclpy library.

{% highlight sh %}
% pip install kclpy
{% endhighlight %}

2. Create a file named app.py.

{% highlight python %}
import kclpy
import logging
import json

log = logging.getLogger('main')

class MyKinesisProcessor(kclpy.RecordProcessor):
    """
    Sample python kinesis processor
    """

    def __init__(self):
        super(MyKinesisProcessor, self).__init__(checkpoint_freq_seconds=5)

    def process_record(self, data, partition_key, sequence_number):
        log.debug("Got record: {}".format(data))
        # returning True tells kclpy to checkpoint immediately
        return True

def main():
    logging.basicLogging()
    log.setLevel(logging.DEBUG)
    kclpy.start(MyKinesisProcessor())

if __name__ == "__main__":
    main()

{% endhighlight %}

3. Start processing the stream.

{% highlight sh %}
% export STREAM_NAME=my-kinesis-stream
% export APPLICATION_NAME=my-app
% export EXECUTABLE_NAME="python app.py"
% java -Xmx200m -jar sylvite.jar
{% endhighlight %}

#### Logging
{: .home-subtitle}

As the KCL multi-lang protocol communicates with the child processes using STDOUT,
all child processes must log messages to STDERR.  These will be echoed in the
STDOUT of the parent process.

**Note:** If your stream has more than one shard, the KCL will spawn one child process
for each shard.  This will result in merged log messages.  For traceablity, you
may want to include the process id in your log messages.  Here's an example that
will prefix log messages with the process id.

{% highlight python %}
import logging

BASIC_FORMAT = "[%(process)d] %(levelname)s:%(name)s:%(message)s"
logging.basicConfig(BASIC_FORMAT)
{% endhighlight %}
