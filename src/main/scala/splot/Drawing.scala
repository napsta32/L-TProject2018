package splot

import d3v4.Selection
import org.scalajs.dom

abstract class Drawing {

  def append(d: Drawing): Drawing
  def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing
  def getSelection[Datum](): Selection[dom.EventTarget]

}

object splotdrawing {

  def setData[Node, Edge](graph: Graph[Node, Edge]): this.type = {
    Context.setData(graph)
    this
  }

}
