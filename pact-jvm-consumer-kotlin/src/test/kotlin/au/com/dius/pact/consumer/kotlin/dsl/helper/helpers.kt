package au.com.dius.pact.consumer.kotlin.dsl.helper

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.dsl.PactDslRequestWithPath
import au.com.dius.pact.consumer.dsl.PactDslResponse
import au.com.dius.pact.model.RequestResponsePact
import au.com.dius.pact.consumer.kotlin.dsl.data.GivenBlockResult
import org.assertj.core.api.Assertions.assertThat

fun assertGivenBlockResultsAreEqual(
    actual: List<GivenBlockResult>,
    expected: List<GivenBlockResult>
) {
    assertThat(actual).hasSize(expected.size)
    repeat(actual.size) {
        assertGivenBlockResultAreEqual(actual[it], expected[it])
    }
}

private fun assertGivenBlockResultAreEqual(
    actual: GivenBlockResult,
    expected: GivenBlockResult
) {
    assertThat(actual.requestDescription).isEqualTo(expected.requestDescription)
    assertThat(actual.path).isEqualTo(expected.path)
    assertThatRequestLambdasAreEqual(actual.requestDetails, expected.requestDetails)
    assertThatResponseLambdasAreEqual(actual.responseDetails, expected.responseDetails)
}

private fun assertThatRequestLambdasAreEqual(
    actualLambdas: PactDslRequestWithPath.() -> PactDslRequestWithPath,
    expectedLambda: PactDslRequestWithPath.() -> PactDslRequestWithPath
) {
    val actual = requestResponsePactFrom(actualLambdas)
    val expected = requestResponsePactFrom(expectedLambda)
    assertThat(actual).isEqualTo(expected)
}

private fun requestResponsePactFrom(expectedLambda: PactDslRequestWithPath.() -> PactDslRequestWithPath): RequestResponsePact {
    return pactDslRequestWithPath().expectedLambda().toPact()
}

private fun assertThatResponseLambdasAreEqual(
    actualLambda: PactDslResponse.() -> PactDslResponse,
    expectedLambda: PactDslResponse.() -> PactDslResponse
) {
    val actual = pactDslResponse().actualLambda().toPact()
    val expected = pactDslResponse().expectedLambda().toPact()
    assertThat(actual).isEqualTo(expected)
}

private fun PactDslRequestWithPath.toPact(): RequestResponsePact {
    return this.willRespondWith()
        .toPact()
}

private fun pactDslResponse(): PactDslResponse = pactDslRequestWithPath().willRespondWith()

private fun pactDslRequestWithPath(): PactDslRequestWithPath {
    return ConsumerPactBuilder(CONSUMER_NAME)
        .hasPactWith(PROVIDER_NAME)
        .given(GIVEN_STATE_1)
        .uponReceiving(REQUEST_DESCRIPTION_1)
        .path(PATH)
}
