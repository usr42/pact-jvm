package au.com.dius.pact.consumer.kotlin.dsl.examples

import au.com.dius.pact.consumer.kotlin.dsl.KPact
import au.com.dius.pact.consumer.kotlin.dsl.by
import au.com.dius.pact.consumer.kotlin.dsl.invoke
import au.com.dius.pact.consumer.kotlin.dsl.respond
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DesignDecisions {
    @Test
    fun proseLike() {
        val infixDsl =
        // tag::proseLike[]
            KPact between "Some Consumer" and1 "Some Provider" defined by { // <1> <2>
                given providerState "a certain state on the provider" then { // <3>
                    whenever receiving "a request for something" withPath "/hello" and { // <4>
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } then respond with { // <5>
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }
                    // end::proseLike[]

                    whenever receiving "another request for something" withPath "/hello" and {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } then respond with {
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }
                }

                given providerState "other state on the provider" then {
                    whenever receiving "a request for something" withPath "/hello" and {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } then respond with {
                        status(404)
                        body("{\"hello\": \"harry\"}")
                    }

                    whenever receiving "another request for something" withPath "/hello" and {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } then respond with {
                        status(404)
                        body("{\"hello\": \"harry\"}")
                    }
                }
            }

        Assertions.assertThat(infixDsl).isEqualTo(ConsumerPactBuilderExample.consumerPactBuilderExample)
    }

    @Test
    fun proseLikeShorter() {
        val infixDsl =
        // tag::proseLikeShorter[]
            KPact between "Some Consumer" and "Some Provider" { // <1> <2>
                given providerState "a certain state on the provider" { // <3>
                    whenever receiving "a request for something" withPath "/hello" { // <4>
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } then respond with { // <5>
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }
                    // end::proseLikeShorter[]

                    whenever receiving "another request for something" withPath "/hello" {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } then respond with {
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }
                }

                given providerState "other state on the provider" {
                    whenever receiving "a request for something" withPath "/hello" {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } then respond with {
                        status(404)
                        body("{\"hello\": \"harry\"}")
                    }

                    whenever receiving "another request for something" withPath "/hello" {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } then respond with {
                        status(404)
                        body("{\"hello\": \"harry\"}")
                    }
                }
            }

        Assertions.assertThat(infixDsl).isEqualTo(ConsumerPactBuilderExample.consumerPactBuilderExample)
    }

    @Test
    fun infixDsl() {
        val infixDsl =
        // tag::CamelCase[]
            KPact between "Some Consumer" and "Some Provider" isDefinedBy { // <1> <2>
                given providerState "a certain state on the provider" then { // <3>
                    whenever receiving "a request for something" withPath "/hello" and { // <4>
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } thenRespondWith { // <5>
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }
                    // end::CamelCase[]

                    whenever receiving "another request for something" withPath "/hello" and {
                        method("POST")
                        body("{\"name\": \"harry\"}")
                    } thenRespondWith {
                        status(200)
                        body("{\"hello\": \"harry\"}")
                    }
                }

                given providerState "other state on the provider" then {
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

        Assertions.assertThat(infixDsl).isEqualTo(ConsumerPactBuilderExample.consumerPactBuilderExample)
    }
}