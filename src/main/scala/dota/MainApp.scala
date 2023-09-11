package dota

import javafx.{scene => jfxs}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.image.Image
import scalafx.stage.Stage
import scalafxml.core.{FXMLLoader, NoDependencyResolver}

object MainApp extends JFXApp {
  val rootResource = getClass.getResource("view/RootLayout.fxml")
  // Initialize the loader object
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  // Load root layout from fxml file
  loader.load();
  // Retrieve the root component BorderPane from the FXML
  val roots = loader.getRoot[jfxs.layout.BorderPane]
  // Initialize stage
  stage = new PrimaryStage {
    title = "Dota Arena"
    width = 900
    height = 600
    icons += new Image(getClass.getResource("image/menuLogo.png").toURI.toString)
    scene = new Scene {
      root = roots
    }
  }

  // Show main menu page
  def showMainMenu() = {
    val resource = getClass.getResource("view/MainMenu.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Show guide page as popout page
  def showGuide() = {
    val resource = getClass.getResource("view/Guide.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    val guideStage = new Stage{
      title = "Guide"
      width = 900
      height = 638
      icons += new Image(getClass.getResource("/dota/image/menuLogo.png").toURI.toString)
      scene = new Scene {
        root = roots
      }
    }
    guideStage.showAndWait()
  }

  // Show match history
  def showMatchHistory(): Unit = {
    val resource = getClass.getResource("view/MatchHistory.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Show Player 1 hero selection page
  def showP1HeroSelection(): Unit = {
    val resource = getClass.getResource("view/P1HeroSelection.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Show Player 2 hero selection page
  def showP2HeroSelection(): Unit = {
    val resource = getClass.getResource("view/P2HeroSelection.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Show game page
  def showGame(): Unit = {
    val resource = getClass.getResource("view/Game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  // Show main menu at start
  showMainMenu()
}