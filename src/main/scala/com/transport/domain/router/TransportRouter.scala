package com.transport.domain.router

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.FromConfig
import com.transport.domain.worker.TransportWorker

class TransportRouter extends Actor with ActorLogging {
  private val router = context.actorOf(FromConfig.props(Props[TransportWorker]), "transport-router")

  override def receive: Receive = {
    case msg => router.tell(msg, sender())
  }
}

object TransportRouter {
  val props: Props = Props(classOf[TransportRouter])
}
