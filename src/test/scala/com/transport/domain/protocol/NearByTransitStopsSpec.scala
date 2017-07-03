package com.transport.domain.protocol

import com.transport.domain.otp.OTPGraphService
import org.opentripplanner.common.model.GenericLocation
import org.opentripplanner.routing.core.{RoutingRequest, TraverseMode, TraverseModeSet}
import org.opentripplanner.routing.services.GraphService
import org.opentripplanner.routing.vertextype.TransitStop
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._
import scala.concurrent.Await
import scala.concurrent.duration._

class NearByTransitStopsSpec extends FlatSpec with Matchers with ScalaFutures {
  implicit val graphService: GraphService = OTPGraphService.apply
  OTPGraphService.get.waitTillAllGraphsLoaded()

  "TransportRequest" should "execute the NearByTransitStops request" in {
    val req = new RoutingRequest(new TraverseModeSet(TraverseMode.BICYCLE))
    req.routerId = "sf"
    req.from = new GenericLocation(37.771291, -122.412597) //setFromString("DNA Lounge")

    val result = Await.result(TransportRequest(req, NearbyTransitStops).execute, 10 seconds)
    val transitStops = result.asInstanceOf[java.util.List[TransitStop]].asScala.toList
    transitStops.length shouldBe 32

    transitStops.find(stop => stop.getLat == 37.790087 && stop.getLon == -122.392997) match {
      case None => fail()
      case Some(_) => //Do Nothing
    }
  }
}
