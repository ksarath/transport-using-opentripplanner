package com.transport.domain.Protocol

import org.opentripplanner.routing.core.RoutingRequest

sealed trait RequestType
case object ShortestPath extends RequestType
case object NearbyTransitStops extends RequestType
case object ComputeTraversalCost extends RequestType
case object TripPlan extends RequestType

case class UnknownRequestException(msg: String) extends RuntimeException

case class TransportRequest(request: RoutingRequest, `type`: RequestType)
