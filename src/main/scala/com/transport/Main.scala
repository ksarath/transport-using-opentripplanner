package com.transport

import java.io.File

import akka.actor.ActorSystem
import akka.event.Logging
import com.transport.domain.protocol.{ShortestPath, TransportRequest}
import com.transport.domain.router.TransportRouter
import com.transport.util.Configuration
import org.opentripplanner.common.model.GenericLocation
import org.opentripplanner.routing.core.{RoutingRequest, TraverseMode, TraverseModeSet}
import org.opentripplanner.standalone.{CommandLineParameters, OTPMain}
import akka.pattern.ask
import akka.util.Timeout
import com.transport.domain.otp.OTPGraphService

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App with Configuration {
  implicit val actorSystem = ActorSystem("transport-actor-system")
  implicit val timeout = Timeout(10 seconds)
  private val logger = Logging(actorSystem, getClass)

  createGraphObj()

  /*private val router = actorSystem.actorOf(TransportRouter.props, "otp")
  OTPGraphService.get.waitTillAllGraphsLoaded

  val req = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE))
  req.routerId = "sf"
  req.from = new GenericLocation(37.771291, -122.412597) //setFromString("DNA Lounge")
  req.to = new GenericLocation(37.765125, -122.409432) //setToString("Franklin Square")
  val result = router ? TransportRequest(req, ShortestPath)
  println(Await.result(result, 10 seconds))
  */

  sys addShutdownHook {
    logger.debug("shutting down the transport-actor-system")
    Await.result(actorSystem.terminate(), 10 seconds)
  }

  private def createGraphObj() = {
    val graphBuildPath = config.getString("com.transport.otp.graph.build.path")
    val graphBuildDir = new File(graphBuildPath)

    val params = new CommandLineParameters()
    params.build = graphBuildDir
    params.basePath = graphBuildDir.getParentFile.getParent
    params.graphDirectory = graphBuildDir.getParentFile
    params.cacheDirectory = new File(params.basePath + "/cache")
    val main = new OTPMain(params)
    main.run()
  }
}
