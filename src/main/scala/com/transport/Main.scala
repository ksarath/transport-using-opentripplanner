package com.transport

import akka.actor.ActorSystem
import akka.event.Logging
import com.transport.domain.router.TransportRouter

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {
  implicit val actorSystem = ActorSystem("transport-actor-system")
  private val logger = Logging(actorSystem, getClass)

  private val router = actorSystem.actorOf(TransportRouter.props, "otp")

  router ! "work"

  sys addShutdownHook {
    logger.debug("shutting down the transport-actor-system")
    Await.result(actorSystem.terminate(), 10 seconds)
  }
}
