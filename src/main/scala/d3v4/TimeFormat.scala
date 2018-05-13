package d3v4

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("d3-time-format", JSImport.Namespace)
@js.native
object d3timeformat extends js.Object {
  def timeParse(specifier: String): js.Function1[String, js.Date] = js.native
  def timeFormat(specifier: String): js.Function1[js.Date, String] = js.native
  def utcParse(specifier: String): js.Function1[String, js.Date] = js.native
  def utcFormat(specifier: String): js.Function1[js.Date, String] = js.native
}
