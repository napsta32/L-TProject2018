package d3v4

import scalajs.js
import scalajs.js.{undefined, `|`}
import scala.scalajs.js.annotation._

// https://github.com/d3/d3-axis

@JSImport("d3-geo", JSImport.Namespace)
@js.native
object d3geo extends js.Object {

  def geoPath(): GeoPath = js.native
  def geoMercator(): Projection = js.native

}

@js.native
trait GeoPath extends js.Object {

  def area(): Double = js.native
  def measure(): Double = js.native
  def centroid(): js.Tuple2[Double, Double] = js.native
  def projection(): Projection = js.native
  def projection(newProjection: Projection): GeoPath = js.native

}

@js.native
trait Projection extends js.Object {

  def scale(): Double = js.native
  def scale(k: Double): Projection = js.native

  def translate(): js.Tuple2[Double, Double] = js.native
  def translate(t: js.Tuple2[Double, Double]): Projection = js.native

  def center(): js.Tuple2[Double, Double] = js.native
  def center(deg: js.Tuple2[Double, Double]): Projection = js.native

}