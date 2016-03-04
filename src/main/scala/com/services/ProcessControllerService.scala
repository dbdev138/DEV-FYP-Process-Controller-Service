package com.services

import com.support.RouteHandlerService
import com.support.LoggingSupport
import com.support.ProcessControllerHelper

import java.io.{IOException}
import scala.io.{Source}
import java.net.{URL, HttpURLConnection, SocketTimeoutException}

import net.liftweb.json._
import net.liftweb.json.Serialization.write
import java.util.Calendar
import net.liftweb.json.JsonDSL._



object ProcessControllerService {

    implicit val formats = DefaultFormats
    
        def processController_BG(queryType: String, queryValue: String): String = {
            
            println("----------------------------------------------------------------------------------------------------------")
            println("-----> Get BBDS")

            
            val businessListMasterJson = ProcessControllerHelper.getRouteHandler(queryType, queryValue)
            // val businessListMasterJson = getRouteHandler("town", "lusk") 
            val businessListMasterJValue = parse(businessListMasterJson)
            
            //Filter values into Lists
            val businessNames       =  for { JField("name", JString(name)) <- businessListMasterJValue } yield name
            val businessAddresses   =  for { JField("address", JString(address)) <- businessListMasterJValue } yield address
            val businessPhones      =  for { JField("phone", JString(phone)) <- businessListMasterJValue } yield phone
    
            println("----------------------------------------------------------------------------------------------------------")
            println("-----> Get GTS")
            
            def getGeoCords_lat(address: String): String = { RouteHandlerService.processControllerGTS_lat(address) }
            def getGeoCords_lng(address: String): String = { RouteHandlerService.processControllerGTS_lng(address) }
            def stripQuotes(x: String):           String = {x.replace("\"","")}
            
            val geoCordsListMasterJson_lat = for(address <- businessAddresses) yield getGeoCords_lat(address)
            val geoCordsListMaster_lat     = for(lat <- geoCordsListMasterJson_lat) yield stripQuotes(lat)

            val geoCordsListMasterJson_lng = for(address <- businessAddresses) yield getGeoCords_lng(address)
            val geoCordsListMaster_lng     = for(lng <- geoCordsListMasterJson_lng) yield stripQuotes(lng)
    
            println("----------------------------------------------------------------------------------------------------------")
            println("-----> Combining Lists")
            
            // println(businessNames)
            // println(businessAddresses)
            // println(businessPhones)
            // println(geoCordsListMaster_lat)
            // println(geoCordsListMaster_lng)
            
            case class DataSet(b_name: String, b_address: String, b_phone: String, b_lat: String, b_lng: String)
            val min = List(businessNames, businessAddresses, businessPhones, geoCordsListMaster_lat, geoCordsListMaster_lng).map(_.size).min
            val dataSets = (0 until min) map { i => DataSet(businessNames(i), businessAddresses(i), businessPhones(i), geoCordsListMaster_lat(i), geoCordsListMaster_lng(i)) }
            
            // println(dataSets)
           
            println("----------------------------------------------------------------------------------------------------------")
            println("-----> Build & Return Json Object")
           
            val dataSetsJson = write(dataSets)
            // println(dataSetsJson)
            
            return dataSetsJson
            
            
        }
        
}
