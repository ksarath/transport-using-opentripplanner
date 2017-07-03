package com.transport.domain.protocol

import com.transport.util.Configuration
import org.opentripplanner.api.resource.GraphPathToTripPlanConverter
import org.opentripplanner.routing.algorithm.AStar
import org.opentripplanner.routing.core.RoutingRequest
import org.opentripplanner.routing.services.GraphService
import org.opentripplanner.routing.spt.GraphPath

import scala.collection.JavaConversions._
import scala.concurrent.Future

case class TransportRequest(request: RoutingRequest, `type`: RequestType) {
  import scala.concurrent.ExecutionContext.Implicits.global

  def execute()(implicit graphService: GraphService): Future[Any] = Future {
    `type` execute request
  }
}

private[protocol] trait RequestType {
  //TODO: Add assert conditions / validate method for request

  def execute(request: RoutingRequest)(implicit graphService: GraphService): Any
}
case object ShortestPath extends RequestType {
  //TODO: Add assert conditions / validate method for request

  override def execute(request: RoutingRequest)(implicit graphService: GraphService): Any = {
    request.setRoutingContext(graphService.getRouter(request.routerId).graph)
    new AStar().getShortestPathTree(request)
  }
}
case object NearbyTransitStops extends RequestType with Configuration {
  private val radiusMeters = config.getDouble("com.transport.otp.graph.transit.nearby.radius")

  //TODO: Add assert conditions / validate method for request

  override def execute(request: RoutingRequest)(implicit graphService: GraphService): Any = {
    val graph = graphService.getRouter(request.routerId).graph
    graph.streetIndex.getNearbyTransitStops(request.from.getCoordinate, radiusMeters)
  }
}

case class TripPlan(paths: List[GraphPath]) extends RequestType {
  //TODO: Add assert conditions / validate method for request

  override def execute(request: RoutingRequest)(implicit graphService: GraphService): Any = {
    GraphPathToTripPlanConverter.generatePlan(paths, request)
  }
}

case class UnknownRequestException(msg: String) extends RuntimeException
