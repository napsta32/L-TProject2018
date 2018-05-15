package example

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import splot.g

import scala.scalajs.js.JSON

object TestSplot {

  @JSExportTopLevel("myproject")
  protected def getInstance(): this.type = this

  @JSExport
  def main (args: Array[String]): Unit = {

    g.layers(0, 0, 900, 450) {
      /*
      canvas(0, 100, 900, 200) {
        // Draw shapes
      }
      */
      /*
      map(0, 0, 900, 450) {
        // Draw map of something
      }
      */
      /*
      plot (40, 40, 400, 400) {
        // Plot data here
      }
      */
      g.chord (100, 100, 500, 500) {
        val data = g.buildGraph(
          (10: Int, "A" -> "B"),
          (30: Int, "A" -> "C"),
          (60: Int, "D" -> "E"),
          (70: Int, "F" -> "G"),
          (80: Int, "H" -> "C")
        )

        g.setData(data)
      }
      g.chord (100, 100, 200, 200) {
        val data = g.buildGraph (
          (10: Int, "A" -> "B"),
          (30: Int, "A" -> "C"),
          (60: Int, "D" -> "E"),
          (70: Int, "F" -> "G"),
          (80: Int, "H" -> "C")
        )

        g.setData(data)
        /*
        g.data = g.matrix(g.row(100, 200, 200, 0, 0),
          g.row(100, 200, 200, 0, 0),
          g.row(100, 200, 200, 0, 0),
          g.row(100, 200, 200, 0, 0))
        g.data = g.matrix(g.column(100, 200, 200, 0, 0),
          g.column(100, 200, 200, 0, 0),
          g.column(100, 200, 200, 0, 0),
          g.column(100, 200, 200, 0, 0))
                         */
      }
    }

  }

}
