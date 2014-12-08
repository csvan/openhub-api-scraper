package miner.organisation

import java.text.SimpleDateFormat
import java.util.logging

import database._
import database.entity._

import scala.xml.{Node, NodeSeq}

/**
 * Created by christopher on 22/11/14.
 */
object OrganisationXmlProcessor {

  def extractOrganisation(organisationXML: NodeSeq) = {

    val name = (organisationXML \ "name").text

    OrganisationManager.findOneBy("name", name) match {

      // Organisation already exists, do not make a duplicate
      case Some(organisation) => logging.Logger.getAnonymousLogger.fine(s"[Supervisor] organisation exists: $name")

      // Extract the organisation data
      case None =>

        try {

          val organisation = new Organisation()

          try {
            organisation.setName((organisationXML \ "name").text)
          } catch {
            case e: Exception =>
              organisation.setName("NOT FOUND")
          }

          try {
            organisation.setDescription((organisationXML \ "description").text)
          } catch {
            case e: Exception =>
              organisation.setDescription("NOT FOUND")
          }

          try {
            organisation.setUrlName((organisationXML \ "url_name").text)
          } catch {
            case e: Exception =>
              organisation.setUrlName("NOT FOUND")
          }

          try {
            organisation.setUrl((organisationXML \ "url").text)
          } catch {
            case e: Exception =>
              organisation.setUrl("NOT FOUND")
          }

          try {
            organisation.setHomepageUrl((organisationXML \ "homepage_url").text)
          } catch {
            case e: Exception =>
              organisation.setHomepageUrl("NOT FOUND")
          }

          try {
            organisation.setHtmlUrl((organisationXML \ "html_url").text)
          } catch {
            case e: Exception =>
              organisation.setHtmlUrl("NOT FOUND")
          }

          try {
            organisation.setSmallLogoUrl((organisationXML \ "small_logo_url").text)
          } catch {
            case e: Exception =>
              organisation.setSmallLogoUrl("NOT FOUND")
          }

          try {
            organisation.setMediumLogoUrl((organisationXML \ "medium_logo_url").text)
          } catch {
            case e: Exception =>
              organisation.setMediumLogoUrl("NOT FOUND")
          }

          try {
            organisation.setAffiliatedCommitters((organisationXML \ "affiliated_committers").text.toInt)
          } catch {
            case e: Exception =>
              organisation.setAffiliatedCommitters(-1)
          }

          try {
            organisation.setProjectsCount((organisationXML \ "projects_count").text.toInt)
          } catch {
            case e: Exception =>
              organisation.setProjectsCount(-1)
          }

          // Always try to find existing fields first
          (organisationXML \\ "type").foreach((node: Node) => {
            val typeTitle = node.text
            OrganisationTypeManager.findOneBy("title", typeTitle) match {
              case Some(orgType) =>
                organisation.setOrganisationType(orgType)
              case None =>
                val orgType = new OrganisationType()
                orgType.setTitle(typeTitle)
                OrganisationTypeManager.persist(orgType)
                organisation.setOrganisationType(orgType)
            }
          })

          // Set the dates
          val formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
          val created = formatter.parse((organisationXML \ "created_at").text)
          val modified = formatter.parse((organisationXML \ "updated_at").text)

          organisation.setCreatedAt(created)
          organisation.setUpdatedAt(modified)

          OrganisationManager.persist(organisation)

          logging.Logger.getAnonymousLogger.fine(s"[Supervisor] succesfully added Organisation with id ${organisation.getId}")
          //logging.Logger.getAnonymousLogger.fine(s"[Supervisor] Total organisations processed: ${success += 1}")

        } catch {
          case e: Exception =>
            var stringified = organisationXML.toString()
            logging.Logger.getAnonymousLogger.severe(s"[Supervisor] failed to process organisation because: ${e.getMessage()}")
          //logging.Logger.getAnonymousLogger.fine(s"[Supervisor] Total organisations failed: ${fail += 1}")
        }
    }
  }
}

