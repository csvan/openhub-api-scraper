package database

import database.entity.IEntity
import database.manager.AbstractDAO

import scala.collection.JavaConverters._

/**
 * Created by christopher on 11/08/14.
 *
 * Wraps the Java managers in order to provide seamless Scala integration
 */
abstract class AbstractManager[T <: IEntity, M <: AbstractDAO[T]](val manager: M) extends Manager[T] {

  def persist(t: T) = manager.persist(t)

  def remove(id: Int) = manager.remove(id)

  def update(t: T) = manager.update(t)

  def count: Int = manager.count()

  def find(id: Int): Option[T] = optionResult(manager.find(id))

  private def optionResult(result: T): Option[T] = {
    if (result != null) {
      Some(result)
    } else {
      None
    }
  }

  def findOneBy(conditions: String*): Option[T] =
    optionResult(manager.findOneBy(conditions: _*))

  def all: Option[List[T]] =
    optionResultList(manager.all().asScala.toList)

  private def optionResultList(result: List[T]): Option[List[T]] = {
    if (result != null && result.nonEmpty) {
      Some(result)
    } else {
      None
    }
  }

  def range(maxResults: Int, firstResult: Int): Option[List[T]] =
    optionResultList(manager.range(maxResults, firstResult).asScala.toList)

  def findBy(conditions: String*): Option[List[T]] =
    optionResultList(manager.findBy(conditions: _*).asScala.toList)
}
