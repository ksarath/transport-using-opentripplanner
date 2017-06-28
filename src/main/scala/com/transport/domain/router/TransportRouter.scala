package com.transport.domain.router

import akka.actor.{Actor, ActorLogging, Props}
import akka.routing.FromConfig
import com.transport.domain.Protocol.{TransportRequest, UnknownRequestException}
import com.transport.domain.worker.TransportWorker

class TransportRouter extends Actor with ActorLogging {
  private val router = context.actorOf(FromConfig.props(Props[TransportWorker]), "transport-router")

  override def receive: Receive = {
    case req: TransportRequest => router.tell(req, sender())

    case msg =>
      val error = s"""Received unknown message ${msg}"""
      log.error(error)
      sender() ! UnknownRequestException(error)
  }
}

object TransportRouter {
  val props: Props = Props(classOf[TransportRouter])
}
