package com.transport

import java.io.File

import akka.actor.ActorSystem
import akka.event.Logging
import com.transport.domain.router.TransportRouter
import com.transport.util.Configuration
import org.opentripplanner.standalone.{CommandLineParameters, OTPMain}

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App with Configuration {
  implicit val actorSystem = ActorSystem("transport-actor-system")
  private val logger = Logging(actorSystem, getClass)

  private val router = actorSystem.actorOf(TransportRouter.props, "otp")

  createGraphObj()

  //router ! "work"

  sys addShutdownHook {
    logger.debug("shutting down the transport-actor-system")
    Await.result(actorSystem.terminate(), 10 seconds)
  }

  private def createGraphObj() = {
    val graphBuildPath = config.getString("com.transport.otp.graph.build.path")
    val graphBuildDir = new File(graphBuildPath)

    val params = new CommandLineParameters()
    params.build = graphBuildDir
    val main = new OTPMain(params)
    main.run()
  }
}
