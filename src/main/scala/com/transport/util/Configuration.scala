package com.transport.util

import com.typesafe.config.ConfigFactory

trait Configuration {
  lazy val config = {
    val fallback = ConfigFactory.defaultReference()
    ConfigFactory.load().withFallback(fallback)
  }
}
