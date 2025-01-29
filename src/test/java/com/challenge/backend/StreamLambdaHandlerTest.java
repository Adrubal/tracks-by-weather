package com.challenge.backend;


import com.amazonaws.serverless.proxy.internal.LambdaContainerHandler;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.challenge.backend.service.TracksByWeatherService;
import jakarta.ws.rs.HttpMethod;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StreamLambdaHandlerTest {

    private static StreamLambdaHandler handler;
    private static Context lambdaContext;

    @Mock
    private TracksByWeatherService tracksByWeatherService;

    @BeforeAll
    public static void setUp() {
        handler = new StreamLambdaHandler();
        lambdaContext = new MockLambdaContext();
    }

    @Test
    public void invalidResource_streamRequest_responds404() {
        InputStream requestStream = new AwsProxyRequestBuilder("/invalid", HttpMethod.GET)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
                .buildStream();
        ByteArrayOutputStream responseStream = new ByteArrayOutputStream();

        handle(requestStream, responseStream);

        AwsProxyResponse response = readResponse(responseStream);
        assertNotNull(response);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatusCode());
    }

    private void handle(InputStream is, ByteArrayOutputStream os) {
        try {
            handler.handleRequest(is, os, lambdaContext);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    private AwsProxyResponse readResponse(ByteArrayOutputStream responseStream) {
        try {
            return LambdaContainerHandler.getObjectMapper().readValue(responseStream.toByteArray(), AwsProxyResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
            fail("Error while parsing response: " + e.getMessage());
        }
        return null;
    }
}
