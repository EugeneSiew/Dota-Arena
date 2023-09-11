package dota.view

import dota.MainApp
import dota.util.{HeroSelectionsHolder, PlayerDataHolder}
import scalafx.event.ActionEvent
import scalafx.scene.control.{Alert, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.scene.text.Text
import scalafx.stage.Stage
import scalafxml.core.macros.sfxml

@sfxml
class P2HeroSelectionController(private val P2Bg: ImageView,
                                private val strengthLogo: ImageView,
                                private val marsImg: ImageView,
                                private val agilityLogo: ImageView,
                                private val juggernautImg: ImageView,
                                private val intelligenceLogo: ImageView,
                                private val zeusImg: ImageView,
                                private var P2NameField: TextField,
                                private var P2Hero: Text,
                                private var stage: Stage) {


  // Play hero selection background music
  val media = new Media(getClass.getResource("/dota/sound/heroSelection.wav").toURI.toString)
  val mediaPlayer = new MediaPlayer(media = media)
  mediaPlayer.setCycleCount(MediaPlayer.Indefinite)
  mediaPlayer.setVolume(0.8)
  mediaPlayer.play()

  // Player 2 Hero Selection Background
  P2Bg.image = new Image(getClass.getResourceAsStream("/dota/image/P2Bg.jpg"))

  // Mars
  strengthLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Strength.png"))
  marsImg.image = new Image(getClass.getResourceAsStream("/dota/image/Mars.png"))

  // Juggernaut
  agilityLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Agility.png"))
  juggernautImg.image = new Image(getClass.getResourceAsStream("/dota/image/Juggernaut.png"))

  // Zeus
  intelligenceLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Intelligence.png"))
  zeusImg.image = new Image(getClass.getResourceAsStream("/dota/image/Zeus.png"))

  // Display Player 2's hero selection
  def handleMars(action: ActionEvent): Unit = {
    P2Hero.text = "Mars"
  }

  def handleJuggernaut(action: ActionEvent): Unit = {
    P2Hero.text = "Juggernaut"
  }

  def handleZeus(action: ActionEvent): Unit = {
    P2Hero.text = "Zeus"
  }

  // Move to Player 2's hero selection phase
  def handleStart(actionEvent: ActionEvent): Unit = {
    // Check if Player 2 entered name
    if (P2NameField.text.value.isEmpty || P2NameField.text.value == null) {

      val incorrectData = new Alert(Alert.AlertType.Error) {
        initOwner(stage)
        title = "Incomplete Data"
        headerText = "Please enter Player 2's name!"
      }.showAndWait()

    }

    // Check if Player 2 chose a hero
    else if (P2Hero.text.value != "Mars" & P2Hero.text.value != "Juggernaut" & P2Hero.text.value != "Zeus") {

      new Alert(Alert.AlertType.Error) {
        initOwner(stage)
        title = "Incomplete Data"
        headerText = "Please choose Player 2's hero!"
      }.showAndWait()

    }

    // Start Game
    else {
      HeroSelectionsHolder.heroSelectionsHolder += P2Hero.text.value
      PlayerDataHolder.playerDataHolder += P2NameField.text.value

      mediaPlayer.stop()
      MainApp.showGame()
    }
  }

  // Allow user to return to main menu
  def handleCancel(actionEvent: ActionEvent): Unit = {
    mediaPlayer.stop()
    MainApp.showMainMenu()
  }


}