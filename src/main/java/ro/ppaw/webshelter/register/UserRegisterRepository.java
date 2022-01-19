package ro.ppaw.webshelter.register;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ro.ppaw.webshelter.login.User;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserRegisterRepository  extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String usernmae);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enable = TRUE WHERE a.username = ?1")
    int enableAppUser(String username);
}
