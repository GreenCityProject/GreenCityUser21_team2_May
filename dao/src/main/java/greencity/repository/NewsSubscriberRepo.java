package greencity.repository;

import greencity.entity.NewsSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewsSubscriberRepo extends JpaRepository<NewsSubscriber, Long> {

    Optional<NewsSubscriber> findByEmail(String email);

    void deleteByUnsubscribeToken(String unsubscribeToken);

    Optional<NewsSubscriber> findByUnsubscribeToken(String unsubscribeToken);
}