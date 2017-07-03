package com.transport.domain.router

import akka.actor.ActorSystem
import akka.testkit.TestKit
import com.transport.util.Configuration
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpecLike}
import akka.pattern.ask
import akka.util.Timeout
import com.transport.domain.otp.OTPGraphService
import com.transport.domain.protocol.{ShortestPath, TransportRequest, UnknownRequestException}
import org.opentripplanner.common.model.GenericLocation
import org.opentripplanner.routing.core.{RoutingRequest, TraverseMode, TraverseModeSet}
import org.opentripplanner.routing.spt.ShortestPathTree

import scala.concurrent.Await
import scala.concurrent.duration._

object TestConfiguration extends Configuration

class TransportRouterSpec extends TestKit(ActorSystem("testsystem", TestConfiguration.config)) with
  WordSpecLike with Matchers with ScalaFutures {
  private val router = system.actorOf(TransportRouter.props, "otp")
  implicit val timeout = Timeout(10 seconds)
  OTPGraphService.get.waitTillAllGraphsLoaded()

  "TransportRouter" must {
    "throw exception for unknown message" in {
      val unknownMsg = "Unknown Message"
      val result = Await.result(router ? unknownMsg, 1 second)
      result.isInstanceOf[UnknownRequestException] shouldBe true
      result.asInstanceOf[UnknownRequestException].msg shouldBe s"Received unknown message ${unknownMsg}"
    }

    "throw exception if request type is null" in {
      val req = TransportRequest(null, null)
      val result = Await.result(router ? req, 1 second)
      result.isInstanceOf[RuntimeException] shouldBe true
      result.asInstanceOf[RuntimeException].getMessage should startWith(s"Error occurred while processing the request: ${req}")
    }

    "calculate shortest path between 'DNA Lounge' and 'Franklin Square'" in {
      val rReq = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE))
      rReq.routerId = "sf"
      rReq.from = new GenericLocation(37.771291, -122.412597) //setFromString("DNA Lounge")
      rReq.to = new GenericLocation(37.765125, -122.409432) //setToString("Franklin Square")
      val req = TransportRequest(rReq, ShortestPath)

      val shortestPathResult = Await.result(router ? req, 10 seconds)
      shortestPathResult.isInstanceOf[ShortestPathTree] shouldBe true
      shortestPathResult.asInstanceOf[ShortestPathTree].getPaths().get(0).getWalkDistance shouldBe 1688.7588999999998
    }

    "throw exception if something is missing in the request" in {
      val rReq = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE))
      rReq.routerId = "sf"
      val req = TransportRequest(rReq, ShortestPath)

      val shortestPathResult = Await.result(router ? req, 10 seconds)
      shortestPathResult.isInstanceOf[RuntimeException] shouldBe true
      shortestPathResult.asInstanceOf[RuntimeException].getMessage should startWith(s"Error occurred while processing the request: ${req}")
    }
  }
}
