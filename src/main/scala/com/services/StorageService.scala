package com.services


import com.amazonaws.AmazonClientException
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.transfer.TransferManager
import com.amazonaws.services.s3.transfer.Upload

import java.io.{PrintWriter, File}
import java.util.Calendar
import java.time.LocalDate

import net.liftweb.json._
import net.liftweb.json.Serialization.write
import net.liftweb.json.JsonDSL._

object StorageService {
    
    implicit val formats = DefaultFormats
    
    
    //HelperMethods
      def getDateFileTag(): String = {
      val time = LocalDate.now
      
      val dateFileTag = time.toString()
      return dateFileTag
  }
  
     def timeStamp(): String = {
       val time = Calendar.getInstance.getTimeInMillis().toInt
       return time.toString()
   }
    
    
    def writeObjectToS3(data: String, fileName: String): String = { 
        
        
        val f: File = new File(fileName)
        f.createNewFile()
        val pw: PrintWriter = new PrintWriter(f)
        pw.write(data);
        pw.close();
        
        val accessKeyId = "AKIAJWMAYENFLOUTQ72A" 
        val secretAccessKey = "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"
        
        val cred: AWSCredentials = new BasicAWSCredentials(accessKeyId, secretAccessKey)
        // AWSCredentials cred = new BasicAWSCredentials(accessKeyId, secretAccessKey);
        
        val tm: TransferManager = new TransferManager(cred)
        
        val upload: Upload = tm.upload("microdg-test", fileName, f)
        
        
        upload.waitForCompletion();
        println("Upload complete.");
        f.getAbsoluteFile().delete();
        
        
        return "Find the file at this location:"
    }
}