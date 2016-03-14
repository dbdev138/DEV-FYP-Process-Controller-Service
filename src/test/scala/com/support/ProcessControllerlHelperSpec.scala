package com.support

import org.scalatest.{FlatSpec, ShouldMatchers}
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import com.support.ProcessControllerHelper

class ProcessControllerHelperSpec extends FlatSpec {
    
    implicit val formats = DefaultFormats
    
    
    //-------------------------------------------------------------------------------------------------------------------------------
    //TEST - The Record Counter Function
    case class DataSet(b_name: String, b_address: String, b_phone: String, b_lat: String, b_lng: String)
    val testDataSet = IndexedSeq[DataSet] (
        DataSet("x", "x", "x", "x", "x"),
        DataSet("x", "x", "x", "x", "x"),
        DataSet("x", "x", "x", "x", "x")
        )

    "Calling the recordCount function on the test Data Set" should "produce the integer value 3" in {
        assert(ProcessControllerHelper.recordCount(testDataSet) == 3)
    }
    //-------------------------------------------------------------------------------------------------------------------------------
    
    //-------------------------------------------------------------------------------------------------------------------------------
    //TEST - The Remote Services invocation response
    val sampleParam1 = "region"
    val sampleParam2 = "Munster"
    val sampleParam3 = "gts"
    val sampleParam4 = "115A Sarsfield Park Lucan Co Dublin"
    val sampleParam5 = "http://localhost:8082/geoTaggings/addresses/115A+Sarsfield+Park+Lucan+Co+Dublin"
    
    val encodedUrl = "115A+Sarsfield+Park+Lucan+Co+Dublin"
    val expectedJson_county = """[{"id":8,"name":"Village Art Gallery","channel":"art","channel_type":"gallery","phone":"+35318492236","address":"83 Strand Street Skerries Co. Dublin","town":"Skerries","county":"Dublin","region":"Munster"}]"""
    val expectedJson_gts = """[{"lat":"53.35870269999999","lng":"-6.4438657"}]"""
    
    s"Calling the Remote Service switch function with the parameters $sampleParam1 and $sampleParam2" should "return content matching the expected JSON provided" in {
        assert(ProcessControllerHelper.callRemoteService(sampleParam1, sampleParam2) == expectedJson_county)
    }
    
    s"Passing the parameter $sampleParam4 to the encode url function" should s"return $encodedUrl" in {
        assert(ProcessControllerHelper.encodeUrl(sampleParam4) == encodedUrl)
    }
    
    s"Calling the Remote Service switch function with the parameters $sampleParam3 and $sampleParam4" should "retrun content matching the expected JSON provided" in {
        assert(ProcessControllerHelper.callRemoteService(sampleParam3, encodedUrl) == expectedJson_gts)
    }
    
    s"Calling the Get Service function with the parameters: $sampleParam5" should "retrun content matching the expected JSON provided" in {
        assert(ProcessControllerHelper.getService(sampleParam5) == expectedJson_gts)
    }
   //-------------------------------------------------------------------------------------------------------------------------------
   
   
    
}


















