package splot

class Chord extends Drawing {
  override def append(d: Drawing): Drawing = {
    this
  }
  override def setData[Node, Edge](graph: Graph[Node, Edge]): Drawing = {
    this
  }

  def show(): Chord = {

    this
  }
}

object splotchord {

  def chord(): (=> Unit) => Unit = chord(400, 400)(_)
  def chord(width: Int, height: Int): (=> Unit) => Unit = chord(0, 0, width, height)(_)
  def chord(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    var chordObject = new Chord()

    Context
      .append(chordObject) // Append to higher order drawing
      .push(chordObject)

    body

    chordObject.show()

    Context
      .pop()
  }

}
