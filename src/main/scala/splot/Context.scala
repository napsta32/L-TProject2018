package splot

object Context {
  private var contextStack: List[Drawing] = List[Drawing]()

  def append(drawing: Drawing): this.type = {
    if(contextStack.size > 0)
      contextStack(0).append(drawing)
    this
  }

  def setData[Node, Edge](graph: Graph[Node, Edge]): this.type = {
    if(contextStack.size > 0)
      contextStack(0).setData(graph)
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
}

