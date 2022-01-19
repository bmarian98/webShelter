package ro.ppaw.webshelter.login;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserLogin(User userData){

        User user = userRepository.findByUsername(userData.getUsername());
        bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(userData.getPassword());
        if(user.getPassword().equals(password))
        {
            LOGGER.info("USER LOGIN SUCCEED!");
            return user;
        }

        LOGGER.warn("USER: " + user + "couldn't login!");
        return null;
    }
}
