package com.challenge.backend.service;

import com.challenge.backend.model.dto.AccessTokenDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class AccessTokenServiceTest {

    @InjectMocks
    private AccessTokenService accessTokenService;

    private static DynamoDbClient ddb;
    private static MockedStatic<DynamoDbClient> dynamoDbClient;

    @BeforeAll
    public static void beforeEach() {
        ddb = Mockito.mock(DynamoDbClient.class);
        dynamoDbClient = Mockito.mockStatic(DynamoDbClient.class);
        dynamoDbClient.when(DynamoDbClient::create).thenReturn(ddb);
    }

    @Test
    void saveAccessTokenTest() {

        PutItemResponse response = Mockito.mock(PutItemResponse.class);
        DynamoDbResponseMetadata dynamoDbResponseMetadata = Mockito.mock(DynamoDbResponseMetadata.class);
        Mockito.when(response.responseMetadata()).thenReturn(dynamoDbResponseMetadata);
        Mockito.when(ddb.putItem(ArgumentMatchers.any(PutItemRequest.class))).thenReturn(response);

        accessTokenService.saveAccessToken("spotify", "token");
        Mockito.verify(ddb, Mockito.times(1)).putItem(ArgumentMatchers.any(PutItemRequest.class));
    }

    @Test
    void getAccessTokenTest_no_item_found() {

        GetItemResponse response = Mockito.mock(GetItemResponse.class);
        Mockito.when(ddb.getItem(ArgumentMatchers.any(GetItemRequest.class))).thenReturn(response);

        Optional<AccessTokenDto> accessToken = accessTokenService.getAccessToken("spotify");
        Assertions.assertTrue(accessToken.isEmpty());
    }

    @Test
    void getAccessTokenTest_item_found() {

        GetItemResponse response = Mockito.mock(GetItemResponse.class);
        Mockito.when(ddb.getItem(ArgumentMatchers.any(GetItemRequest.class))).thenReturn(response);

        AttributeValue attributeValue = Mockito.mock(AttributeValue.class);
        Map<String, AttributeValue> returnedItem = new HashMap<>();
        returnedItem.put("app", attributeValue);
        returnedItem.put("token", attributeValue);
        returnedItem.put("date", attributeValue);
        Mockito.when(response.item()).thenReturn(returnedItem);

        LocalDateTime localDateTime = LocalDateTime.now();
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("America/Chicago"));

        Mockito.when(attributeValue.s())
                .thenReturn("spotify")
                .thenReturn("token").thenReturn(zonedDateTime.toString());

        Optional<AccessTokenDto> accessToken = accessTokenService.getAccessToken("spotify");

        Assertions.assertTrue(accessToken.isPresent());
        Assertions.assertEquals("token", accessToken.get().getToken());
        Assertions.assertEquals("spotify", accessToken.get().getAppName());

    }
}
