package example

import d3v4._
import org.scalajs.dom.EventTarget

import scala.language.implicitConversions
import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.{Tuple2}

object ScalaJSExample {

  @JSExportTopLevel("myproject")
  protected def getInstance(): this.type = this

  def groupTicks(d: ChordGroup, step: Double): js.Array[js.Dictionary[Double]] = {
    val k: Double = (d.endAngle - d.startAngle) / d.value
    d3.range(0, d.value, step).map((v: Double) => js.Dictionary("value" -> v, "angle" -> (v * k + d.startAngle)))
  }

  def htmlGen(d)= {
    return "<strong>Country: </strong><span class='details'>" + d.properties.name + "<br></span>" + "<strong>Population: </strong><span class='details'>" + format(d.population) +"</span>"
  }

  @JSExport
  def main(args: Array[String]): Unit = {
    var format = d3.format(",")

    // Set tooltips
    var tip = d3.tip()
      .attr("class", "d3-tip")
    .offset(js.Tuple2(-10, 0))
    .html(htmlGen)

    var margin = {top: 0, right: 0, bottom: 0, left: 0}
    var width = 960 - margin.left - margin.right
    var height = 500 - margin.top - margin.bottom;

    var color = d3.scaleThreshold()
      .domain(js.Array(10000,100000,500000,1000000,5000000,10000000,50000000,100000000,500000000,1500000000))
    .range(js.Array("rgb(247,251,255)", "rgb(222,235,247)", "rgb(198,219,239)", "rgb(158,202,225)", "rgb(107,174,214)",
                      "rgb(66,146,198)","rgb(33,113,181)","rgb(8,81,156)","rgb(8,48,107)","rgb(3,19,43)"))

    var path = d3.geoPath()

    var svg = d3.select("body")
      .append("svg")
      .attr("width", width)
      .attr("height", height)
      .append("g")
      .attr("class", "map")

    var projection = d3.geoMercator()
      .scale(130)
      .translate( js.Tuple2(width / 2, height / 1.5) )

    var path = d3.geoPath().projection(projection)

    svg.call(tip)

    d3.queue()
      .defer(d3.json, "world_countries.json")
      .defer(d3.tsv, "world_population.tsv")
      .await(ready);

    function ready(error, data, population) {
      var populationById = {};

      population.forEach(function(d) { populationById[d.id] = +d.population; });
      data.features.forEach(function(d) { d.population = populationById[d.id] });

      svg.append("g")
        .attr("class", "countries")
        .selectAll("path")
        .data(data.features)
        .enter().append("path")
        .attr("d", path)
        .style("fill", function(d) { return color(populationById[d.id]); })
        .style('stroke', 'white')
      .style('stroke-width', 1.5)
      .style("opacity",0.8)
        // tooltips
        .style("stroke","white")
        .style('stroke-width', 0.3)
      .on('mouseover',function(d){
        tip.show(d);

        d3.select(this)
          .style("opacity", 1)
          .style("stroke","white")
          .style("stroke-width",3);
      })
      .on('mouseout', function(d){
        tip.hide(d);

        d3.select(this)
          .style("opacity", 0.8)
          .style("stroke","white")
          .style("stroke-width",0.3);
      });

      svg.append("path")
        .datum(topojson.mesh(data.features, function(a, b) { return a.id !== b.id; }))
        // .datum(topojson.mesh(data.features, function(a, b) { return a !== b; }))
        .attr("class", "names")
        .attr("d", path);
    }
}

