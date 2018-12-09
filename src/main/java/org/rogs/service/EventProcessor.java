package org.rogs.service;

import org.rogs.model.Event;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface EventProcessor {

    ResponseEntity<Object> processEvent(String eventType, Event event, Boolean isJson, Map<String, String> requestHeader) throws Exception;

    ResponseEntity receiveMessages(String queueName);

    ResponseEntity deleteMessages(String queueName);

    ResponseEntity createQueue(String queueName);

    ResponseEntity deleteQueue(String queueName);


}
