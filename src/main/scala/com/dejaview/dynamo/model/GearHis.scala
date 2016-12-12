package com.dejaview.dynamo.model

import java.sql.{Date, Time}

case class GearHis(hisId : Long, dynamoGearId : Long, startTime : Time, endTime : Time,
                   currWriteTps : Long, expectedWriteTps : Long, changedWriteTps : Long, updateTime : Date ) {

}
