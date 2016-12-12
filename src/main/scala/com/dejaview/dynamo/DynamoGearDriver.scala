package com.dejaview.dynamo

import java.util.Properties

import com.dejaview.dynamo.config.PropConfig
import com.dejaview.dynamo.gear.DynamoGear
import com.dejaview.dynamo.mysql.MySQLConnPool
import com.typesafe.scalalogging.slf4j.Logger
import org.slf4j.LoggerFactory
import scopt.OptionParser

object DynamoGearDriver extends App {

  var appConfig : ConfigOption = _
  var propConfig : PropConfig = _
  val logger = Logger(LoggerFactory.getLogger(this.getClass.getName))

  case class ConfigOption(table : String = "", local : Boolean = false)

  val appName = "ballast-dynamo-gear"

  val parser = new OptionParser[ConfigOption](appName) {
    head(appName)
    opt[String]('t', "table").action { (x, c) =>
      c.copy(table = x) }.text("table")
    opt[Boolean]('l', "local").action { (x, c) =>
      c.copy(local = x) }.text("local")
    help("help").text("help message")
  }

  var argsConfig : Option[ConfigOption] = parser.parse(args, ConfigOption())

  argsConfig match {
    case Some(config) => println(s"configration: $argsConfig")
    case None => {
      println("Bad Arguements !!!")
      parser.showUsageAsError()
    }
  }

  appConfig = argsConfig.get
  loadProperties()


  DynamoGear.assignTps()

  MySQLConnPool.close()
  logger.info("Finished.")

  def getAppConfig() : ConfigOption = {
    return appConfig
  }

  def setAppConfig(config : ConfigOption) : Unit = {
    appConfig = config
  }

  def loadProperties() : Unit = {

    val prop = new Properties()
//    val propFile = if(!testMode) "/production.properties" else "/test.properties"
    val propFile = "/production.properties"
    val stream = getClass.getResourceAsStream(propFile)
    prop.load(stream)
    propConfig = new PropConfig(prop)
  }

}
