package d3v4

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSImport}

// https://github.com/d3/d3-axis

@JSImport("d3-tip", JSImport.Default)
@js.native
class d3tip() extends js.Object {
  def show(): this.type = js.native
  def show[Datum](d: Datum): this.type = js.native
  def hide(): this.type = js.native
  def hide[Datum](d: Datum): this.type = js.native
  def attr(name: String, value: Primitive): this.type = js.native
  def style(name: String, value: Primitive): this.type = js.native
  // Public: Set or get the direction of the tooltip
  //
  // v - One of n(north), s(south), e(east), or w(west), nw(northwest),
  //     sw(southwest), ne(northeast) or se(southeast)
  //
  // Returns tip or direction
  def direction(): String = js.native
  def direction(value: String): this.type = js.native
  def offset(): js.Tuple2[Double, Double] = js.native
  def offset(value: js.Tuple2[Double, Double]): this.type = js.native
  def html(): String = js.native
  def html(value: String): this.type = js.native
  def html(value: js.Function1[js.Object, String]): js.Function = js.native
  def destroy(): this.type = js.native
}
