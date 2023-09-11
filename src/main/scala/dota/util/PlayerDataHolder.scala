package dota.util

import scala.collection.mutable.ArrayBuffer

// Array buffer created to store the hero selection of P1 & P2
object HeroSelectionsHolder {
  var heroSelectionsHolder = new ArrayBuffer[String]()
}

// Array buffer created to store the names of P1 & P2
object PlayerDataHolder {
  var playerDataHolder = new ArrayBuffer[String]()
}