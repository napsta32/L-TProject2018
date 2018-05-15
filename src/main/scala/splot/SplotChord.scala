package splot

import scala.language.implicitConversions
import scalajs.js
import d3v4.{Chord, ChordArray, ChordGroup, Primitive, Selection, d3}
import org.scalajs.dom
import org.scalajs.dom.EventTarget

import scala.scalajs.js.UndefOr

class SplotChord(selection: Selection[dom.EventTarget], x: Int, y: Int, width: Int, height: Int) extends Drawing {
  val d3Selection: Selection[dom.EventTarget] = selection
  var data: Graph[String, Double] = null
  var matrix: Matrix[Double] = null
  var cols: Seq[String] = null

  private var outerRadiusFunc: () => Double = () => Math.min(width, height) * 0.5 - 40
  private var innerRadiusFunc: () => Double = () => outerRadiusFunc() - 30
  private var zoomEnabled = true
  private var loadSVG: Selection[EventTarget] => Unit = x => ()

  override def append(d: Drawing): Drawing = {
    this
  }

  override def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing = {
    this.data = graph.asInstanceOf[Graph[String, Double]]
    this
  }

  override def setValue[T](propName: String, func: () => T): Unit = {
    if(func.isInstanceOf[() => Double]) {
      propName match {
        case g.CHORD_OUTER_RADIUS => outerRadiusFunc = func.asInstanceOf[() => Double]
        case g.CHORD_INNER_RADIUS => innerRadiusFunc = func.asInstanceOf[() => Double]
        case _ => throw new NoSuchFieldError("chord." + propName)
      }
    } else {
      throw new Exception("Invalid type in chord.setValue(" + propName + ")")
    }
  }

  def show(): SplotChord = {
    if(data == null && this.matrix == null) return this

    implicit def fun2datumfun[Datum](x: (Datum) => Primitive): js.Function3[Datum, Int, UndefOr[Int], Primitive] = (v: Datum, _: Int, _: UndefOr[Int]) => x(v)
    implicit def obj2primitive(x: js.Object): Primitive = x.toString()

    def groupTicks(d: ChordGroup, step: Double): js.Array[js.Dictionary[Double]] = {
      val k: Double = (d.endAngle - d.startAngle) / d.value
      d3.range(0, d.value, step).map((v: Double) => js.Dictionary("value" -> v, "angle" -> (v * k + d.startAngle)))
    }

    val svg = d3Selection
    val outerRadius = outerRadiusFunc()
    val innerRadius = innerRadiusFunc()

    val formatValue = d3.formatPrefix(",.0", 1e2)
    val chord = d3.chord().padAngle(0.05).sortSubgroups(d3.descending)
    val arc = d3.arc().innerRadius(innerRadius).outerRadius(outerRadius)
    val ribbon = d3.ribbon().radius(innerRadius)

    var ncols: Int = 0
    if(data == null)
      ncols = cols.size
    else ncols = data.nodes().size

    val color = d3.scaleOrdinal[Int, String]().domain(d3.range(4)).range(
      js.Array(List.fill(ncols)(SplotUtils.getRandomColor()): _*))

    var matrix: js.Array[js.Array[Double]] = null
    if(data == null)
      matrix = this.matrix.toJSMatrix()
    else matrix = data.toDenseMatrix()

    val gdom: Selection[ChordArray] = svg.append("g")
      .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")")
      .datum(chord(matrix))

    if(zoomEnabled) {
      def zoomed: () => Unit = () => {
        gdom.style("stroke-width", 1.5 / d3.event.transform.k + "px")
        // g.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")"); // not in d3 v4
        gdom.attr("transform", d3.event.transform); // updated for d3 v4
        ()
      }
      var zoom = d3.zoom().scaleExtent(js.Array(1, 8)).on("zoom", zoomed)
      svg.call(zoom)
    }

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

    loadSVG(svg)

    this
  }

  override def getSelection(): Selection[EventTarget] = d3Selection

  override def setCountryHandler(eventName: String, handler: (Selection[EventTarget], String) => Unit): Drawing =
    throw new NoSuchMethodError("chord.setCountryHandler")

  override def setCountryColor(setter: String => String): Drawing = throw new NoSuchMethodError("chord.setCountryColor")

  override def setZoom(enabled: Boolean): Drawing = {
    zoomEnabled = enabled
    this
  }

  override def onLoadSVG(callback: Selection[EventTarget] => Unit): Drawing = {
    loadSVG = callback
    this
  }

  override def setData[Node, Edge](m: Matrix[Edge], cols: Seq[Node]): Drawing = {
    this.matrix = m.asInstanceOf[Matrix[Double]]
    this.cols = cols.asInstanceOf[Seq[String]]
    this
  }
}

object publicSplotchord {

  def chord(): (=> Unit) => Unit = chord(400, 400)(_)
  def chord(width: Int, height: Int): (=> Unit) => Unit = chord(0, 0, width, height)(_)
  def chord(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    val chordObject = new SplotChord(Context.buildSVG(Context.getD3Selection(x, y, width, height), width, height), x, y, width, height)

    Context
      .append(chordObject) // Append to higher order drawing
      .push(chordObject)

    body

    chordObject.show()

    Context
      .pop()
  }

}
