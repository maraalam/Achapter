package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import es.ucm.fdi.iw.model.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("register")
public class RegisterController {

    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Encodes a password, so that it can be saved for future checking. Notice
     * that encoding the same password multiple times will yield different
     * encodings, since encodings contain a randomly-generated salt.
     *
     * @param rawPassword to encode
     * @return the encoded password (typically a 60-character string)
     * for example, a possible encoding of "test" is
     * {bcrypt}$2y$12$XCKz0zjXAP6hsFyVc8MucOzx6ER6IsC1qo5zQbclxhddR1t6SfrHm
     */
    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @PostMapping("/new")
    @Transactional
    public String registerUser(HttpServletResponse response, @ModelAttribute User user, 
            Model model) throws IOException {

        log.info("registrando usuario:  " + user.getUsername());

        User target = null;

        List<User> u = entityManager.createNamedQuery("User.byUsername", User.class)
                .setParameter("username", user.getUsername())
                .getResultList(); //como es clave primaria, la lista tiene 0 o 1 elementos.

        if (u.isEmpty()) {
            target = new User();
            target.setUsername(user.getUsername());
            target.setPassword(encodePassword(user.getPassword()));
            target.setFirstName(user.getFirstName());
            target.setLastName(user.getLastName());
            target.setRoles("USER");
            target.setEnabled(true);
            entityManager.persist(target);
            entityManager.flush(); // forces DB to add user & assign valid id
            long id = target.getId();   // retrieve assigned id from DB

            target = entityManager.find(User.class, id);
            // model.addAttribute("user", target);
            log.info("Registrado usuario:  " + target.getUsername());
        }
        else
            log.info("No se pudo registrar. Usuario duplicado: " + u.get(0).getUsername());

        return "redirect:/login";
    }

}