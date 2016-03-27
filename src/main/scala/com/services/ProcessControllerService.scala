package com.services

import com.support.RouteHandlerService
import com.support.LoggingSupport
import com.support.ProcessControllerHelper
import com.models.RuntimeStats

import java.io.{IOException, FileWriter, BufferedWriter}
import scala.io.{Source}
import java.net.{URL, HttpURLConnection, SocketTimeoutException}

import net.liftweb.json._
import net.liftweb.json.Serialization.write
import java.util.Calendar
import net.liftweb.json.JsonDSL._
import scala.collection.mutable.ListBuffer

import awscala._, s3._


object ProcessControllerService {

    implicit val formats = DefaultFormats
    
        def processController_BG(queryType: String, queryValue: String): String = {
            
            LoggingSupport.logProgress("Get BBDS")
            
            val businessListMasterJson = ProcessControllerHelper.getRouteHandler(queryType, queryValue)
            val businessListMasterJValue = parse(businessListMasterJson)
            
            //Filter values into Lists
            val businessNames       =  for { JField("name", JString(name)) <- businessListMasterJValue } yield name
            val businessAddresses   =  for { JField("address", JString(address)) <- businessListMasterJValue } yield address
            val businessPhones      =  for { JField("phone", JString(phone)) <- businessListMasterJValue } yield phone
    
            LoggingSupport.logProgress("Get GTS")
            
            def getGeoCords_lat(address: String): String = { RouteHandlerService.processControllerGTS_lat(address) }
            def getGeoCords_lng(address: String): String = { RouteHandlerService.processControllerGTS_lng(address) }
            def stripQuotes(x: String):           String = {x.replace("\"","")}
            
            val geoCordsListMasterJson_lat = for(address <- businessAddresses) yield getGeoCords_lat(address)
            val geoCordsListMaster_lat     = for(lat <- geoCordsListMasterJson_lat) yield stripQuotes(lat)

            val geoCordsListMasterJson_lng = for(address <- businessAddresses) yield getGeoCords_lng(address)
            val geoCordsListMaster_lng     = for(lng <- geoCordsListMasterJson_lng) yield stripQuotes(lng)
    
            LoggingSupport.logProgress("Combining Lists")
            
            case class DataSet(b_name: String, b_address: String, b_phone: String, b_lat: String, b_lng: String)
            val min = List(businessNames, businessAddresses, businessPhones, geoCordsListMaster_lat, geoCordsListMaster_lng).map(_.size).min
            val dataSets = (0 until min) map { i => DataSet(businessNames(i), businessAddresses(i), businessPhones(i), geoCordsListMaster_lat(i), geoCordsListMaster_lng(i)) }
            
            LoggingSupport.logProgress("Build & Return Json Object")
           
            val dataSetsJson = write(dataSets)
            
            return dataSetsJson
            
            
        }
        
        def processController_BG_with_stats(queryType: String, queryValue: String): String = {
            val start_pc    = ProcessControllerHelper.timeStamp()

            LoggingSupport.logProgress("Get BBDS")

            val start_bbds    = ProcessControllerHelper.timeStamp()
            
            val businessListMasterJson = ProcessControllerHelper.getRouteHandler(queryType, queryValue)
            // val businessListMasterJson = getRouteHandler("town", "lusk") 
            val businessListMasterJValue = parse(businessListMasterJson)
            
            //Filter values into Lists
            val businessNames       =  for { JField("name", JString(name)) <- businessListMasterJValue } yield name
            val businessAddresses   =  for { JField("address", JString(address)) <- businessListMasterJValue } yield address
            val businessPhones      =  for { JField("phone", JString(phone)) <- businessListMasterJValue } yield phone
            
            val end_bbds      = ProcessControllerHelper.timeStamp()
            
            LoggingSupport.logProgress("Get GTS")
            
            val start_gts    = ProcessControllerHelper.timeStamp()
            
            def getGeoCords_lat(address: String): String = { RouteHandlerService.processControllerGTS_lat(address) }
            def getGeoCords_lng(address: String): String = { RouteHandlerService.processControllerGTS_lng(address) }
            def stripQuotes(x: String):           String = {x.replace("\"","")}
            
            val geoCordsListMasterJson_lat = for(address <- businessAddresses) yield getGeoCords_lat(address)
            val geoCordsListMaster_lat     = for(lat <- geoCordsListMasterJson_lat) yield stripQuotes(lat)

            val geoCordsListMasterJson_lng = for(address <- businessAddresses) yield getGeoCords_lng(address)
            val geoCordsListMaster_lng     = for(lng <- geoCordsListMasterJson_lng) yield stripQuotes(lng)
            
            val end_gts      = ProcessControllerHelper.timeStamp()
            
            LoggingSupport.logProgress("Creating FileLocation Lists")
            
            val start_ss     = ProcessControllerHelper.timeStamp()
            
            val fileName = "location_"+queryValue+"_accessed_"+ProcessControllerHelper.getDateFileTag()+"_"+ProcessControllerHelper.timeStamp().toString()+".json"
            val baseUrl = "https://s3-eu-west-1.amazonaws.com/microdg-test/"
            val loctaion = baseUrl + fileName
            
            //Workaround - populate a list with the same filename
            val listSize = businessNames.length
            val locations = List.fill(listSize)(loctaion)
            
            val end_ss      = ProcessControllerHelper.timeStamp()
            
            LoggingSupport.logProgress("Combining Lists")
            
            case class DataSet(file_location: String, b_name: String, b_address: String, b_phone: String, b_lat: String, b_lng: String)
            val min = List(locations, businessNames, businessAddresses, businessPhones, geoCordsListMaster_lat, geoCordsListMaster_lng).map(_.size).min
            val dataSets = (0 until min) map { i => DataSet(locations(i), businessNames(i), businessAddresses(i), businessPhones(i), geoCordsListMaster_lat(i), geoCordsListMaster_lng(i)) }
            
            LoggingSupport.logProgress("Build & Return JSON Object")
           
            val dataSetsJson = write(dataSets)
            
            LoggingSupport.logProgress("Storing Object in S3 Bucket")
            
            //Write to Storage service
            //ProcessControllerHelper.getService(s"http://localhost:8084/storageServices/s3/processControllers/processA/withObject/"+"""$dataSetsJson"""+"/andDestination/"+fileName)
            
            val end_pc      = ProcessControllerHelper.timeStamp()
            
            LoggingSupport.logProgress("Recording Stats")
            
            val time_stamp = ProcessControllerHelper.getDateFileTag()
            val num_records_returned = ProcessControllerHelper.recordCount(dataSets)
            val runtime_pc  = ProcessControllerHelper.timeDifference(start_pc, end_pc)
            val runtime_bbds  = ProcessControllerHelper.timeDifference(start_bbds, end_bbds)
            val runtime_gts  = ProcessControllerHelper.timeDifference(start_gts, end_gts)
            val runtime_ss  = ProcessControllerHelper.timeDifference(start_ss, end_ss)
            
            //Remove head from Runtime Stats Queue
            RuntimeStats.stats.dequeue
            //Push statistics to to the queue
            RuntimeStats.stats.enqueue(com.models.RuntimeStats.RuntimeStats(time_stamp, num_records_returned, runtime_pc, runtime_bbds, runtime_gts, runtime_ss))
            
            return dataSetsJson
            
        }
        
        
        def getRuntimeStatistics(): String = {
            val json = write(RuntimeStats.stats)
            return json
        }
        
        def getSampleStatistics(): String = {
            val json = write(RuntimeStats.sample_stats)
            return json
        }
        
}






















































