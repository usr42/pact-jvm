package au.com.dius.pact.consumer.kotlin.dsl

import au.com.dius.pact.consumer.kotlin.dsl.block.WithProviderBlock
import au.com.dius.pact.consumer.kotlin.dsl.data.GivenBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.helper.GIVEN_STATE_1
import au.com.dius.pact.consumer.kotlin.dsl.helper.GIVEN_STATE_2
import au.com.dius.pact.consumer.kotlin.dsl.helper.PATH
import au.com.dius.pact.consumer.kotlin.dsl.helper.REQUEST_DESCRIPTION_1
import au.com.dius.pact.consumer.kotlin.dsl.helper.REQUEST_DESCRIPTION_2
import au.com.dius.pact.consumer.kotlin.dsl.helper.assertGivenBlockResultsAreEqual
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class WithProviderBlockTest {
    @Test
    fun withProviderBlock() {
        val withProviderBlock = WithProviderBlock()
        val actual = withProviderBlock.given providerIsInState GIVEN_STATE_1 then {
            whenever receiving REQUEST_DESCRIPTION_1 withPath PATH thenRespondWith KPact.EmptyResponse
        }

        val expectedGivenBlockResults = listOf(
            GivenBlockResult(
                REQUEST_DESCRIPTION_1,
                PATH,
                KPact.EmptyRequest,
                KPact.EmptyResponse
            )
        )

        assertThat(actual).hasSize(1)
        val actualResult = actual.first()
        assertThat(actualResult.providerState).isEqualTo(GIVEN_STATE_1)
        assertGivenBlockResultsAreEqual(actualResult.givenBlockResults, expectedGivenBlockResults)
    }

    @Test
    fun withProviderBlockTwoGiven() {
        val withProviderBlock = WithProviderBlock()
        withProviderBlock.given providerIsInState GIVEN_STATE_1 then {
            whenever receiving REQUEST_DESCRIPTION_1 withPath PATH thenRespondWith KPact.EmptyResponse
        }
        val actual = withProviderBlock.given providerIsInState GIVEN_STATE_2 then {
            whenever receiving REQUEST_DESCRIPTION_2 withPath PATH thenRespondWith KPact.EmptyResponse
        }

        val givenBlockResult = GivenBlockResult(
            REQUEST_DESCRIPTION_1,
            PATH,
            KPact.EmptyRequest,
            KPact.EmptyResponse
        )

        assertThat(actual).hasSize(2)

        val firstActualResult = actual[0]
        assertThat(firstActualResult.providerState).isEqualTo(GIVEN_STATE_1)
        assertGivenBlockResultsAreEqual(firstActualResult.givenBlockResults, listOf(givenBlockResult))

        val secondActualResult = actual[1]
        assertThat(secondActualResult.providerState).isEqualTo(GIVEN_STATE_2)
        assertGivenBlockResultsAreEqual(
            secondActualResult.givenBlockResults,
            listOf(givenBlockResult.copy(requestDescription = REQUEST_DESCRIPTION_2))
        )
    }
}
