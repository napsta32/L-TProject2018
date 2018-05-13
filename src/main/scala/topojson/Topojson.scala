package topojson

import d3v4.Primitive

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("topojson", JSImport.Namespace)
@js.native
object topojson extends js.Object {
    def mesh[Topology](topology: js.Array[Topology], comparator: js.Function2[Topology, Topology, Primitive]): this.type = js.native
}