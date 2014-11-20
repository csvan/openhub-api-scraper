package database

import database.entity.IEntity

/**
 * Created by christopher on 09/09/14.
 * <p/>
 * Defines a Data Access Object for a certain entity E, identified by primary integer P.
 */
trait Manager[T <: IEntity] {

  def persist(t: T)

  def remove(id: Int)

  def update(t: T)

  def find(id: Int): Option[T]

  def range(maxResults: Int, firstResult: Int): Option[List[T]]

  def all: Option[List[T]]

  def count: Int
}
