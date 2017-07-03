package com.transport.domain.otp

import java.io.File

import com.transport.util.Configuration
import org.opentripplanner.routing.impl.{GraphScanner, InputStreamGraphSource}
import org.opentripplanner.routing.services.GraphService

import scala.collection.JavaConverters._

class OTPGraphService extends Configuration {
  private val graphDirPath = config.getString("com.transport.otp.graph.dir")
  private val routerIds = config.getStringList("com.transport.otp.graph.routerIds")
  private val graphDir = new File(graphDirPath)

  private val graphService = initializeGraphService(graphDir)
  loadGraphs(graphService, graphDir, routerIds)

  private def initializeGraphService(graphDir: File) = {
    val graphSourceFactory = new InputStreamGraphSource.FileFactory(graphDir)
    graphSourceFactory.basePath = graphDir

    val graphService = new GraphService(true)
    graphService.graphSourceFactory = graphSourceFactory

    graphService
  }

  private def loadGraphs(graphService: GraphService, graphDir: File, routerIds: java.util.List[String]) = {
    val graphScanner = new GraphScanner(graphService, graphDir, true)
    graphScanner.basePath = graphDir
    if (routerIds.size() > 0) {
      graphScanner.defaultRouterId = routerIds.get(0)
    }
    graphScanner.autoRegister = routerIds
    graphScanner.startup()
  }

  def waitTillAllGraphsLoaded(): Unit = {
    waitTillAllGraphsLoaded(this.routerIds.asScala.toList)
  }

  private def waitTillAllGraphsLoaded(routers: List[String]): Unit = {
    if( routers.forall(r => graphService.getRouter(r) != null) ) return

    waitTillAllGraphsLoaded(routers)
  }
}

object OTPGraphService {
  private val graphService = new OTPGraphService()

  //TODO: Do we need to initialize multiple graph services OR single graph service?
  def apply: GraphService = graphService.graphService

  def get: OTPGraphService = graphService
}
