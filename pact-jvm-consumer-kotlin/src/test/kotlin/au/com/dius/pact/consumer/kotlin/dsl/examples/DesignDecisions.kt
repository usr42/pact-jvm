package au.com.dius.pact.consumer.kotlin.dsl.examples

import au.com.dius.pact.consumer.kotlin.dsl.KPact
import au.com.dius.pact.consumer.kotlin.dsl.by
import au.com.dius.pact.consumer.kotlin.dsl.invoke
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class DesignDecisions {
    @Test
    fun proseLike() {
        val infixDsl =
        // tag::proseLike[]
            KPact between "Some Consumer" and1 "Some Provider" defined by { // <1>
                given providerIsInState "a certain state on the provider" then {
                    // end::proseLike[]
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

        Assertions.assertThat(infixDsl).isEqualTo(ConsumerPactBuilderExample.consumerPactBuilderExample)
    }

    @Test
    fun proseLikeShorter() {
        val infixDsl =
        // tag::proseLikeShorter[]
            KPact between "Some Consumer" and2 "Some Provider" { // <1>
                given providerIsInState "a certain state on the provider" then {
                    // end::proseLikeShorter[]
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

        Assertions.assertThat(infixDsl).isEqualTo(ConsumerPactBuilderExample.consumerPactBuilderExample)
    }

    @Test
    fun infixDsl() {
        val infixDsl =
        // tag::CamelCase[]
            KPact between "Some Consumer" and "Some Provider" isDefinedBy { // <1>
                given providerIsInState "a certain state on the provider" then {
                    // end::CamelCase[]
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

        Assertions.assertThat(infixDsl).isEqualTo(ConsumerPactBuilderExample.consumerPactBuilderExample)
    }
}