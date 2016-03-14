package com.server

import com.services.ProcessControllerService

import akka.actor.ActorSystem
import com.support.CORSSupport
import spray.http.MediaTypes
import spray.routing.{Route, SimpleRoutingApp}

object ProcessConrollerServer extends App with SimpleRoutingApp with CORSSupport{


  implicit val actorSystem = ActorSystem()


  //Custom directive to replace the inclcusion of the stated return type header
  def getJson(route: Route) = get{
    respondWithMediaType(MediaTypes.`application/json`){
      route
    }
  }

  //Define Each route independently as lazy vals to keep code clean
  //Link the names of each route in the start server method

  lazy val helloRoute = get {
      cors{
        path("hello") {
          complete {
            "Welcome to the Process Controller Service \n here are a list of the available routes:"
          }
        }
      }
  }
  
  
  lazy val processController_BG = getJson {
      cors{
        path("api" / "processControllers" / "processA" / "locationType" / Segment / "locationValue" / Segment) { (queryType, queryValue) =>
          complete {
            ProcessControllerService.processController_BG(queryType, queryValue)
          }
        }
      }
  }
  
  lazy val processController_BG_with_stats = getJson {
      cors{
        path("api" / "processControllers" / "processA" / "locationType" / Segment / "locationValue" / Segment/ "withStats") { (queryType, queryValue) =>
          complete {
            ProcessControllerService.processController_BG_with_stats(queryType, queryValue)
          }
        }
      }
  }
  
  lazy val processController_BG_Statistics = getJson {
      cors{
        path("api" / "processControllers" / "processA" / "getStatistics") {
          complete {
            ProcessControllerService.getRuntimeStatistics()
          }
        }
      }
  }
  

  startServer(interface = "localhost", port = 8083) {
    helloRoute~
    processController_BG~
    processController_BG_with_stats~
    processController_BG_Statistics
  }

}