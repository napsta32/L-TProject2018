
package topojson {

  object topojson

}

package object topojson {

  implicit def d3toTopojsonClient(d3t: topojson.type): topojsonclient.type = topojsonclient

}