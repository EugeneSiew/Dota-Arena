package dota.model
import scalafx.beans.property.DoubleProperty

// Player class
class Player(val name: String, var heroName: String) {
  // Create the hero selected by the player
  var hero: Hero = getPlayerHero()
  var lose = false
  // Used to update the health and mana bar whenever it changes
  var heroHP = DoubleProperty(hero.hp)
  var heroMana = DoubleProperty(hero.currentMana)

  // Create player's hero
  def getPlayerHero(): Hero = {
    val playerHero = heroName match {
      case "Mars" => new Mars()
      case "Juggernaut" => new Juggernaut()
      case "Zeus" => new Zeus()
    }
    playerHero
  }

  // Player wins game
  def lost(): Unit = {
    this.lose = true
  }

}
