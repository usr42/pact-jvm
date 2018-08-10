package au.com.dius.pact.consumer.kotlin.dsl

import au.com.dius.pact.consumer.kotlin.dsl.block.WithGivenBlock
import au.com.dius.pact.consumer.kotlin.dsl.data.GivenBlockResult
import au.com.dius.pact.consumer.kotlin.dsl.helper.BODY
import au.com.dius.pact.consumer.kotlin.dsl.helper.HEADER_MAP
import au.com.dius.pact.consumer.kotlin.dsl.helper.HEADER_NAME
import au.com.dius.pact.consumer.kotlin.dsl.helper.HEADER_VALUE
import au.com.dius.pact.consumer.kotlin.dsl.helper.PATH
import au.com.dius.pact.consumer.kotlin.dsl.helper.REQUEST_DESCRIPTION_1
import au.com.dius.pact.consumer.kotlin.dsl.helper.REQUEST_DESCRIPTION_2
import au.com.dius.pact.consumer.kotlin.dsl.helper.assertGivenBlockResultsAreEqual
import org.junit.jupiter.api.Test

class WithGivenBlockTest {
    @Test
    fun withGivenBlockNew() {
        val givenBlock = WithGivenBlock()
        val results = givenBlock.whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
            headers(HEADER_NAME, HEADER_VALUE)
        } thenRespondWith {
            body(BODY)
            headers(HEADER_MAP)
        }

        val expectedResults = listOf(GivenBlockResult(
            REQUEST_DESCRIPTION_1,
            PATH,
            { headers(HEADER_NAME, HEADER_VALUE) },
            {
                body(BODY)
                headers(HEADER_MAP)
            }
        ))

        assertGivenBlockResultsAreEqual(results, expectedResults)
    }

    @Test
    fun withGivenBlockNewTwoAndBlocks() {
        val givenBlock = WithGivenBlock()
        val result = givenBlock.whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
            headers(HEADER_NAME, HEADER_VALUE)
        } and {
            body(BODY)
        } thenRespondWith {
            body(BODY)
            headers(HEADER_MAP)
        }

        val expectedResult = listOf(GivenBlockResult(
            REQUEST_DESCRIPTION_1,
            PATH,
            { headers(HEADER_NAME, HEADER_VALUE); body(BODY) },
            {
                body(BODY)
                headers(HEADER_MAP)
            }
        ))

        assertGivenBlockResultsAreEqual(result, expectedResult)
    }

    @Test
    fun withGivenBlockNewTwoWhenever() {
        val givenBlock = WithGivenBlock()
        givenBlock.whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
            headers(HEADER_NAME, HEADER_VALUE)
        } thenRespondWith {
            body(BODY)
            headers(HEADER_MAP)
        }
        val results = givenBlock.whenever receiving REQUEST_DESCRIPTION_2 withPath PATH and {
            headers(HEADER_NAME, HEADER_VALUE)
        } thenRespondWith {
            body(BODY)
            headers(HEADER_MAP)
        }

        val expectedResult = GivenBlockResult(
            REQUEST_DESCRIPTION_1,
            PATH,
            { headers(HEADER_NAME, HEADER_VALUE) },
            {
                body(BODY)
                headers(HEADER_MAP)
            }
        )
        val expectedResults = listOf(expectedResult, expectedResult.copy(requestDescription = REQUEST_DESCRIPTION_2))

        assertGivenBlockResultsAreEqual(results, expectedResults)
    }
}
