package splot

import scala.language.implicitConversions
import scalajs.js
import d3v4.{Chord, ChordArray, ChordGroup, Primitive, Selection, d3}
import org.scalajs.dom
import org.scalajs.dom.EventTarget

import scala.scalajs.js.UndefOr

class SplotChord(selection: Selection[dom.EventTarget]) extends Drawing {
  val d3Selection: Selection[dom.EventTarget] = selection
  var data: Graph[String, Double] = null

  override def append(d: Drawing): Drawing = {
    this
  }

  override def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing = {
    this.data = graph.asInstanceOf[Graph[String, Double]]
    this
  }

  def show(): SplotChord = {
    implicit def fun2datumfun[Datum](x: (Datum) => Primitive): js.Function3[Datum, Int, UndefOr[Int], Primitive] = (v: Datum, _: Int, _: UndefOr[Int]) => x(v)
    implicit def obj2primitive(x: js.Object): Primitive = x.toString()

    def groupTicks(d: ChordGroup, step: Double): js.Array[js.Dictionary[Double]] = {
      val k: Double = (d.endAngle - d.startAngle) / d.value
      d3.range(0, d.value, step).map((v: Double) => js.Dictionary("value" -> v, "angle" -> (v * k + d.startAngle)))
    }

    val svg = d3Selection
    val width = svg.attr("width").toDouble
    val height = svg.attr("height").toDouble
    val outerRadius = Math.min(width, height) * 0.5 - 40
    val innerRadius = outerRadius - 30

    val formatValue = d3.formatPrefix(",.0", 1e3)

    val chord = d3.chord().padAngle(0.05).sortSubgroups(d3.descending)

    val arc = d3.arc().innerRadius(innerRadius).outerRadius(outerRadius)

    val ribbon = d3.ribbon().radius(innerRadius)

    val color = d3.scaleOrdinal[Int, String]().domain(d3.range(4)).range(
      js.Array(List.fill(data.nodes().size)(SplotUtils.getRandomColor()): _*))

    val gdom: Selection[ChordArray] = svg.append("g").attr("transform", "translate(" + width / 2 + "," + height / 2 + ")").datum(chord(data.toDenseMatrix()))

    val group: Selection[ChordGroup] = gdom.append("g").attr("class", "groups")
      .selectAll("g")
      .data((c: ChordArray) => c.groups)
      .enter().append("g")

    group.append("path").style("fill", (d: ChordGroup) => color(d.index))
      .style("stroke", (d: ChordGroup) => d3.rgb(color(d.index)).darker())
      .attr("d", (x: ChordGroup) => arc(x))

    var groupTick = group.selectAll(".group-tick").data((d: ChordGroup) => groupTicks(d, 1e3))
      .enter().append("g").attr("class", "group-tick")
      .attr("transform", (d: js.Dictionary[Double]) =>  "rotate(" + (d("angle") * 180 / Math.PI - 90) + ") translate(" + outerRadius + ",0)")

    groupTick.append("line").attr("x2", 6)

    groupTick.filter((d: js.Dictionary[Double], _: Int, _: UndefOr[Int]) => d("value") % 5e3 == 0).append("text")
      .attr("x", 8)
      .attr("dy", ".35em")
      .attr("transform", (d: js.Dictionary[Double]) => if(d("angle") > Math.PI) "rotate(180) translate(-16)" else null)
      .style("text-anchor", (d: js.Dictionary[Double], _: Int, _: UndefOr[Int]) => if(d("angle") > Math.PI) "end" else null)
      .text((d: js.Dictionary[Double]) => formatValue(d("value")))

    gdom.append("g").attr("class", "ribbons").selectAll("path").data((c: ChordArray) => c)
      .enter().append("path")
      .attr("d", (d: Chord) => ribbon(d))
      .style("fill", (d: Chord) => color(d.target.index))
      .style("stroke", (d: Chord) => d3.rgb(color(d.target.index)).darker())

    this
  }

  override def getSelection[Datum](): Selection[EventTarget] = d3Selection
}

object publicSplotchord {

  def chord(): (=> Unit) => Unit = chord(400, 400)(_)
  def chord(width: Int, height: Int): (=> Unit) => Unit = chord(0, 0, width, height)(_)
  def chord(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    val chordObject = new SplotChord(Context.buildSVG(Context.getD3Selection(), width, height))

    Context
      .append(chordObject) // Append to higher order drawing
      .push(chordObject)

    body

    chordObject.show()

    Context
      .pop()
  }

}
