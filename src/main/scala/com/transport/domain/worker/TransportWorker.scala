package com.transport.domain.worker

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.transport.domain.Protocol.{TransportRequest, UnknownRequestException}

class TransportWorker extends Actor with ActorLogging {
  import TransportWorker.processRequest

  override def receive: Receive = {
    case req: TransportRequest => processRequest(req, sender())

    case msg =>
      val error = s"""Received unknown message ${msg}"""
      log.error(error)
      sender() ! UnknownRequestException(error)
  }
}

object TransportWorker {
  def processRequest(request: TransportRequest, sender: ActorRef): Unit = {
    //TODO: process the request
  }
}
