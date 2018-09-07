package au.com.dius.pact.consumer.kotlin.dsl.examples

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.consumer.Pact
import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.kotlin.dsl.helper.CONSUMER_NAME
import au.com.dius.pact.consumer.kotlin.dsl.helper.PROVIDER_NAME
import au.com.dius.pact.consumer.kotlin.dsl.kPact
import au.com.dius.pact.model.RequestResponsePact
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

private object Junit5DslExample {
    // tag::kPact[]
    @Pact(provider = "test_provider", consumer = "test_consumer")
    fun createPact(builder: PactDslWithProvider): RequestResponsePact {
        return builder.kPact {
            given("test state") {
                whenever("ExampleJavaConsumerPactTest test interaction").withPath("/") {
                    method("GET")
                } thenRespondWith {
                    status(200)
                    body("{\"responsetest\": true}")
                }
            }
        }
    }
    // end::kPact[]
}

private object Junit5ConsumerPactBuilderExample {
    // tag::ConsumerPactBuilder[]
    @Pact(provider = "test_provider", consumer = "test_consumer")
    fun createPact(builder: PactDslWithProvider): RequestResponsePact {
        return builder
            .given("test state")
                .uponReceiving("ExampleJavaConsumerPactTest test interaction")
                    .path("/")
                    .method("GET")
                .willRespondWith()
                    .status(200)
                    .body("{\"responsetest\": true}")
            .toPact()
    }
    // end::ConsumerPactBuilder[]
}

class Junit5ExampleTest {
    @Test
    fun testJunit5Pact() {
        val dslPact = Junit5DslExample.createPact(pactDslWithProvider())
        val consumerPactBuilderPact = Junit5ConsumerPactBuilderExample.createPact(pactDslWithProvider())

        assertThat(dslPact).isEqualTo(consumerPactBuilderPact)
    }

    private fun pactDslWithProvider() = ConsumerPactBuilder(CONSUMER_NAME)
        .hasPactWith(PROVIDER_NAME)
}