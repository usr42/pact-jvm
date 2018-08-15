package au.com.dius.pact.consumer.kotlin.dsl

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.kotlin.dsl.helper.BODY
import au.com.dius.pact.consumer.kotlin.dsl.helper.CONSUMER_NAME
import au.com.dius.pact.consumer.kotlin.dsl.helper.GIVEN_STATE_1
import au.com.dius.pact.consumer.kotlin.dsl.helper.GIVEN_STATE_2
import au.com.dius.pact.consumer.kotlin.dsl.helper.HEADER_MAP
import au.com.dius.pact.consumer.kotlin.dsl.helper.HEADER_NAME
import au.com.dius.pact.consumer.kotlin.dsl.helper.HEADER_VALUE
import au.com.dius.pact.consumer.kotlin.dsl.helper.PATH
import au.com.dius.pact.consumer.kotlin.dsl.helper.PROVIDER_NAME
import au.com.dius.pact.consumer.kotlin.dsl.helper.REQUEST_DESCRIPTION_1
import au.com.dius.pact.consumer.kotlin.dsl.helper.REQUEST_DESCRIPTION_2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class KPactTest {
    private val classicPact = ConsumerPactBuilder(CONSUMER_NAME)
        .hasPactWith(PROVIDER_NAME)
            .given(GIVEN_STATE_1)
                .uponReceiving(REQUEST_DESCRIPTION_1)
                    .path(PATH)
                    .headers(HEADER_NAME, HEADER_VALUE)
                .willRespondWith()
                    .body(BODY)
                    .headers(HEADER_MAP)

                .uponReceiving(REQUEST_DESCRIPTION_2)
                    .path(PATH)
                .willRespondWith()
                    .body(BODY)
                    .headers(HEADER_MAP)
            .given(GIVEN_STATE_2)
                .uponReceiving(REQUEST_DESCRIPTION_1)
                    .path(PATH)
                    .headers(HEADER_NAME, HEADER_VALUE)
                .willRespondWith()
                    .body(BODY)
                    .headers(HEADER_MAP)

                .uponReceiving(REQUEST_DESCRIPTION_2)
                    .path(PATH)
                .willRespondWith()
        .toPact()

    @Test
    fun testInfix() {
        val pactFromDsl = kPact {
            consumer withName CONSUMER_NAME andProvider PROVIDER_NAME havePact {
                given providerIsInState GIVEN_STATE_1 then {
                    whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }

                    whenever receiving REQUEST_DESCRIPTION_2 withPath PATH thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }
                }

                given providerIsInState GIVEN_STATE_2 then {
                    whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }

                    whenever receiving REQUEST_DESCRIPTION_2 withPath PATH thenRespondWith KPact.EmptyResponse
                }
            }
        }

        assertThat(pactFromDsl).isEqualTo(classicPact)
    }

    @Test
    fun testFluent() {
        val pactFromDsl = kPact {
            consumer(CONSUMER_NAME).hasPactWith(PROVIDER_NAME) {
                given(GIVEN_STATE_1) {
                    whenever(REQUEST_DESCRIPTION_1).withPath(PATH) {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }

                    whenever(REQUEST_DESCRIPTION_2).withPath(PATH)
                        .thenRespondWith {
                            body(BODY)
                            headers(HEADER_MAP)
                        }
                }

                given(GIVEN_STATE_2) {
                    whenever(REQUEST_DESCRIPTION_1).withPath(PATH) {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }

                    whenever(REQUEST_DESCRIPTION_2).withPath(PATH)
                        .thenRespondWith(KPact.EmptyResponse)
                }
            }
        }

        assertThat(pactFromDsl).isEqualTo(classicPact)
    }

    @Test
    fun testInfixStringExtension() {
        val pactFromDsl = kPact {
            "consumer" and "provider" havePact {
                given providerIsInState GIVEN_STATE_1 then {
                    whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }

                    whenever receiving REQUEST_DESCRIPTION_2 withPath PATH thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }
                }

                given providerIsInState GIVEN_STATE_2 then {
                    whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }

                    whenever receiving REQUEST_DESCRIPTION_2 withPath PATH thenRespondWith KPact.EmptyResponse
                }
            }
        }

        assertThat(pactFromDsl).isEqualTo(classicPact)
    }

    @Test
    fun testFluentStringExtension() {
        val pactFromDsl = kPact {
            "consumer".hasPactWith("provider") {
                given(GIVEN_STATE_1) {
                    whenever(REQUEST_DESCRIPTION_1).withPath(PATH) {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }

                    whenever(REQUEST_DESCRIPTION_2).withPath(PATH)
                        .thenRespondWith {
                            body(BODY)
                            headers(HEADER_MAP)
                        }
                }

                given(GIVEN_STATE_2) {
                    whenever(REQUEST_DESCRIPTION_1).withPath(PATH) {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }

                    whenever(REQUEST_DESCRIPTION_2).withPath(PATH)
                        .thenRespondWith(KPact.EmptyResponse)
                }
            }
        }

        assertThat(pactFromDsl).isEqualTo(classicPact)
    }

    @Test
    fun onlyOneConsumerProviderPerPact() {
        val pactFromDsl = KPact between CONSUMER_NAME andProvider PROVIDER_NAME isDefinedBy {
            given providerIsInState GIVEN_STATE_1 then {
                whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
                    headers(HEADER_NAME, HEADER_VALUE)
                } thenRespondWith {
                    body(BODY)
                    headers(HEADER_MAP)
                }

                whenever receiving REQUEST_DESCRIPTION_2 withPath PATH thenRespondWith {
                    body(BODY)
                    headers(HEADER_MAP)
                }
            }

            given providerIsInState GIVEN_STATE_2 then {
                whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
                    headers(HEADER_NAME, HEADER_VALUE)
                } thenRespondWith {
                    body(BODY)
                    headers(HEADER_MAP)
                }

                whenever receiving REQUEST_DESCRIPTION_2 withPath PATH thenRespondWith KPact.EmptyResponse
            }
        }

        assertThat(pactFromDsl).isEqualTo(classicPact)
    }

    @Test
    fun onlyOneConsumerProviderPerPactFluent() {
        val pactFromDsl = KPact.consumer(CONSUMER_NAME).hasPactWith(PROVIDER_NAME) {
            given(GIVEN_STATE_1) {
                whenever(REQUEST_DESCRIPTION_1).withPath(PATH) {
                    headers(HEADER_NAME, HEADER_VALUE)
                } thenRespondWith {
                    body(BODY)
                    headers(HEADER_MAP)
                }

                whenever(REQUEST_DESCRIPTION_2).withPath(PATH)
                    .thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }
            }

            given(GIVEN_STATE_2) {
                whenever(REQUEST_DESCRIPTION_1).withPath(PATH) {
                    headers(HEADER_NAME, HEADER_VALUE)
                } thenRespondWith {
                    body(BODY)
                    headers(HEADER_MAP)
                }

                whenever(REQUEST_DESCRIPTION_2).withPath(PATH)
                    .thenRespondWith(KPact.EmptyResponse)
            }
        }

        assertThat(pactFromDsl).isEqualTo(classicPact)
    }

    @Test
    fun fromPactDslWithProvider() {
        val pactDslWithProvider = ConsumerPactBuilder(CONSUMER_NAME)
            .hasPactWith(PROVIDER_NAME)

        val pactFromDsl = pactDslWithProvider.kPact {
            given(GIVEN_STATE_1) {
                whenever(REQUEST_DESCRIPTION_1).withPath(PATH) {
                    headers(HEADER_NAME, HEADER_VALUE)
                } thenRespondWith {
                    body(BODY)
                    headers(HEADER_MAP)
                }

                whenever(REQUEST_DESCRIPTION_2).withPath(PATH)
                    .thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }
            }

            given(GIVEN_STATE_2) {
                whenever(REQUEST_DESCRIPTION_1).withPath(PATH) {
                    headers(HEADER_NAME, HEADER_VALUE)
                } thenRespondWith {
                    body(BODY)
                    headers(HEADER_MAP)
                }

                whenever(REQUEST_DESCRIPTION_2).withPath(PATH)
                    .thenRespondWith(KPact.EmptyResponse)
            }
        }

        assertThat(pactFromDsl).isEqualTo(classicPact)
    }

    @Test
    fun multipleAndBlocks() {
        val pactWithMultipleAndBlocks = kPact {
            consumer withName CONSUMER_NAME andProvider PROVIDER_NAME havePact {
                given providerIsInState GIVEN_STATE_1 then {
                    whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
                        headers(HEADER_NAME, HEADER_VALUE)
                    } and {
                        body(BODY)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }
                }
            }
        }

        val pactWithCombinedAndBlock = kPact {
            consumer withName CONSUMER_NAME andProvider PROVIDER_NAME havePact {
                given providerIsInState GIVEN_STATE_1 then {
                    whenever receiving REQUEST_DESCRIPTION_1 withPath PATH and {
                        headers(HEADER_NAME, HEADER_VALUE)
                        body(BODY)
                    } thenRespondWith {
                        body(BODY)
                        headers(HEADER_MAP)
                    }
                }
            }
        }

        assertThat(pactWithMultipleAndBlocks).isEqualTo(pactWithCombinedAndBlock)
    }
}
