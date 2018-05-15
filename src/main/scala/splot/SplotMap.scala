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

  override def append(d: Drawing): Drawing = throw new NoSuchMethodError("worldmap.append")

  override def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing = throw new NoSuchMethodError("worldmap.setData")

  override def getSelection(): Selection[EventTarget] = d3Selection

  override def setValue[T](propName: String, func: () => T): Unit = throw new NoSuchMethodError("worldmap.setValue")

  def show(): Unit = {

    case class Margin(left: Int, right: Int, top: Int, bottom: Int)

    var format = d3.format(",")

    var htmlGen = (dobj: js.Object) => {
      var d = dobj.asInstanceOf[Feature]
      "<strong>Country: </strong><span class='details'>" +
        d.properties.get("name") + "<br></span>" +
        "<strong>Population: </strong><span class='details'>" +
        format(d.population) +"</span>"
    }

    // Set tooltips
    var tip = d3.tip()
      .attr("class", "d3-tip")
      .offset(js.Tuple2(-10, 0))
      .html(htmlGen)

    var margin = Margin(top = 0, right = 0, bottom = 0, left = 0)
    var width = _width - margin.left - margin.right
    var height = _height - margin.top - margin.bottom

    var customColor: ThresholdScale[Primitive, Primitive] = d3.scaleThreshold()
      .domain(js.Array[Primitive](10000,100000,500000,1000000,5000000,10000000,50000000,100000000,500000000,1500000000))
      .range(js.Array("rgb(247,251,255)", "rgb(222,235,247)", "rgb(198,219,239)", "rgb(158,202,225)", "rgb(107,174,214)",
        "rgb(66,146,198)","rgb(33,113,181)","rgb(8,81,156)","rgb(8,48,107)","rgb(3,19,43)"))

    // var path = d3.geoPath()

    var svg = selection
      .append("g")
      .attr("class", "map")

    var projection = d3.geoMercator()
      .scale(130)
      .translate( js.Tuple2(width / 2, height / 1.5) )

    var path = d3.geoPath().projection(projection)

    svg.call(tip)

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
        var population = populationObj.asInstanceOf[js.Array[js.Dictionary[String]]]

        var populationById = js.Dictionary[String]()

        population.foreach((d: js.Dictionary[String]) => populationById.update(d.get("id").get, d.get("population").get) )
        data.features.map(d => populationById.get("id"))

        // population.foreach((d: js.Dictionary[String]) => println(JSON.stringify(d)))
        // println(JSON.stringify(data))

        var getColor: (Feature, Int, UndefOr[Int]) => Primitive = (d: Feature, _: Int, _: UndefOr[Int]) => {
          if(!populationById.get(d.id).isDefined) {
            println("Could not find id " + d.id)
            null
          } else customColor(populationById.get(d.id).get)
        }

        var countries = svg.append("g")
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
        //.style("stroke", "green")
        // .style("stroke-width", "1.5")
        // .style("opacity","1")
        countries.style("fill", getColor).style("stroke", "white")
          .style("stroke-width", "0.3")
        // tooltips
        countries.on("mouseover",(d: Feature) => {
          tip.show(d)
          d3.select(nodes.get(d.id).get)
            .style("opacity", "1")
            .style("stroke","white")
            .style("stroke-width","3")
          ()
        })
          .on("mouseout", (d: Feature) => {
            tip.hide(nodes.get(d.id).get.`__data__`)
            d3.select(nodes.get(d.id).get)
              .style("opacity", "0.8")
              //.style("stroke","white")
              .style("stroke-width","0.3")
            ()
          })

        // Unselect
        d3.select("body")
          .style("opacity", "0.8")
          // .style("stroke","white")
          .style("stroke-width","0.3")

        var meshFunction: (Feature, Feature) => Primitive = (a: Feature, b: Feature) => a.id != b.id
        svg.append("path")
          .datum(topojson.mesh[Feature](data.features, meshFunction))
          // .datum(topojson.mesh(data.features, function(a, b) { return a !== b; }))
          .attr("class", "names")
          .attr("d", path)
      })

  }
}

object splotmapfunc {

  def worldmap(): (=> Unit) => Unit = worldmap(400, 400)(_)
  def worldmap(width: Int, height: Int): (=> Unit) => Unit = worldmap(0, 0, width, height)(_)
  def worldmap(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    val worldObject = new SplotMap(Context.buildSVG(Context.getD3Selection(), width, height), x, y, width, height)

    Context
      .append(worldObject) // Append to higher order drawing
      .push(worldObject)

    body

    worldObject.show()

    Context
      .pop()
  }

}