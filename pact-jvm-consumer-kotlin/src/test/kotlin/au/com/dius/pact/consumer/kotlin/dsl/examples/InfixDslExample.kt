package au.com.dius.pact.consumer.kotlin.dsl.examples

import au.com.dius.pact.consumer.kotlin.dsl.KPact
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class InfixDslExample {
    @Test
    fun infixDsl() {
        val infixDsl =
        // tag::infixDSL[]
            KPact between "Some Consumer" and "Some Provider" isDefinedBy {
                given providerIsInState "a certain state on the provider" then {
                    whenever receiving "a request for something" withPath "/hello" and {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } thenRespondWith {
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }

                    whenever receiving "another request for something" withPath "/hello" and {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } thenRespondWith {
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }
                }

                given providerIsInState "other state on the provider" then {
                    whenever receiving "a request for something" withPath "/hello" and {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } thenRespondWith {
                        status(404)
                        body("{\"hello\": \"harry\"}")
                    }

                    whenever receiving "another request for something" withPath "/hello" and {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } thenRespondWith {
                        status(404)
                        body("{\"hello\": \"harry\"}")
                    }
                }
            }
        // end::infixDSL[]

        assertThat(infixDsl).isEqualTo(ConsumerPactBuilderExample.consumerPactBuilderExample)
    }
}