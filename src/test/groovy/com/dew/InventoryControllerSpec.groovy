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
        when: "save a new product inventory"
        var product = new CreateProductInventoryCommand("123", "321", 20)
        var status = client.save(product)

        then: "should return 201"
        status == HttpStatus.CREATED

        when: "find the product inventory"
        var response = client.find("321")

        then: "should return the product inventory"
        response.body.present
        response.body().code == "123"

        when: "find the product inventory"
        var notFoundResponse = client.find("1234")

        then: "should return 404"
        notFoundResponse.status == HttpStatus.NOT_FOUND
        !notFoundResponse.body.present

        when: "save same product inventory"
        client.save(product)

        then: "should return 409"
        thrown(HttpClientResponseException)

        when: "update stock"
        status = client.updateStock("321", 30)

        then: "should return 200"
        status == HttpStatus.OK

        when: "find the product inventory"
        response = client.find("321")

        then: "should return the product inventory"
        response.body.present
        response.body().stock == 30
    }

    @Override
    Map<String, String> getProperties() {
        mongo.start()

        return ["mongodb.uri": mongo.replicaSetUrl]
    }
}
