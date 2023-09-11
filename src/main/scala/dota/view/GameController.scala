package dota.view

import dota.MainApp
import dota.model.Game
import dota.util.PlayerDataHolder
import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, Label, ProgressIndicator}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{GridPane, Pane}
import scalafx.scene.text.Text
import scalafx.scene.media.{Media, MediaPlayer}
import scalafxml.core.macros.sfxml

@sfxml
class GameController(private val gameBg: ImageView,
                     private var player1Name: Text,
                     private var player2Name: Text,
                     private var player1HP: ProgressIndicator,
                     private var player1HPText: Label,
                     private var player1Mana: ProgressIndicator,
                     private var player1ManaText: Label,
                     private var player2HP: ProgressIndicator,
                     private var player2HPText: Label,
                     private var player2Mana: ProgressIndicator,
                     private var player2ManaText: Label,
                     private var player1ManaLow: Text,
                     private var player1ManaLowPane: Pane,
                     private var player2ManaLowPane: Pane,
                     private var player2ManaLow: Text,
                     private var player1HeroImg: ImageView,
                     private var player2HeroImg: ImageView,
                     private var player1GridPane: GridPane,
                     private var player2GridPane: GridPane,
                     private var player1AttackButton: Button,
                     private var player1AttackIcon: ImageView,
                     private var player1AbilityButton: Button,
                     private var player1AbilityIcon: ImageView,
                     private var player1ManaButton: Button,
                     private var player2AttackButton: Button,
                     private var player2AttackIcon: ImageView,
                     private var player2AbilityButton: Button,
                     private var player2AbilityIcon: ImageView,
                     private var player2ManaButton: Button,
                     private var gameStatusText: Text,
                     private var rematchButton: Button,
                     private var backButton: Button
                    ) {

  // Play game background music
  val gameMedia = new Media(getClass.getResource("/dota/sound/Game.wav").toURI.toString)
  val mediaPlayer = new MediaPlayer(media = gameMedia)
  mediaPlayer.setCycleCount(MediaPlayer.Indefinite)
  mediaPlayer.setVolume(0.4)
  mediaPlayer.play()

  // Winner sound
  val winnerMedia = new Media(getClass.getResource("/dota/sound/Winner.wav").toURI.toString)
  val winnerMediaPlayer = new MediaPlayer(media = winnerMedia)

  // Game Background
  gameBg.image = new Image(getClass.getResourceAsStream("/dota/image/gameBg.jpg"))

  // Initialise game
  var playerNames = PlayerDataHolder.playerDataHolder
  var game = new Game(playerNames(0), playerNames(1))

  // Get the game's current status and display
  gameStatusText.text <== game.gameStatusContent

  // Function to change camel case class name (abilities) to spaced names
  // Eg: BladeDance -> Blade Dance
  def convertCamelCaseToWords(className: String): String = {
    val words = "[A-Z][a-z]*".r.findAllIn(className).toList
    words.mkString(" ")
  }

  // Player 1 details
  player1Name.text = playerNames(0) + ": " + game.player1.hero.name
  player1HeroImg.image = new Image(getClass.getResourceAsStream("/dota/image/" + game.player1.hero.name + ".png"))
  // Player 1 Attack Button
  var player1AttackName = game.player1.hero.attack.getClass.getSimpleName.dropRight(1)
  player1AttackIcon.image = new Image(getClass.getResourceAsStream("/dota/image/" + player1AttackName + ".png"))
  player1AttackIcon.setVisible(false)
  player1AttackButton.text = player1AttackName
  //Player 1 Ability Button
  var player1AbilityName = game.player1.hero.ability.getClass.getSimpleName.dropRight(1)
  player1AbilityIcon.image = new Image(getClass.getResourceAsStream("/dota/image/" + player1AbilityName + ".png"))
  player1AbilityIcon.setVisible(false)
  player1AbilityButton.text = convertCamelCaseToWords(player1AbilityName)

  // Player 2 details
  player2Name.text = playerNames(1) + ": " + game.player2.hero.name
  player2HeroImg.image = new Image(getClass.getResourceAsStream("/dota/image/" + game.player2.hero.name + ".png"))
  // Player 2 Attack Button
  var player2AttackName = game.player2.hero.attack.getClass.getSimpleName.dropRight(1)
  player2AttackIcon.image = new Image(getClass.getResourceAsStream("/dota/image/" + player2AttackName + ".png"))
  player2AttackIcon.setVisible(false)
  player2AttackButton.text = player2AttackName
  //Player 2 Ability Button
  var player2AbilityName = game.player2.hero.ability.getClass.getSimpleName.dropRight(1)
  player2AbilityIcon.image = new Image(getClass.getResourceAsStream("/dota/image/" + player2AbilityName + ".png"))
  player2AbilityIcon.setVisible(false)
  player2AbilityButton.text = convertCamelCaseToWords(player2AbilityName)

  // Game controls visibility
  player2GridPane.disable.value = true // Player 1 will start first

  // Hide rematch and return to main menu button
  rematchButton.setVisible(false)
  backButton.setVisible(false)

  // Hide mana low message
  player1ManaLowPane.visible = false
  player1ManaLow.visible = false
  player2ManaLowPane.visible = false
  player2ManaLow.visible = false

  // Change turns
  game.isCurrentTurn.onChange((_, _, _) => {
    // Disable grid pane containing buttons
    player1GridPane.disable.value = player2GridPane.disable.value
    player2GridPane.disable.value = !player2GridPane.disable.value

    // Display insufficient mana to use ability message
    // Display only when it's player 1's turn and their hero has insufficient mana
    if (!player1GridPane.disable.value & game.player1.hero.currentMana < game.player1.hero.ability.manaCost) {
      player1ManaLowPane.visible = true
      player1ManaLow.visible = true
    }
    else {
      player1ManaLowPane.visible = false
      player1ManaLow.visible = false
    }

    // Display only when it's player 2's turn and their hero has insufficient mana
    if (!player2GridPane.disable.value & game.player2.hero.currentMana < game.player2.hero.ability.manaCost) {
      player2ManaLowPane.visible = true
      player2ManaLow.visible = true
    }
    else {
      player2ManaLowPane.visible = false
      player2ManaLow.visible = false
    }

  })

  // Game ends
  game.isEndGame.onChange( (_,_,_) => {
    // Hide mana low message
    player1ManaLowPane.visible = false
    player1ManaLow.visible = false
    player2ManaLowPane.visible = false
    player2ManaLow.visible = false

    // Stop game music
    mediaPlayer.stop()

    // Play winner music
    winnerMediaPlayer.setCycleCount(MediaPlayer.Indefinite)
    winnerMediaPlayer.play()

    // Disable grid pane
    player1GridPane.disable.value = true
    player2GridPane.disable.value = true

    // Show rematch and back buttons
    rematchButton.visible.value = true
    backButton.visible.value = true
  })


  // HP Bar
  def updateHpBar(hp: Double, maxHP: Double, hpBar: ProgressIndicator, hpText: Label): Unit = {
    val hpValue = hp / maxHP
    hpBar.progress = hpValue

    // Change hp bar color based on current health
    val style = hpValue match {
      case _ if hpValue >= 0.8 => "-fx-accent: YellowGreen;"
      case _ if hpValue >= 0.5 => "-fx-accent: Gold;"
      case _ if hpValue >= 0.2 => "-fx-accent: Yellow;"
      case _ => "-fx-accent: Red;"
    }
    hpBar.setStyle(style)

    // Update HP value text beside hp bar
    hpText.text = f"$hp%.2f" + "/" + f"$maxHP%.2f"
  }


  // Initialise HP bar with hero stats
  updateHpBar(game.player1.hero.hp, game.player1.hero.maxHP, player1HP, player1HPText)
  updateHpBar(game.player2.hero.hp, game.player2.hero.maxHP, player2HP, player2HPText)

  // Update player 1's HP bar when or if changed
  game.player1.heroHP.onChange({
    updateHpBar(game.player1.hero.hp, game.player1.hero.maxHP, player1HP, player1HPText)

    if (game.player1.hero.hp <= 0)
      player2HeroImg.image = new Image(getClass.getResourceAsStream("/dota/image/Victory.png"))

  })

  // Update player 2's HP bar when or if changed
  game.player2.heroHP.onChange( {
    updateHpBar(game.player2.hero.hp, game.player2.hero.maxHP, player2HP, player2HPText)
    if (game.player2.hero.hp <= 0)
      player1HeroImg.image = new Image(getClass.getResourceAsStream("/dota/image/Victory.png"))

  }
  )

  // Mana bar
  def updateManaBar(mana: Double, maxMana: Double, manaBar: ProgressIndicator, manaText: Label): Unit = {
    val manaValue = mana / maxMana
    manaBar.progress = manaValue
    manaText.text = f"$mana%.2f" + "/" + f"$maxMana%.2f"
  }

  // Initialise mana bar with hero stats
  updateManaBar(game.player1.hero.currentMana, game.player1.hero.maxMana, player1Mana, player1ManaText)
  updateManaBar(game.player2.hero.currentMana, game.player2.hero.maxMana, player2Mana, player2ManaText)

  // Update Mana bar when if changed
  game.player1.heroMana.onChange({
    updateManaBar(game.player1.hero.currentMana, game.player1.hero.maxMana, player1Mana, player1ManaText)

    // Check if hero has enough mana to use ability
    if (game.player1.hero.currentMana < game.player1.hero.ability.manaCost) {
      player1AbilityButton.setDisable(true)
    }
    else {
      player1AbilityButton.setDisable(false)
    }
  }

  )

  game.player2.heroMana.onChange({
    updateManaBar(game.player2.hero.currentMana, game.player2.hero.maxMana, player2Mana, player2ManaText)
    // Check if hero has enough mana to use ability
    if (game.player2.hero.currentMana < game.player2.hero.ability.manaCost) {
      player2AbilityButton.setDisable(true)
    }
    else {
      player2AbilityButton.setDisable(false)
    }

  }

  )

  // Player buttons
  def handleP1Attack(actionEvent: ActionEvent): Unit = {
    game.gameStatusContent.value = game.player1.hero.attackOpponent(game.player1.hero.attack, game.player2, game.player1)
    game.checkGameStatus()
  }

  // When mouse entered button area, display the attack icon for player 1's hero
  def handleP1AttackEnter(event: MouseEvent): Unit = {
    player1AttackIcon.setVisible(true)
  }

  // When mouse exited button area remove display for the attack icon for player 1's hero
  def handleP1AttackExit(event: MouseEvent): Unit = {
    player1AttackIcon.setVisible(false)
  }

  def handleP1Ability(actionEvent: ActionEvent): Unit = {
    game.gameStatusContent.value = game.player1.hero.useAbility(game.player1.hero.ability, game.player1, game.player2)
    game.checkGameStatus()
  }

  // When mouse entered button area, display the ability icon for player 1's hero
  def handleP1AbilityEnter(event: MouseEvent): Unit = {
    player1AbilityIcon.setVisible(true)
  }

  // When mouse exited button area remove display for the ability icon for player 1's hero
  def handleP1AbilityExit(event: MouseEvent): Unit = {
    player1AbilityIcon.setVisible(false)
  }

  def handleP1Mana(actionEvent: ActionEvent): Unit = {
    game.gameStatusContent.value = game.player1.hero.restoreMana(game.player1.hero.mana , game.player1)
    player1ManaButton.setDisable(true)
    game.checkGameStatus()
  }

  def handleP1Forfeit(actionEvent: ActionEvent): Unit = {
    game.player1.lost()
    player2HeroImg.image = new Image(getClass.getResourceAsStream("/dota/image/Victory.png"))
    game.checkGameStatus()
  }

  def handleP2Attack(actionEvent: ActionEvent): Unit = {
    game.gameStatusContent.value = game.player2.hero.attackOpponent(game.player2.hero.attack, game.player1, game.player2)
    game.checkGameStatus()
  }

  // When mouse entered button area, display the attack icon for player 2's hero
  def handleP2AttackEnter(event: MouseEvent): Unit = {
    player2AttackIcon.setVisible(true)
  }

  // When mouse exited button area remove display for the attack icon for player 2's hero
  def handleP2AttackExit(event: MouseEvent): Unit = {
    player2AttackIcon.setVisible(false)
  }

  def handleP2Ability(actionEvent: ActionEvent): Unit = {
      game.gameStatusContent.value = game.player2.hero.useAbility(game.player2.hero.ability, game.player2, game.player1)
      game.checkGameStatus()
  }

  // When mouse entered button area, display the ability icon for player 2's hero
  def handleP2AbilityEnter(event: MouseEvent): Unit = {
    player2AbilityIcon.setVisible(true)
  }

  // When mouse exited button area remove display for the ability icon for player 2's hero
  def handleP2AbilityExit(event: MouseEvent): Unit = {
    player2AbilityIcon.setVisible(false)
  }

  def handleP2Mana(actionEvent: ActionEvent): Unit = {
    game.gameStatusContent.value = game.player2.hero.restoreMana(game.player2.hero.mana, game.player2)
    player2ManaButton.setDisable(true)
    game.checkGameStatus()
  }

  def handleP2Forfeit(actionEvent: ActionEvent): Unit = {
    game.player2.lost()
    player1HeroImg.image = new Image(getClass.getResourceAsStream("/dota/image/Victory.png"))
    game.checkGameStatus()
  }

  // Rematch and return to main menu buttons
  def handleRematch(actionEvent: ActionEvent): Unit = {
    mediaPlayer.stop()
    winnerMediaPlayer.stop()
    MainApp.showP1HeroSelection()
  }

  def handleBack(actionEvent: ActionEvent): Unit = {
    mediaPlayer.stop()
    winnerMediaPlayer.stop()
    MainApp.showMainMenu()
  }
}