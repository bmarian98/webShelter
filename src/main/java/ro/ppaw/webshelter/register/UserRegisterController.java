package ro.ppaw.webshelter.register;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.ppaw.webshelter.login.LoginController;
import ro.ppaw.webshelter.login.User;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/register")
public class UserRegisterController {
    private UserRegisterService registrationService;



    @PostMapping("/registration")
    public ResponseEntity<User> registration(@RequestBody RegistrationRequest request){
        User user = registrationService.register(request);
        Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
        LOGGER.debug("/registration call succeed request [" + request + "]");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/registration/confirm")
    public String confirm(@RequestParam("token") String token){
        Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
        LOGGER.debug("/registration/confirm call succed token: " + token );
        return registrationService.confirmToken(token);
    }
}
