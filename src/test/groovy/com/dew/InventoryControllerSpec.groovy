package com.dew

import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.spock.Testcontainers
import org.testcontainers.utility.DockerImageName
import spock.lang.Specification

@MicronautTest
@Testcontainers
class InventoryControllerSpec extends Specification implements TestPropertyProvider {

    public static MongoDBContainer mongo = new MongoDBContainer(DockerImageName.parse('mongo:latest'))
            .withExposedPorts(27017)

    @Inject
    InventoryClient client

    def "interact with inventory api"() {
        when:
        var product = new CreateProductInventoryCommand("123", "321", 20)
        var status = client.save(product)

        then:
        status == HttpStatus.CREATED

        when:
        var response = client.find("321")

        then:
        response.body.present
        response.body().code == "123"

        when:
        var notFoundResponse = client.find("1234")

        then:
        notFoundResponse.status == HttpStatus.NOT_FOUND
        !notFoundResponse.body.present

        when:
        client.save(product)

        then:
        thrown(HttpClientResponseException)
    }

    @Override
    Map<String, String> getProperties() {
        mongo.start()

        return ["mongodb.uri": mongo.replicaSetUrl]
    }
}
