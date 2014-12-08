package miner.project

import java.text.SimpleDateFormat
import java.util.logging

import database._
import database.entity._

import scala.xml._

/**
 * Created by christopher on 19/11/14.
 */
object ProjectXmlProcessor {

  def extractProject(projectXML: NodeSeq) = {

    try {

      val project = new Project()

      try {
        project.setName((projectXML \ "name").text)
      } catch {
        case e: Exception =>
          project.setName("NOT FOUND")
      }

      try {
        project.setDescription((projectXML \ "description").text)
      } catch {
        case e: Exception =>
          project.setDescription("NOT FOUND")
      }

      try {
        project.setDownloadUrl((projectXML \ "download_url").text)
      } catch {
        case e: Exception =>
          project.setDownloadUrl("NOT FOUND")
      }

      try {
        project.setHomepageUrl((projectXML \ "homepage_url").text)
      } catch {
        case e: Exception =>
          project.setHomepageUrl("NOT FOUND")
      }

      try {
        project.setHtmlUrl((projectXML \ "html_url").text)
      } catch {
        case e: Exception =>
          project.setHtmlUrl("NOT FOUND")
      }

      try {
        project.setSmallLogoUrl((projectXML \ "small_logo_url").text)
      } catch {
        case e: Exception =>
          project.setSmallLogoUrl("NOT FOUND")
      }

      try {
        project.setMediumLogoUrl((projectXML \ "medium_logo_url").text)
      } catch {
        case e: Exception =>
          project.setMediumLogoUrl("NOT FOUND")
      }

      try {
        project.setId((projectXML \ "id").text.toInt)
      } catch {
        case e: Exception =>
          project.setId(-1)
      }

      try {
        project.setUserCount((projectXML \ "user_count").text.toInt)
      } catch {
        case e: Exception =>
          project.setUserCount(-1)
      }

      try {
        project.setRatingCount((projectXML \ "rating_count").text.toInt)
      } catch {
        case e: Exception =>
          project.setRatingCount(-1)
      }

      try {
        project.setReviewCount((projectXML \ "review_count").text.toInt)
      } catch {
        case e: Exception =>
          project.setReviewCount(-1)
      }

      try {
        project.setAverageRating((projectXML \ "average_rating").text.toFloat)
      } catch {
        case e: Exception =>
          project.setAverageRating(-1)
      }

      // Set the dates
      val formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
      val created = formatter.parse((projectXML \ "created_at").text)
      val modified = formatter.parse((projectXML \ "updated_at").text)

      project.setCreatedAt(created)
      project.setUpdatedAt(modified)

      ProjectManager.persist(project)

      // Always try to find existing fields first
      (projectXML \\ "tag").foreach((node: Node) => {
        val tagTitle = node.text
        TagManager.findOneBy("title", tagTitle) match {
          case Some(tag) =>
            project.addTag(tag)
          case None =>
            val tag = new Tag()
            tag.setTitle(tagTitle)
            TagManager.persist(tag)
            project.addTag(tag)
        }
      })

      (projectXML \\ "license").foreach((node: NodeSeq) => {
        val name = (node \ "name").text
        LicenseManager.findOneBy("name", name) match {
          case Some(license) =>
            project.addLicense(license)
          case None =>
            val licence = new License()
            licence.setName(name)
            licence.setNiceName((node \ "nice_name").text)
            LicenseManager.persist(licence)
            project.addLicense(licence)
        }
      })

      (projectXML \\ "link").foreach((node: NodeSeq) => {
        val url = (node \ "url").text
        LinkManager.findOneBy("url", url) match {
          case Some(link) =>
            project.addLink(link)
          case None =>
            val link = new Link()
            link.setUrl(url)
            link.setTitle((node \ "title").text)

            val categoryTitle = (node \ "category").text
            CategoryManager.findOneBy("title", categoryTitle) match {
              case Some(category) =>
                link.setCategory(category)

              case None =>
                val category = new Category()
                category.setTitle(categoryTitle)
                CategoryManager.persist(category)
                link.setCategory(category)
            }

            LinkManager.persist(link)
            project.addLink(link)
        }
      })

      ProjectManager.update(project)

      logging.Logger.getAnonymousLogger.fine(s"[Supervisor] succesfully added Project with id ${project.getId}")
      //logging.Logger.getAnonymousLogger.fine(s"[Supervisor] Total projects processed: ${success += 1}")

    } catch {
      case e: Exception =>
        var stringified = projectXML.toString()
        logging.Logger.getAnonymousLogger.severe(s"[Supervisor] failed to process project because: ${e.getMessage()}")
      //logging.Logger.getAnonymousLogger.fine(s"[Supervisor] Total projects failed: ${fail += 1}")
    }
  }
}
