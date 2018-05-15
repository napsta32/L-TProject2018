package splot

abstract class Drawing {

  def append(d: Drawing): Drawing
  def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing

}

object splotdrawing {

  def setData[Node, Edge](graph: Graph[Node, Edge]): this.type = {
    Context.setData(graph)
    this
  }

}
