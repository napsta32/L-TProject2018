package splot

class Layers extends Drawing {

  override def append(d: Drawing): Drawing = {
    // TODO:
    this
  }

  override def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing = throw new NoSuchElementException("layers.setData")

}

object splotlayers {

  def layers(): (=> Unit) => Unit = layers(400, 400)(_)
  def layers(width: Int, height: Int): (=> Unit) => Unit = layers(0, 0, width, height)(_)
  def layers(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    var layersObject = new Layers()

    Context
      .append(layersObject)
      .push(layersObject)

    body

    Context
      .pop()
  }

}
