package au.com.dius.pact.consumer.kotlin.dsl.examples

import au.com.dius.pact.consumer.ConsumerPactBuilder

object ConsumerPactBuilderExample {
    val consumerPactBuilderExample =
        ConsumerPactBuilder
            .consumer("Some Consumer")
            .hasPactWith("Some Provider")
                .given("a certain state on the provider")
                    .uponReceiving("a request for something")
                        .path("/hello")
                        .method("POST")
                        .body("{\"name\": \"harry\"}")
                    .willRespondWith()
                        .status(200)
                        .body("{\"hello\": \"harry\"}")

                    .uponReceiving("another request for something")
                        .path("/hello")
                        .method("POST")
                        .body("{\"name\": \"harry\"}")
                    .willRespondWith()
                        .status(200)
                        .body("{\"hello\": \"harry\"}")

                .given("other state on the provider")
                    .uponReceiving("a request for something")
                        .path("/hello")
                        .method("POST")
                        .body("{\"name\": \"harry\"}")
                    .willRespondWith()
                        .status(404)
                        .body("{\"hello\": \"harry\"}")

                    .uponReceiving("another request for something")
                        .path("/hello")
                        .method("POST")
                        .body("{\"name\": \"harry\"}")
                    .willRespondWith()
                        .status(404)
                        .body("{\"hello\": \"harry\"}")
            .toPact()
}