package splot

import scala.language.implicitConversions
import scala.scalajs.js.|
import scalajs.js

abstract class Graph[Node, Edge] {
  import Graph._

  implicit def node: Monoid[Node]
  implicit def edge: Monoid[Edge]

  def isEmpty: Boolean

  def source: Node
  def dest: Node
  def value: Edge

  def tail: Graph[Node, Edge]

  // O(n)
  // Really necessary?
  def last(): Graph[Node, Edge] = {
    if(tail.isEmpty) this
    else tail.last()
  }

  // O(n log n)
  def nodes(visited: Set[Node]): List[Node] = {
    if(isEmpty) return List[Node]()
    val visitSource = !visited.contains(source)
    val visitDest = source!=dest && !visited.contains(dest)
    if(visitSource && visitDest) return source :: dest :: tail.nodes(visited + source + dest)
    else if(visitSource) return source :: tail.nodes(visited + source)
    else if(visitDest) return dest :: tail.nodes(visited + dest)
    else return tail.nodes(visited)
  }

  // O(n log n)
  def nodes(): List[Node] = nodes(Set[Node]())

  // O(n^2)
  def toDenseMatrix(): js.Array[js.Array[Edge]] = {
    var map: Map[Node, Int] = Map[Node, Int]()
    var counter: Int = 0
    nodes().foreach((n: Node) => {
      map = map + (n -> counter)
      counter += 1
    })

    // var m = Array.ofDim[Node](map.size, map.size)
    var m = js.Array(List.fill(map.size)(js.Array[Edge](List.fill(map.size)(edge.unit): _*)): _*)
    foreach((v: Edge, s: Node, d: Node) => {
      m(map.get(s).get)(map.get(d).get) = v
      ()
    })
    m
  }

  // O(n)
  def apply(source: Node, dest: Node)(implicit fillEmpties: Boolean = true): Edge = {
    if(fillEmpties && isEmpty) edge.unit
    else if(node.equals(source, this.source) && node.equals(dest, this.dest)) value
    else tail(source, dest)
  }

  // O(n)
  def apply(direction: (Node, Node)): Edge = {
    this.apply(direction._1, direction._2)
  }

  // O(n) when f(_) = false
  def filter(f: (Node, Node) => Boolean): Graph[Node, Edge] = {
    if(f(source, dest)) graph(value, source -> dest, tail.filter(f))
    else tail.filter(f)
  }

  def foreach(f: (Edge, Node, Node) => Unit): Unit = {
    if(isEmpty) ()
    else {
      f(value, source, dest)
      tail.foreach(f)
    }
  }

  // O(1)
  def map[NewEdge <: Edge](f: (Edge, Node, Node) => NewEdge)
                  (implicit node: Monoid[Node], edge: Monoid[NewEdge]): Graph[Node, NewEdge] = {
    if(isEmpty) emptyGraph()
    else graph(f(value, source, dest), (source, dest), tail.map[NewEdge](f))
  }

  // O(1)
  def map[NewEdge](f: (Edge, (Node, Node)) => NewEdge)
                  (implicit node: Monoid[Node], edge: Monoid[NewEdge]): Graph[Node, NewEdge] = {
    if(isEmpty) emptyGraph()
    else graph(f(value, source -> dest), (source, dest), tail.map(f))
  }

  // O(1)
  def flatMap[NewEdge](f: (Edge, Node, Node) => Graph[Node, NewEdge])
                      (implicit node: Monoid[Node], edge: Monoid[NewEdge]): Graph[Node, NewEdge] = {
    if(isEmpty) return emptyGraph()
    concat(f(value, source, dest), tail.flatMap(f))
  }

  // O(1)
  def flatMap[NewEdge](f: (Edge, (Node, Node)) => Graph[Node, NewEdge])
                      (implicit node: Monoid[Node], edge: Monoid[NewEdge]): Graph[Node, NewEdge] = {
    if(isEmpty) return emptyGraph()
    concat(f(value, source -> dest), tail.flatMap(f))
  }

}

trait Row[A] {
  var _items:Seq[A] = null

  def getItems(): Seq[A] = _items
}

class Matrix[A](_rows: Seq[Row[A]]) {

  val rows = _rows

  def toJSMatrix()(implicit monoid: Monoid[A]): js.Array[js.Array[A]] = {
    if(rows.isEmpty) throw new Exception("Empty matrix")
    val fixedSize = rows.size
    for(row <- rows) if(row.getItems().size != fixedSize) throw new Exception("Expected square matrix")

    var m = js.Array(List.fill(fixedSize)(js.Array[A](List.fill(fixedSize)(monoid.unit): _*)): _*)
    for(row <- rows;i <- 0 until fixedSize)
      for(item <- row.getItems();j <- 0 until fixedSize) {
        m(i)(j) = item
      }
    m
  }
}

object Graph {

  def row[A](items: A*): Row[A] = new Row[A] {
    _items = items.seq
  }
  def matrix[A](rows: Row[A]*): Matrix[A] = new Matrix(rows.seq)

  // O(1)
  def emptyGraph[Node, Edge]()(implicit _node: Monoid[Node], _edge: Monoid[Edge]): Graph[Node, Edge] = new Graph[Node, Edge] {
    override def edge: Monoid[Edge] = _edge
    override def node: Monoid[Node] = _node

    override def isEmpty = true

    override def source = throw new NoSuchElementException("emptyGraph.source")
    override def dest = throw new NoSuchElementException("emptyGraph.dest")
    override def value = throw new NoSuchElementException("emptyGraph.value")

    override def tail = throw new NoSuchElementException("emptyGraph.tail")
  }

  // O(1)
  def graph[Node, Edge](_value: Edge, direction: (Node, Node), tl: => Graph[Node, Edge])
                       (implicit _node: Monoid[Node], _edge: Monoid[Edge]): Graph[Node, Edge] =
    new Graph[Node, Edge] {
      override def edge: Monoid[Edge] = _edge
      override def node: Monoid[Node] = _node

      override def isEmpty = false

      override def source: Node = direction._1
      override def dest: Node = direction._2
      override def value: Edge = _value

      override def tail: Graph[Node, Edge] = tl
    }

  def buildGraph[Node, Edge](graph_data: (Edge, (Node, Node))*)
                            (implicit _node: Monoid[Node], _edge: Monoid[Edge]): Graph[Node, Edge] = {
    if(graph_data.size == 0) emptyGraph()
    else graph(graph_data(0)._1, graph_data(0)._2._1 -> graph_data(0)._2._2, buildGraph(graph_data.tail: _*))
  }

  /* def buildGraph[Node, Edge](graph_data: (Edge, (Node, Node))*)
                            (implicit _node: Monoid[Node], _edge: Monoid[Edge]): Graph[Node, Edge] = {
    if(graph_data.size == 0) emptyGraph()
    else graph(graph_data(0)._1, graph_data(0)._2._1 -> graph_data(0)._2._2, buildGraph(graph_data.tail: _*))
  } */

  def buildGraph3[Node, Edge](graph_data: (Edge, Node, Node)*)
                            (implicit _node: Monoid[Node], _edge: Monoid[Edge]): Graph[Node, Edge] = {
    if(graph_data.size == 0) emptyGraph()
    else graph(graph_data(0)._1, graph_data(0)._2 -> graph_data(0)._3, buildGraph3(graph_data.tail: _*))
  }

  // O(1)
  def concat[Node, Edge](g1: Graph[Node, Edge], g2: Graph[Node, Edge])
                        (implicit node: Monoid[Node], edge: Monoid[Edge]): Graph[Node, Edge] = {
    if(g1.tail.isEmpty) return graph(g1.value, g1.source -> g1.dest, g2)
    else return graph(g1.value, g1.source -> g1.dest, concat(g1.tail, g2))
  }

}