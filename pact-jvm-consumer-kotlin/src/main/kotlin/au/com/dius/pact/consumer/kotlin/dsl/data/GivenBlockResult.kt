package au.com.dius.pact.consumer.kotlin.dsl.data

import au.com.dius.pact.consumer.dsl.PactDslRequestWithPath
import au.com.dius.pact.consumer.dsl.PactDslResponse

data class GivenBlockResult internal constructor(
    internal val requestDescription: String,
    internal val path: String,
    internal val requestDetails: PactDslRequestWithPath.() -> PactDslRequestWithPath,
    internal val responseDetails: PactDslResponse.() -> PactDslResponse
)
