package org.rogs.service;

import org.rogs.model.Event;
import org.rogs.service.sqs.SqsSendMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DefaultProcessor implements EventProcessor {
    private SqsSendMessageService sendMessageService;

    @Autowired
    public DefaultProcessor(SqsSendMessageService sendMessageService) {
        this.sendMessageService = sendMessageService;
    }

    @Override
    public ResponseEntity<Object> processEvent(String eventType, Event event, Boolean isJson, Map<String, String> requestHeader) {
        return sendMessageService.sendMessage(eventType,event);
    }

    @Override
    public ResponseEntity receiveMessages(String queueName) {
        return sendMessageService.receiveMessage(queueName);
        //return new ResponseEntity(events, HttpStatus.OK);
    }

    @Override
    public ResponseEntity deleteMessages(String queueName) {
        return sendMessageService.deleteMessages(queueName);
    }

    @Override
    public ResponseEntity createQueue(String queueName) {
        return sendMessageService.createQueue(queueName);
    }

    @Override
    public ResponseEntity deleteQueue(String queueName) {
        return sendMessageService.deleteQueue(queueName);
    }
}
