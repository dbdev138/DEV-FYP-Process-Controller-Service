package com.support

import org.scalatest.{FlatSpec, ShouldMatchers}
import net.liftweb.json._
import net.liftweb.json.Serialization.write
import com.support.ProcessControllerHelper
import java.util.Calendar
import java.time.LocalDate

class ProcessControllerHelperSpec extends FlatSpec {
    
    implicit val formats = DefaultFormats
    
    //TEST DATA
    val expectedJson_town = """[[{
	"id": 12,
	"name": "Village Art Gallery",
	"channel": "art",
	"channel_type": "gallery",
	"phone": "+35318492236",
	"address": "83 Strand Street Skerries Co. Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 13,
	"name": "Carrolls Pierhouse Hotel Restaurant",
	"channel": "f&b",
	"channel_type": "restaurant",
	"phone": "+35318491033",
	"address": "Harbour Road Skerries Co. Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 14,
	"name": "Central Cafe",
	"channel": "f&b",
	"channel_type": "cafe",
	"phone": "+35318491374",
	"address": "75 Strand Street Skerries Co.Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 15,
	"name": "O'Sheas Bar & Restaurant",
	"channel": "f&b",
	"channel_type": "pub",
	"phone": "+35318491374",
	"address": "17 New Street Skerries Co Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 16,
	"name": "Olive",
	"channel": "f&b",
	"channel_type": "cafe",
	"phone": "+35318490310",
	"address": "86A Strand Street Skerries Co. Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 17,
	"name": "Parachute Cafe",
	"channel": "f&b",
	"channel_type": "cafe",
	"phone": "+35318492322",
	"address": "47 Thomas Hand Street Skerries Co. Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 18,
	"name": "Pasta Pizza",
	"channel": "f&b",
	"channel_type": "restaurant",
	"phone": "+35318492724",
	"address": "54 Strand Street Skerries Co. Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 19,
	"name": "Pheonix Chinese Take Away",
	"channel": "f&b",
	"channel_type": "takeaway",
	"phone": "+35318490848",
	"address": "South Strand Skerries Co Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 20,
	"name": "Russelle Restauran",
	"channel": "f&b",
	"channel_type": "restaurant",
	"phone": "+35318492450",
	"address": "24 Strand Street Skerries Co. Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 21,
	"name": "The Cottage Bistro",
	"channel": "f&b",
	"channel_type": "restaurant",
	"phone": "+35318492450",
	"address": "17 New Street Skerries Co. Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}, {
	"id": 22,
	"name": "The Lifeboat Restaurant",
	"channel": "f&b",
	"channel_type": "restaurant",
	"phone": "+35318490109",
	"address": "Harbour Road Skerries Co Dublin",
	"town": "Skerries",
	"county": "Dublin",
	"region": "Leinster"
}]]"""
    val sampleParam1 = "town"
    val sampleParam2 = "Skerries"
    val sampleParam3 = "gts"
    val sampleParam4 = "115A Sarsfield Park Lucan Co Dublin"
    val sampleParam5 = "http://localhost:8082/geoTaggings/addresses/115A+Sarsfield+Park+Lucan+Co+Dublin"
    
    val encodedUrl = "115A+Sarsfield+Park+Lucan+Co+Dublin"
    
    val expectedJson_gts = """[{"lat":"53.35870269999999","lng":"-6.4438657"}]"""
    
    
    //-------------------------------------------------------------------------------------------------------------------------------
    //TEST - The Record Counter Function
    case class DataSet(b_name: String, b_address: String, b_phone: String, b_lat: String, b_lng: String)
    val testDataSet = IndexedSeq[DataSet] (
        DataSet("x", "x", "x", "x", "x"),
        DataSet("x", "x", "x", "x", "x"),
        DataSet("x", "x", "x", "x", "x")
        )

    "TEST-1: Calling the recordCount function on the test Data Set" should "produce the integer value 3" in {
        assert(ProcessControllerHelper.recordCount(testDataSet) == 3)
    }
    //-------------------------------------------------------------------------------------------------------------------------------
    
    //-------------------------------------------------------------------------------------------------------------------------------
    //TEST - The Remote Services invocation response (See Test Data Below)
    
    
    // s"TEST-2: Calling the Remote Service switch function with the parameters $sampleParam1 and $sampleParam2" should "return content matching the expected JSON provided" in {
    //     assert(ProcessControllerHelper.callRemoteService(sampleParam1, sampleParam2) == expectedJson_town)
    // }
    
    s"TEST-3: Passing the parameter $sampleParam4 to the encode url function" should s"return $encodedUrl" in {
        assert(ProcessControllerHelper.encodeUrl(sampleParam4) == encodedUrl)
    }
    
    s"TEST-4: Calling the Remote Service switch function with the parameters $sampleParam3 and $sampleParam4" should "retrun content matching the expected JSON provided" in {
        assert(ProcessControllerHelper.callRemoteService(sampleParam3, encodedUrl) == expectedJson_gts)
    }
    
    s" TEST-5: Calling the Get Service function with the parameters: $sampleParam5" should "retrun content matching the expected JSON provided" in {
        assert(ProcessControllerHelper.getService(sampleParam5) == expectedJson_gts)
    }
   //-------------------------------------------------------------------------------------------------------------------------------
   
   s"TEST-6: Calling the Get Remote Service function with the parameters: Geo Tagging Service (retrieve lat & lng), $sampleParam3 and $sampleParam4" should "retrun content matching the expected JSON provided" in {
        assert(ProcessControllerHelper.getRemoteService("Geo Tagging Service (retrieve lat & lng) ", sampleParam3, encodedUrl) == expectedJson_gts)
    }

   
//   s"TEST-7: Calling the getRouteHandler function with parameters $sampleParam1 and $sampleParam2" should "return content matching the expected JSON provided" in {
//         assert(ProcessControllerHelper.getRouteHandler(sampleParam1, sampleParam2) == expectedJson_town)
//     }
   
   
   s"TEST-8: Calling the TimeStamp function" should "the correct time in Miilis" in {
        val time = Calendar.getInstance.getTimeInMillis().toInt
        assert(ProcessControllerHelper.timeStamp() == time)
    }
    
    s"TEST-9: Calling the Time Difference function" should "return a integer value of zero" in {
        val time = Calendar.getInstance.getTimeInMillis().toInt
        assert(ProcessControllerHelper.timeDifference(time, time) == 0)
    }
    
    // s"TEST-10: Calling the Get Date File Tag function" should "return a value equal to todays date (yyyy-mm-dd) in" {
    //     val date = LocalDate.now
    //     val dateFileTag = date.toString()
    //     assert(ProcessControllerHelper.getDateFileTag() == dateFileTag)
    // }
}


















