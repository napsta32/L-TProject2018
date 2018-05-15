package splot {

  object g {
    val CHORD_INNER_RADIUS = "innerRadius"
    val CHORD_OUTER_RADIUS = "outerRadius"
  }

}

package object splot {

  implicit def splot2layers(layers: g.type): splotlayers.type = splotlayers
  implicit def splot2chord(chord: g.type): publicSplotchord.type = publicSplotchord
  implicit def splot2graph(graph: g.type): Graph.type = Graph
  implicit def splot2drawing(drawing: g.type): splotdrawing.type = splotdrawing

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
