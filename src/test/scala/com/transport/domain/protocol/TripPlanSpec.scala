package com.transport.domain.protocol

import com.transport.domain.otp.OTPGraphService
import com.vividsolutions.jts.geom.Coordinate
import org.opentripplanner.routing.core.{RoutingRequest, State, TraverseMode, TraverseModeSet}
import org.opentripplanner.routing.location.StreetLocation
import org.opentripplanner.routing.services.GraphService
import org.opentripplanner.routing.spt.GraphPath
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Await
import scala.concurrent.duration._

class TripPlanSpec extends FlatSpec with Matchers with ScalaFutures {
  implicit val graphService: GraphService = OTPGraphService.apply
  OTPGraphService.get.waitTillAllGraphsLoaded()

  "TransportRequest" should "execute the TripPlan request" in {
    val req = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE))
    req.routerId = "sf"

    val tripPlanRequest = TransportRequest(req, TripPlan(List(
      new GraphPath(new State(new StreetLocation("v1", new Coordinate(37.771291, -122.412597), "v1"), req), true),
      new GraphPath(new State(new StreetLocation("v2", new Coordinate(37.765125, -122.409432), "v2"), req), true)
    )))

    //val tripResult = Await.result(tripPlanRequest.execute, 10 seconds)
  }
}
