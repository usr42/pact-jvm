package au.com.dius.pact.consumer.kotlin.dsl

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.dsl.PactDslRequestWithPath
import au.com.dius.pact.consumer.dsl.PactDslResponse
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.kotlin.dsl.block.TopLevelBlock
import au.com.dius.pact.consumer.kotlin.dsl.block.TopLevelBlock.WithProvider
import au.com.dius.pact.consumer.kotlin.dsl.block.WithProviderBlock
import au.com.dius.pact.consumer.kotlin.dsl.data.WithProviderBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.transformation.toPact
import au.com.dius.pact.model.RequestResponsePact

fun PactDslWithProvider.kPact(function: WithProviderBlock.() -> List<WithProviderBlockResult>): RequestResponsePact {
    return WithProvider(this).havePact(function)
}

object KPact {
    val EmptyRequest: PactDslRequestWithPath.() -> PactDslRequestWithPath = { this }
    val EmptyResponse: PactDslResponse.() -> PactDslResponse = { this }

    infix fun between(consumer: String): TopLevelBlock.WithConsumer {
        return TopLevelBlock().consumer.withName(consumer)
    }

    fun consumer(consumer: String): TopLevelBlock.WithConsumer {
        return TopLevelBlock().consumer.withName(consumer)
    }
}

object by {
    operator fun invoke(withProviderBlock: WithProviderBlock.() -> List<WithProviderBlockResult>): PactDslWithProvider.() -> RequestResponsePact {
        val fromProviderToResult = WithProviderBlock().withProviderBlock()
        return {
            toPact(fromProviderToResult)
        }
    }
}

operator fun String.invoke(withProviderBlock: WithProviderBlock.() -> List<WithProviderBlockResult>): ConsumerPactBuilder.() -> RequestResponsePact {
    val fromProviderToResult = WithProviderBlock().withProviderBlock()
    return {
        hasPactWith(this@invoke).toPact(fromProviderToResult)
    }
}