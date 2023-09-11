package dota.view

import dota.MainApp
import dota.util.MediaPlayerMediator
import scalafx.event.ActionEvent
import scalafxml.core.macros.sfxml

@sfxml
class RootLayoutController(){

  // Functions
  def handleBack(action: ActionEvent): Unit = {
    MediaPlayerMediator.stopMediaPlayer()
    MainApp.showMainMenu()
  }

  def handleExit(): Unit = {
    System.exit(0)
  }

  def handleGuide(action: ActionEvent): Unit = {
    MainApp.showGuide()
  }

  def handleHistory(action: ActionEvent): Unit = {
    MainApp.showMatchHistory()
  }
}
