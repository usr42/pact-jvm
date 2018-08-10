package au.com.dius.pact.consumer.kotlin.dsl.block

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.kotlin.dsl.data.KPactResult
import au.com.dius.pact.consumer.kotlin.dsl.data.WithProviderBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.transformation.toPact

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
        infix fun andProvider(providerName: String): WithProvider {
            val pactWithProvider = ConsumerPactBuilder(consumerName).hasPactWith(providerName)
            return WithProvider(pactWithProvider)
        }

        fun hasPactWith(
            providerName: String,
            withProviderBlock: WithProviderBlock.() -> List<WithProviderBlockResult>
        ): KPactResult {
            return andProvider(providerName).havePact(withProviderBlock)
        }
    }

    class WithProvider internal constructor(private val pactDslWithProvider: PactDslWithProvider) {
        infix fun havePact(withProviderBlock: WithProviderBlock.() -> List<WithProviderBlockResult>): KPactResult {
            val fromProviderToResult = WithProviderBlock().withProviderBlock()
            val pact = pactDslWithProvider.toPact(fromProviderToResult)
            return KPactResult(pact)
        }
    }
}
