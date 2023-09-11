package dota.util

import scalafx.scene.media.MediaPlayer

object MediaPlayerMediator {
  // A variable to store the currently active MediaPlayer
  private var mediaPlayer: Option[MediaPlayer] = None

  // This method is used to set the active MediaPlayer
  def setMediaPlayer(player: MediaPlayer): Unit = {
    // Check if there is an existing MediaPlayer
    mediaPlayer.foreach { existingPlayer =>
      // If the existing player is currently playing, stop it
      if (existingPlayer.status == MediaPlayer.Status.Playing) {
        existingPlayer.stop()
      }
      // Dispose of the existing player's resources
      existingPlayer.dispose()
    }

    // Store the new MediaPlayer as the active one
    mediaPlayer = Some(player)
  }

  // This method is used to stop and dispose of the active MediaPlayer
  def stopMediaPlayer(): Unit = {
    // Check if there is an active MediaPlayer
    mediaPlayer.foreach { player =>
      // If the active player is currently playing, stop it
      if (player.status == MediaPlayer.Status.Playing) {
        player.stop()
      }
      // Dispose of the active player's resources
      player.dispose()
    }

    // Set the active MediaPlayer to None, indicating no active player
    mediaPlayer = None
  }
}
