---
layout: page
title: Configuration
permalink: /configuration/
---
As Sylvite is a wrapper around the KCL, all of the KCL configuration is supported.

Sylvite supports both environment variables and configuration files.

#### Mandatory Configuration

<p>
The following settings are required, otherwise either Sylvite or the KCL code will complain on startup.
</p>

<dl class="configuration">
  <dt>
    <strong>STREAM_NAME</strong> (streamName)
  </dt>
  <dd>
  The Kinesis stream to process.
  </dd>
</dl>  

<dl>
  <dt>
    <strong>APPLICATION_NAME</strong> (applicationName)
  </dt>
  <dd>
  The application name.  A dynamodb table will be created with this name if one doesn't exist.  The dynamodb table is used for keeping track of the position (checkpointing) each shard in the stream.
  </dd>
</dl>

<dl>
  <dt>
    <strong>EXECUTABLE_NAME</strong> (executableName)
  </dt>
  <dd>
  The child executable to spawn for each shard.  The executable must speak the KCL multi-lang protocol.  All environment variables passed to the parent process will be inherited by the child process.
  </dd>
</dl>  


#### Optional Configuration

The following settings are optional and all have default values if not set.

<dl class="configuration">
  <dt>
    <strong>CALL_PROCESS_RECORDS_EVENFOR_EMPTY_RECORD_LIST</strong> (callProcessRecordsEvenForEmptyRecordList)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>false</p>
  Call the RecordProcessor::processRecords() API even if GetRecords returned an empty record list.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>CLEANUP_LEASES_UPON_SHARD_COMPLETION</strong> (cleanupLeasesUponShardCompletion)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>true</p>
  Clean up shards we've finished processing (don't wait for expiration in Kinesis).
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>CLOUD_WATCH_CREDENTIALS_PROVIDER</strong> (cloudWatchCredentialsProvider)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>null</p>
  AWSCredentialsProvider for the cloudwatch access.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>DYNAMO_DB_CREDENTIALS_PROVIDER</strong> (dynamoDBCredentialsProvider)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>null</p>
  AWSCredentialsProvider for the dynamodb access.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>FAILOVER_TIME_MILLIS</strong> (failoverTimeMillis)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>10000</p>
  Lease duration (leases not renewed within this period will be claimed by others).
  </dd>
</dl>  

<dl class="configuration">
  <dt>
    <strong>IDLE_TIME_BETWEEN_READS_IN_MILLIS</strong> (idleTimeBetweenReadsInMillis)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>1000</p>
  Idle time between calls to fetch data from Kinesis.  This should be tuned with MAX_RECORDS in order to ensure
  you are not falling behind.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>INITIAL_LEASE_TABLE_READ_CAPACITY</strong> (initialLeaseTableReadCapacity)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>10</p>
  Read capacity to provision when creating the lease table.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>INITIAL_LEASE_TABLE_WRITE_CAPACITY</strong> (initialLeaseTableWriteCapacity)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>10</p>
  Write capacity to provision when creating the lease table.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>INITIAL_POSITION_IN_STREAM</strong> (streamName)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>TRIM_HORIZON</p>
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
  <p class="first"><strong>Default:</strong>null</p>
  AWSCredentialsProvider for the kinesis access.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>MAX_LEASES_FOR_WORKER</strong> (maxLeasesForWorker)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>2,147,483,647</p>
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
  <p class="first"><strong>Default:</strong>1</p>
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
  <p class="first"><strong>Default:</strong>10000</p>
  Max records to fetch in a Kinesis getRecords() call.  This should be tuned with
  IDLE_TIME_BETWEEN_READS_IN_MILLIS in order to ensure you are not falling behind.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>METRICS_BUFFER_TIME_MILLIS</strong> (metricsBufferTimeMillis)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>10000</p>
  Metrics are buffered for at most this long before publishing to CloudWatch.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>METRICS_ENABLED_DIMENSIONS</strong> (metricsEnabledDimensions)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>?</p>
  Sets the dimensions that are allowed to be emitted in metrics.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>METRICS_LEVEL</strong> (metricsLevel)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>DETAILED</p>
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
  <p class="first"><strong>Default:</strong>10000</p>
  Max number of metrics to buffer before publishing to CloudWatch.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>PARENT_SHARD_POLL_INTERVAL_MILLIS</strong> (parentShardPollIntervalMillis)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>10000</p>
  Wait for this long between polls to check if parent shards are done.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>REGION_NAME</strong> (regionName)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>null</p>
  The aws region name for the service.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>TASK_BACKOFF_TIME_MILLIS</strong> (taskBackoffTimeMillis)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>true</p>
  Backoff period when tasks encounter an exception.
  </dd>
</dl>

<dl class="configuration">
  <dt>
    <strong>VALIDATE_SEQUENCE_NUMBER_BEFORE_CHECKPOINTING</strong> (validateSequenceNumberBeforeCheckpointing)
  </dt>
  <dd>
  <p class="first"><strong>Default:</strong>true</p>
  Whether KCL should validate client provided sequence numbers with a call to Amazon Kinesis before actually checkpointing.
  If true, this calls a kinesis getIterator() api call, and may cause throttling errors if you are checkpointing frequently.
  </dd>
</dl>
