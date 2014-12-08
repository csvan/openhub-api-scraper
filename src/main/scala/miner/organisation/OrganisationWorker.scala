package miner.organisation

import java.util.logging

import akka.actor.Actor
import miner.{DoneMining, HTTPAccessor}

import scala.xml.{Node, NodeSeq, XML}

/**
 * Created by christopher on 18/11/14.
 */
class OrganisationWorker extends Actor with HTTPAccessor {

  def receive = {

    case MineOrganisations(key, start, elements) =>
      logging.Logger.getAnonymousLogger.info(s"[Worker] $key received MineOrganisations message")
      var httpClient = makeClient()

      for (i ← start to (start + elements)) {

        try {
          val url = s"https://www.openhub.net/orgs.xml?api_key=$key&page=$i"
          val request = getGet(url)
          val httpResponse = httpClient.execute(request)
          val entity = httpResponse.getEntity
          if (entity != null) {

            val inputStream = entity.getContent
            val content = io.Source.fromInputStream(inputStream).getLines().mkString
            inputStream.close()

            val xml = XML.loadString(content)
            val orgs = xml \\ "org"

            if (orgs.length > 0) {
              orgs.map {
                (node: Node) =>
                  sender() ! ExtractedOrganisation(node)
                  logging.Logger.getAnonymousLogger.info(s"[Worker] $key sent org from page $i back to Supervisor")
              }

              /*
              val page = new Page()
              page.setPageNumber(i)
              PageManager.persist(page)
              */
              logging.Logger.getAnonymousLogger.info(s"[Worker] $key Succesfully processed page $i")

            } else {
              logging.Logger.getAnonymousLogger.info(s"[Worker] $key found no orgs on page $i")
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
      }
      logging.Logger.getAnonymousLogger.info(s"[Worker] $key has finished and is returning to Supervisor")
      sender() ! DoneMiningOrganisations(start + elements)
  }
}

case class MineOrganisations(key: String, start: Int, elements: Int)

case class ExtractedOrganisation(orgXML: NodeSeq)

case class DoneMiningOrganisations(lastPage: Int)

