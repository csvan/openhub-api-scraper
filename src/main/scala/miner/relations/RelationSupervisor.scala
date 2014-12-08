package miner.relations

import akka.actor.Props
import miner._
import miner.relations.outside.OutsideProjectSupervisor
import miner.relations.portfolio.PortfolioProjectSupervisor

/**
 * Created by christopher on 22/11/14.
 */
class RelationSupervisor(keys: List[String]) extends Supervisor {

  val keyRotator = new RotatingList[String](keys)

  val portfolioSupervisor = context.actorOf(Props(new PortfolioProjectSupervisor(keys)))

  val outsideSupervisor = context.actorOf(Props(new OutsideProjectSupervisor(keys)))

  def receive = {
    case StartMining =>
      portfolioSupervisor ! StartSupervisor
      outsideSupervisor ! StartSupervisor
  }
}