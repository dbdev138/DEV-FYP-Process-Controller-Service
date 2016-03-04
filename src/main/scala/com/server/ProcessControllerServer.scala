package com.server

import com.services.ProcessControllerService

import akka.actor.ActorSystem
import com.support.CORSSupport
import spray.http.MediaTypes
import spray.routing.{Route, SimpleRoutingApp}

object GTServer extends App with SimpleRoutingApp with CORSSupport{


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
  

  startServer(interface = "localhost", port = 8083) {
    helloRoute~
    processController_BG
  }

}