package splot

object splotchord extends Drawing {

  def chord(): (=> Unit) => Unit = chord(400, 400)(_)
  def chord(width: Int, height: Int): (=> Unit) => Unit = chord(0, 0, width, height)(_)
  def chord(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    Context
      .append(this) // Append to higher order drawing
      .push(this)

    body

    Context
      .pop()
  }

  override def append(d: Drawing): Drawing = {

    this
  }
}
