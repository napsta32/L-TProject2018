package splot

import java.util

object Context {
  private var contextStack: util.Stack[Drawing] = null

  def append(drawing: Drawing): this.type = {
    if(contextStack.size() > 0)
      contextStack.peek().append(drawing)
    this
  }

  def push(drawing: Drawing): this.type = {
    contextStack.push(drawing)
    this
  }

  def get(): Drawing = contextStack.peek()

  def pop(): this.type = {
    contextStack.pop()
    this
  }
}

