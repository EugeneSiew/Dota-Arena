package dota.view

import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import scala.io.Source
import scala.util.{Failure, Success, Try, Using}

@sfxml
class MatchHistoryController(private var matchRecords: Text,
                             private val historyBg: ImageView) {

  // Match history background image
  historyBg.image = new Image(getClass.getResourceAsStream("/dota/image/guideBg.png"))

  // Read and display the match records
  val result = Using(Source.fromFile("src/main/scala/dota/util/MatchHistory.txt")) { source =>
    val records = source.getLines().toList.reverse.mkString("\n")
    matchRecords.text = records
  }

  // Print any error message when reading the file
  result match {
    case Success(_) =>
    // File read and text set successfully
    case Failure(exception) =>
      println(s"Error reading file: ${exception.getMessage}")
  }
}