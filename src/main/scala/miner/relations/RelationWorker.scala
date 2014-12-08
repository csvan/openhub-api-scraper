package miner.relations

import java.util.logging.Logger

import akka.actor.Actor
import database.entity.Organisation
import database.{OrganisationManager, ProjectManager}

import scala.xml.NodeSeq

/**
 * Created by christopher on 22/11/14.
 */
class RelationWorker extends Actor {

  def receive = {
    case ProcessRelation(organisation, relationXML) =>
      try {
        val projectName = (relationXML \ "name").text
        ProjectManager.findOneBy("name", projectName) match {
          case Some(project) =>
            organisation.addPortfolioProject(project)
            OrganisationManager.update(organisation)
          case None =>
            Logger.getAnonymousLogger.info(s"[Worker] Project with name $projectName does not exist")
        }
      } catch {
        case e: Exception =>
          Logger.getAnonymousLogger.severe(s"[Worker] Failed to establish a connection because: ${e.getMessage}")
      }
  }
}

case class ProcessRelation(organisation: Organisation, relationXML: NodeSeq)
