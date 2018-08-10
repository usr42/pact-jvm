package au.com.dius.pact.consumer.kotlin.dsl.block

import au.com.dius.pact.consumer.kotlin.dsl.data.GivenBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.data.WithProviderBlockResult

class WithProviderBlock internal constructor() {
    private val withProviderBlockResults = mutableListOf<WithProviderBlockResult>()

    val given: WithProviderSeed = WithProviderSeed()

    fun given(
        providerState: String,
        withGivenBlock: WithGivenBlock.() -> List<GivenBlockResult>
    ): List<WithProviderBlockResult> {
        return given.providerIsInState(providerState).then(withGivenBlock)
    }

    inner class WithProviderSeed internal constructor() {
        infix fun providerIsInState(providerState: String): WithState {
            return WithState(providerState)
        }
    }

    inner class WithState internal constructor(private val providerState: String) {
        infix fun then(withGivenBlock: WithGivenBlock.() -> List<GivenBlockResult>): List<WithProviderBlockResult> {
            val withGivenBlocks = WithGivenBlock().withGivenBlock()
            withProviderBlockResults += WithProviderBlockResult(providerState, withGivenBlocks)
            return withProviderBlockResults
        }
    }
}
