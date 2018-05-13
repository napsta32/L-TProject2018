package d3v4

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("d3-request", JSImport.Namespace)
@js.native
object d3request extends js.Object {

  def request(url: String, callback: (XHR) => Unit): Unit = js.native
  def csv(url: String, callback: (js.Any, js.Array[js.Dictionary[String]]) => Unit): Unit = js.native
  def tsv(url: String, callback: (js.Any, js.Array[js.Dictionary[String]]) => Unit): Unit = js.native

}

@js.native
trait XHR extends js.Object {
  def responseText: String
}