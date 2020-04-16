td-spark-example
===

An example td-spark application.

- Java8 is required as Spark 2.4.x does not support JDK11

## Project Structure

```
lib        # Put td-spark-assembly jar file here
src        # Example source code (TDSparkExample)
build.sbt  # Build definition
```

## Usage

```
$ export TD_API_KEY=(Your TD API Key)
$ ./sbt

# Run the example program
sbt:td-spark-example> run
[info] Compiling 1 Scala source to /Users/leo/work/td/2020-04-15-mw/td-spark-example/target/scala-2.11/classes ...
[info] running example.TDSparkExample
Using Spark's default log4j profile: org/apache/spark/log4j-defaults.properties
20/04/15 18:00:09 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
2020-04-15 18:00:10.793-0700 debug [spark] Loading com.treasuredata.spark package - (package.scala:23)
2020-04-15 18:00:10.802-0700  info [spark] td-spark version:20.4.0, revision:f6bdc8e, build_time:2020-04-10T07:03:29.264+0000 - (package.scala:24)
2020-04-15 18:00:11.025-0700  info [TDServiceConfig] td-spark site: us - (TDServiceConfig.scala:36)
20/04/15 18:00:11 INFO log: Logging initialized @275255ms to org.eclipse.jetty.util.log.Slf4jLog
20/04/15 18:00:11 INFO TDClient: td-client version: unknown
2020-04-15 18:00:11.852-0700  info [TDSparkExample] Reading a TD Table - (TDSparkExample.scala:49)
2020-04-15 18:00:14.274-0700  info [TDRelation] Fetching the partition list of sample_datasets.www_access within time range:[2014-10-03 07:20:45Z,2014-10-03 07:23:20Z) - (TDRelation.scala:172)
2020-04-15 18:00:15.260-0700  info [TDRelation] Retrieved 1 partition entries - (TDRelation.scala:179)
+----------+---------------+--------------------+----+----+
|      time|           host|                path|code|size|
+----------+---------------+--------------------+----+----+
|1412320978|   200.72.21.63|   /category/finance| 200|  59|
|1412320962| 136.27.214.160|   /item/office/4216| 200|  54|
|1412320945|104.159.186.145|    /search/?c=Games| 200|  79|
|1412320911| 100.192.40.170|    /item/books/4494| 200|  93|
|1412320878| 108.126.158.84|/category/electro...| 200|  75|
|1412320845|200.129.205.208|/category/electro...| 200|  62|
+----------+---------------+--------------------+----+----+

2020-04-15 18:00:16.496-0700  info [TDSparkExample] Submitting a Presto query and reading the result - (TDSparkExample.scala:59)
2020-04-15 18:00:18.831-0700  info [TDPrestoJDBCRDD]  - (TDPrestoRelation.scala:106)
Submit Presto query:
select time, host, path, code, size
from sample_datasets.www_access
where td_time_range(time, 1412320845, 1412321000)
and size > 50 and size < 100
+----------+---------------+--------------------+----+----+
|      time|           host|                path|code|size|
+----------+---------------+--------------------+----+----+
|1412320978|   200.72.21.63|   /category/finance| 200|  59|
|1412320911| 100.192.40.170|    /item/books/4494| 200|  93|
|1412320878| 108.126.158.84|/category/electro...| 200|  75|
|1412320845|200.129.205.208|/category/electro...| 200|  62|
|1412320962| 136.27.214.160|   /item/office/4216| 200|  54|
|1412320945|104.159.186.145|    /search/?c=Games| 200|  79|
+----------+---------------+--------------------+----+----+

[success] Total time: 14 s, completed Apr 15, 2020 6:00:20 PM
```

## Create a standalone Spark application


```
# Create a portable executable package into target/pack folder
$ ./sbt pack


# target/pack/bin folder contains a script to launch a local Spark cluster.
$ ./target/pack/bin/td-spark-example
...TDSparkExample
```
