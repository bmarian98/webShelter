package ro.ppaw.webshelter.login;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class LoginController {
    private final UserService userService;
    Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    public LoginController(UserService userRepository) {
        this.userService = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User userData){


        User user = userService.getUserLogin(userData);
        LOGGER.debug("User: " + user);
        LOGGER.info("/user/login succeed!");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
