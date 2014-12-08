package miner.organisation

import java.util.logging

import akka.actor.{Actor, Props}
import miner.{RotatingList, DoneMining, StartMining}

/**
 * Created by christopher on 22/11/14.
 */
class OrganisationSupervisor(keys: List[String]) extends Actor {

  val keyLoop = new RotatingList[String](keys)

  var worker = context.actorOf(Props[OrganisationWorker], name = "worker")

  def receive = {

    case DoneMiningOrganisations(last) =>
      logging.Logger.getAnonymousLogger.info(s"[Supervisor] has received DoneMining message - last processed page was $last")

      worker ! MineOrganisations(keyLoop.next(), last+1, last+5)

    case StartMining =>
      logging.Logger.getAnonymousLogger.info(s"[Supervisor] has received StartMining message")
      worker ! MineOrganisations(keyLoop.next(), 1, 5)

    case ExtractedOrganisation(xml) =>
      OrganisationXmlProcessor.extractOrganisation(xml)
  }
}
