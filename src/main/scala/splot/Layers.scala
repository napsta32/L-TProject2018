package splot

object splotlayers extends Drawing {

  def layers(): (=> Unit) => Unit = layers(400, 400)(_)
  def layers(width: Int, height: Int): (=> Unit) => Unit = layers(0, 0, width, height)(_)
  def layers(x: Int, y: Int, width: Int, height: Int)(body: => Unit): Unit = {
    Context
      .append(this)
      .push(this)

    body

    Context
      .pop()
  }

  def append(d: Drawing): Drawing = {
    // TODO:
    this
  }

}
