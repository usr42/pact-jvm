package au.com.dius.pact.consumer.kotlin.dsl.block

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.kotlin.dsl.data.WithProviderBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.transformation.toPact
import au.com.dius.pact.model.RequestResponsePact

@KPactMarker
class TopLevelBlock internal constructor() {
    val consumer: TopLevelSeed = TopLevelSeed()

    fun consumer(consumer: String): WithConsumer {
        return this.consumer.withName(consumer)
    }

    class TopLevelSeed internal constructor() {
        infix fun withName(consumerName: String): WithConsumer {
            return WithConsumer(consumerName)
        }
    }

    class WithConsumer internal constructor(private val consumerName: String) {
        infix fun and(providerName: String): WithProvider {
            val pactWithProvider = ConsumerPactBuilder(consumerName).hasPactWith(providerName)
            return WithProvider(pactWithProvider)
        }

        fun hasPactWith(
            providerName: String,
            withProviderBlock: WithProviderBlock.() -> List<WithProviderBlockResult>
        ): RequestResponsePact {
            return and(providerName).havePact(withProviderBlock)
        }

        infix fun and1(providerName: String): WithProvider {
            val pactWithProvider = ConsumerPactBuilder(consumerName).hasPactWith(providerName)
            return WithProvider(pactWithProvider)
        }
    }

    class WithProvider internal constructor(private val pactDslWithProvider: PactDslWithProvider) {
        infix fun havePact(withProviderBlock: WithProviderBlock.() -> List<WithProviderBlockResult>): RequestResponsePact {
            val fromProviderToResult = WithProviderBlock().withProviderBlock()
            return pactDslWithProvider.toPact(fromProviderToResult)
        }

        infix fun isDefinedBy(withProviderBlock: WithProviderBlock.() -> List<WithProviderBlockResult>): RequestResponsePact {
            return havePact(withProviderBlock)
        }

        infix fun defined(toPact: PactDslWithProvider.() -> RequestResponsePact): RequestResponsePact {
            return pactDslWithProvider.toPact()
        }
    }
}
