package com.personal.stub.tests;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testng.Assert;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class Mock {
    @Rule
    public WireMockRule wm = new WireMockRule();

    @Before
    public void init() {

    }

    @Test
    public void stubbing() {
        wm.stubFor(get(urlEqualTo("/quotes/1"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "text/plain")
                        .withBody("I've suffered for my art...now it's your turn!")));

        //Response response = client.target("http://localhost:8080/quotes/1").request().get();

        Response response1 = RestAssured.given()
                .baseUri("http://localhost:8080/quotes/1")
                .when()
                .get()
                .then()
                .extract().response();

        Assert.assertEquals(response1.getBody().asString(),
                "I've suffered for my art...now it's your turn!");

    }

    @Test
    public void verification() {
        wm.stubFor(post(urlEqualTo("/quotes")).willReturn(aResponse().withStatus(200)));

        RestAssured.given()
                .baseUri("http://localhost:8080/quotes")
                .body("Some cause happiness wherever they go; others, whenever they go.")
                //.contentType("application/json")
                .contentType("text/plain")
                .when()
                .post();


        wm.verify(postRequestedFor(urlEqualTo("/quotes"))
                .withRequestBody(containing("Some cause happiness wherever they go")));
    }
}
