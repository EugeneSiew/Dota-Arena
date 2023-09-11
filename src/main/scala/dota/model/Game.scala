package dota.model

import dota.util.{HeroSelectionsHolder, PlayerDataHolder}
import scalafx.beans.property.{BooleanProperty, StringProperty}

import scala.util.{Failure, Success, Try}
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ArrayBuffer

class Game(val player1Name: String, val player2Name: String){

  // Get the player's hero selection
  val selectedHeroes: ArrayBuffer[String] = HeroSelectionsHolder.heroSelectionsHolder
  // Get the hero names that the players chose to be created in the player class
  val player1HeroName: String = selectedHeroes(0)
  val player2HeroName: String = selectedHeroes(1)
  // Clear the array buffer so the heroes don't accumulate to the next game
  selectedHeroes.clear()

  // Initialise Players
  val player1 = new Player(player1Name, player1HeroName)
  val player2 = new Player(player2Name, player2HeroName)

  // Initialise variables
  var isEndGame: BooleanProperty = BooleanProperty(false)
  var isCurrentTurn: BooleanProperty = BooleanProperty(true) //true = player one's turn, false = player two's turn
  var gameStatusContent: StringProperty = new StringProperty() // Print the attack/ability the player used + damage and any status


  // Determine winner
  def winner: Player = {
    if (player1.lose) player2
    else if (player2.lose) player1
    else null
  }

  // Determine loser
  def loser: Player = {
    if (player1.lose) player1
    else if (player2.lose) player2
    else null
  }

  // Moderate the game flow, either checking if one player lost or switching turns
  def checkGameStatus(): Unit = {
    if (player1.lose || player2.lose) {
      isEndGame.value = true
      // Store the match details
      storeMatch(winner, loser)
      gameStatusContent.value = "Player " + winner.name + " won with " + winner.hero.name + "!"
    }
    else {
      // Change turns. If true (P1), change to false for P2's turn and vice versa
      isCurrentTurn.value = !isCurrentTurn.value
    }
  }

  // Store the match details of who won against who
  def storeMatch(winner: Player, loser: Player): Unit = {

    // Use file writer to write the details to a text file
    val matchRecord = Try {
      val writer = new FileWriter("src/main/scala/dota/util/MatchHistory.txt", true)
      val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))

      // Store the time and match details
      writer.write(s"$timestamp - ${winner.name} (${winner.hero.name}) won against ${loser.name} (${loser.hero.name})\n")
      writer.close()
    }

    // Print saving status of the record to the file
    matchRecord match {
      case Success(_) => println("Match record saved successfully.")
      case Failure(exception) => println("An error occurred while writing match record: " + exception.getMessage)
    }

  }

}