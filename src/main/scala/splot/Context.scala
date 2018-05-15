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

  def push(drawing: Drawing): this.type = {
    contextStack = drawing :: contextStack
    this
  }

  def pop(): this.type = {
    contextStack = contextStack.tail
    this
  }

  def getD3Selection(default: String = "#splot"): Selection[dom.EventTarget] = {
    if(contextStack.nonEmpty)
      return contextStack.head.getSelection()
    // println("Selecting default " + layerCount + ": " + contextStack)
    d3v4.d3.select(default)
  }

  def addLayer(): Unit = layerCount += 1
  def removeLayer(): Unit = layerCount -= 1
  def buildLayerContainer(selection: Selection[dom.EventTarget]): Selection[dom.EventTarget] =
    selection.append("div").attr("style", "position: absolute;z-index: " + layerCount)

  def buildSVG(selection: Selection[dom.EventTarget], width: Int, height: Int): Selection[dom.EventTarget] = {
    selection.append("svg").attr("width", width + "").attr("height", height + "")
  }
}
