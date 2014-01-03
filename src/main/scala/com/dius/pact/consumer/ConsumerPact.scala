package com.dius.pact.consumer

import com.dius.pact.author._
import akka.actor.ActorSystem
import com.dius.pact.author.PactServerConfig
import com.dius.pact.model.Pact
import scala.concurrent.Future

case class ConsumerPact(pact: Pact) {
  def runConsumer(config: PactServerConfig, state: String)(test: => Boolean)(implicit system: ActorSystem = ActorSystem()): Future[PactVerification.VerificationResult] = {
    implicit val executionContext = system.dispatcher

    for {
      started <- MockServiceProvider(config, pact).start
      inState <- started.enterState(state)
      result = test
      actualInteractions <- inState.interactions
      verified = PactVerification(pact.interactions, actualInteractions)(result)
      fileWrite = PactGeneration(pact, verified)
      stopped <- inState.stop
    } yield { verified }
  }
}

object ConsumerPact {
  implicit def pimp(pact: Pact) = ConsumerPact(pact)
}
