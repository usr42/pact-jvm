package au.com.dius.pact.consumer.dsl

import au.com.dius.pact.consumer.ConsumerPactBuilder
import au.com.dius.pact.model.OptionalBody
import au.com.dius.pact.model.generators.Generators
import au.com.dius.pact.model.matchingrules.MatchingRuleGroup
import au.com.dius.pact.model.matchingrules.MatchingRulesImpl
import au.com.dius.pact.model.matchingrules.RegexMatcher
import au.com.dius.pact.model.matchingrules.TypeMatcher
import com.google.common.net.MediaType
import org.apache.http.entity.ContentType
import spock.lang.Issue
import spock.lang.Specification

import static au.com.dius.pact.consumer.dsl.PactDslResponse.DEFAULT_JSON_CONTENT_TYPE_REGEX

class PactDslResponseSpec extends Specification {

  def 'allow matchers to be set at root level'() {
    expect:
    response.matchingRules.rulesForCategory('body').matchingRules == [
      '$': new MatchingRuleGroup([TypeMatcher.INSTANCE])]

    where:
    pact = ConsumerPactBuilder.consumer('complex-instruction-service')
      .hasPactWith('workflow-service')
      .uponReceiving('a request to start a workflow')
      .path('/startWorkflowProcessInstance')
      .willRespondWith()
      .body(PactDslJsonRootValue.numberType())
      .toPact()
    interaction = pact.interactions.first()
    response = interaction.response
  }

  def 'default json content type should match common variants'() {
      def acceptableDefaultContentTypes = [
              'application/json;charset=utf-8',
              'application/json; charset=UTF-8',
              'application/json; charset=utf-8',

              ContentType.APPLICATION_JSON.toString(),
              MediaType.JSON_UTF_8.toString(),
      ]

      expect:
        acceptableDefaultContentTypes.each {
            it.matches(DEFAULT_JSON_CONTENT_TYPE_REGEX)
        }
  }

  def 'sets up any default state when created'() {
    given:
    ConsumerPactBuilder consumerPactBuilder = ConsumerPactBuilder.consumer('spec')
    PactDslRequestWithPath request = new PactDslRequestWithPath(consumerPactBuilder, 'spec', 'spec', [], 'test', '/',
      'GET', [:], [:], OptionalBody.empty(), new MatchingRulesImpl(), new Generators(), null, null)
    PactDslResponse defaultResponseValues = new PactDslResponse(consumerPactBuilder, request, null, null)
      .headers(['test': 'test'])
      .body('{"test":true}')
      .status(499)

    when:
    PactDslResponse subject = new PactDslResponse(consumerPactBuilder, request, null, defaultResponseValues)

    then:
    subject.responseStatus == 499
    subject.responseHeaders == [test: 'test']
    subject.responseBody == OptionalBody.body('{"test":true}')
  }

  @Issue('#716')
  def 'set the content type header correctly'() {
    given:
    def builder = ConsumerPactBuilder.consumer('spec').hasPactWith('provider')
    def body = new PactDslJsonBody().numberValue('key', 1).close()

    when:
    def pact = builder
      .given('Given the body method is invoked before the header method')
      .uponReceiving('a request for some response')
      .path('/bad/content-type/matcher')
      .method('GET')
      .willRespondWith()
      .status(200)
      .body(body)
      .matchHeader('Content-Type', 'application/json')

      .given('Given the body method is invoked after the header method')
      .uponReceiving('a request for some response')
      .path('/no/content-type/matcher')
      .method('GET')
      .willRespondWith()
      .status(200)
      .matchHeader('Content-Type', 'application/json')
      .body(body)
      .toPact()

    def responses = pact.interactions*.response

    then:
    responses[0].matchingRules.rulesForCategory('header').matchingRules['Content-Type'].rules == [
      new RegexMatcher('application/json')
    ]
    responses[1].matchingRules.rulesForCategory('header').matchingRules['Content-Type'].rules == [
      new RegexMatcher('application/json')
    ]
  }

  @Issue('#748')
  def 'uponReceiving should pass the path on'() {
    given:
    def builder = ConsumerPactBuilder.consumer('spec').hasPactWith('provider')

    when:
    def pact = builder
      .uponReceiving('a request for response No 1')
      .path('/response/1')
      .method('GET')
      .willRespondWith()
      .status(200)
      .uponReceiving('a request for the same path')
      .willRespondWith()
      .status(200)
      .toPact()

    then:
    pact.interactions*.request.path == ['/response/1', '/response/1']
  }

}
