package au.com.dius.pact.consumer.kotlin.dsl.data

data class WithProviderBlockResult internal constructor(
    internal val providerState: String,
    internal val givenBlockResults: List<GivenBlockResult>
)
