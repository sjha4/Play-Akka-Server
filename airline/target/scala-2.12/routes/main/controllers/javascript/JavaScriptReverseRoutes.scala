
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/samir/Desktop/3rd sem/SOC/P2/airline/conf/routes
// @DATE:Sun Sep 10 08:45:51 EDT 2017

import play.api.routing.JavaScriptReverseRoute


import _root_.controllers.Assets.Asset
import _root_.play.libs.F

// @LINE:6
package controllers.javascript {

  // @LINE:6
  class ReverseHomeController(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:22
    def getOperators: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getOperators",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "operators"})
        }
      """
    )
  
    // @LINE:20
    def sayHello: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.sayHello",
      """
        function(name0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "local-hello/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("name", name0))})
        }
      """
    )
  
    // @LINE:26
    def getOperatorFlightDetails: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getOperatorFlightDetails",
      """
        function(operator0,flight1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "operators/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("operator", operator0)) + "/flights/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("flight", flight1))})
        }
      """
    )
  
    // @LINE:14
    def postFail: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.postFail",
      """
        function(airline0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "actor/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("airline", airline0)) + "/confirm_fail"})
        }
      """
    )
  
    // @LINE:24
    def getOperatorFlights: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getOperatorFlights",
      """
        function(operator0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "operators/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("operator", operator0)) + "/flights"})
        }
      """
    )
  
    // @LINE:18
    def postReset: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.postReset",
      """
        function(airline0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "actor/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("airline", airline0)) + "/reset"})
        }
      """
    )
  
    // @LINE:12
    def postTrips: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.postTrips",
      """
        function(from0,to1) {
          return _wA({method:"POST", url:"""" + _prefix + { _defaultPrefix } + """" + "trip/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("from", from0)) + "/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("to", to1))})
        }
      """
    )
  
    // @LINE:16
    def postNoResponse: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.postNoResponse",
      """
        function(airline0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "actor/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("airline", airline0)) + "/confirm_no_response"})
        }
      """
    )
  
    // @LINE:8
    def getTrips: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getTrips",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "trips"})
        }
      """
    )
  
    // @LINE:6
    def index: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.index",
      """
        function() {
          return _wA({method:"GET", url:"""" + _prefix + """"})
        }
      """
    )
  
    // @LINE:10
    def getTripDetails: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.HomeController.getTripDetails",
      """
        function(tripID0) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "trips/" + encodeURIComponent((""" + implicitly[play.api.mvc.PathBindable[String]].javascriptUnbind + """)("tripID", tripID0))})
        }
      """
    )
  
  }

  // @LINE:29
  class ReverseAssets(_prefix: => String) {

    def _defaultPrefix: String = {
      if (_prefix.endsWith("/")) "" else "/"
    }

  
    // @LINE:29
    def versioned: JavaScriptReverseRoute = JavaScriptReverseRoute(
      "controllers.Assets.versioned",
      """
        function(file1) {
          return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[play.api.mvc.PathBindable[Asset]].javascriptUnbind + """)("file", file1)})
        }
      """
    )
  
  }


}
