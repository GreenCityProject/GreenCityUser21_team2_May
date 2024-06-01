package greencity.service;

import greencity.dto.newssubscriber.NewsSubscriberRequestDto;
import greencity.dto.newssubscriber.NewsSubscriberResponseDto;
import greencity.entity.NewsSubscriber;
import greencity.exception.exceptions.BadRequestException;
import greencity.repository.NewsSubscriberRepo;
import greencity.security.jwt.JwtTool;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NewsSubscriberServiceImpl implements NewsSubscriberService {

    private final NewsSubscriberRepo newsSubscriberRepo;
    private final JwtTool jwtTool;

    @Override
    @Transactional
    public NewsSubscriberRequestDto subscribe(NewsSubscriberRequestDto subscriberRequestDto) {

        // todo: Check user sign-in response, maybe return ResponseDto with token
        if (isSubscriberExists(subscriberRequestDto.getEmail()))
            throw new BadRequestException("Email subscribed already");
        this.newsSubscriberRepo.save(new NewsSubscriber(null, subscriberRequestDto.getEmail(), jwtTool.generateTokenKey()));
        return subscriberRequestDto;
    }

    private boolean isSubscriberExists(String email){
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
    public Long unsubscribe(String email, String unsubscribeToken) {
        if (checkToken(email, unsubscribeToken)) {
            newsSubscriberRepo.deleteByEmail(email);

            // todo: do i have to return Long?
            return 1L;
        }
        throw new BadRequestException("Token doesn't match");
    }

    private boolean checkToken(String email, String unsubscribeToken) {
        Optional<NewsSubscriber> newsSubscriberOptional = newsSubscriberRepo.findByEmail(email);
        if (newsSubscriberOptional.isPresent()) {
            NewsSubscriber newsSubscriber = newsSubscriberOptional.get();
            return newsSubscriber.getUnsubscribeToken().equals(unsubscribeToken);
        } else return false;
    }

    // todo: refactor as mapper
    private NewsSubscriberResponseDto mapToDto(NewsSubscriber subscriber) {
        return NewsSubscriberResponseDto.builder()
                .email(subscriber.getEmail())
                .unsubscribeToken(subscriber.getUnsubscribeToken())
                .build();
    }
}
