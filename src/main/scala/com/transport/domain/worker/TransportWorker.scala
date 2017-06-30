package com.transport.domain.worker

import akka.actor.{Actor, ActorLogging}
import com.transport.domain.otp.OTPGraphService
import com.transport.domain.protocol.{TransportRequest, UnknownRequestException}
import org.opentripplanner.routing.services.GraphService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class TransportWorker extends Actor with ActorLogging {
  implicit val graphService: GraphService = OTPGraphService.apply

  override def receive: Receive = {
    case req: TransportRequest =>
      val originalSender = sender()
      req.execute.onComplete {
        case Success(result) => originalSender ! result
        case Failure(ex) =>
          val errorMsg = s"""Error occurred while processing the request: ${req}, Exception:
              ${Option(ex.getCause).map(t => s" due to ${t.getMessage}").getOrElse("")}"""
          log.error(errorMsg)

          originalSender ! new RuntimeException(errorMsg, ex)
      }

    case msg =>
      val error = s"""Received unknown message ${msg}"""
      log.error(error)
      sender() ! UnknownRequestException(error)
  }
}
