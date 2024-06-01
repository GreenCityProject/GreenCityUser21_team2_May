package greencity.repository;

import greencity.entity.NewsSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsSubscriberRepo extends JpaRepository<NewsSubscriber, Long> {

}