package greencity.service;


import greencity.dto.newssubscriber.NewsSubscriberRequestDto;

/**
 * Provides the interface to manage sending emails to {@code NewsSubscriber}.
 */

public interface NewsSubscriberService {
    NewsSubscriberRequestDto subscribe(NewsSubscriberRequestDto subscriberRequestDto);
}
