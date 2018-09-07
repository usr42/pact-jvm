package au.com.dius.pact.consumer.kotlin.dsl.examples

import au.com.dius.pact.consumer.kotlin.dsl.KPact
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class FluentDslExample {
    @Test
    fun fluentExample() {
        val fluentExample =
        // tag::fluentDSL[]
            KPact.consumer("Some Consumer").hasPactWith("Some Provider") {
                given("a certain state on the provider") {
                    whenever("a request for something").withPath("/hello") {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    }.thenRespondWith {
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }

                    whenever("another request for something").withPath("/hello") {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    }.thenRespondWith {
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }
                }

                given("other state on the provider") {
                    whenever("a request for something").withPath("/hello") {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    }.thenRespondWith {
                        status(404)
                        body("{\"hello\": \"harry\"}")
                    }

                    whenever("another request for something").withPath("/hello") {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    }.thenRespondWith {
                        status(404)
                        body("{\"hello\": \"harry\"}")
                    }
                }
            }
        // end::fluentDSL[]

        assertThat(fluentExample).isEqualTo(ConsumerPactBuilderExample.consumerPactBuilderExample)
    }
}