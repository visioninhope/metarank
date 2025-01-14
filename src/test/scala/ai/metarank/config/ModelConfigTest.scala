package ai.metarank.config

import ai.metarank.config.BoosterConfig.XGBoostConfig
import ai.metarank.ml.rank.LambdaMARTRanker.LambdaMARTConfig
import cats.data.NonEmptyList
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import ai.metarank.model.Key.FeatureName

class ModelConfigTest extends AnyFlatSpec with Matchers {
  it should "parse default options for xgboost" in {
    val yaml =
      """type: xgboost
        |seed: 0""".stripMargin
    val decoded = io.circe.yaml.parser.parse(yaml).flatMap(_.as[BoosterConfig])
    decoded shouldBe Right(XGBoostConfig(seed = 0))
  }

  it should "parse options for xgboost" in {
    val yaml =
      """type: xgboost
        |iterations: 200
        |learningRate: 0.2
        |ndcgCutoff: 5
        |maxDepth: 7
        |seed: 0
        |sampling: 0.8""".stripMargin
    val decoded = io.circe.yaml.parser.parse(yaml).flatMap(_.as[BoosterConfig])
    decoded shouldBe Right(
      XGBoostConfig(seed = 0, iterations = 200, learningRate = 0.2, ndcgCutoff = 5, maxDepth = 7, sampling = 0.8)
    )
  }

  it should "decode minimal lambdamart model config" in {
    val yaml =
      """
        |type: lambdamart
        |features: [foo]""".stripMargin
    val decoded = io.circe.yaml.parser.parse(yaml).flatMap(_.as[ModelConfig])
    decoded shouldBe Right(
      LambdaMARTConfig(
        backend = XGBoostConfig(),
        features = NonEmptyList.one(FeatureName("foo")),
        weights = Map.empty
      )
    )
  }

}
