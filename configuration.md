---
layout: page
title: Configuration
permalink: /configuration/
---
As Sylvite is a wrapper around the KCL, all of the KCL configuration is supported.

Configuration values can be set via either environment variables of via a configuration file.  See the
[usage](asdf) page for examples.

#### Required Configuration
{: .home-subtitle}

<p>
The following settings are required.
</p>

<dl>
  <dt>
    <strong>APPLICATION_NAME</strong> (applicationName)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  Used by the KCL as the name of this application. Will be used as the name
  of a Amazon DynamoDB table which will store the lease and checkpoint
  information for workers with this application name.
  </dd>
</dl>

<dl>
  <dt>
    <strong>EXECUTABLE_NAME</strong> (executableName)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  The child executable to spawn for each shard.  The executable must speak the KCL multi-lang protocol.  All environment variables passed to the parent process will be inherited by the child process.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>STREAM_NAME</strong> (streamName)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  The Kinesis stream to process.
  </dd>
</dl>


#### Optional Configuration
{: .home-subtitle}

The following settings are optional and all have default values if not set.

<dl class="configuration">
  <dt>
    <strong>AWS_CREDENTIALS_PROVIDER</strong> (awsCredentialsProvider)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> DefaultAWSCredentialsProviderChain</p>
  The default credential provider used to sign AWS requests.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>CALL_PROCESS_RECORDS_EVENFOR_EMPTY_RECORD_LIST</strong> (callProcessRecordsEvenForEmptyRecordList)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> boolean</p>
  <p class="first"><strong>Default:</strong>false</p>
  Call the RecordProcessor::processRecords() API even if GetRecords returned an empty record list.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>CLEANUP_LEASES_UPON_SHARD_COMPLETION</strong> (cleanupLeasesUponShardCompletion)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> boolean</p>
  <p class="first"><strong>Default:</strong> true</p>
  Cleanup leases upon shards completion (don't wait until they expire in Kinesis).
  Keeping leases takes some tracking/resources (e.g. they need to be renewed, assigned), so by default we try
  to delete the ones we don't need any longer.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>CLOUD_WATCH_CREDENTIALS_PROVIDER</strong> (cloudWatchCredentialsProvider)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> DefaultAWSCredentialsProviderChain</p>
  AWSCredentialsProvider for the cloudwatch access.  Should only be set if you want
  to override the default AWS_CREDENTIALS_PROVIDER.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>DYNAMO_DB_CREDENTIALS_PROVIDER</strong> (dynamoDBCredentialsProvider)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> DefaultAWSCredentialsProviderChain</p>
  AWSCredentialsProvider for the dynamodb access.  Should only be set if you want
  to override the default AWS_CREDENTIALS_PROVIDER.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>FAILOVER_TIME_MILLIS</strong> (failoverTimeMillis)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 10000</p>
  Fail over time in milliseconds. A worker which does not renew it's lease within this time interval
  will be regarded as having problems and it's shards will be assigned to other workers.
  For applications that have a large number of shards, this may be set to a higher number to reduce
  the number of DynamoDB IOPS required for tracking leases.
  </dd>
</dl>  

<dl class="configuration">
  <dt>
    <strong>IDLE_TIME_BETWEEN_READS_IN_MILLIS</strong> (idleTimeBetweenReadsInMillis)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 1000</p>
  Idle time between calls to fetch data from Kinesis.  This should be tuned with MAX_RECORDS in order to ensure
  you are not falling behind.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>INITIAL_LEASE_TABLE_READ_CAPACITY</strong> (initialLeaseTableReadCapacity)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 10</p>
  The Amazon DynamoDB table used for tracking leases will be provisioned with this read capacity.  Only applies
  if the table does not exist, otherwise the capacity is not changed.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>INITIAL_LEASE_TABLE_WRITE_CAPACITY</strong> (initialLeaseTableWriteCapacity)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 10</p>
  The Amazon DynamoDB table used for tracking leases will be provisioned with this write capacity.  Only applies
  if the table does not exist, otherwise the capacity is not changed.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>INITIAL_POSITION_IN_STREAM</strong> (streamName)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> one of [LATEST, TRIM_HORIZON]</p>
  <p class="first"><strong>Default:</strong> TRIM_HORIZON</p>
  One of LATEST or TRIM_HORIZON. The Amazon Kinesis Client Library will start fetching records from
  this position when the application starts up if there are no checkpoints. If there are checkpoints,
  it will process records from the checkpoint position.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>KINESIS_CREDENTIALS_PROVIDER</strong> (kinesisCredentialsProvider)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> DefaultAWSCredentialsProviderChain</p>
  AWSCredentialsProvider for the kinesis access.  Should only be set if you want
  to override the default AWS_CREDENTIALS_PROVIDER.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>MAX_ACTIVE_THREADS</strong> (maxActiveThreads)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 0</p>
  The maximum number of threads the multi-lang daemon will use.  The default
  value of 0 does not limit the number of threads and should only be changed if
  you really know what you're doing.
  </dd>
</dl>


<dl class="configuration">
  <dt>
    <strong>MAX_LEASES_FOR_WORKER</strong> (maxLeasesForWorker)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 2,147,483,647</p>
  The max number of leases (shards) this worker should process. This can be useful to
  avoid overloading (and thrashing) a worker when a host has resource constraints
  or during deployment.
  NOTE: Setting this to a low value can cause data loss if workers are not able to pick up all shards in the
  stream due to the max limit.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>MAX_LEASES_TO_STEAL_AT_ONE_TIME</strong> (maxLeasesToStealAtOneTime)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 1</p>
  Max leases to steal from a more loaded Worker at one time (for load balancing).
  Setting this to a higher number can allow for faster load convergence (e.g. during deployments, cold starts),
  but can cause higher churn in the system.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>MAX_RECORDS</strong> (maxRecords)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 10000</p>
  Max records to fetch in a Kinesis getRecords() call.  This should be tuned with
  IDLE_TIME_BETWEEN_READS_IN_MILLIS in order to ensure you are not falling behind.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>METRICS_BUFFER_TIME_MILLIS</strong> (metricsBufferTimeMillis)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 10000</p>
  Metrics are buffered for at most this long before publishing to CloudWatch.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>METRICS_ENABLED_DIMENSIONS</strong> (metricsEnabledDimensions)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> null</p>
  Sets the dimensions that are allowed to be emitted in metrics.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>METRICS_LEVEL</strong> (metricsLevel)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> one of [NONE, SUMMARY, DETAILED]</p>
  <p class="first"><strong>Default:</strong> DETAILED</p>
  Sets metrics level that should be enabled. Possible values are:
  <ul>
    <li>NONE</li>
    <li>SUMMARY</li>
    <li>DETAILED</li>
  </ul>
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>METRICS_MAX_QUEUE_SIZE</strong> (metricsMaxQueueSize)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 10000</p>
  Max number of metrics to buffer before publishing to CloudWatch.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>PARENT_SHARD_POLL_INTERVAL_MILLIS</strong> (parentShardPollIntervalMillis)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 10000</p>
  Interval in milliseconds between polling to check for parent shard completion.
  Polling frequently will take up more DynamoDB IOPS (when there are leases for shards waiting on
  completion of parent shards).
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>PROCESSING_LANGUAGE</strong> (processingLanguage)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> null</p>
  The language you are using to process the stream.  This has no purpose other
  than augmenting the multi-lang user-agent string.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>REGION_NAME</strong> (regionName)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> null</p>
  The aws region name for the service.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>SHARD_SYNC_INTERVAL_MILLIS</strong> (shardSyncIntervalMillis)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 60000</p>
  Shard sync interval in milliseconds - e.g. wait for this long between shard sync tasks.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>TASK_BACKOFF_TIME_MILLIS</strong> (taskBackoffTimeMillis)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> integer</p>
  <p class="first"><strong>Default:</strong> 500</p>
  Backoff time in milliseconds for Amazon Kinesis Client Library tasks (in the event of failures).
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>USER_AGENT</strong> (userAgent)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> null</p>
  Override the default user-agent used in aws requests.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>VALIDATE_SEQUENCE_NUMBER_BEFORE_CHECKPOINTING</strong> (validateSequenceNumberBeforeCheckpointing)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> boolean</p>
  <p class="first"><strong>Default:</strong> true</p>
  Whether KCL should validate client provided sequence numbers with a call to Amazon Kinesis before actually checkpointing.
  If true, this calls a kinesis getIterator() api call, and may cause throttling errors if you are checkpointing frequently.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>WORKER_ID</strong> (workerId)
  </dt>
  <dd>
  <p class="first"><strong>Type:</strong> string</p>
  <p class="first"><strong>Default:</strong> null</p>
  Explicit worker id for the given worker, used to distinguish different workers/processes of a Kinesis application. These must be unique between workers.
  </dd>
</dl>
