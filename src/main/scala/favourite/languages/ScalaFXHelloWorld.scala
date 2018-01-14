package favourite.languages

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.HBox
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.text.Text
import scalafx.scene.control.{Dialog, TextField}

import java.io._
import scalaj.http._
import org.json4s.DefaultFormats
import org.json4s.native.Serialization

import com.typesafe.config.ConfigFactory

object ScalaFXHelloWorld extends JFXApp {

  stage = new PrimaryStage {
    title = "Favourite Language"
    width = 450
    height = 150
    scene = new Scene {
      fill = Color.rgb(38, 38, 38)
      content = new HBox {
        padding = Insets(50, 80, 50, 80)
        children = Seq(
          new Text {
            text = "Username"
            style = "-fx-font: normal bold 14pt sans-serif"
            fill = new LinearGradient(
              endX = 0,
              stops = Stops(Color.LightGray, Color.DarkGray))
            spacing = 15
          },
          new TextField {
            prefColumnCount = 10
          },
          new Text {
            val config = loadConfigOrThrow[Config](ConfigFactory.load.getConfig("favourite.languages"))
            println(s"CONFIG --> $config")
	    text = repoData("kohrVid", config.githubAuth)
          }
        )
      }
    }
  }

  def repoData(username: String, auth: String): String = {
    implicit val formats = DefaultFormats

    // Why is string interpolation such a bitch?
    val languages = "{\n edges {\n size\n node {\nid\nname\ncolor\n}\n  }\n}"
    val repositories = "repositories(first: 100, after: \""+ auth + "\") {\n edges {\n   cursor\n   node {\nname\nlanguages(first: 20) \n" + languages + "}\n }\n    }"
    val q = "{\n  user(login: \"" + username + "\") {\n    name\n" + repositories + "\n  }\n}\n"

    val query = Serialization.write(
      Map(
        "query" -> q,
        "variables" -> "{}",
        "operationName" -> null
      )
    )

    val response: HttpResponse[String] = Http(
    "https://api.github.com/graphql"
    ).headers(Seq(
      "Authorization" -> s"bearer $auth"
    )).postData(query).asString
    response.body
  }
}
