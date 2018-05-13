package d3v4

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("d3-request", JSImport.Namespace)
@js.native
object d3request extends js.Object {

  type CallbackType = js.Function2[js.Any, js.Array[js.Dictionary[String]], Unit]
  type DataCallback = js.Function1[js.Object, Unit]

  def request(url: String, callback: (XHR) => Unit): Unit = js.native
  def csv(url: String, callback: CallbackType): Unit = js.native
  def tsv(url: String, callback: CallbackType): Unit = js.native
  def json(url: String, data: DataCallback): Unit = js.native
  // def csv(url: String, callback: js.Function): Unit = js.native
  // def tsv(url: String, callback: js.Function): Unit = js.native
  // def json(url: String, data: js.Function): Unit = js.native

}

@js.native
trait XHR extends js.Object {
  def responseText: String
}