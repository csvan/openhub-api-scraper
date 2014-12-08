package miner.project

import java.util.logging

import akka.actor.Actor
import database.PageManager
import database.entity.Page
import miner.{DoneMining, HTTPAccessor}

import scala.xml.{Node, NodeSeq, XML}

/**
 * Created by christopher on 18/11/14.
 */
class ProjectWorker extends Actor with HTTPAccessor {

  def receive = {

    case MineProjects(key, start, elements) =>

      var break = false

      logging.Logger.getAnonymousLogger.info(s"[ProjectWorker] $key received MineProjects($key, $start, $elements) message")
      var httpClient = makeClient()

      for (i ← start to (start + elements)) {

        if (!break) {

          // Check so that we have not already checked this page
          PageManager.findOneBy("pageNumber", i.toString) match {
            case None =>
              try {
                val url = s"https://www.openhub.net/projects.xml?api_key=$key&page=$i"
                val request = getGet(url)
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
                        //logging.Logger.getAnonymousLogger.info(s"[ProjectWorker] $key sent project from page $i back to Supervisor")
                    }

                    val page = new Page()
                    page.setPageNumber(i)
                    PageManager.persist(page)
                    logging.Logger.getAnonymousLogger.info(s"[ProjectWorker] $key Succesfully processed page $i")

                  } else {
                    logging.Logger.getAnonymousLogger.info(s"[ProjectWorker] $key found no projects on page $i")
                    break = true
                    sender() ! DoneMiningProjects(i)
                  }
                } else {
                  logging.Logger.getAnonymousLogger.severe(s"[ProjectWorker] $key Failed to retrieve page $i because illegal response was given")
                }
              } catch {
                case e: Exception =>
                  logging.Logger.getAnonymousLogger.severe(s"[ProjectWorker] $key Failed to retrieve page $i because $e")
                  httpClient.close()
                  httpClient = null
                  httpClient = makeClient()
              }

            case _ => logging.Logger.getAnonymousLogger.info(s"[ProjectWorker] $key Skipping page $i")
          }
        }
      }
      logging.Logger.getAnonymousLogger.info(s"[ProjectWorker] $key has finished and is returning to Supervisor")

      httpClient.close()
      httpClient = null

      if (!break) {
        sender() ! DoneMiningProjects(start + elements)
      }
  }
}

case class MineProjects(key: String, start: Int, elements: Int)

case class ExtractedProject(projectXML: NodeSeq)

case class DoneMiningProjects(lastPage: Int)