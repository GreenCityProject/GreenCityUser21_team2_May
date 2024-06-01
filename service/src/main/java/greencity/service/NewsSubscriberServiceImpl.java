package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;
import greencity.repository.NewsSubscriberRepo;
import greencity.security.jwt.JwtTool;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NewsSubscriberServiceImpl implements NewsSubscriberService {

    private final NewsSubscriberRepo newsSubscriberRepo;
    private final JwtTool jwtTool;

    @Override
    public NewsSubscriberRequestDto subscribe(NewsSubscriberRequestDto subscriberRequestDto) {
        // todo: add to db
        return subscriberRequestDto;
    }

    @Override
    public List<NewsSubscriberResponseDto> getAll() {
        List<NewsSubscriber> subscribers = this.newsSubscriberRepo.findAll();
        return subscribers.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // todo: refactor as mapper
    private NewsSubscriberResponseDto mapToDto(NewsSubscriber subscriber) {
        return NewsSubscriberResponseDto.builder()
                .email(subscriber.getEmail())
                .unsubscribeToken(subscriber.getUnsubscribeToken())
                .build();
    }
}
