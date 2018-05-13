package d3v4

import scalajs.js
import scalajs.js.{`|`, undefined}
import scala.scalajs.js.annotation._
import org.scalajs.dom.CanvasRenderingContext2D

// https://github.com/d3/d3-shape
@JSImport("d3-shape", JSImport.Namespace)
@js.native
object d3shape extends js.Object {
  def area[T](): Area[T] = js.native
  def pie(): PieGenerator = js.native
  def arc(): ArcGenerator = js.native
  def line(): LineGenerator = js.native
  def curveBasisClosed: CurveFactory = js.native
  def curveCardinalClosed: CurveFactory = js.native
  def curveCatmullRomClosed: CurveCatmullRomFactory = js.native
  def curveLinear: CurveFactory = js.native
  def curveLinearClosed: CurveFactory = js.native
}

@js.native
trait BaseGenerator[G <: BaseGenerator[G]] extends js.Object {
  def context(context: CanvasRenderingContext2D):G = js.native
  def context():CanvasRenderingContext2D = js.native
}

@js.native
trait BaseLineGenerator[G <: BaseLineGenerator[G]] extends js.Object with BaseGenerator[G] {
  def curve(curve: CurveFactory): G = js.native
}

@js.native
trait LineGenerator extends BaseLineGenerator[LineGenerator] {
  def apply(data: js.Array[js.Tuple2[Double, Double]]): String = js.native

  def x[T](): Double = js.native
  def x[T](x: Double): LineGenerator = js.native
  def x[T](x: js.Function2[T, Int, Double]): LineGenerator = js.native
  def y[T](): Double | js.Function2[T, Int, Double] = js.native
  def y[T](x: Double): LineGenerator = js.native
  def y[T](y: js.Function2[T, Int, Double]): LineGenerator = js.native
}

@js.native
trait CurveFactory extends js.Object

@js.native
trait CurveCatmullRomFactory extends CurveFactory {
  def alpha(alpha: Double): CurveCatmullRomFactory = js.native
}

@js.native
trait PieGenerator extends js.Object {
  def value(value: Double): PieGenerator = js.native
  def padAngle(angle: Double): PieGenerator = js.native
  def apply[Datum](data: js.Array[Datum]): js.Array[PieArcDatum[Datum]] = js.native
}

@js.native
trait PieArcDatum[Datum] extends ArcDatum {
  def data: Datum = js.native
  def value: Double = js.native
  def index: Int = js.native
  def startAngle: Double = js.native
  def endAngle: Double = js.native
  def padAngle: Double = js.native
}

@js.native
trait ArcDatum extends js.Object

@js.native
trait BaseArcGenerator[G <: BaseArcGenerator[G]] extends js.Object with BaseGenerator[G] {
  def innerRadius(radius: Double): G = js.native
  def outerRadius(radius: Double): G = js.native
  def cornerRadius(radius: Double): G = js.native
}

@js.native
trait ArcGenerator extends BaseArcGenerator[ArcGenerator] {
  def apply[T](arguments: T): String = js.native
  def centroid[T](arguments: T): js.Tuple2[Double, Double] = js.native
}

@js.native
trait ArcGeneratorWithContext extends BaseArcGenerator[ArcGeneratorWithContext] {
  def apply[T <: ArcDatum](arguments: T): Unit = js.native
}

@js.native
trait Line[T] extends js.Function1[T,String] {

  def apply[T]():Line[T] = js.native

  def x(): Double | js.Function2[T, Int, Double] = js.native

  def x(x: Double): Line[T] = js.native

  def x(x: js.Function2[T, Int, Double]): Line[T] = js.native

  def y(): Double | js.Function2[T, Int, Double] = js.native

  def y(x: Double): Line[T] = js.native

  def y(y: js.Function2[T, Int, Double]): Line[T] = js.native

  def interpolate(): String | js.Function1[js.Array[js.Tuple2[Double, Double]], String] = js.native

  def interpolate(interpolate: String | js.Function1[js.Array[js.Tuple2[Double, Double]], String]): Line[T] = js.native

  def tension(): Double = js.native

  def tension(tension: Double): Line[T] = js.native

  def defined(): js.Function2[T, Double, Boolean] = js.native

  def defined(defined: js.Function2[T, Int, Boolean]): Line[T] = js.native

  def radial(): Line[T] = js.native

  def radius(): js.Function2[T, Double, Double] = js.native

  def radius(radius: Double): Line[T] = js.native

  def radius(radius: js.Function2[T, Double, Double]): Line[T] = js.native

  def angle(): js.Function2[T, Double, Double] = js.native

  def angle(angle: Double): Line[T] = js.native

  def angle(angle: js.Function2[T, Double, Double]): Line[T] = js.native

}

@js.native
trait Area[T] extends js.Object {
  def apply[T]():Area[T] = js.native

  def x(): Double | js.Function2[T, Int, Double] = js.native

  def x(x: Double): Area[T] = js.native

  def x(x: js.Function2[T, Int, Double]): Area[T] = js.native

  def x0(): Double | js.Function2[T, Int, Double] = js.native

  def x0(x0: Double): Area[T] = js.native

  def x0(x0: js.Function2[T, Int, Double]): Area[T] = js.native

  def x1(): Double | js.Function2[T, Int, Double] = js.native

  def x1(x1: Double): Area[T] = js.native

  def x1(x1: js.Function2[T, Int, Double]): Area[T] = js.native

  def y(): Double | js.Function2[T, Int, Double] = js.native

  def y(y: Double): Area[T] = js.native

  def y(y: js.Function2[T, Int, Double]): Area[T] = js.native

  def y0(): Double | js.Function2[T, Int, Double] = js.native

  def y0(y0: Double): Area[T] = js.native

  def y0(y0: js.Function2[T, Int, Double]): Area[T] = js.native

  def y1(): Double | js.Function2[T, Int, Double] = js.native

  def y1(y1: Double): Area[T] = js.native

  def y1(y1: js.Function2[T, Int, Double]): Area[T] = js.native

  def radial(): Line[T] = js.native

  def interpolate(): String | js.Function1[js.Array[js.Tuple2[Double, Double]], String] = js.native

  def interpolate(interpolate: String | js.Function1[js.Array[js.Tuple2[Double, Double]], String]): Area[T] = js.native

  def tension(): Double = js.native

  def tension(tension: Double): Area[T] = js.native

  def defined(): js.Function2[T, Int, Boolean] = js.native

  def defined(defined: js.Function2[T, Int, Boolean]): Area[T] = js.native
}
