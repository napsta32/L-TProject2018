package example

import d3v4.{ListenerFunction0, _}
import topojson.topojson

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.{JSON, UndefOr, |}
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom

object ScalaJSExample {


  val WORLD_COUNTRIES_URL = "https://s3-eu-west-1.amazonaws.com/languages-and-translators/world_countries.json"
  val WORLD_POPULATION_URL = "https://s3-eu-west-1.amazonaws.com/languages-and-translators/world_population.tsv"

  @js.native
  trait Polygon extends js.Object {
    def coordinates: js.Array[js.Array[js.Tuple2[Double, Double]]]
  }

  @js.native
  trait Feature extends js.Object {
    def properties: js.Dictionary[String]
    def id: String
    def geometry: Polygon
    def population : Double
  }

  @js.native
  trait CountryStruct extends js.Object {
    def properties: js.Dictionary[String]
    def population: Primitive
    def features: js.Array[Feature]
  }

  @JSExportTopLevel("myproject")
  protected def getInstance(): this.type = this

  def groupTicks(d: ChordGroup, step: Double): js.Array[js.Dictionary[Double]] = {
    var k: Double = (d.endAngle - d.startAngle) / d.value
    d3.range(0, d.value, step).map((v: Double) => js.Dictionary("value" -> v, "angle" -> (v * k + d.startAngle)))
  }

  @JSExport
  def main(args: Array[String]): Unit = {

    var pi = Math.PI
    var tau = 2 * pi


    var width = Math.max(960, dom.window.innerWidth)
    var height = Math.max(500, dom.window.innerHeight);

    // Initialize the projection to fit the world in a 1×1 square centered at the origin.
    var projection = d3.geoMercator()
      .scale(1 / tau)
      .translate(js.Tuple2(0,0));

    var path = d3.geoPath().projection(projection);

    var tile = d3.tile().size(js.Tuple2[Double,Double](width, height));

    type ListenerFunction0 = js.Function0[Unit]
    var z: ListenerFunction0 = zoomed()

    var zoom = d3.zoom()
      .scaleExtent(js.Array[Double](2048,16384))
      .on("zoom",  z);

    var svg = d3.select("svg")
      .attr("width", width)
      .attr("height", height);

    var raster = svg.append("g");
    var vector = svg.append("path");

    var url = "https://gist.githubusercontent.com/mbostock/4090846/raw/d534aba169207548a8a3d670c9c2cc719ff05c47/world-50m.json";

    d3.json(url, function(error, world) {
      if (error) throw error;

      vector.datum(topojson.feature(world, world.objects.countries));

      // Compute the projected initial center.
      var center = projection(js.Array[Double](-98.5, 39.5));

      // Apply a zoom transform equivalent to projection.{scale,translate,center}.
      svg
        .call(zoom)
        .call(zoom.transform, d3.zoomIdentity
          .translate(width / 2, height / 2)
          .scale(1 << 12)
          .translate(-center[0], -center[1]));

    });

    def zoomed() {
      var transform = d3.event.transform

      var tiles = tile
        .scale(transform.k)
        .translate(js.Tuple2[Double,Double](transform.x, transform.y))
      ();


      projection
        .scale(transform.k / tau)
        .translate(js.Tuple2[Double,Double](transform.x, transform.y));

      vector.attr("d", path);

      var image = raster
        .attr("transform", stringify(tiles.scale, tiles.translate))
        .selectAll("image")
        .data(tiles, function(d) { return d; });

      image.exit().remove();

      image.enter().append("image")
        .attr("xlink:href", function(d) { return "http://" + "abc"[d[1] % 3] + ".tile.openstreetmap.org/" + d[2] + "/" + d[0] + "/" + d[1] + ".png"; })
        .attr("x", function(d) { return d[0] * 256; })
        .attr("y", function(d) { return d[1] * 256; })
        .attr("width", 256)
        .attr("height", 256);
    }


    def stringify(scale: Double, translate: js.Object) {
      var k = scale / 256
      var r = Math.round(scale%1);
      "translate(" + r(translate[0] * scale) + "," + r(translate[1] * scale) + ") scale(" + k + ")";
    }

  }

}