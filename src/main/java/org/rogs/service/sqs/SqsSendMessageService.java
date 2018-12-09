package org.rogs.service.sqs;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.rogs.model.Event;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SqsSendMessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsSendMessageService.class);
    static String ACCESS_KEY_ID = "<REPLACE-ACCESS_KEY_ID>";
    static String SECRET_ACCESS_KEY = "<REPLACE_SECRET_ACCESS_KEY>";
    private AmazonSQS sqs;
    private ObjectMapper getObjectMapper;

    @PostConstruct
    public void init() {
        try {
            AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);
            ClientConfiguration clientConfiguration = new ClientConfiguration();
            //clientConfiguration.setProxyPort(9090);
            //clientConfiguration.setProxyHost("<REPLACE_PROXY_FOR_PCF");
            sqs = AmazonSQSClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .withClientConfiguration(clientConfiguration)
                    .withRegion(Regions.US_WEST_2).build();

        } catch (AmazonSQSException e) {
            LOGGER.info(e.getMessage());
            LOGGER.info(">>>>>>>>>>>>>>>");
            LOGGER.info(e.getErrorCode());
        }
        getObjectMapper = new ObjectMapper();
    }

    public ResponseEntity<Object> sendMessage(String queueName, Event event) {

        //try {
            String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
            LOGGER.info("Queue url to send message : {} " , queueUrl);
            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(getEventMetaDataAsString(event))
                    .withDelaySeconds(5);
            SendMessageResult sendMessageResult = sqs.sendMessage(send_msg_request);

            LOGGER.info("results httpStatusCode : {} ", sendMessageResult.getSdkHttpMetadata().getHttpStatusCode());

            LOGGER.info("results messageID : {} ", sendMessageResult.getMessageId());
            LOGGER.info("Message sent successfully");
            return getResponseEntity("Message sent successfully", HttpStatus.OK);
        /*} catch (QueueDoesNotExistException e) {
            // e.printStackTrace();
            LOGGER.info(e.getMessage());
            return getResponseEntity(e.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        }*/
    }

    public ResponseEntity receiveMessage(String queueName) {

        try {
            String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
            LOGGER.info("Queue url to receive message : {} " , queueUrl);
            // receive messages from the queue
            List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

            List<Event> events = new ArrayList<>();
            messages.forEach(message -> {
                try {
                    events.add(getObjectMapper.readValue(message.getBody(), Event.class));
                } catch (IOException e) {
                    //e.printStackTrace();
                    LOGGER.info("Error happened while mapping message  : {}" ,e.getMessage());
                }
            });

            return getResponseEntity(events);
        } catch (QueueDoesNotExistException e) {
            e.printStackTrace();
            return getResponseEntity(e.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        }
    }

    public ResponseEntity deleteMessages(String queueName) {
        try {
            String queueUrl = sqs.getQueueUrl(queueName).getQueueUrl();
            LOGGER.info("Queue url to delete message : {} " , queueUrl);
            // receive messages from the queue
            List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

            // delete messages from the queue
            for (Message m : messages) {
                sqs.deleteMessage(queueUrl, m.getReceiptHandle());
            }

            LOGGER.info( "{} Message deleted successfully" , messages.size());
            return getResponseEntity(messages.size() + " Message deleted successfully", HttpStatus.OK);
        } catch (QueueDoesNotExistException e) {
            // e.printStackTrace();
            LOGGER.info(e.getMessage());
            return getResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity createQueue(String queueName) {
        try {
            CreateQueueResult create_result = sqs.createQueue(queueName);
            LOGGER.info("Queue got created and the queue url is :  {} ", create_result.getQueueUrl());
            return getResponseEntity("Queue got created and the queue url is : "+create_result.getQueueUrl(), HttpStatus.OK);
        }
     catch (AmazonSQSException e) {
        /*if (!e.getErrorCode().equals("QueueAlreadyExists")) {
            throw e;
        }*/

         LOGGER.info("Error happened while creating queue   {} ", e.getErrorCode());
         return getResponseEntity(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }


    }

    public ResponseEntity deleteQueue(String queueName) {
        try {
            // Get the URL for a queue
            String queue_url = sqs.getQueueUrl(queueName).getQueueUrl();
            LOGGER.info("The queue_url   : " + queue_url);
            // Delete the Queue
            sqs.deleteQueue(queue_url);
            LOGGER.info("Queue got deleted");
            return getResponseEntity(queueName +" Queue deleted successfully", HttpStatus.OK);
        } catch (AmazonSQSException e) {
            LOGGER.info(e.getMessage());
            return getResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private String getEventMetaDataAsString(Event event) {
        JSONObject eventJsonObject = new JSONObject(event);
        try {
            String payLoad = (String) event.getPayload();
            if (!StringUtils.isEmpty(payLoad) && payLoad.startsWith("{")) {
                JSONObject payLoadJsonObject = new JSONObject(payLoad);
                eventJsonObject.put("payload", payLoadJsonObject);
            } else if (!StringUtils.isEmpty(payLoad) && payLoad.startsWith("[")) {
                JSONArray payLoadJsonObject = new JSONArray(payLoad);
                eventJsonObject.put("payload", payLoadJsonObject);
            }
        } catch (Exception e) {
            LOGGER.error("ProducerExcpetion Thrown while jsonizing the payload and Exception is::::{}",
                    ExceptionUtils.printStackTrace(e, 0));
        }
        // Getting this extra filed from getEventTimeString so have to remove
        if (eventJsonObject.has("eventTimeString")) {
            eventJsonObject.remove("eventTimeString");
        }
        if (eventJsonObject.has("eventTime")) {
            eventJsonObject.put("eventTime", event.getEventTime().format(DateTimeFormatter.ISO_DATE_TIME));
        }

        // Getting this extra filed from getTimeStampString so have to remove from HeaderReference
        if (eventJsonObject.has("headerReference")) {
            JSONObject headerRefJsonObj = eventJsonObject.getJSONObject("headerReference");
            if (headerRefJsonObj.has("timestampString")) {
                headerRefJsonObj.remove("timestampString");
            }
            if (headerRefJsonObj.has("timestamp")) {
                if (headerRefJsonObj.getString("timestamp") != null && !headerRefJsonObj.getString("timestamp").isEmpty())
                    headerRefJsonObj.put("timestamp", event.getHeaderReference().getTimestamp().format(DateTimeFormatter.ISO_DATE_TIME));
            }

        }

        return eventJsonObject.toString();
    }

    private ResponseEntity<Object> getResponseEntity(String message, HttpStatus status) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("status", status.toString());
        responseBody.put("timestamp", new Date().toString());
        responseBody.put("message", message);
        return new ResponseEntity<>(responseBody, status);
    }

    private ResponseEntity<Object> getResponseEntity(List<Event> events) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("status", HttpStatus.OK.toString());
        responseBody.put("timestamp", new Date().toString());
        responseBody.put("events", events);
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
