package com.transport.domain.worker

import akka.actor.{Actor, ActorLogging}

class TransportWorker extends Actor with ActorLogging {
  override def receive: Receive = {
    case _ =>
      val msg = s"""${self.path}: Received your (${sender()}) work"""
      log.debug(msg)
      sender() ! msg
  }
}
