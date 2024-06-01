package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;
import greencity.exception.exceptions.BadRequestException;
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
        if (isEmailAlreadySubscribed(subscriberRequestDto.getEmail()))
            throw new BadRequestException("Email subscribed already");
        this.newsSubscriberRepo.save(new NewsSubscriber(null, subscriberRequestDto.getEmail(), jwtTool.generateTokenKey()));
        return subscriberRequestDto;
    }

    private boolean isEmailAlreadySubscribed(String email){
        return this.newsSubscriberRepo.findByEmail(email).isPresent();
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
