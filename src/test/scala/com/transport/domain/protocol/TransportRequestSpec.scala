package com.transport.domain.protocol

import org.opentripplanner.routing.core.RoutingRequest
import org.opentripplanner.routing.services.GraphService
import org.scalatest.{FlatSpec, Matchers}
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent.Await
import scala.concurrent.duration._

class TransportRequestSpec extends FlatSpec with Matchers with ScalaFutures {
  "TransportRequest" should "execute the corresponding request" in {
    implicit val graphService: GraphService = null
    object DummyRequestType extends RequestType {
      override def execute(request: RoutingRequest)(implicit graphService: GraphService): Any = {
        "success"
      }
    }

    val request = TransportRequest(null, DummyRequestType)
    val response = Await.result(request.execute, 1 second)
    response shouldBe "success"
  }
}
