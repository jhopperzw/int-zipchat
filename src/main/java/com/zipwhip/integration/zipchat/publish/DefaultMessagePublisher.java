package com.zipwhip.integration.zipchat.publish;

import com.zipwhip.integration.zipchat.domain.Subscriber;
import com.zipwhip.integration.zipchat.domain.SubscriberEvent;
import com.zipwhip.integration.zipchat.repository.SubscriberRepository;
import com.zipwhip.message.domain.InboundMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DefaultMessagePublisher implements MessagePublisher {

  private final SubscriberRepository subscriberRepository;

  @Override
  public void publishMessage(Iterable<Subscriber> subscribers, InboundMessage message) {
    // filter out subscriber matching "from" in "message"
    // TODO
  }

  /**
   * Return modified event text for publishing to subscribers
   * @param subEvent
   * @param message
   * @return
   */
  private String transformCommandMessage(SubscriberEvent subEvent, InboundMessage message) {
    // TODO, replace "/join channelFoo" with "JohnDoe has joined"
    return null;
  }
}
