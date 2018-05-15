package splot

import scalajs.js
import topojson.topojson
import d3v4.{Primitive, Selection, ThresholdScale, d3}
import org.scalajs.dom
import org.scalajs.dom.EventTarget

import scala.scalajs.js.{UndefOr, |}

@js.native
trait Polygon extends js.Object {
  def coordinates: js.Array[js.Array[js.Tuple2[Double, Double]]]
}

@js.native
trait Feature extends js.Object {
  def properties: js.Dictionary[String]
  def id: String
  def geometry: Polygon
  def population : Primitive
}

@js.native
trait CountryStruct extends js.Object {
  def properties: js.Dictionary[String]
  def population: Primitive
  def features: js.Array[Feature]
}

class SplotMap(selection: Selection[dom.EventTarget], x: Int, y: Int, _width: Int, _height: Int) extends Drawing {
  private val WORLD_COUNTRIES_URL = "https://s3-eu-west-1.amazonaws.com/languages-and-translators/world_countries.json"
  private val WORLD_POPULATION_URL = "https://s3-eu-west-1.amazonaws.com/languages-and-translators/world_population.tsv"

  private val d3Selection: Selection[dom.EventTarget] = selection

  private var countryEventHandlers: Map[String, (Selection[dom.EventTarget], String) => Unit] = Map()
  private var countrColorSetter: String => String = x => SplotUtils.getRandomColor()
  private var enableZoom: Boolean = true
  private var loadSVG: Selection[EventTarget] => Unit = x => ()

  override def append(d: Drawing): Drawing = throw new NoSuchMethodError("worldmap.append")

  override def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing = throw new NoSuchMethodError("worldmap.setData")

  override def getSelection(): Selection[EventTarget] = d3Selection

  override def setValue[T](propName: String, func: () => T): Unit = throw new NoSuchMethodError("worldmap.setValue")

  def show(): Unit = {

    case class Margin(left: Int, right: Int, top: Int, bottom: Int)

    var margin = Margin(top = 0, right = 0, bottom = 0, left = 0)
    var width = _width - margin.left - margin.right
    var height = _height - margin.top - margin.bottom

    var svg = selection
      .append("g")
      .attr("class", "map")

    var projection = d3.geoMercator()
      .scale(130)
      .translate( js.Tuple2(width / 2, height / 1.5) )

    //// ZOOM
    var gdom = svg.append("g")
    if(enableZoom) {
      if(false) {
        var prebuild = svg.append("rect")
          .attr("class", "background")
          .style("opacity", "0")
          .attr("width", width)
          .attr("height", height)
      }

      def zoomed: () => Unit = () => {
        gdom.style("stroke-width", 1.5 / d3.event.transform.k + "px")
        // g.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")"); // not in d3 v4
        gdom.attr("transform", d3.event.transform); // updated for d3 v4
        ()
      }
      var zoom = d3.zoom().scaleExtent(js.Array(1, 8)).on("zoom", zoomed)
      svg.call(zoom)

      var reset:() => Unit = () => {
        // TODO: svg.transition
        // svg.transition().duration(750).call( zoom.transform, d3.zoomIdentity );
      }
      // TODO: if(false) prebuild.on("click", reset)
    }

    var path = d3.geoPath().projection(projection)

    type ResponseCallback = js.Function2[js.Any, js.Array[js.Dictionary[String]], Unit]
    type DataCallback = js.Function1[js.Object, Unit]
    type JSONCallback = js.Function2[String, DataCallback, Unit]
    type RequestCallback = js.Function2[String, ResponseCallback, Unit]
    type CallbackType = JSONCallback | RequestCallback

    var callJSON: JSONCallback = d3.json
    var callTSV: RequestCallback = d3.tsv

    d3.queue()
      .defer(callJSON, WORLD_COUNTRIES_URL)
      .defer(callTSV, WORLD_POPULATION_URL)
      .await((error: js.Any, dataObj: js.Object, populationObj: js.Object) => {
        var data = dataObj.asInstanceOf[CountryStruct]

        var getColor: (Feature, Int, UndefOr[Int]) => Primitive = (d: Feature, _: Int, _: UndefOr[Int]) => {
          /*if(!populationById.get(d.id).isDefined) {
            println("Could not find id " + d.id)
            null
          } else customColor(populationById.get(d.id).get)*/
          if(d.properties.get("name").isDefined)
            countrColorSetter(d.properties.get("name").get)
          else countrColorSetter(null)
        }

        var countries = gdom
          .attr("class", "countries")
          .selectAll("path")
          .data(data.features)
          .enter()
          .append("path").attr("d", path)
        // println(JSON.stringify(last_country.nodes()))

        var nodes = js.Dictionary[d3v4.Node]()
        countries.nodes().foreach(e => {
          val f = e.`__data__`.asInstanceOf[Feature]
          nodes.update(f.id, e)
        })

        countries.style("fill", getColor).style("stroke", "white")
          .style("stroke-width", "0.3")

        for ((k,v) <- countryEventHandlers) {
          countries.on(k, (d: Feature) => {
            v(d3.select(nodes.get(d.id).get), d.properties.get("name").get)
          })
        }

        var meshFunction: (Feature, Feature) => Primitive = (a: Feature, b: Feature) => a.id != b.id
        svg.append("path")
          .datum(topojson.mesh[Feature](data.features, meshFunction))
          // .datum(topojson.mesh(data.features, function(a, b) { return a !== b; }))
          .attr("class", "names")
          .attr("d", path)

        loadSVG(svg)
      })

  }

  override def setCountryHandler(eventName: String, handler: (Selection[EventTarget], String) => Unit): Drawing = {
    countryEventHandlers = countryEventHandlers + (eventName -> handler)
    this
  }

  override def setCountryColor(setter: String => String): Drawing = {
    countrColorSetter = setter
    this
  }

  override def setZoom(enabled: Boolean): Drawing = {
    enableZoom = enabled
    this
  }

  override def onLoadSVG(callback: Selection[EventTarget] => Unit): Drawing = {
    loadSVG = callback
    this
  }

  override def setData[A, B](m: Matrix[B], cols: Row[A]): Drawing = throw new NoSuchMethodError("worldmap.setData")
}

object splotmapfunc {

  def worldmap(): (=> Unit) => Unit = worldmap(400, 400)(_)
  def worldmap(width: Int, height: Int): (=> Unit) => Unit = worldmap(0, 0, width, height)(_)
  def worldmap(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    val worldObject = new SplotMap(Context.buildSVG(Context.getD3Selection(x, y, width, height), width, height), x, y, width, height)

    Context
      .append(worldObject) // Append to higher order drawing
      .push(worldObject)

    body

    worldObject.show()

    Context
      .pop()
  }

}