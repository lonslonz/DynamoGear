package com.dejaview.dynamo.gear

import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.{Region, Regions}
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import com.dejaview.dynamo.DynamoGearDriver
import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory

object DynamoDBAccessor {

  val logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  def initDynamo() : DynamoDB = {

    println("+ Connecting dynamo")

    val awsCreds : BasicAWSCredentials  =
      new BasicAWSCredentials(DynamoGearDriver.propConfig.awsAccessKey,
                              DynamoGearDriver.propConfig.awsSecretAccessKey)

    logger.info(s"Credential : ${awsCreds.toString()}")

    val amazonClient : AmazonDynamoDBClient = new AmazonDynamoDBClient(awsCreds)
    logger.info(s"Make client : ${amazonClient.toString()}")

    //TODO
    if(DynamoGearDriver.appConfig.local) {
      amazonClient.withEndpoint(DynamoGearDriver.propConfig.awsDynamoLocal)
    } else {
      amazonClient.withRegion(Region.getRegion(Regions.fromName(DynamoGearDriver.propConfig.awsDynamoRegion)))
    }

    logger.info(s"Connected with Region: ${amazonClient.toString()}")

    val dynamoDB = new DynamoDB(amazonClient)
    logger.info(s"Connected with Region : ${dynamoDB.toString}")

    return dynamoDB
  }
  def getCurrCapacity(dynamoDb : DynamoDB) : (Long, Long) = {

    val table = dynamoDb.getTable(DynamoGearDriver.appConfig.table)

    val tableDesc = table.describe()

    logger.info("Table : " + tableDesc.getTableStatus())

    val readCap = tableDesc.getProvisionedThroughput().getReadCapacityUnits()
    val writeCap = tableDesc.getProvisionedThroughput().getWriteCapacityUnits()

    logger.info(s"Current read capacitoy : ${readCap}, write capacity : ${writeCap}")
    return (readCap, writeCap)
  }

  def updateCapacity(dynamoDb : DynamoDB, readCap : Long, writeCap : Long) : Unit = {

    val table = dynamoDb.getTable(DynamoGearDriver.appConfig.table)

    val provisionedThroughput = new ProvisionedThroughput()
      .withReadCapacityUnits(readCap)
      .withWriteCapacityUnits(writeCap)

    logger.info("Updating table")
    table.updateTable(provisionedThroughput)

    logger.info("Waiting for active")
    table.waitForActive()

  }

}
