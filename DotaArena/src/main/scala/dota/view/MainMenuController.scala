package dota.view

import dota.MainApp
import dota.util.MediaPlayerMediator
import scalafx.event.ActionEvent
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.media.{Media, MediaPlayer}
import scalafx.util.Duration
import scalafxml.core.macros.sfxml

@sfxml
class MainMenuController(private val mainMenuLogo: ImageView, private val mainMenuBg: ImageView) {
  // Play main menu background music
  val filePath = getClass.getResource("/dota/sound/mainMenu.wav").toURI.toString
  var mediaPlayer: MediaPlayer = createMediaPlayer()

  // Method to create a media player
  def createMediaPlayer(): MediaPlayer = {
    val media = new Media(filePath)
    val player = new MediaPlayer(media)

    player.setOnReady(() => {
      // Media is loaded and ready to play
      player.seek(Duration.Zero)
      player.setCycleCount(MediaPlayer.Indefinite)
      player.play()
      MediaPlayerMediator.setMediaPlayer(player) // Use the mediator to set the media player
    })

    player
  }


  // Display main menu images
  mainMenuLogo.image = new Image(getClass.getResourceAsStream("/dota/image/menuLogo.png"))
  mainMenuBg.image = new Image(getClass.getResourceAsStream("/dota/image/menuBg.jpg"))

  // Functions
  def handleExit(action: ActionEvent): Unit = {
    System.exit(0)
  }

  def handlePlay(action: ActionEvent): Unit = {
    MediaPlayerMediator.stopMediaPlayer()
    MainApp.showP1HeroSelection()
  }
}