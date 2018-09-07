package au.com.dius.pact.consumer.kotlin.dsl.transformation

import au.com.dius.pact.consumer.dsl.PactDslRequestWithPath
import au.com.dius.pact.consumer.dsl.PactDslRequestWithoutPath
import au.com.dius.pact.consumer.dsl.PactDslResponse
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.dsl.PactDslWithState
import au.com.dius.pact.consumer.kotlin.dsl.data.GivenBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.data.WithProviderBlockResult
import au.com.dius.pact.model.RequestResponsePact

internal fun PactDslWithProvider.toPact(results: List<WithProviderBlockResult>): RequestResponsePact {
    val response = results.fold(this.asHasGivenMethod()) { hasGivenMethod, result ->
        val (providerState, givenBlockResults) = result
        hasGivenMethod.given(providerState).toResponse(givenBlockResults).asHasGiven()
    } as ResponseHasGivenMethod
    return response.pact
}

private fun PactDslWithState.toResponse(givenBlockResults: List<GivenBlockResult>): PactDslResponse {
    val responseHasUponReceivingMethod =
        givenBlockResults.fold(this.asHasUponReceiving()) { withState, givenBlockResult ->
            withState.applyGivenBlockResult(givenBlockResult)
        } as ResponseHasUponReceivingMethod
    return responseHasUponReceivingMethod.response
}

private fun HasUponReceivingMethod.applyGivenBlockResult(
    givenBlockResult: GivenBlockResult
): HasUponReceivingMethod {
    return with(givenBlockResult) {
        uponReceiving(requestDescription)
            .path(path)
            .requestDetails()
        .willRespondWith()
            .responseDetails()
        .asHasUponReceiving()
    }
}

private fun PactDslWithState.asHasUponReceiving(): HasUponReceivingMethod {
    return WithStateHasUponReceivingMethod(this)
}

private class WithStateHasUponReceivingMethod(private val withState: PactDslWithState) : HasUponReceivingMethod {
    override fun uponReceiving(description: String): HasPathMethod {
        return withState.uponReceiving(description).asHasPathMethod()
    }
}

private fun PactDslRequestWithoutPath.asHasPathMethod(): HasPathMethod {
    return WithoutPathHasPathMethod(this)
}

private class WithoutPathHasPathMethod(private val withoutPath: PactDslRequestWithoutPath) : HasPathMethod {
    override fun path(path: String): PactDslRequestWithPath {
        return withoutPath.path(path)
    }
}

private fun PactDslResponse.asHasUponReceiving(): HasUponReceivingMethod {
    return ResponseHasUponReceivingMethod(this)
}

private class ResponseHasUponReceivingMethod(val response: PactDslResponse) : HasUponReceivingMethod {
    override fun uponReceiving(description: String): HasPathMethod {
        return response.uponReceiving(description).asHasPathMethod()
    }
}

private fun PactDslRequestWithPath.asHasPathMethod(): HasPathMethod {
    return WithPathHasPathMethod(this)
}

private class WithPathHasPathMethod(private val withPath: PactDslRequestWithPath) : HasPathMethod {
    override fun path(path: String): PactDslRequestWithPath {
        return withPath.path(path)
    }
}

private interface HasUponReceivingMethod {
    fun uponReceiving(description: String): HasPathMethod
}

private interface HasPathMethod {
    fun path(path: String): PactDslRequestWithPath
}

private fun PactDslWithProvider.asHasGivenMethod(): HasGivenMethod {
    return WithProviderHasGivenMethod(this)
}

private class WithProviderHasGivenMethod(private val withProvider: PactDslWithProvider) : HasGivenMethod {
    override fun given(state: String): PactDslWithState {
        return withProvider.given(state)
    }
}

private fun PactDslResponse.asHasGiven(): HasGivenMethod {
    return ResponseHasGivenMethod(this)
}

private class ResponseHasGivenMethod(private val response: PactDslResponse) : HasGivenMethod {
    override fun given(state: String): PactDslWithState {
        return response.given(state)
    }

    val pact: RequestResponsePact
        get() = response.toPact()
}

private interface HasGivenMethod {
    fun given(state: String): PactDslWithState
}
