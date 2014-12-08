package miner.relations.outside

import java.util.logging
import java.util.logging.Logger

import akka.actor.Actor
import database.entity.{Organisation, Project}
import database.{OrganisationManager, ProjectManager}

import scala.xml.{Node, NodeSeq}

/**
 * Created by christopher on 23/11/14.
 */
class OutsideProjectWorker extends Actor {

  def receive = {
    case ProcessOutsideProjects(organisation, relationXML) =>
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
      logging.Logger.getAnonymousLogger.info(s"[Worker] found no Outside Projects to process for organisation ${organisation.getName}")
    }
  }

  def processRelation(organisation: Organisation, relationXML: NodeSeq) = {
    try {
      val projectName = (relationXML \ "name").text
      ProjectManager.findOneBy("name", projectName) match {
        case Some(project) =>
          organisation.addOutsideProject(project)
          OrganisationManager.update(organisation)
        case None =>
          Logger.getAnonymousLogger.info(s"[Worker] Outside Project with name $projectName does not exist for organisation ${organisation.getName}")

          val manualProject = new Project()
          manualProject.setName(projectName)
          manualProject.setManuallyInserted(true)

          ProjectManager.persist(manualProject)

          organisation.addOutsideProject(manualProject)
          OrganisationManager.update(organisation)
      }
    } catch {
      case e: Exception =>
        Logger.getAnonymousLogger.severe(s"[Worker] Failed to establish a connection because: ${e.getMessage}")
    }
  }
}

case class ProcessOutsideProjects(organisation: Organisation, relationXML: NodeSeq)