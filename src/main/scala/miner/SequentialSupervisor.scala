package miner

import java.util.logging

import akka.actor.{Actor, Props}
import miner.organisation.MineOrganisations
import miner.project._

/**
 * Created by christopher on 19/11/14.
 */
class SequentialSupervisor(keys: List[String]) extends Actor {

  val keyLoop = new RotatingList[String](keys)

  var worker = context.actorOf(Props[ProjectWorker], name = "worker")

  def receive = {

    case DoneMiningProjects(last) =>
      logging.Logger.getAnonymousLogger.info(s"[Supervisor] has received DoneMining message")
      worker ! MineProjects(keyLoop.next(), last+1, 20)

    case StartMining =>
      logging.Logger.getAnonymousLogger.info(s"[Supervisor] has received StartMining message")
      worker ! MineProjects(keyLoop.next(), 1, 10)

    case ExtractedProject(xml) =>
      ProjectXmlProcessor.extractProject(xml)
  }
}

case class StartMining()

case class DoneMining()
