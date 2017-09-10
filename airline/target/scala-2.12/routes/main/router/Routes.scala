
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/samir/Desktop/3rd sem/SOC/P2/airline/conf/routes
// @DATE:Sat Sep 09 22:23:03 EDT 2017

package router

import play.core.routing._
import play.core.routing.HandlerInvokerFactory._

import play.api.mvc._

import _root_.controllers.Assets.Asset
import _root_.play.libs.F

class Routes(
  override val errorHandler: play.api.http.HttpErrorHandler, 
  // @LINE:6
  HomeController_0: controllers.HomeController,
  // @LINE:23
  Assets_1: controllers.Assets,
  val prefix: String
) extends GeneratedRouter {

   @javax.inject.Inject()
   def this(errorHandler: play.api.http.HttpErrorHandler,
    // @LINE:6
    HomeController_0: controllers.HomeController,
    // @LINE:23
    Assets_1: controllers.Assets
  ) = this(errorHandler, HomeController_0, Assets_1, "/")

  def withPrefix(prefix: String): Routes = {
    router.RoutesPrefix.setPrefix(prefix)
    new Routes(errorHandler, HomeController_0, Assets_1, prefix)
  }

  private[this] val defaultPrefix: String = {
    if (this.prefix.endsWith("/")) "" else "/"
  }

  def documentation = List(
    ("""GET""", this.prefix, """controllers.HomeController.index"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """trips""", """controllers.HomeController.getTrips()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """trips/""" + "$" + """tripID<[^/]+>""", """controllers.HomeController.getTripDetails(tripID:String)"""),
    ("""POST""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """trip/""" + "$" + """from<[^/]+>/""" + "$" + """to<[^/]+>""", """controllers.HomeController.postTrips(from:String, to:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """local-hello/""" + "$" + """name<[^/]+>""", """controllers.HomeController.sayHello(name:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """operators""", """controllers.HomeController.getOperators()"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """operators/""" + "$" + """operator<[^/]+>/flights""", """controllers.HomeController.getOperatorFlights(operator:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """operators/""" + "$" + """operator<[^/]+>/flights/""" + "$" + """flight<[^/]+>""", """controllers.HomeController.getOperatorFlightDetails(operator:String, flight:String)"""),
    ("""GET""", this.prefix + (if(this.prefix.endsWith("/")) "" else "/") + """assets/""" + "$" + """file<.+>""", """controllers.Assets.versioned(path:String = "/public", file:Asset)"""),
    Nil
  ).foldLeft(List.empty[(String,String,String)]) { (s,e) => e.asInstanceOf[Any] match {
    case r @ (_,_,_) => s :+ r.asInstanceOf[(String,String,String)]
    case l => s ++ l.asInstanceOf[List[(String,String,String)]]
  }}


  // @LINE:6
  private[this] lazy val controllers_HomeController_index0_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix)))
  )
  private[this] lazy val controllers_HomeController_index0_invoker = createInvoker(
    HomeController_0.index,
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "index",
      Nil,
      "GET",
      this.prefix + """""",
      """ An example controller showing a sample home page""",
      Seq()
    )
  )

  // @LINE:8
  private[this] lazy val controllers_HomeController_getTrips1_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("trips")))
  )
  private[this] lazy val controllers_HomeController_getTrips1_invoker = createInvoker(
    HomeController_0.getTrips(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getTrips",
      Nil,
      "GET",
      this.prefix + """trips""",
      """""",
      Seq()
    )
  )

  // @LINE:10
  private[this] lazy val controllers_HomeController_getTripDetails2_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("trips/"), DynamicPart("tripID", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_getTripDetails2_invoker = createInvoker(
    HomeController_0.getTripDetails(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getTripDetails",
      Seq(classOf[String]),
      "GET",
      this.prefix + """trips/""" + "$" + """tripID<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:12
  private[this] lazy val controllers_HomeController_postTrips3_route = Route("POST",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("trip/"), DynamicPart("from", """[^/]+""",true), StaticPart("/"), DynamicPart("to", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_postTrips3_invoker = createInvoker(
    HomeController_0.postTrips(fakeValue[String], fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "postTrips",
      Seq(classOf[String], classOf[String]),
      "POST",
      this.prefix + """trip/""" + "$" + """from<[^/]+>/""" + "$" + """to<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:14
  private[this] lazy val controllers_HomeController_sayHello4_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("local-hello/"), DynamicPart("name", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_sayHello4_invoker = createInvoker(
    HomeController_0.sayHello(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "sayHello",
      Seq(classOf[String]),
      "GET",
      this.prefix + """local-hello/""" + "$" + """name<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:16
  private[this] lazy val controllers_HomeController_getOperators5_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("operators")))
  )
  private[this] lazy val controllers_HomeController_getOperators5_invoker = createInvoker(
    HomeController_0.getOperators(),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getOperators",
      Nil,
      "GET",
      this.prefix + """operators""",
      """""",
      Seq()
    )
  )

  // @LINE:18
  private[this] lazy val controllers_HomeController_getOperatorFlights6_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("operators/"), DynamicPart("operator", """[^/]+""",true), StaticPart("/flights")))
  )
  private[this] lazy val controllers_HomeController_getOperatorFlights6_invoker = createInvoker(
    HomeController_0.getOperatorFlights(fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getOperatorFlights",
      Seq(classOf[String]),
      "GET",
      this.prefix + """operators/""" + "$" + """operator<[^/]+>/flights""",
      """""",
      Seq()
    )
  )

  // @LINE:20
  private[this] lazy val controllers_HomeController_getOperatorFlightDetails7_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("operators/"), DynamicPart("operator", """[^/]+""",true), StaticPart("/flights/"), DynamicPart("flight", """[^/]+""",true)))
  )
  private[this] lazy val controllers_HomeController_getOperatorFlightDetails7_invoker = createInvoker(
    HomeController_0.getOperatorFlightDetails(fakeValue[String], fakeValue[String]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.HomeController",
      "getOperatorFlightDetails",
      Seq(classOf[String], classOf[String]),
      "GET",
      this.prefix + """operators/""" + "$" + """operator<[^/]+>/flights/""" + "$" + """flight<[^/]+>""",
      """""",
      Seq()
    )
  )

  // @LINE:23
  private[this] lazy val controllers_Assets_versioned8_route = Route("GET",
    PathPattern(List(StaticPart(this.prefix), StaticPart(this.defaultPrefix), StaticPart("assets/"), DynamicPart("file", """.+""",false)))
  )
  private[this] lazy val controllers_Assets_versioned8_invoker = createInvoker(
    Assets_1.versioned(fakeValue[String], fakeValue[Asset]),
    play.api.routing.HandlerDef(this.getClass.getClassLoader,
      "router",
      "controllers.Assets",
      "versioned",
      Seq(classOf[String], classOf[Asset]),
      "GET",
      this.prefix + """assets/""" + "$" + """file<.+>""",
      """ Map static resources from the /public folder to the /assets URL path""",
      Seq()
    )
  )


  def routes: PartialFunction[RequestHeader, Handler] = {
  
    // @LINE:6
    case controllers_HomeController_index0_route(params) =>
      call { 
        controllers_HomeController_index0_invoker.call(HomeController_0.index)
      }
  
    // @LINE:8
    case controllers_HomeController_getTrips1_route(params) =>
      call { 
        controllers_HomeController_getTrips1_invoker.call(HomeController_0.getTrips())
      }
  
    // @LINE:10
    case controllers_HomeController_getTripDetails2_route(params) =>
      call(params.fromPath[String]("tripID", None)) { (tripID) =>
        controllers_HomeController_getTripDetails2_invoker.call(HomeController_0.getTripDetails(tripID))
      }
  
    // @LINE:12
    case controllers_HomeController_postTrips3_route(params) =>
      call(params.fromPath[String]("from", None), params.fromPath[String]("to", None)) { (from, to) =>
        controllers_HomeController_postTrips3_invoker.call(HomeController_0.postTrips(from, to))
      }
  
    // @LINE:14
    case controllers_HomeController_sayHello4_route(params) =>
      call(params.fromPath[String]("name", None)) { (name) =>
        controllers_HomeController_sayHello4_invoker.call(HomeController_0.sayHello(name))
      }
  
    // @LINE:16
    case controllers_HomeController_getOperators5_route(params) =>
      call { 
        controllers_HomeController_getOperators5_invoker.call(HomeController_0.getOperators())
      }
  
    // @LINE:18
    case controllers_HomeController_getOperatorFlights6_route(params) =>
      call(params.fromPath[String]("operator", None)) { (operator) =>
        controllers_HomeController_getOperatorFlights6_invoker.call(HomeController_0.getOperatorFlights(operator))
      }
  
    // @LINE:20
    case controllers_HomeController_getOperatorFlightDetails7_route(params) =>
      call(params.fromPath[String]("operator", None), params.fromPath[String]("flight", None)) { (operator, flight) =>
        controllers_HomeController_getOperatorFlightDetails7_invoker.call(HomeController_0.getOperatorFlightDetails(operator, flight))
      }
  
    // @LINE:23
    case controllers_Assets_versioned8_route(params) =>
      call(Param[String]("path", Right("/public")), params.fromPath[Asset]("file", None)) { (path, file) =>
        controllers_Assets_versioned8_invoker.call(Assets_1.versioned(path, file))
      }
  }
}
