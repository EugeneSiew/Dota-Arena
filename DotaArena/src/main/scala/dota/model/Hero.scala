package dota.model

import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.util.Duration

import scala.util.Random

// Describes the common characteristics of all heroes
abstract class Hero() {
  // Variable initialisation
  val name: String
  val maxHP: Double
  var hp: Double
  val maxMana: Double
  var currentMana: Double
  var baseDamage: Double
  var armor: Double
  var evasionChance: Double
  var lifestealChance: Double
  var attack: Attack
  var ability: Ability
  var mana: Mana

  // Sound effect when using mana potion
  val manaPotionMedia = new Media(getClass.getResource("/dota/sound/manaPotion.wav").toURI.toString)
  val manaPotionMediaPlayer = new MediaPlayer(media = manaPotionMedia)

  // Attack method
  def attackOpponent(selectedAttack: Attack, opponentPlayer: Player, currentPlayer: Player, damageMultiplier: Double = 1): String = {
    // Calculate damage
    var damage = (selectedAttack.damageValue * currentPlayer.hero.baseDamage) / opponentPlayer.hero.armor

    // Holds the message indicating lifesteal occurrence during an attack
    var lifestealMsg: String = ""

    // Lowest damage is 1
    if (damage < 1) {
      damage = 1
    }

    // Play attack sound
    if (damageMultiplier <= 1) currentPlayer.hero.attackSound()

    // Check for evasion
    if (opponentPlayer.hero.evasionChance > currentPlayer.hero.evasionChance) {
      val random = Random.nextDouble()
      if (random <= opponentPlayer.hero.evasionChance) {
        opponentPlayer.hero match {
          // Check for agility hero because only agility hero can evade
          case agilityHero: Agility => {
            damage = 0
            agilityHero.evadeSound()
            return opponentPlayer.name + " evaded the attack!\n"
          }
          case _ =>
        }

      }
    }

    // Check for Mar's bulwark ability (armor >= 40)
    if (opponentPlayer.hero.armor >= 40) {
      opponentPlayer.hero match{
        case mars: Mars =>
          mars.armor -= mars.ability.armorBonus
          damage = 0
          return opponentPlayer.name + " used bulwark. Attack is blocked.\n"
      }
    }

    // Check for Damage Multiplier (Juggernaut critical ability or Zeus damage ability)
    if (damageMultiplier > 1) {
      damage *= damageMultiplier
    }

    // HP Reduction
    opponentPlayer.hero.hp -= damage

    // Lifesteal calculation
    val chance = Random.nextDouble()
    if (chance <= currentPlayer.hero.lifestealChance) {
      // Lifesteal by 35%
      var lifestealValue = damage * 0.35
      currentPlayer.hero.hp += lifestealValue
      if (currentPlayer.hero.hp >= 100) currentPlayer.hero.hp = 100
      currentPlayer.heroHP.value = currentPlayer.hero.hp
      lifestealMsg  += "Lifestealed for " + f"$lifestealValue%.2f" + " HP!\n"
    }

    // Check if hero dead
    if (opponentPlayer.hero.hp <= 0) {
      opponentPlayer.hero.hp = 0
      opponentPlayer.heroHP.value = opponentPlayer.hero.hp
      // If hero dead, player loses
      opponentPlayer.lost()
    }
    else { // Update HP
      opponentPlayer.heroHP.value = opponentPlayer.hero.hp
    }

    // Return game status text
    opponentPlayer.name + " took " + f"$damage%.2f" + " damage! \n" + lifestealMsg
  }

  // Deduct mana from player's mana pool when they use an ability
  def useMana(currentPlayer: Player): Unit = {
    currentPlayer.hero.currentMana -= ability.manaCost
    if (currentPlayer.hero.currentMana <= 0) {
      currentPlayer.hero.currentMana = 0
    }
    currentPlayer.heroMana.value = currentPlayer.hero.currentMana
  }

  // Action to restore mana
  def restoreMana(mana: Mana, currentPlayer: Player): String = {
    // Use potion to restore mana
    currentPlayer.hero.currentMana += mana.manaValue

    // Mana cannot exceed 100
    if (currentPlayer.hero.currentMana > 100) {
      currentPlayer.hero.currentMana = 100
    }

    // Play mana potion sound effect
    manaPotionMediaPlayer.play()
    currentPlayer.heroMana.value = currentPlayer.hero.currentMana

    // Print mana restoration status
    if (currentPlayer.hero.currentMana == 100) {
      return currentPlayer.name + " used a mana potion to restore to full mana."
    }
    else {
      return currentPlayer.name + " used a mana potion to restore " + mana.manaValue + " mana."
    }
  }

  // Create an abstract method, to be implemented in each specifc hero's class
  def useAbility(selectedAbility: Ability, currentPlayer: Player, opponentPlayer: Player): String

  // Play attack sound effect
  def attackSound(): Unit
}

// Strength trait has extra armor
trait Strength {
  val armorBonus = 15
}

// Agility trait has evasion
trait Agility {
  val evasionBonus = 0.3
  def evadeSound(): Unit
}

// Intelligence trait has bonus damage
trait Intelligence {
  val damageBonus = 10
}

// Mars hero
class Mars extends Hero with Strength {
  val name: String = "Mars"
  val maxHP: Double = 100.0
  var hp: Double = maxHP
  val maxMana: Double = 70.0
  var currentMana: Double = maxMana
  var baseDamage: Double = 20
  var armor: Double = 15 + armorBonus
  var evasionChance: Double = 0
  var lifestealChance: Double = 0.2
  var attack: Attack = Spear
  var ability: Ability = Bulwark
  var mana: Mana = manaPotion

  // Mars attack (Spear)
  val spearMedia = new Media(getClass.getResource("/dota/sound/Spear.wav").toURI.toString)
  val spearMediaPlayer = new MediaPlayer(media = spearMedia)

  // Mars ability (Bulwark)
  val bulwarkMedia = new Media(getClass.getResource("/dota/sound/Bulwark.wav").toURI.toString)
  val bulwarkMediaPlayer = new MediaPlayer(media = bulwarkMedia)

  // Use bulwark
  def useAbility(selectedAbility: Ability, currentPlayer: Player, opponentPlayer: Player): String = {
    // Avoid hero's armor continuously increasing if bulwark is used round after round
    if (currentPlayer.hero.armor >= 40) currentPlayer.hero.armor -= currentPlayer.hero.ability.armorBonus
    // Buff hero armor
    currentPlayer.hero.armor += selectedAbility.armorBonus
    useMana(currentPlayer)
    // Play bulwark sound
    bulwarkMediaPlayer.seek(Duration.Zero)
    bulwarkMediaPlayer.play()
    currentPlayer.name + " used Bulwark. Buffing up armor and blocking " + opponentPlayer.name + "\'s next attack.\n"
  }

  // Play spear sound
  def attackSound(): Unit = {
    spearMediaPlayer.seek(Duration.Zero)
    spearMediaPlayer.play()
  }

}

// Juggernaut hero
class Juggernaut extends Hero with Agility {
  val name: String = "Juggernaut"
  val maxHP: Double = 100.0
  var hp: Double = maxHP
  val maxMana: Double = 70.0
  var currentMana: Double = maxMana
  var baseDamage: Double = 24
  var armor: Double = 15
  var evasionChance: Double = evasionBonus
  var lifestealChance: Double = 0.25
  var attack: Attack = Slash
  var ability: Ability = BladeDance
  var mana: Mana = manaPotion

  // Juggernaut attack (Slash)
  val slashMedia = new Media(getClass.getResource("/dota/sound/Slash.wav").toURI.toString)
  val slashMediaPlayer = new MediaPlayer(media = slashMedia)

  // Juggernaut ability (Blade Dance)
  val bladeDanceMedia = new Media(getClass.getResource("/dota/sound/bladeDance.wav").toURI.toString)
  val bladeDanceMediaPlayer = new MediaPlayer(media = bladeDanceMedia)

  // Evade sound
  val evadeMedia = new Media(getClass.getResource("/dota/sound/Evade.mp3").toURI.toString)
  val evadeMediaPlayer = new MediaPlayer(media = evadeMedia)

  // Use blade dance ability
  def useAbility(selectedAbility: Ability, currentPlayer: Player, opponentPlayer: Player): String = {
    selectedAbility match { // Check if the ability used is blade dance
      case bladeDance: Attack =>
        val message = super.attackOpponent(bladeDance, opponentPlayer, currentPlayer, bladeDance.damageMultiplier)
        useMana(currentPlayer)
        // Play blade dance sound
        bladeDanceMediaPlayer.seek(Duration.Zero)
        bladeDanceMediaPlayer.play()
        currentPlayer.name + " used Blade Dance. Dealing critical damage. \n" + message
      case _ =>
        "Ability cannot be downcasted to an Attack"
    }
  }

  // Play slash sound
  def attackSound(): Unit = {
    slashMediaPlayer.seek(Duration.Zero)
    slashMediaPlayer.play()
  }

  // Play evasion sound when evaded attack
  def evadeSound(): Unit = {
    evadeMediaPlayer.seek(Duration.Zero)
    evadeMediaPlayer.play()
  }
}

// Zeus hero
class Zeus extends Hero with Intelligence {
  val name: String = "Zeus"
  val maxHP: Double = 100.0
  var hp: Double = maxHP
  val maxMana: Double = 70.0
  var currentMana: Double = maxMana
  var baseDamage: Double = 26 + damageBonus
  var armor: Double = 13
  var evasionChance: Double = 0
  var lifestealChance: Double = 0.3
  var attack: Attack = Thunderbolt
  var ability: Ability = ThundergodsWrath
  var mana: Mana = manaPotion

  // Zeus attack (Thunderbolt)
  val thunderboltMedia = new Media(getClass.getResource("/dota/sound/Thunderbolt.wav").toURI.toString)
  val thunderboltMediaPlayer = new MediaPlayer(media = thunderboltMedia)

  // Zeus ability (Thundergod's Wrath)
  val thundergodsWrathMedia = new Media(getClass.getResource("/dota/sound/thundergodsWrath.wav").toURI.toString)
  val wrathMediaPlayer = new MediaPlayer(media = thundergodsWrathMedia)

  def useAbility(selectedAbility: Ability, currentPlayer: Player, opponentPlayer: Player): String = {
    selectedAbility match {
      case thundergodsWrath: Attack =>
        val message = super.attackOpponent(thundergodsWrath, opponentPlayer, currentPlayer, thundergodsWrath.damageMultiplier)
        useMana(currentPlayer)
        wrathMediaPlayer.seek(Duration.Zero)
        wrathMediaPlayer.play()
        currentPlayer.name + " used Thundergod's Wrath. Dealing heavy magical damage. \n" + message
      case _ =>
        "Ability cannot be downcasted to an Attack"
    }
  }

  // Play thunderbolt sound
  def attackSound(): Unit = {
    thunderboltMediaPlayer.seek(Duration.Zero)
    thunderboltMediaPlayer.play()
  }
}
