package dota.view

import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.media.{Media, MediaPlayer, MediaView}
import scalafxml.core.macros.sfxml

@sfxml
class GuideController(private val guideLogo: ImageView,
                      private val guideBg: ImageView,
                      private val heroesBg: ImageView,
                      private val strengthLogo: ImageView,
                      private val marsImg: ImageView,
                      private val agilityLogo: ImageView,
                      private val juggernautImg: ImageView,
                      private val intelligenceLogo: ImageView,
                      private val zeusImg: ImageView,
                      private var vid: MediaView
                      ) {

  // Background image for guide
  guideLogo.image = new Image(getClass.getResourceAsStream("/dota/image/menuLogo.png"))
  guideBg.image = new Image(getClass.getResourceAsStream("/dota/image/guideBg.png"))

  // Heroes background image
  heroesBg.image = new Image(getClass.getResourceAsStream("/dota/image/guideBg.png"))

  // Mars
  strengthLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Strength.png"))
  marsImg.image = new Image(getClass.getResourceAsStream("/dota/image/Mars.png"))

  // Juggernaut
  agilityLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Agility.png"))
  juggernautImg.image = new Image(getClass.getResourceAsStream("/dota/image/Juggernaut.png"))

  // Zeus
  intelligenceLogo.image = new Image(getClass.getResourceAsStream("/dota/image/Intelligence.png"))
  zeusImg.image = new Image(getClass.getResourceAsStream("/dota/image/Zeus.png"))

}