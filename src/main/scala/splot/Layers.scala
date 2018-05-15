package splot

import d3v4.Selection
import org.scalajs.dom
import org.scalajs.dom.EventTarget

class Layers(selection: Selection[dom.EventTarget], x: Int, y: Int, width: Int, height: Int) extends Drawing {

  private var myLayerCount: Int = 0
  private var d3Selection: Selection[dom.EventTarget] = selection

  override def append(d: Drawing): Drawing = {
    // Deprecated
    this
  }

  override def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing = throw new NoSuchElementException("layers.setData")

  override def getSelection(): Selection[EventTarget] = {
    Context.addLayer()
    myLayerCount += 1
    Context.buildLayerContainer(d3Selection, x, y, width, height)
  }

  def destroyLayers(): Unit = {
    for (i <- 1 to myLayerCount)
      Context.removeLayer()
  }

  override def setValue[T](propName: String, func: () => T): Unit = throw new NoSuchMethodError("layers.setDouble")
}

object splotlayers {

  def layers(): (=> Unit) => Unit = layers(400, 400)(_)
  def layers(width: Int, height: Int): (=> Unit) => Unit = layers(0, 0, width, height)(_)
  def layers(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    val layersObject = new Layers(Context.getD3Selection(), x, y, width, height)

    Context
      .append(layersObject)
      .push(layersObject)

    body

    Context
      .pop()
  }

}
