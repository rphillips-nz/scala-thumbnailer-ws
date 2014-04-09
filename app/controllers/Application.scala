package controllers

import play.api._
import play.api.mvc._
import play.api.libs.ws.WS
import play.api.libs.concurrent.Execution.Implicits._
import java.io.FileInputStream
import java.io.ByteArrayInputStream
import nz.co.rossphillips.thumbnailer._

object Application extends Controller {

	val thumbnailer = new Thumbnailer

	def index = Action {
		Ok(views.html.index())
	}

	def thumbnailFromUrl(url: String) = Action.async {
		WS.url(url).get().map { response =>
			if (response.status == 200) {
				val input = new ByteArrayInputStream(response.getAHCResponse.getResponseBodyAsBytes)
				val bytes = thumbnailer.generateThumbnail(input, response.header("Content-Type").get)
				input.close()
				Ok(bytes).as("image/png")
			} else {
				Status(response.status)
			}
		}
	}

	def thumbnailFromData = Action(parse.multipartFormData) { request =>
		request.body.file("file").map { file =>
			val input = new FileInputStream(file.ref.file)
			val bytes = thumbnailer.generateThumbnail(input, file.contentType.get)
			input.close()
			Ok(bytes).as("image/png")
		}.getOrElse {
			BadRequest
		}
	}

}
