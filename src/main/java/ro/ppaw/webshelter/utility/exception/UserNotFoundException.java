package ro.ppaw.webshelter.utility.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import ro.ppaw.webshelter.login.LoginController;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
        Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
        LOGGER.error(Marker.ANY_MARKER, message);
    }
}