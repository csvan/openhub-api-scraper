package miner

import java.util
import java.util.logging

import akka.actor.Actor
import database.PageManager
import database.entity.Page
import org.apache.http.Header
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.{CloseableHttpClient, HttpClientBuilder}
import org.apache.http.message.BasicHeader

import scala.xml.{Node, XML}

/**
 * Created by christopher on 18/11/14.
 */
class Worker extends Actor {

  def makeClient(): CloseableHttpClient = {

    val headers = new util.LinkedList[Header]()
    headers.add(new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"))
    headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate,sdch"))
    headers.add(new BasicHeader("Accept-Language", "en-GB,en;q=0.8,en-US;q=0.6,sv;q=0.4"))
    headers.add(new BasicHeader("Cache-Control", "max-age=0"))
    headers.add(new BasicHeader("Connection", "keep-alive"))
    headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36"))

    HttpClientBuilder.create().setDefaultHeaders(headers).build()
  }

  def receive = {

    case MinePages(key, start, elements) =>
      logging.Logger.getAnonymousLogger.info(s"[Worker] $key received MinePages message")
      var httpClient = makeClient()

      for (i ← start until (start + elements)) {

        // Check so that we have not already checked this page
        PageManager.findOneBy("pageNumber", i.toString) match {
          case None =>
            try {
              val url = s"https://www.openhub.net/projects.xml?api_key=$key&page=$i"
              val request = new HttpGet(url)
              request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
              request.addHeader("Accept-Encoding", "gzip,deflate,sdch")
              request.addHeader("Accept-Language", "en-GB,en;q=0.8,en-US;q=0.6,sv;q=0.4")
              request.addHeader("Cache-Control", "max-age=0")
              request.addHeader("Connection", "keep-alive")
              request.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36")

              val httpResponse = httpClient.execute(request)
              val entity = httpResponse.getEntity
              if (entity != null) {

                val inputStream = entity.getContent
                val content = io.Source.fromInputStream(inputStream).getLines().mkString
                inputStream.close()

                val xml = XML.loadString(content)
                val projects = xml \\ "project"

                if (projects.length > 0) {
                  projects.map {
                    (node: Node) =>
                      sender() ! ExtractedProject(node)
                      logging.Logger.getAnonymousLogger.info(s"[Worker] $key sent project from page $i back to Supervisor")
                  }

                  val page = new Page()
                  page.setPageNumber(i)
                  PageManager.persist(page)
                  logging.Logger.getAnonymousLogger.info(s"[Worker] $key Succesfully processed page $i")

                } else {
                  logging.Logger.getAnonymousLogger.info(s"[Worker] $key found no projects on page $i")
                }
              } else {
                logging.Logger.getAnonymousLogger.severe(s"[Worker] $key Failed to retrieve page $i because illegal response was given")
              }
            } catch {
              case e: Exception =>
                logging.Logger.getAnonymousLogger.severe(s"[Worker] $key Failed to retrieve page $i because $e")
                httpClient.close()
                httpClient = null
                httpClient = makeClient()
            }

          case _ => logging.Logger.getAnonymousLogger.info(s"[Worker] $key Skipping page $i")
        }
      }
      logging.Logger.getAnonymousLogger.info(s"[Worker] $key has finished and is returning to Supervisor")
      sender() ! DoneMining
  }
}
