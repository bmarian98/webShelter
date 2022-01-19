package ro.ppaw.webshelter.register;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.ppaw.webshelter.login.LoginController;
import ro.ppaw.webshelter.login.User;
import ro.ppaw.webshelter.login.UserRole;
import ro.ppaw.webshelter.login.UserService;
import ro.ppaw.webshelter.utility.email.EmailSender;
import ro.ppaw.webshelter.utility.email.EmailValidator;
import ro.ppaw.webshelter.utility.token.ConfirmationToken;
import ro.ppaw.webshelter.utility.token.ConfirmationTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserRegisterService {
    private final UserRegisterRepository userRegisterRepository;

    private final static String USER_NOT_FOUND = "Utilizatorul cu email-ul %s nu a fost gasit!";
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    private final UserService userService;
    private final EmailValidator emailValidator;

    private final EmailSender emailSender;



//
//    @Override
//    public UserDetails loadUserByUsername(String usernamme) throws UsernameNotFoundException {
//        return userRegisterRepository.findUserByUsernamme(usernamme).orElseThrow(
//                () -> new UserNotFoundException(String.format(USER_NOT_FOUND, usernamme)));
//    }

    public String singUpUser(User appUser){
        boolean userExists = userRegisterRepository.findUserByUsername(appUser.getUsername()).isPresent();

        if(userExists){
            Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
            LOGGER.error(Marker.ANY_NON_NULL_MARKER, "USER already use!" + appUser);
            throw new IllegalStateException("Exista un user cu acest nume de utilizator!");
        }

        String encoded = bCryptPasswordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encoded);

        userRegisterRepository.save(appUser);

        String uuid = UUID.randomUUID().toString();
        ConfirmationToken token = new ConfirmationToken(
                uuid,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(token);

        Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
        LOGGER.info("singUP succeed!");
        return uuid;
    }

    public int enableAppUser(String username) {
        return userRegisterRepository.enableAppUser(username);
    }

    public User register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(!isValidEmail){
            Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
            LOGGER.error(Marker.ANY_NON_NULL_MARKER, "Invalid email!");
            throw new IllegalStateException("email invalid");
        }
        User user = new User(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                request.getUsername(),
                request.getSex().charAt(0),
                UserRole.USER
        );
        String token = singUpUser(
                user
        );

        String link = "http://localhost:8080/user/registration/confirm?token=" + token;
        String fullName = request.getFirstName() + ' ' + request.getLastName();
        emailSender.send(request.getEmail(),
                buildEmail(fullName, link));

        Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
        LOGGER.info("registration succeed!");
        return user;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("Codul de verificare nu a fost gasit!"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("Adresa de email a fost deja utilizata!");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiredAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Codul de verificare a expirat!");
        }

        confirmationTokenService.setConfirmedAt(token);
        enableAppUser(
                confirmationToken.getAppUser().getUsername());
        return "Confirmat!";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Activare cont</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Salut " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Iti multumim pentru inregistrare. Te rugam acceseaza link-ul de mai jos pentru a-ti activa contul: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activeaza acum</a> </p></blockquote>\n Link-ul va expira in 5 minute. <p>Iti multumim!</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}
