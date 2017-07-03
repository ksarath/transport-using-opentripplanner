package com.transport.domain.protocol

import com.transport.domain.otp.OTPGraphService
import org.opentripplanner.common.model.GenericLocation
import org.opentripplanner.routing.core.{RoutingRequest, TraverseMode, TraverseModeSet}
import org.opentripplanner.routing.services.GraphService
import org.opentripplanner.routing.spt.ShortestPathTree
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Await
import scala.concurrent.duration._

class ShortestPathSpec extends FlatSpec with Matchers with ScalaFutures {
  implicit val graphService: GraphService = OTPGraphService.apply
  OTPGraphService.get.waitTillAllGraphsLoaded()

  "TransportRequest" should "execute the ShortestPath request" in {
    val req = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE))
    req.routerId = "sf"
    req.from = new GenericLocation(37.771291, -122.412597) //setFromString("DNA Lounge")
    req.to = new GenericLocation(37.765125, -122.409432) //setToString("Franklin Square")

    val shortestPathResult = Await.result(TransportRequest(req, ShortestPath).execute, 10 seconds)
    shortestPathResult.isInstanceOf[ShortestPathTree] shouldBe true
    shortestPathResult.asInstanceOf[ShortestPathTree].getPaths().get(0).getWalkDistance shouldBe 1688.7588999999998
  }
}
