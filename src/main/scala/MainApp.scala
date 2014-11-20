import akka.actor.{ActorSystem, Props}
import database.PageManager
import miner.{StartMining, SequentialSupervisor}

import scala.xml._

/**
 * Created by christopher on 18/11/14.
 */
object MainApp extends App {

  // Load the provided keys
  val keys = List("Zhpu7sifbIP1S0S51v4Tw")

  val realKeys = List("keys go here")

  val count = PageManager.count

  val system = ActorSystem("OpenHub")

  val superVisor = system.actorOf(Props(new SequentialSupervisor(realKeys)))
  superVisor ! StartMining
}
