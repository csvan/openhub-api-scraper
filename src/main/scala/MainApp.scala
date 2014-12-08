import akka.actor.{ActorSystem, Props}
import database.{ProjectManager, PageManager}
import miner.organisation.OrganisationSupervisor
import miner.{SequentialSupervisor, XMLRetriever, StartMining}
import miner.relations.RelationSupervisor


/**
 * Created by christopher on 18/11/14.
 */
object MainApp extends App {

  // Load the provided keys
  val keys = List("Zhpu7sifbIP1S0S51v4Tw")

  val realKeys = List("Put your keys here")

  // Warm up the DL
  val count = PageManager.count

  val system = ActorSystem("OpenHub")

  // Use any (or all) of the below in order to mine full or partial data

  //val superVisor = system.actorOf(Props(new OrganisationSupervisor(keys.reverse)))
  //superVisor ! StartMining

  //val superVisor = system.actorOf(Props(new SequentialSupervisor(realKeys.reverse)))
  //superVisor ! StartMining

  //val superVisor = system.actorOf(Props(new RelationSupervisor(realKeys)))
  //superVisor ! StartMining

  //Fixer.startFixing()
}

object Fixer extends XMLRetriever {

  def startFixing(): Unit = {

    val client = makeClient()

    for (i <- 0 to 614604) ProjectManager.find(i) match {
      case Some(project) =>
      case None =>
        println(s"Project $i does not exist")
    }
  }
}
