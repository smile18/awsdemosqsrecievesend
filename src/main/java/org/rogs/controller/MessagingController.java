package org.rogs.controller;

import org.rogs.model.Event;
import org.rogs.service.EventProcessor;

import java.util.Map;
import java.util.logging.Logger;

@RestController("messageController")
@RequestMapping("/v1/events")
public class MessagingController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessagingController.class);

    @Autowired
    private EventProcessor eventProcessor;

    @RequestMapping(value = "/sendMessage/{queueName}", method = RequestMethod.POST)
    public ResponseEntity<Object> sendMessage(
            @PathVariable("queueName") String queueName,
            @Validated @RequestBody Event event,
            @ApiParam(hidden = true)
            @RequestHeader Map<String, String> requestHeader) throws Exception {

        return eventProcessor.processEvent(queueName, event, Boolean.TRUE, requestHeader);
    }

    @GetMapping("/receiveMessages/{queueName}")
    public ResponseEntity receiveMessages(@PathVariable String queueName) {
        return eventProcessor.receiveMessages(queueName);
    }

    @GetMapping("/deleteMessages/{queueName}")
    public ResponseEntity deleteMessages(@PathVariable String queueName) {
        return eventProcessor.deleteMessages(queueName);
    }

    @GetMapping("/createQueue/{queueName}")
    public ResponseEntity createMessage(@PathVariable String queueName) {
        return eventProcessor.createQueue(queueName);
    }

    @GetMapping("/deleteQueue/{queueName}")
    public ResponseEntity deleteMessage(@PathVariable String queueName) {
        return eventProcessor.deleteQueue(queueName);
    }
}
