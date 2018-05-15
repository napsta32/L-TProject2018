package example

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import splot.{MyTuple, SplotUtils, g}

import scala.scalajs.js.JSON

object TestSplot {

  @JSExportTopLevel("myproject")
  protected def getInstance(): this.type = this

  @JSExport
  def main (args: Array[String]): Unit = {

    g.layers(1000, 600) {
      g.chord (100, 100,300, 300) {
        val data = g.matrix(
          // Specify amount of flow, source and destination
          g.row(11975,  5871, 8916, 2868),
          g.row(1951, 10048, 2060, 6171),
          g.row(8010, 16145, 8090, 8045),
          g.row(1013,   990,  940, 6907)
        )

        g.setData(data, g.row("A", "B", "C", "D")) // Send data to plotter
        // Set custom property
        g.setValue(g.CHORD_OUTER_RADIUS, 150)
      }
    }

    g.layers(1000, 600) {
      g.chord (300, 300) {
        val data = g.buildGraph(
          (10: Int, "A" -> "B"),
          (30: Int, "A" -> "C"),
          (60: Int, "D" -> "E"),
          (70: Int, "F" -> "G"),
          (80: Int, "H" -> "C")
        )

        g.setData(data)
        g.setValue(g.CHORD_OUTER_RADIUS, 150)
      }
    }

    g.layers(1000, 600) {
      g.chord (300, 300) {
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
    }

    g.layers (900, 450) {
      g.worldmap(900, 450) {
        g.setZoom(true) // Enable zoom features

        /// Handlers
        g.setCountryHandler(g.EVENT_MOUSE_OVER, (dom, countryName) => {
          dom // Received selected DOM object
            .style("opacity", "1") // Maximize opacity of selected country
          println(countryName) // Show selected country in console
        })

        g.setCountryHandler(g.EVENT_MOUSE_OUT, (dom, countryName) => {
          dom
            .style("opacity", "0.8") // Hide country
            .style("fill", SplotUtils.getRandomColor()) // Change color
        })
      }
    }

  }

}
