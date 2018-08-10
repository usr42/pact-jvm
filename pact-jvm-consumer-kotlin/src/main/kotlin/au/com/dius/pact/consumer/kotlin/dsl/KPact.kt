package au.com.dius.pact.consumer.kotlin.dsl

import au.com.dius.pact.consumer.dsl.PactDslRequestWithPath
import au.com.dius.pact.consumer.dsl.PactDslResponse
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.kotlin.dsl.block.TopLevelBlock
import au.com.dius.pact.consumer.kotlin.dsl.block.TopLevelBlock.WithProvider
import au.com.dius.pact.consumer.kotlin.dsl.block.WithProviderBlock
import au.com.dius.pact.consumer.kotlin.dsl.data.KPactResult
import au.com.dius.pact.consumer.kotlin.dsl.data.WithProviderBlockResult
import au.com.dius.pact.model.RequestResponsePact

fun kPact(lambda: TopLevelBlock.() -> KPactResult): RequestResponsePact {
    val mainBlock = TopLevelBlock()
    return mainBlock.lambda().pact
}

fun PactDslWithProvider.kPact(function: WithProviderBlock.() -> List<WithProviderBlockResult>): RequestResponsePact {
    val havePact = WithProvider(this).havePact(function)
    return havePact.pact
}

object KPact {
    val EmptyRequest: PactDslRequestWithPath.() -> PactDslRequestWithPath = { this }
    val EmptyResponse: PactDslResponse.() -> PactDslResponse = { this }
}
