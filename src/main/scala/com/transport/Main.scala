package com.transport

import akka.actor.ActorSystem

object Main extends App {
  implicit val actorSystem = ActorSystem("com.transport.actor-system")
}
