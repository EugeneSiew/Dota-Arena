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
class P1HeroSelectionController(private val P1Bg: ImageView,
                                private val strengthLogo: ImageView,
                                private val marsImg: ImageView,
                                private val agilityLogo: ImageView,
                                private val juggernautImg: ImageView,
                                private val intelligenceLogo: ImageView,
                                private val zeusImg: ImageView,
                                private var P1NameField: TextField,
                                private var P1Hero: Text,
                                private var stage: Stage) {


  // Play hero selection background music
  val media = new Media(getClass.getResource("/dota/sound/heroSelection.wav").toURI.toString)
  val mediaPlayer = new MediaPlayer(media = media)
  mediaPlayer.setCycleCount(MediaPlayer.Indefinite)
  mediaPlayer.setVolume(0.8)
  mediaPlayer.play()

  // Player 1 Hero Selection Background
  P1Bg.image = new Image(getClass.getResourceAsStream("/dota/image/p1Bg.png"))

  // Mars
  strengthLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Strength.png"))
  marsImg.image = new Image(getClass.getResourceAsStream("/dota/image/Mars.png"))

  // Juggernaut
  agilityLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Agility.png"))
  juggernautImg.image = new Image(getClass.getResourceAsStream("/dota/image/Juggernaut.png"))

  // Zeus
  intelligenceLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Intelligence.png"))
  zeusImg.image = new Image(getClass.getResourceAsStream("/dota/image/Zeus.png"))

  // Display Player 1's hero selection
  def handleMars(action: ActionEvent): Unit = {
    P1Hero.text = "Mars"
  }

  def handleJuggernaut(action: ActionEvent): Unit = {
    P1Hero.text = "Juggernaut"
  }

  def handleZeus(action: ActionEvent): Unit = {
    P1Hero.text = "Zeus"
  }

  // Move to Player 2's hero selection phase
  def handleNext(actionEvent: ActionEvent): Unit = {
    // Check if Player 1 entered name
    if (P1NameField.text.value.isEmpty || P1NameField.text.value == null) {

      new Alert(Alert.AlertType.Error) {
        initOwner(stage)
        title = "Incomplete Data"
        headerText = "Please enter Player 1's name!"
      }.showAndWait()

    }

    // Check if Player 1 chose a hero
    else if (P1Hero.text.value != "Mars" & P1Hero.text.value != "Juggernaut" & P1Hero.text.value != "Zeus") {

      new Alert(Alert.AlertType.Error) {
        initOwner(stage)
        title = "Incomplete Data"
        headerText = "Please choose Player 1's hero!"
      }.showAndWait()

    }

    // Move to Player 2 hero selection phase
    else {
      // Remove any old names and values
      PlayerDataHolder.playerDataHolder.clear()
      HeroSelectionsHolder.heroSelectionsHolder.clear()

      HeroSelectionsHolder.heroSelectionsHolder += P1Hero.text.value
      PlayerDataHolder.playerDataHolder += P1NameField.text.value
      mediaPlayer.stop()
      MainApp.showP2HeroSelection()
    }
  }

  // Allow user to return to main menu
  def handleCancel(actionEvent: ActionEvent): Unit = {
    mediaPlayer.stop()
    MainApp.showMainMenu()
  }


}