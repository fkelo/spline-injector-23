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
