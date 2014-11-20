package miner

import java.util.logging

import akka.actor.{Actor, Props}

import scala.xml.NodeSeq

/**
 * Created by christopher on 19/11/14.
 */
class SequentialSupervisor(keys: List[String]) extends Actor {

  val pagesToProcess = 950
  var increment = 1
  var currentKey = 0

  var worker = context.actorOf(Props[Worker], name = "worker")

  def receive = {

    case DoneMining =>
      logging.Logger.getAnonymousLogger.info(s"[Supervisor] has received DoneMining message")

      increment += 950

      // Loop the keys if necessary
      if (currentKey >= keys.length - 1) {
        currentKey = 0
      }
      else {
        currentKey += 1
      }

      worker ! MinePages(keys(currentKey), increment, pagesToProcess)

    case StartMining =>
      logging.Logger.getAnonymousLogger.info(s"[Supervisor] has received StartMining message")
      worker ! MinePages(keys(currentKey), increment, pagesToProcess)

    case ExtractedProject(xml) =>
      ProjectXmlProcessor.extractedProject(xml)
  }
}

case class StartMining()

case class DoneMining()

case class MinePages(key: String, start: Int, elements: Int)

case class ExtractedProject(projectXML: NodeSeq)
