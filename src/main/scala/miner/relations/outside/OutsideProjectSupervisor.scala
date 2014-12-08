package miner.relations.outside

import java.util.logging

import akka.actor.Props
import database.OrganisationManager
import database.entity.Organisation
import miner._

/**
 * Created by christopher on 22/11/14.
 */
class OutsideProjectSupervisor(keys: List[String]) extends Supervisor with XMLRetriever {

  val keyRotator = new RotatingList[String](keys)

  val worker = context.actorOf(Props(new OutsideProjectWorker()))

  def receive = {
    case StartSupervisor =>
      createBindings()
  }

  def createBindings() = {

    OrganisationManager.all match {
      case Some(organisations) =>
        organisations.foreach((organisation) => {
          println(organisation.getHtmlUrl)
          processOrganisation(organisation, keyRotator.next())
        })
    }
  }

  def processOrganisation(organisation: Organisation, key: String): Unit = {

    logging.Logger.getAnonymousLogger.info(s"[Worker] Processing organisation ${organisation.getName}")

    var client = makeClient()

    // Get the main page of the portfolio relations
    var url = organisation.getHtmlUrl + "/outside_projects.xml" + s"?api_key=$key"
    var xml = getXML(url, client)

    // Check how many pages we will need to go through
    var pages = 0
    try {
      pages = ((xml \\ "items_available").text.toInt / 20) + 1
    } catch {
      case e: Exception =>
        pages = 1
    }

    worker ! ProcessOutsideProjects(organisation, xml)

    if (pages > 1) {
      for (i ← 2 to pages) {
        url = organisation.getHtmlUrl + "/outside_projects.xml" + s"?api_key=$key&page=$i"
        xml = getXML(url, client)
        worker ! ProcessOutsideProjects(organisation, xml)
      }
    }

    client.close()
    client = null
  }
}