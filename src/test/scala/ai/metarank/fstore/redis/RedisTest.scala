package ai.metarank.fstore.redis

import ai.metarank.config.StateStoreConfig.RedisStateConfig.{CacheConfig, PipelineConfig}
import ai.metarank.fstore.redis.client.RedisClient
import cats.effect.unsafe.implicits.global
import org.scalatest.{BeforeAndAfterAll, Suite}

import scala.concurrent.duration._

trait RedisTest extends BeforeAndAfterAll { this: Suite =>
  def cache        = CacheConfig(0, 0.seconds)
  def pipeline     = PipelineConfig(1, 0.second)
  lazy val client  = RedisClient.create("localhost", 6379, 0, pipeline).allocated.unsafeRunSync()._1
  lazy val client2 = RedisClient.create("localhost", 6379, 1, pipeline).allocated.unsafeRunSync()._1

  override def beforeAll() = {
    super.beforeAll()
    client.reader.flushall().get()
    client2.reader.flushall().get()
  }

  override def afterAll() = {
    client.lettuce.close()
    client2.lettuce.close()
  }

}