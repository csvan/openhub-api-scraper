package miner.relations.portfolio

import java.util.logging
import java.util.logging.Logger

import akka.actor.Actor
import database.entity.{Organisation, Project}
import database.{OrganisationManager, ProjectManager}

import scala.xml.{Node, NodeSeq}

/**
 * Created by christopher on 23/11/14.
 */
class PortfolioProjectWorker extends Actor {

  def receive = {
    case ProcessPortfolioProjects(organisation, relationXML) =>
      processOrganisationProjectsPage(organisation, relationXML)
  }

  def processOrganisationProjectsPage(organisation: Organisation, relationXML: NodeSeq) = {
    val projects = relationXML \\ "project"
    if (projects.length > 0) {
      projects.foreach {
        (node: Node) =>
          processRelation(organisation, node)
      }
    } else {
      logging.Logger.getAnonymousLogger.info(s"[Worker] found no Portfolio Projects to process for organisation ${organisation.getName}")
    }
  }

  def processRelation(organisation: Organisation, relationXML: NodeSeq) = {
    try {
      val projectName = (relationXML \ "name").text
      ProjectManager.findOneBy("name", projectName) match {
        case Some(project) =>
          organisation.addPortfolioProject(project)
          OrganisationManager.update(organisation)
        case None =>
          Logger.getAnonymousLogger.info(s"[Worker] Portfolio Project with name $projectName does not exist for organisation ${organisation.getName}")

          val manualProject = new Project()
          manualProject.setName(projectName)
          manualProject.setManuallyInserted(true)

          ProjectManager.persist(manualProject)

          organisation.addPortfolioProject(manualProject)
          OrganisationManager.update(organisation)
      }
    } catch {
      case e: Exception =>
        Logger.getAnonymousLogger.severe(s"[Worker] Failed to establish a connection because: ${e.getMessage}")
    }
  }
}

case class ProcessPortfolioProjects(organisation: Organisation, relationXML: NodeSeq)