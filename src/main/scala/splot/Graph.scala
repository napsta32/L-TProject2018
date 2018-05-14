package splot

import scalajs.js

abstract class Graph[Node, Edge] {
  import Graph._

  def isEmpty: Boolean

  def source: Node
  def dest: Node
  var value: Edge

  var tail: Graph[Node, Edge]

  def last(): Graph[Node, Edge] = {
    if(tail.isEmpty) this
    else tail.last()
  }

  def concat(g2: Graph[Node, Edge]): Graph[Node, Edge] = {
    last().tail = g2
    this
  }

  def nodes(): Set[Node] = {
    if(isEmpty) return Set[Node]()
    tail.nodes() + source + dest
  }

  // def toMatrix(): js.Array[js.Array[Node]] = ???

  def apply(source: Node, dest: Node)(implicit node: Monoid[Node], edge: Monoid[Edge]): Edge = {
    if(isEmpty) edge.unit
    else if(node.equals(source, this.source) && node.equals(dest, this.dest)) value
    else tail(source, dest)
  }

  def apply(direction: (Node, Node)): Edge = {
    this(direction._1, direction._2)
  }

  def filter(f: (Node, Node) => Boolean): Graph[Node, Edge] = {
    if(f(source, dest)) graph(value, source -> dest, tail.filter(f))
    else tail.filter(f)
  }

  def map[NewEdge](f: (Edge, Node, Node) => NewEdge): Graph[Node, NewEdge] = {
    if(isEmpty) emptyGraph()
    else graph(f(value, source, dest), (source, dest), tail.map(f))
  }

  def map[NewEdge](f: (Edge, (Node, Node)) => NewEdge): Graph[Node, NewEdge] = {
    if(isEmpty) emptyGraph()
    else graph(f(value, source, dest), (source, dest), tail.map(f))
  }

  def flatMap[NewEdge](f: (Edge, Node, Node) => Graph[Node, NewEdge]): Graph[Node, NewEdge] = {
    if(isEmpty) return emptyGraph()
    f(value, source -> dest).concat(tail.flatMap(f))
  }

  def flatMap[NewEdge](f: (Edge, (Node, Node)) => Graph[Node, NewEdge]): Graph[Node, NewEdge] = {
    if(isEmpty) return emptyGraph()
    f(value, source -> dest).concat(tail.flatMap(f))
  }

}

object Graph {

  def emptyGraph[Node, Edge](): Graph[Node, Edge] = new Graph[Node, Edge] {
    def isEmpty = true

    def source = throw new NoSuchElementException("empty.source")
    def dest = throw new NoSuchElementException("empty.dest")
    def value = throw new NoSuchElementException("empty.value")

    def tail = throw new NoSuchElementException("empty.tail")
  }

  def graph[Node, Edge](value: Edge, direction: (Node, Node), tl: => Graph[Node, Edge]): Graph[Node, Edge] = new Graph {
    def isEmpty = false

    def source: Node = direction._1
    def dest: Node = direction._2
    def value: Edge = value

    def tail: Graph[Node, Edge] = tl
  }

}