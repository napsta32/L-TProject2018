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
    case class Margin(left: Int, right: Int, top: Int, bottom: Int)

    var format = d3.format(",")

    // Set tooltips
    var tip = d3.tip()
      .attr("class", "d3-tip")
      .offset(js.Tuple2(-10, 0))
      .html(htmlGen)

    val margin = Margin(top = 0, right = 0, bottom = 0, left = 0)
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
      val population = js.Array(js.Dictionary[String]("id" -> "CHN", "name" -> "China", "population" -> "1330141295"),
        js.Dictionary[String]("id" -> "IND", "name" -> "India", "population" -> "1173108018"),
        js.Dictionary[String]("id" -> "USA", "name" -> "United States", "population" -> "310232863"),
        js.Dictionary[String]("id" -> "IDN", "name" -> "Indonesia", "population" -> "242968342"),
        js.Dictionary[String]("id" -> "BRA", "name" -> "Brazil", "population" -> "201103330"),
        js.Dictionary[String]("id" -> "PAK", "name" -> "Pakistan", "population" -> "177276594"),
        js.Dictionary[String]("id" -> "BGD", "name" -> "Bangladesh", "population" -> "158065841"),
        js.Dictionary[String]("id" -> "NGA", "name" -> "Nigeria", "population" -> "152217341"),
        js.Dictionary[String]("id" -> "RUS", "name" -> "Russia", "population" -> "139390205"),
        js.Dictionary[String]("id" -> "JPN", "name" -> "Japan", "population" -> "126804433"),
        js.Dictionary[String]("id" -> "MEX", "name" -> "Mexico", "population" -> "112468855"),
        js.Dictionary[String]("id" -> "PHL", "name" -> "Philippines", "population" -> "99900177"),
        js.Dictionary[String]("id" -> "VNM", "name" -> "Vietnam", "population" -> "89571130"),
        js.Dictionary[String]("id" -> "ETH", "name" -> "Ethiopia", "population" -> "88013491"),
        js.Dictionary[String]("id" -> "DEU", "name" -> "Germany", "population" -> "82282988"),
        js.Dictionary[String]("id" -> "EGY", "name" -> "Egypt", "population" -> "80471869"),
        js.Dictionary[String]("id" -> "TUR", "name" -> "Turkey", "population" -> "77804122"),
        js.Dictionary[String]("id" -> "COD", "name" -> "Congo, Democratic Republic of the", "population" -> "70916439"),
        js.Dictionary[String]("id" -> "IRN", "name" -> "Iran", "population" -> "67037517"),
        js.Dictionary[String]("id" -> "THA", "name" -> "Thailand", "population" -> "66404688"),
        js.Dictionary[String]("id" -> "FRA", "name" -> "France", "population" -> "64057792"),
        js.Dictionary[String]("id" -> "GBR", "name" -> "United Kingdom", "population" -> "61284806"),
        js.Dictionary[String]("id" -> "ITA", "name" -> "Italy", "population" -> "58090681"),
        js.Dictionary[String]("id" -> "MMR", "name" -> "Burma", "population" -> "53414374"),
        js.Dictionary[String]("id" -> "ZAF", "name" -> "South Africa", "population" -> "49109107"),
        js.Dictionary[String]("id" -> "KOR", "name" -> "Korea, South", "population" -> "48636068"),
        js.Dictionary[String]("id" -> "UKR", "name" -> "Ukraine", "population" -> "45415596"),
        js.Dictionary[String]("id" -> "COL", "name" -> "Colombia", "population" -> "44205293"),
        js.Dictionary[String]("id" -> "SDN", "name" -> "Sudan", "population" -> "41980182"),
        js.Dictionary[String]("id" -> "TZA", "name" -> "Tanzania", "population" -> "41892895"),
        js.Dictionary[String]("id" -> "ARG", "name" -> "Argentina", "population" -> "41343201"),
        js.Dictionary[String]("id" -> "ESP", "name" -> "Spain", "population" -> "40548753"),
        js.Dictionary[String]("id" -> "KEN", "name" -> "Kenya", "population" -> "40046566"),
        js.Dictionary[String]("id" -> "POL", "name" -> "Poland", "population" -> "38463689"),
        js.Dictionary[String]("id" -> "DZA", "name" -> "Algeria", "population" -> "34586184"),
        js.Dictionary[String]("id" -> "CAN", "name" -> "Canada", "population" -> "33759742"),
        js.Dictionary[String]("id" -> "UGA", "name" -> "Uganda", "population" -> "33398682"),
        js.Dictionary[String]("id" -> "MAR", "name" -> "Morocco", "population" -> "31627428"),
        js.Dictionary[String]("id" -> "PER", "name" -> "Peru", "population" -> "29907003"),
        js.Dictionary[String]("id" -> "IRQ", "name" -> "Iraq", "population" -> "29671605"),
        js.Dictionary[String]("id" -> "SAU", "name" -> "Saudi Arabia", "population" -> "29207277"),
        js.Dictionary[String]("id" -> "AFG", "name" -> "Afghanistan", "population" -> "29121286"),
        js.Dictionary[String]("id" -> "NPL", "name" -> "Nepal", "population" -> "28951852"),
        js.Dictionary[String]("id" -> "UZB", "name" -> "Uzbekistan", "population" -> "27865738"),
        js.Dictionary[String]("id" -> "VEN", "name" -> "Venezuela", "population" -> "27223228"),
        js.Dictionary[String]("id" -> "MYS", "name" -> "Malaysia", "population" -> "26160256"),
        js.Dictionary[String]("id" -> "GHA", "name" -> "Ghana", "population" -> "24339838"),
        js.Dictionary[String]("id" -> "YEM", "name" -> "Yemen", "population" -> "23495361"),
        js.Dictionary[String]("id" -> "TWN", "name" -> "Taiwan", "population" -> "23024956"),
        js.Dictionary[String]("id" -> "PRK", "name" -> "Korea, North", "population" -> "22757275"),
        js.Dictionary[String]("id" -> "SYR", "name" -> "Syria", "population" -> "22198110"),
        js.Dictionary[String]("id" -> "ROU", "name" -> "Romania", "population" -> "22181287"),
        js.Dictionary[String]("id" -> "MOZ", "name" -> "Mozambique", "population" -> "22061451"),
        js.Dictionary[String]("id" -> "AUS", "name" -> "Australia", "population" -> "21515754"),
        js.Dictionary[String]("id" -> "LKA", "name" -> "Sri Lanka", "population" -> "21513990"),
        js.Dictionary[String]("id" -> "MDG", "name" -> "Madagascar", "population" -> "21281844"),
        js.Dictionary[String]("id" -> "CIV", "name" -> "Cote d'Ivoire", "population" -> "21058798"),
        js.Dictionary[String]("id" -> "CMR", "name" -> "Cameroon", "population" -> "19294149"),
        js.Dictionary[String]("id" -> "NLD", "name" -> "Netherlands", "population" -> "16783092"),
        js.Dictionary[String]("id" -> "CHL", "name" -> "Chile", "population" -> "16746491"),
        js.Dictionary[String]("id" -> "BFA", "name" -> "Burkina Faso", "population" -> "16241811"),
        js.Dictionary[String]("id" -> "NER", "name" -> "Niger", "population" -> "15878271"),
        js.Dictionary[String]("id" -> "KAZ", "name" -> "Kazakhstan", "population" -> "15460484"),
        js.Dictionary[String]("id" -> "MWI", "name" -> "Malawi", "population" -> "15447500"),
        js.Dictionary[String]("id" -> "ECU", "name" -> "Ecuador", "population" -> "14790608"),
        js.Dictionary[String]("id" -> "KHM", "name" -> "Cambodia", "population" -> "14753320"),
        js.Dictionary[String]("id" -> "SEN", "name" -> "Senegal", "population" -> "14086103"),
        js.Dictionary[String]("id" -> "MLI", "name" -> "Mali", "population" -> "13796354"),
        js.Dictionary[String]("id" -> "GTM", "name" -> "Guatemala", "population" -> "13550440"),
        js.Dictionary[String]("id" -> "AGO", "name" -> "Angola", "population" -> "13068161"),
        js.Dictionary[String]("id" -> "ZMB", "name" -> "Zambia", "population" -> "12056923"),
        js.Dictionary[String]("id" -> "ZWE", "name" -> "Zimbabwe", "population" -> "11651858"),
        js.Dictionary[String]("id" -> "CUB", "name" -> "Cuba", "population" -> "11477459"),
        js.Dictionary[String]("id" -> "RWA", "name" -> "Rwanda", "population" -> "11055976"),
        js.Dictionary[String]("id" -> "GRC", "name" -> "Greece", "population" -> "10749943"),
        js.Dictionary[String]("id" -> "PRT", "name" -> "Portugal", "population" -> "10735765"),
        js.Dictionary[String]("id" -> "TUN", "name" -> "Tunisia", "population" -> "10589025"),
        js.Dictionary[String]("id" -> "TCD", "name" -> "Chad", "population" -> "10543464"),
        js.Dictionary[String]("id" -> "BEL", "name" -> "Belgium", "population" -> "10423493"),
        js.Dictionary[String]("id" -> "GIN", "name" -> "Guinea", "population" -> "10324025"),
        js.Dictionary[String]("id" -> "CZE", "name" -> "Czech Republic", "population" -> "10201707"),
        js.Dictionary[String]("id" -> "SOM", "name" -> "Somalia", "population" -> "10112453"),
        js.Dictionary[String]("id" -> "BOL", "name" -> "Bolivia", "population" -> "9947418"),
        js.Dictionary[String]("id" -> "HUN", "name" -> "Hungary", "population" -> "9880059"),
        js.Dictionary[String]("id" -> "BDI", "name" -> "Burundi", "population" -> "9863117"),
        js.Dictionary[String]("id" -> "DOM", "name" -> "Dominican Republic", "population" -> "9794487"),
        js.Dictionary[String]("id" -> "BLR", "name" -> "Belarus", "population" -> "9612632"),
        js.Dictionary[String]("id" -> "HTI", "name" -> "Haiti", "population" -> "9203083"),
        js.Dictionary[String]("id" -> "SWE", "name" -> "Sweden", "population" -> "9074055"),
        js.Dictionary[String]("id" -> "BEN", "name" -> "Benin", "population" -> "9056010"),
        js.Dictionary[String]("id" -> "AZE", "name" -> "Azerbaijan", "population" -> "8303512"),
        js.Dictionary[String]("id" -> "AUT", "name" -> "Austria", "population" -> "8214160"),
        js.Dictionary[String]("id" -> "HND", "name" -> "Honduras", "population" -> "7989415"),
        js.Dictionary[String]("id" -> "CHE", "name" -> "Switzerland", "population" -> "7623438"),
        js.Dictionary[String]("id" -> "TJK", "name" -> "Tajikistan", "population" -> "7487489"),
        js.Dictionary[String]("id" -> "ISR", "name" -> "Israel", "population" -> "7353985"),
        js.Dictionary[String]("id" -> "SRB", "name" -> "Serbia", "population" -> "7344847"),
        js.Dictionary[String]("id" -> "BGR", "name" -> "Bulgaria", "population" -> "7148785"),
        js.Dictionary[String]("id" -> "HKG", "name" -> "Hong Kong", "population" -> "7089705"),
        js.Dictionary[String]("id" -> "LAO", "name" -> "Laos", "population" -> "6993767"),
        js.Dictionary[String]("id" -> "LBY", "name" -> "Libya", "population" -> "6461454"),
        js.Dictionary[String]("id" -> "JOR", "name" -> "Jordan", "population" -> "6407085"),
        js.Dictionary[String]("id" -> "PRY", "name" -> "Paraguay", "population" -> "6375830"),
        js.Dictionary[String]("id" -> "TGO", "name" -> "Togo", "population" -> "6199841"),
        js.Dictionary[String]("id" -> "PNG", "name" -> "Papua New Guinea", "population" -> "6064515"),
        js.Dictionary[String]("id" -> "SLV", "name" -> "El Salvador", "population" -> "6052064"),
        js.Dictionary[String]("id" -> "NIC", "name" -> "Nicaragua", "population" -> "5995928"),
        js.Dictionary[String]("id" -> "ERI", "name" -> "Eritrea", "population" -> "5792984"),
        js.Dictionary[String]("id" -> "DNK", "name" -> "Denmark", "population" -> "5515575"))
      var populationById = js.Dictionary[String]

      population.foreach((d: js.Dictionary[String]) => {
        populationById.asInstanceOf[js.Dictionary[String]].update(d.get("id").get, d.get("population").get)
      })
      data.features.foreach(function(d) { d.population = populationById[d.id] });

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

