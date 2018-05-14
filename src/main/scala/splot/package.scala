package splot {

  object g

}

package object splot {

  implicit def splot2layers(layers: g.type): splotlayers.type = splotlayers
  implicit def splot2chord(layers: g.type): splotchord.type = splotchord

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


}
