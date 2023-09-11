package dota.model

import scala.util.Random

// Hero abilities
abstract class Ability {
  val manaCost: Double
  val armorBonus: Double = 0
}


// Normal hero attack
abstract class Attack extends Ability {
  val damageValue: Double
  val damageMultiplier: Double = 1
}


// Mars's attack
object Spear extends Attack {
  val manaCost = 0
  val damageValue = 8

}

// Juggernaut's attack
object Slash extends Attack {
  val manaCost = 0
  val damageValue = 10
}

// Zeus's attack
object Thunderbolt extends Attack {
  val manaCost = 0
  val damageValue = 12
}

// Juggernaut's ability
object BladeDance extends Attack {
  val manaCost = 25
  val damageValue = 13
  override val damageMultiplier: Double = 1 + Random.nextDouble() * 0.3
}

// Zeus's ability
object ThundergodsWrath extends Attack {
  val manaCost = 30
  val damageValue = 14
  override val damageMultiplier: Double = 1.4 + Random.nextDouble() * 0.3
}

// Mars's ability
object Bulwark extends Ability{
  val manaCost = 20
  override val armorBonus = 15
}

