
// @GENERATOR:play-routes-compiler
// @SOURCE:C:/Users/samir/Desktop/3rd sem/SOC/P2/airline/conf/routes
// @DATE:Sat Sep 09 03:35:36 EDT 2017


package router {
  object RoutesPrefix {
    private var _prefix: String = "/"
    def setPrefix(p: String): Unit = {
      _prefix = p
    }
    def prefix: String = _prefix
    val byNamePrefix: Function0[String] = { () => prefix }
  }
}
