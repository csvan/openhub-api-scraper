package miner

import akka.actor.{Actor, Props}
import akka.routing.RoundRobinRouter

/**
 * Created by christopher on 18/11/14.
 */
class MultiSupervisor(keys: List[String]) extends Actor {

  val workerRouter = context.actorOf(
    Props[Worker].withRouter(RoundRobinRouter(keys.length)), name = "workerRouter")
  var success = 0
  var fail = 0

  def receive = {

    case StartMining =>
      var increment = 1
      for (i ← 0 until keys.length) {
        workerRouter ! MinePages(keys(i), i + increment, 900)
        increment += 1000
      }

    case ExtractedProject(xml) =>
      ProjectXmlProcessor.extractedProject(xml)
  }
}