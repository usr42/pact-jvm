package au.com.dius.pact.consumer.kotlin.dsl.block

import au.com.dius.pact.consumer.dsl.PactDslRequestWithPath
import au.com.dius.pact.consumer.dsl.PactDslResponse
import au.com.dius.pact.consumer.kotlin.dsl.data.GivenBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.respond

@KPactMarker
class WithGivenBlock internal constructor() {
    private val givenBlockResults = mutableListOf<GivenBlockResult>()

    val whenever: WithGivenSeed = WithGivenSeed()

    fun whenever(requestDescription: String): WithDescription {
        return this.whenever.receiving(requestDescription)
    }

    inner class WithGivenSeed internal constructor() {
        infix fun receiving(requestDescription: String): WithDescription {
            return WithDescription(requestDescription)
        }
    }

    inner class WithDescription internal constructor(private val requestDescription: String) {
        infix fun withPath(path: String): WithPath {
            return WithPath(requestDescription, path)
        }

        fun withPath(
            path: String,
            requestDetailFunction: PactDslRequestWithPath.() -> PactDslRequestWithPath
        ): WithPath {
            return withPath(path).and(requestDetailFunction)
        }

        infix fun withPath(pair: Pair<String, PactDslRequestWithPath.() -> PactDslRequestWithPath>): WithPath {
            val (path, requestDetailFunction) = pair
            return withPath(path).and(requestDetailFunction)
        }
    }

    inner class WithPath internal constructor(
        private val requestDescription: String,
        private val path: String
    ) {
        private val requestDetails: MutableList<PactDslRequestWithPath.() -> PactDslRequestWithPath> = mutableListOf()

        infix fun and(requestDetailFunction: PactDslRequestWithPath.() -> PactDslRequestWithPath): WithPath {
            requestDetails += requestDetailFunction
            return this
        }

        infix fun thenRespondWith(responseDetails: PactDslResponse.() -> PactDslResponse): List<GivenBlockResult> {
            val accumulatedRequestDetails: PactDslRequestWithPath.() -> PactDslRequestWithPath = {
                requestDetails.fold(this) { acc, function ->
                    acc.function()
                }
            }

            val result = GivenBlockResult(
                requestDescription,
                path,
                accumulatedRequestDetails,
                responseDetails
            )
            givenBlockResults += result
            return givenBlockResults
        }

        infix fun then(@Suppress("UNUSED_PARAMETER") x: respond): RespondWrapper {
            return RespondWrapper(this)
        }
    }

    operator fun String.invoke(function: PactDslRequestWithPath.() -> PactDslRequestWithPath): Pair<String, PactDslRequestWithPath.() -> PactDslRequestWithPath> {
        return this to function
    }
}

class RespondWrapper(private val withPath: WithGivenBlock.WithPath) {
    infix fun with(responseDetails: PactDslResponse.() -> PactDslResponse): List<GivenBlockResult> {
        return withPath.thenRespondWith(responseDetails)
    }
}
