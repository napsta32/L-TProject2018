package splot {

  object g {
    val CHORD_INNER_RADIUS = "innerRadius"
    val CHORD_OUTER_RADIUS = "outerRadius"

    val EVENT_MOUSE_OVER = "mouseover"
    val EVENT_MOUSE_OUT = "mouseout"
    val EVENT_PICK_COUNTRY_COLOR = "pick_country_color"
  }

  // object worldmap_context
  // object chord_context

}

package object splot {

  implicit def splot2layers(layers: g.type): splotlayers.type = splotlayers
  implicit def splot2chord(chord: g.type): publicSplotchord.type = publicSplotchord
  implicit def splot2graph(graph: g.type): Graph.type = Graph
  implicit def splot2drawing(drawing: g.type): splotdrawing.type = splotdrawing
  implicit def splot2map(map: g.type): splotmapfunc.type = splotmapfunc

  /*
  implicit def wmc2graph(graph: worldmap_context.type): Graph.type = Graph
  implicit def wmc2drawing(drawing: worldmap_context.type): splotdrawing.type = splotdrawing
  implicit def wmc2map(map: worldmap_context.type): splotmapfunc.type = splotmapfunc

  implicit def chord2graph(graph: worldmap_context.type): Graph.type = Graph
  implicit def chord2drawing(drawing: worldmap_context.type): splotdrawing.type = splotdrawing
  implicit def chord2chord(chord: g.type): publicSplotchord.type = publicSplotchord
  */

  abstract class SemiGroup[A] {
    def equals(a: A, b: A): Boolean = {
      a == b
    }
  }

  abstract class Monoid[A] extends SemiGroup[A] {
    def unit: A
  }

  implicit object StringMonoid extends Monoid[String] {
    def add(x: String, y: String): String = x concat y
    def unit: String = ""
  }

  implicit object IntMonoid extends Monoid[Int] {
    def add(x: Int, y: Int): Int = x + y
    def unit: Int = 0
  }

  implicit object DoubleMonoid extends Monoid[Double] {
    def add(x: Double, y: Double): Double = x + y
    def unit: Double = 0.0
  }

}
