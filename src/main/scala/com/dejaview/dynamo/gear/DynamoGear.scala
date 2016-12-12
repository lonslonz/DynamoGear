package com.dejaview.dynamo.gear

import com.dejaview.dynamo.DynamoGearDriver
import com.dejaview.dynamo.util.CommonUtil
import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory

object DynamoGear {

  val logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def assignTps() : Unit = {

    val startTime = CommonUtil.currFullTime()
    logger.info(s"Start changing tps. table : ${DynamoGearDriver.appConfig.table}, time : ${startTime}")

    val gearSchedule = GearScheduleReader.read(DynamoGearDriver.appConfig.table)

    if(!gearSchedule.isDefined) {
      logger.info("Schedule doesn't exist.")
      return
    }
    logger.info(s"Schedule : ${gearSchedule}")

    val dynamoDb = DynamoDBAccessor.initDynamo()
    val (readCap, writeCap) = DynamoDBAccessor.getCurrCapacity(dynamoDb)

    if(gearSchedule.get.tps == writeCap) {
      logger.info(s"There is no need to change tps " +
                  s"because curr tps(${writeCap}) == expected write tps ${gearSchedule.get.tps}")
      return
    }

    logger.info(s"Start changing tps : ${writeCap} => ${gearSchedule.get.tps}")
    DynamoDBAccessor.updateCapacity(dynamoDb, readCap, gearSchedule.get.tps)
    logger.info(s"Finish changing tps")

    val (changedReadCap, changedWriteCap) = DynamoDBAccessor.getCurrCapacity(dynamoDb)
    if(changedReadCap != readCap) {
      throw new Exception(s"Read capacity changed. old read capacity : ${readCap}, changed : ${changedReadCap}")
    }
    if(changedWriteCap == writeCap) {
      throw new Exception(s"Write capacity NOT changed. old write capacity : ${writeCap}, changed : ${changedWriteCap}")
    }

    logger.info("Saving history")
    HisMonitor.saveChanged(gearSchedule.get, startTime, writeCap, changedWriteCap)

  }
}
