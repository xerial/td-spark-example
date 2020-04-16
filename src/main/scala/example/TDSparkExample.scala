package example

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import wvlet.log.{LogSupport, Logger}

/**
 * An example TD Spark application
 */
object TDSparkExample extends LogSupport {

  def main(args:Array[String]): Unit = {
    run(localMode = true)
  }

  private def run(localMode:Boolean = true): Unit = {
    // Turn off Spark's info logs
    org.apache.log4j.Logger.getLogger("org.apache.spark").setLevel(org.apache.log4j.Level.ERROR)

    // Configure Spark
    val conf = new SparkConf()
            .setAppName("td-spark-eaxample")
            .set("spark.td.site", "us")
            .set("spark.td.apikey", sys.env.getOrElse("TD_API_KEY", ""))
            .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")

    // Run the program locally
    if (localMode) {
      conf.setMaster("local[4]")
    }

    // Create a new Spark session
    val sparkSession = SparkSession.builder.config(conf).getOrCreate()
    try {
      Logger.init
      this.run(sparkSession)
    }
    finally {
      sparkSession.stop()
    }
  }


  private def run(spark:SparkSession): Unit = {
    // Initialize TD Spark environment as `td`
    import com.treasuredata.spark._
    val td = spark.td

    // A) Reading a TD table as Spark DataFrame
    info(s"Reading a TD Table")
    val df = td.table("sample_datasets.www_access")
            .withinUnixTimeRange(from = 1412320845, until = 1412321000) // Selecting a time range
            .df
            .select("time", "host", "path", "code", "size") // Selecting columns to read
            .filter("size > 50 and size < 100") // Apply additional filters

    // Display the DataFrame contents
    df.show()

    // See https://spark.apache.org/docs/latest/sql-programming-guide.html for DataFrame usage

    info(s"Submitting a Presto query and reading the result")
    // B) Making Presto queries (using presto-jdbc internally)
    // If you need to read large query results, method A) is preferred and faster
    val jdbcDf = td.presto(
      """select time, host, path, code, size
        |from sample_datasets.www_access
        |where td_time_range(time, 1412320845, 1412321000)
        |and size > 50 and size < 100""".stripMargin)

    jdbcDf.show()
  }
}
