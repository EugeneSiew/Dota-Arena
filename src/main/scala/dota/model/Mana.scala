package dota.model

// Abstract class to describe mana
abstract class Mana{
  val manaValue: Double
}

// Mana potion that can be used by the players to restore mana
object manaPotion extends Mana {
  val manaValue = 30
}