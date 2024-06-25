package greencity.repository;

import greencity.entity.NicknamesArchive;
import greencity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface NicknamesArchiveRepo extends JpaRepository<NicknamesArchive, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE NicknamesArchive n SET n.activity = false WHERE n.user = :user AND n.nickname != :currentNickname")
    void setFalseActivityForAllUserNicknamesExcept(User user, String currentNickname);
}
