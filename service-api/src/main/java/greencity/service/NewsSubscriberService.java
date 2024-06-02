package greencity.service;


import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;

import java.util.List;

/**
 * Provides the interface to manage sending emails to {@code NewsSubscriber}.
 */

public interface NewsSubscriberService {
    NewsSubscriberRequestDto subscribe(NewsSubscriberRequestDto subscriberRequestDto);

    List<NewsSubscriberResponseDto> getAll();

    void unsubscribe(String email, String unsubscribeToken);
}
