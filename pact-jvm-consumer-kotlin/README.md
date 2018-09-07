# kPact - Kotlin idiomatic DSL for Pact

## Goals

  - the resulting Pact should be good readable
  - writing the Pact should be as simple as possible
  - Compile time safety (e.g. force mandatory data to be set)
  - short and concise syntax (no boilerplate)
  - IDE support (e.g. auto-completion)
  - Things belonging to each other are in the same block
  - Things on the same logical level are on the same indentation level
    (auto-indentation of IDE)
  - Usable also from
    [pact-jvm-consumer-junit5](https://github.com/DiUS/pact-jvm/tree/master/pact-jvm-consumer-junit5)
    (from `PactDslWithProvider` to `RequestResponsePact`)
  - Support both:
    1.  prose like infix syntax (e.g. `"consumer" and "provider"
        havePact`)
    2.  fluent interface syntax (e.g.
        `consumer("consumer").hasPactWith("provider")`)

## Examples

**Infix DSL**

``` java
KPact between "Some Consumer" andProvider "Some Provider" isDefinedBy {
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
```

**Fluent DSL**

``` java
KPact.consumer("Some Consumer").hasPactWith("Some Provider") {
    given("a certain state on the provider") {
        whenever("a request for something").withPath("/hello") {
            method("POST")
            body("{\"name\": \"harry\"}")
        } thenRespondWith {
            status(200)
            body("{\"hello\": \"harry\"}")
        }

        whenever("another request for something").withPath("/hello") {
            method("POST")
            body("{\"name\": \"harry\"}")
        } thenRespondWith {
            status(200)
            body("{\"hello\": \"harry\"}")
        }
    }

    given("other state on the provider") {
        whenever("a request for something").withPath("/hello") {
            method("POST")
            body("{\"name\": \"harry\"}")
        } thenRespondWith {
            status(404)
            body("{\"hello\": \"harry\"}")
        }

        whenever("another request for something").withPath("/hello") {
            method("POST")
            body("{\"name\": \"harry\"}")
        } thenRespondWith {
            status(404)
            body("{\"hello\": \"harry\"}")
        }
    }
}
```

## Detailed Documentation

A more detailed documentation can be found at https://usr42.github.io/pact-jvm/