package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;
import greencity.exception.exceptions.SubscribeException;
import greencity.repository.NewsSubscriberRepo;
import greencity.security.jwt.JwtTool;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsSubscriberServiceImpl implements NewsSubscriberService {

    private final NewsSubscriberRepo newsSubscriberRepo;
    private final JwtTool jwtTool;

    ModelMapper modelMapper = new ModelMapper();

    @Override
    @Transactional
    public NewsSubscriberResponseDto subscribe(NewsSubscriberRequestDto subscriberRequestDto) {
        if (isSubscriberExists(subscriberRequestDto.getEmail()))
            throw new SubscribeException("Email already subscribed");
        NewsSubscriber newsSubscriber =
            new NewsSubscriber(null, subscriberRequestDto.getEmail(), jwtTool.generateTokenKey());
        return modelMapper.map(newsSubscriberRepo.save(newsSubscriber), NewsSubscriberResponseDto.class);
    }

    private boolean isSubscriberExists(String email) {
        return this.newsSubscriberRepo.findByEmail(email).isPresent();
    }

    @Override
    public List<NewsSubscriberResponseDto> getAll() {
        List<NewsSubscriber> subscribers = this.newsSubscriberRepo.findAll();
        return subscribers.stream()
            .map(this::mapToDto)
            .toList();
    }

    @Override
    @Transactional
    public void unsubscribe(String unsubscribeToken) {
        if (checkToken(unsubscribeToken)) {
            newsSubscriberRepo.deleteByUnsubscribeToken(unsubscribeToken);
        } else {
            throw new SubscribeException("Invalid token");
        }
    }

    private boolean checkToken(String unsubscribeToken) {
        Optional<NewsSubscriber> newsSubscriberOptional = newsSubscriberRepo.findByUnsubscribeToken(unsubscribeToken);
        return newsSubscriberOptional.isPresent();
    }

    private NewsSubscriberResponseDto mapToDto(NewsSubscriber subscriber) {
        return NewsSubscriberResponseDto.builder()
            .email(subscriber.getEmail())
            .unsubscribeToken(subscriber.getUnsubscribeToken())
            .build();
    }
}
