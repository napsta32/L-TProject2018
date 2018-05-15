package splot

object SplotUtils {

  def HSVtoRGB(h: Double, s: Double, v: Double): (Double, Double, Double) = {
    var r: Double = 0
    var g: Double = 0
    var b: Double = 0
    var i: Double = 0
    var f: Double = 0
    var p: Double = 0
    var q: Double = 0
    var t: Double = 0

    i = Math.floor(h * 6)
    f = h * 6 - i
    p = v * (1 - s)
    q = v * (1 - f * s)
    t = v * (1 - (1 - f) * s)
    i % 6 match {
      case 0 =>
        r = v
        g = t
        b = p
      case 1 =>
        r = q
        g = v
        b = p
      case 2 =>
        r = p
        g = v
        b = t
      case 3 =>
        r = p
        g = q
        b = v
      case 4 =>
        r = t
        g = p
        b = v
      case 5 =>
        r = v
        g = p
        b = q
    }
    (Math.round(r * 255), Math.round(g * 255), Math.round(b * 255))
  }

  def pad2(str: String): String = {
    if(str.length == 1)
      return "0" + str
    str
  }

  def getRandomColor(): String = {
    val h = Math.random()*2*Math.PI; // Keep random
    val s = 0.5 + Math.round(Math.random()*2)*.25; // Ternary (50%, 75%, 100%)
    val v = 0.5 + Math.round(Math.random()*2)*.25; // Ternary (50%, 75%, 100%)

    var rgb = HSVtoRGB(h, s, v)

    val r = pad2(rgb._1.toInt.toHexString)
    val g = pad2(rgb._2.toInt.toHexString)
    val b = pad2(rgb._3.toInt.toHexString)

    "#" + r + g + b;
  }

}
