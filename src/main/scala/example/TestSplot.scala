package example

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import splot.g

import scala.scalajs.js.JSON

object TestSplot {

  @JSExportTopLevel("myproject")
  protected def getInstance(): this.type = this

  @JSExport
  def main (args: Array[String]): Unit = {

    g.layers(100, 100, 1000, 600) {
      /*
      canvas(0, 100, 900, 200) {
        // Draw shapes
      }
      */
      /*
      plot (40, 40, 400, 400) {
        // Plot data here
      }
      */
      g.chord (100, 100, 1000, 600) {
        val data = g.buildGraph(
          (10: Int, "A" -> "B"),
          (30: Int, "A" -> "C"),
          (60: Int, "D" -> "E"),
          (70: Int, "F" -> "G"),
          (80: Int, "H" -> "C")
        )

        g.setData(data)
        g.setValue(g.CHORD_INNER_RADIUS, 12)
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
        // g.setDouble(g.CHORD_INNER_RADIUS, 12)
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

    g.layers (900, 450) {
      g.worldmap(0, 0, 900, 450) {
        // Draw map of something
      }
    }

  }

}
