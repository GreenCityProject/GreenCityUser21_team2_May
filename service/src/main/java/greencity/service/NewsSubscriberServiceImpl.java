package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NewsSubscriberServiceImpl implements NewsSubscriberService {
    @Override
    public NewsSubscriberRequestDto subscribe(NewsSubscriberRequestDto subscriberRequestDto) {
        ///// save into db
        return subscriberRequestDto;
    }
}
