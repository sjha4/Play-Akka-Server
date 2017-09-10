
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/samir/Desktop/3rd sem/SOC/P2/airline/conf/routes
// @DATE:Sat Sep 09 22:23:03 EDT 2017

import play.api.mvc.Call


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers {

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:16
    def getOperators(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "operators")
    }
  
    // @LINE:14
    def sayHello(name:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "local-hello/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("name", name)))
    }
  
    // @LINE:20
    def getOperatorFlightDetails(operator:String, flight:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "operators/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("operator", operator)) + "/flights/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("flight", flight)))
    }
  
    // @LINE:18
    def getOperatorFlights(operator:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "operators/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("operator", operator)) + "/flights")
    }
  
    // @LINE:12
    def postTrips(from:String, to:String): Call = {
      
      Call("POST", _prefix + { _defaultPrefix } + "trip/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("from", from)) + "/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("to", to)))
    }
  
    // @LINE:8
    def getTrips(): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "trips")
    }
  
    // @LINE:6
    def index(): Call = {
      
      Call("GET", _prefix)
    }
  
    // @LINE:10
    def getTripDetails(tripID:String): Call = {
      
      Call("GET", _prefix + { _defaultPrefix } + "trips/" + play.core.routing.dynamicString(implicitly[play.api.mvc.PathBindable[String]].unbind("tripID", tripID)))
    }
  
  }

  // @LINE:23
  class ReverseAssets(_prefix: => String) {
    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:23
    def versioned(file:Asset): Call = {
      implicit val _rrc = new play.core.routing.ReverseRouteContext(Map(("path", "/public")))
      Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[play.api.mvc.PathBindable[Asset]].unbind("file", file))
    }
  
  }


}
