# Spline Injector (Spark Version 2.3)

To enable pyspark to use Spark Splines it is necessary to inject
the the spline libraries via runtime. Due to the fact that this
is not possible directly via python code you need to write a little
bit of code.

Versions of Software and Libraries:

* [Spline](https://absaoss.github.io/spline/) - Version 0.3.8
* [Spark](https://spark.apache.org/) - Version 2.3.3
* [Scala](https://www.scala-lang.org/) - Version 2.11

First we need the Code fragment which injects the spline into the session.
Because we use the mongo db we directly enabled this inside the code.

Scala:

    package com.elanyo

    import org.apache.spark.sql.SparkSession
    import za.co.absa.spline.core.SparkLineageInitializer._

    object SplineInjector {

      def injectSpline(spark: SparkSession, mongoUrl: String, mongoName: String ): SparkSession = {
        if(mongoUrl != null && mongoName != null){
          System.setProperty("spline.persistence.factory", "za.co.absa.spline.persistence.mongo.MongoPersistenceFactory")
          System.setProperty("spline.mongodb.url", mongoUrl)
          System.setProperty("spline.mongodb.name", mongoName)
        }
        spark.enableLineageTracking()
        spark
      }

      def addTest(a: Integer, b: Integer): Integer = {
        a+b
      }
    }

Ingnore the addTest method. It is only there to test if the library was
properly loaded.

To build the library and put it into your local m2 repository execute the
maven goal install.

Starting the pyspark shell via shell:

    bin/pyspark --packages "com.elanyo:spline-injector-23:0.0.1"

To enable the splines inside the code use the following lines inside pyspark
shell:

    jvm = sc._jvm
    jss = spark._jsparkSession
    ssl = jvm.com.elanyo.SplineInjector.injectSpline(jss, 'mongodb://127.0.0.1:27017', 'lineageTest')

As far as you get no error message the library is properly loaded and the
spline tracking is enabled.

To start the web UI I used the following:

    java -jar spline-web-0.3.8-exec-war.jar -Dspline.mongodb.url=mongodb://127.0.0.1:27017 -Dspline.mongodb.name=lineageTest

To access the Web Interface open http://localhost:8080. The spline web
interface should open right now.
