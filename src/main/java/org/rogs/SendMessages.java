package org.rogs;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.*;

import java.util.Date;
import java.util.List;

public class SendMessages {

    private static final String QUEUE_NAME = "testQueue-gs" + new Date().getTime();
    static String ACCESS_KEY_ID = "<REPLACE-ACCESS_KEY_ID>";
    static String SECRET_ACCESS_KEY = "<REPLACE_SECRET_ACCESS_KEY>";

    public static void main(String[] args)
    {
        AWSCredentials awsCredentials = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_ACCESS_KEY);

        AmazonSQS sqs = AmazonSQSClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .withRegion(Regions.US_WEST_2).build();

        try {
            CreateQueueResult create_result = sqs.createQueue(QUEUE_NAME);
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }

        String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

        SendMessageRequest send_msg_request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody("hello world")
                .withDelaySeconds(5);
        sqs.sendMessage(send_msg_request);


        // Send multiple messages to the queue
        SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
                .withQueueUrl(queueUrl)
                .withEntries(
                        new SendMessageBatchRequestEntry(
                                "msg_1", "Hello from message 1"),
                        new SendMessageBatchRequestEntry(
                                "msg_2", "Hello from message 2")
                                .withDelaySeconds(10));
        sqs.sendMessageBatch(send_batch_request);

        // receive messages from the queue
        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

        // delete messages from the queue
        for (Message m : messages) {
            sqs.deleteMessage(queueUrl, m.getReceiptHandle());
        }


        // Get the URL for a queue
        String queue_url = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

        System.out.println("queueUrl   : " +queueUrl);
        System.out.println("queue_url   : " + queue_url);
        // Delete the Queue
        sqs.deleteQueue(queueUrl);

        System.out.println("Queue got deleted");
    }
}
