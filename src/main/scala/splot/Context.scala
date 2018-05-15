package splot

import d3v4.Selection
import org.scalajs.dom

object Context {
  private var contextStack: List[Drawing] = List[Drawing]()
  private var layerCount: Int = 0

  def append(drawing: Drawing): this.type = {
    if(contextStack.nonEmpty)
      contextStack.head.append(drawing)
    this
  }

  def setData[Node, Edge](graph: Graph[Node, Edge]): this.type = {
    if(contextStack.nonEmpty)
      contextStack.head.setData(graph)
    else throw new Exception("Out of context call (setData)")
    this
  }

  def setValue[T](propName: String, func: () => T): this.type = {
    if(contextStack.nonEmpty)
      contextStack.head.setValue(propName, func)
    else throw new Exception("Out of context call (setValue)")
    this
  }

  def setZoom(enabled: Boolean): this.type = {
    if(contextStack.nonEmpty)
      contextStack.head.setZoom(enabled)
    else throw new Exception("Out of context call (setZoom)")
    this
  }

  def setCountryHandler(eventName: String, handler: (Selection[dom.EventTarget], String) => Unit): this.type = {
    if(contextStack.nonEmpty)
      contextStack.head.setCountryHandler(eventName, handler)
    else throw new Exception("Out of context call (setCountryHandler)")
    this
  }

  def setCountryColor(setter: String => String):this.type = {
    if(contextStack.nonEmpty)
      contextStack.head.setCountryColor(setter)
    else throw new Exception("Out of context call (setCountryHandler)")
    this
  }

  def push(drawing: Drawing): this.type = {
    contextStack = drawing :: contextStack
    this
  }

  def pop(): this.type = {
    contextStack = contextStack.tail
    this
  }

  def getD3Selection(x: Int, y: Int, width: Int, height: Int, default: String = "#splot"): Selection[dom.EventTarget] = {
    if(contextStack.nonEmpty)
      return contextStack.head.getSelection()
    // println("Selecting default " + layerCount + ": " + contextStack)
    d3v4.d3.select(default).append("div")
      .attr("style", s"width: ${width}px;height: ${height}px;clear: both;overflow:hidden;")
      /*
      .attr("style", s"width: ${width}px;height: ${height}px;clear: both;overflow:hidden;" +
              s"transform: translate(${x}px, ${y}px);-ms-transform: translate(${x}px, ${y}px);" +
              s"-webkit-transform: translate(${x}px, ${y}px);")
              */
  }

  def addLayer(): Unit = layerCount += 1
  def removeLayer(): Unit = layerCount -= 1
  def buildLayerContainer(selection: Selection[dom.EventTarget], x: Int, y: Int, width: Int, height: Int): Selection[dom.EventTarget] = {
    if(layerCount <= 0)
      selection.append("div").attr("style",";transform: translate(" + x + "px, " + y +
        "px);-ms-transform: translate(" + x + "px, " + y + "px);-webkit-transform: translate(" + x +
        "px," + y + "px);width: " + width + "px;height: " + height + "px;clear: both;overflow:hidden;")
    else
      selection.append("div").attr("style", "position: absolute;z-index: " + layerCount +
        ";transform: translate(" + x + "px, " + y + "px);-ms-transform: translate(" + x + "px, " + y + "px);" +
        "-webkit-transform: translate(" + x + "px," + y + "px);width: " + width + "px;height: " + height + "px;clear: both;overflow:hidden;")
  }

  def buildSVG(selection: Selection[dom.EventTarget], width: Int, height: Int): Selection[dom.EventTarget] = {
    selection.append("svg").attr("width", width + "").attr("height", height + "")
  }
}
