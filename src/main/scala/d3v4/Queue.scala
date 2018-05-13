package d3v4

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.{UndefOr, |}

@JSImport("d3-queue", JSImport.Namespace)
@js.native
object d3queue extends js.Object {
  def queue(): RequestQueue = js.native
}

@js.native
trait RequestQueue extends js.Object {

  type ResponseCallback = js.Function2[js.Any, js.Array[js.Dictionary[String]], Unit]
  type DataCallback = js.Function1[js.Object, Unit]

  // type CallbackType = (String, js.Function) => Unit
  // type CallbackType1 =
  type CallbackType = js.Function2[String, DataCallback, Unit] | js.Function2[String, ResponseCallback, Unit]

  def defer(callback: CallbackType, args: String): this.type = js.native
  // def defer(callback: CallbackType2, args: String, arg2: UndefOr[String]): this.type = js.native
  // def defer(callback: (String, js.Function1[js.Object, Unit]) => Unit, args: Primitive*): this.type = js.native
  // def defer(callback: (String, (js.Any, js.Array[js.Dictionary[String]]) => Unit) => Unit, args: Primitive*): this.type = js.native
  def abort(): this.type = js.native;
  // def await(callback: js.Function2[js.Any, js.Object, Unit]): this.type = js.native
  def await(callback: js.Function3[js.Any, js.Object, js.Object, Unit]): this.type = js.native
}
