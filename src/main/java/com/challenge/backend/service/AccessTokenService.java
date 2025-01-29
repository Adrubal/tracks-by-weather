package com.challenge.backend.service;

import com.challenge.backend.model.dto.AccessTokenDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class AccessTokenService {

    @Value("${dynamodb.accesstoken.table.name}")
    private String accessTokenTableName;

    public void saveAccessToken(String appName, String token) {
        try (DynamoDbClient ddb = DynamoDbClient.create()) {

            LocalDateTime localDateTime = LocalDateTime.now();
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("America/Chicago"));

            HashMap<String, AttributeValue> itemValues = new HashMap<>();
            itemValues.put("app", AttributeValue.builder().s(appName).build());
            itemValues.put("date", AttributeValue.builder().s(zonedDateTime.toString()).build());
            itemValues.put("token", AttributeValue.builder().s(token).build());

            PutItemRequest request = PutItemRequest.builder()
                    .tableName(accessTokenTableName)
                    .item(itemValues)
                    .build();

            PutItemResponse response = ddb.putItem(request);

            log.info("{} was successfully updated. The request id is "
                    + response.responseMetadata().requestId(), accessTokenTableName);

        } catch (ResourceNotFoundException e) {
            log.error("Error: The Amazon DynamoDB table {} can't be found.", accessTokenTableName, e);
        } catch (DynamoDbException e) {
            log.error(e.getMessage(), e);
        }
    }

    public Optional<AccessTokenDto> getAccessToken(String appName) {

        Optional<AccessTokenDto> itemResponse = Optional.empty();

        try (DynamoDbClient ddb = DynamoDbClient.create()) {

            HashMap<String, AttributeValue> keyToGet = new HashMap<>();
            keyToGet.put("app", AttributeValue.builder()
                    .s(appName)
                    .build());

            GetItemRequest request = GetItemRequest.builder()
                    .key(keyToGet)
                    .tableName(accessTokenTableName)
                    .build();

            Map<String, AttributeValue> returnedItem = ddb.getItem(request).item();

            if (returnedItem.isEmpty()) {
                log.info("No item found with the key {}", appName);
                itemResponse = Optional.empty();
            } else {

                AccessTokenDto response = new AccessTokenDto();
                response.setAppName(returnedItem.get("app").s());
                response.setToken(returnedItem.get("token").s());
                response.setCreationDate(ZonedDateTime.parse(returnedItem.get("date").s()).toLocalDateTime());

                itemResponse = Optional.of(response);
            }

        } catch (DynamoDbException e) {
            log.error(e.getMessage(), e);
        }
        return itemResponse;
    }
}
