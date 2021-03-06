package com.zipwhip.integration.zipchat.publish;

import com.zipwhip.integration.message.TextService;
import com.zipwhip.integration.message.TextServiceWrapper;
import com.zipwhip.integration.message.domain.MessageTracker;
import com.zipwhip.integration.zipchat.domain.Subscriber;
import com.zipwhip.integration.zipchat.domain.SubscriberEvent;
import com.zipwhip.integration.zipchat.repository.SubscriberRepository;
import com.zipwhip.logging.IntegrationFeature;
import com.zipwhip.message.domain.InboundMessage;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DefaultMessagePublisher implements MessagePublisher {

  private final SubscriberRepository subscriberRepository;

  @Autowired
  private TextServiceWrapper textService;

  @Override
  public void publishMessage(InboundMessage message) {

    Subscriber sender = subscriberRepository.findById(message.getPayload().getSourceAddress())
        .orElseThrow(IllegalStateException::new);
    subscriberRepository.findByChannelId(sender.getChannelId()).stream()
        .filter(excludeSelf(message.getPayload().getSourceAddress()))
        .forEach(s -> sendMessage(s.getMobileNumber(), message));
  }

  @Override
  public void publishCommandMessage(SubscriberEvent subEvent, InboundMessage message) {

    String eventPayload = String.join(" ",
        subEvent.getSubscriber().getDisplayName(),
        subEvent.getEventType().getDisplay(),
        "channel",
        subEvent.getChannel().getName());

    subscriberRepository.findByChannelId(subEvent.getChannel().getId()).stream()
        .filter(excludeSelf(subEvent.getSubscriber().getMobileNumber()))
        .forEach(s -> sendMessage(s.getMobileNumber(), message, eventPayload));
  }

  private Predicate<Subscriber> excludeSelf(String myNumber) {
    return s -> !s.getMobileNumber().equals(myNumber);
  }

  private String getMessagePrefix(InboundMessage message) {
    Subscriber subscriber = subscriberRepository.findById(message.getPayload().getSourceAddress())
        .orElseThrow(IllegalStateException::new);
    return subscriber.getDisplayName() + ":";
  }

  private void sendMessage(String destinationAddress, InboundMessage msg) {
    sendMessage(destinationAddress, msg, null);
  }

  private void sendMessage(String destinationAddress, InboundMessage msg, String overrideText) {
    MessageTracker.Origin origin = MessageTracker.Origin.builder()
        .featureName(IntegrationFeature.CONVERSATION_TO_EXTERNAL.toString())
        .integrationId(22)
        .userGenerated(false)
        .orgCustomerId(msg.getOrgCustomerId())
        .build();

    String payload =
        overrideText != null ? overrideText : getMessagePrefix(msg) + msg.getPayload().getBody();

    // todo need response?
    TextService.Response response = textService
        .send(msg.getPayload().getDestAddress(), destinationAddress,
            payload, origin);
  }
}
