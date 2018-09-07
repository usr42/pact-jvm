package au.com.dius.pact.consumer.kotlin.dsl.block

import au.com.dius.pact.consumer.kotlin.dsl.data.GivenBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.data.WithProviderBlockResult

@KPactMarker
class WithProviderBlock internal constructor() {
    private val withProviderBlockResults = mutableListOf<WithProviderBlockResult>()

    val given: WithProviderSeed = WithProviderSeed()

    fun given(
        providerState: String,
        withGivenBlock: WithGivenBlock.() -> List<GivenBlockResult>
    ): List<WithProviderBlockResult> {
        return given.providerState(providerState).then(withGivenBlock)
    }

    inner class WithProviderSeed internal constructor() {
        infix fun providerState(providerState: String): WithState {
            return WithState(providerState)
        }

        infix fun providerState(toWithProviderBlockResult: WithGivenBlock.() -> WithProviderBlockResult): List<WithProviderBlockResult> {
            val withProviderBlockResult = WithGivenBlock().toWithProviderBlockResult()
            withProviderBlockResults += withProviderBlockResult
            return withProviderBlockResults
        }
    }

    inner class WithState internal constructor(private val providerState: String) {
        infix fun then(withGivenBlock: WithGivenBlock.() -> List<GivenBlockResult>): List<WithProviderBlockResult> {
            val withGivenBlocks = WithGivenBlock().withGivenBlock()
            withProviderBlockResults += WithProviderBlockResult(providerState, withGivenBlocks)
            return withProviderBlockResults
        }
    }

    operator fun String.invoke(withGivenBlock: WithGivenBlock.() -> List<GivenBlockResult>): WithGivenBlock.() -> WithProviderBlockResult {
        return {
            val list = withGivenBlock()
            WithProviderBlockResult(this@invoke, list)
        }
    }
}
