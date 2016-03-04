package com.support

import com.support.LoggingSupport
import com.support.ProcessControllerHelper

import java.io.{IOException}
import scala.io.{Source}
import java.net.{URL, HttpURLConnection, SocketTimeoutException}

import net.liftweb.json._
import net.liftweb.json.Serialization.write
import java.util.Calendar
import net.liftweb.json.JsonDSL._

object RouteHandlerService {
    
    implicit val formats = DefaultFormats
    
    // -> BBDS All Businesses
    def processControllerBBDS_all(queryValue: String = "All", serviceName : String = "Base Business Details (retrieve all) Service", queryParam : String = "all") : String = {
        val content = ProcessControllerHelper.getRemoteService(serviceName, queryParam, queryValue)
        LoggingSupport.checkReturnType(content)
        return content
    }
    
    // -> BBDS All Businesses by Town Name
    def processControllerBBDS_town(queryValue: String, serviceName : String = "Base Business Details (by town) Service", queryParam : String = "town") : String = {
        val content = ProcessControllerHelper.getRemoteService(serviceName, queryParam, queryValue)
        LoggingSupport.checkReturnType(content)
        return content
    }
    
    // -> BBDS All Businesses by County Name
    def processControllerBBDS_county(queryValue: String, serviceName : String = "Base Business Details (by county) Service", queryParam : String = "county") : String = {
        val content = ProcessControllerHelper.getRemoteService(serviceName, queryParam, queryValue)
        LoggingSupport.checkReturnType(content)
        return content
    }
    
    // -> BBDS All Businesses by Region Name
    def processControllerBBDS_region(queryValue: String, serviceName : String = "Base Business Details (by region) Service", queryParam : String = "region") : String = {
        val content = ProcessControllerHelper.getRemoteService(serviceName, queryParam, queryValue)
        LoggingSupport.checkReturnType(content)
        return content
    }
    
    // -> GTS Latitude and Longitude for a given address 
    def processControllerGTS_lat_lng(queryValue: String, serviceName : String = "Geo Tagging Service (retrieve lat & lng) ", queryParam : String = "gts") : String = {
        val queryValueEncoded = ProcessControllerHelper.encodeUrl(queryValue)
        val content = ProcessControllerHelper.getRemoteService(serviceName, queryParam, queryValueEncoded)
        LoggingSupport.checkReturnType(content)
        return content
    }
    
    // -> GTS Latitude and Longitude for a given list of addresses
    def processControllerGTS_lat_lng_multi(queryValue: String, serviceName : String = "Geo Tagging Service (retrieve lat & lng for multiple addresses) ", queryParam : String = "gts_multi") : String = {
        val queryValueEncoded = ProcessControllerHelper.encodeUrl(queryValue)
        val content = ProcessControllerHelper.getRemoteService(serviceName, queryParam, queryValueEncoded)
        LoggingSupport.checkReturnType(content)
        return content
    }
    
    // -> GTS Latitude and Longitude for a given address 
    def processControllerGTS_lat(queryValue: String, serviceName : String = "Geo Tagging Service (retrieve lat) ", queryParam : String = "gts_lat") : String = {
        val queryValueEncoded = ProcessControllerHelper.encodeUrl(queryValue)
        val content = ProcessControllerHelper.getRemoteService(serviceName, queryParam, queryValueEncoded)
        LoggingSupport.checkReturnType(content)
        return content
    }
    
    // -> GTS Latitude and Longitude for a given address 
    def processControllerGTS_lng(queryValue: String, serviceName : String = "Geo Tagging Service (retrieve lng) ", queryParam : String = "gts_lng") : String = {
        val queryValueEncoded = ProcessControllerHelper.encodeUrl(queryValue)
        val content = ProcessControllerHelper.getRemoteService(serviceName, queryParam, queryValueEncoded)
        LoggingSupport.checkReturnType(content)
        return content
    }
    
    
}





