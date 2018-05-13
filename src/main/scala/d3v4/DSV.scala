package d3v4

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

// https://github.com/d3/d3-dsv
@JSImport("d3-dsv", JSImport.Namespace)
@js.native
object d3dsv extends js.Object {

  def dsvFormat(separator: String): dsv = js.native

  def csvParse(text: String): js.Array[js.Dictionary[String]] = js.native
  def tsvParse(text: String): js.Array[js.Dictionary[String]] = js.native

  def csvFormat(obj: js.Array[js.Dictionary[String]]): String = js.native
  def tsvFormat(obj: js.Array[js.Dictionary[String]]): String = js.native

}

@js.native
trait dsv extends js.Object {

  def parse(text: String): js.Array[js.Dictionary[String]] = js.native
  def format(obj: js.Array[js.Dictionary[String]]): String = js.native

}