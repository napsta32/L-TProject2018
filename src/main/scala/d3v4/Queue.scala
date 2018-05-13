package d3v4

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("d3-request", JSImport.Namespace)
@js.native
object d3queue extends js.Object {
  def queue(): D3Queue = js.native
}

@js.native
trait D3Queue extends js.Object {
  type CallbackType = (String, js.Function) => Unit

  def defer(callback: CallbackType, args: Primitive*): this.type = js.native
  // def defer(callback: (String, js.Function1[js.Object, Unit]) => Unit, args: Primitive*): this.type = js.native
  // def defer(callback: (String, (js.Any, js.Array[js.Dictionary[String]]) => Unit) => Unit, args: Primitive*): this.type = js.native
  def abort(): this.type = js.native;
  def await(callback: (js.Any, js.Object*) => Unit): this.type = js.native
}
