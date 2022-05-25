package es.ucm.fdi.iw.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import com.fasterxml.jackson.databind.JsonNode;
import es.ucm.fdi.iw.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import es.ucm.fdi.iw.model.User.Role;

import java.util.List;

import javax.persistence.EntityManager;

/**
 *  Site administration.
 *
 *  Access to this end-point is authenticated.
 */
@Controller
@RequestMapping("admin")
public class AdminController {

	private static final Logger log = LogManager.getLogger(AdminController.class);
    @Autowired 
    private EntityManager entityManager;
	@GetMapping("/")
    public String index(Model model) {
        return "admin";
    }

    @ModelAttribute("books")
    public List<Book> getBooksList() {
        return entityManager.createNamedQuery("Book.all", Book.class).getResultList();
    }
    @ModelAttribute("usuarios")
    public List<User> getUsuariosList( Model model) {
    log.info("Obteniendo usuarios");

    return entityManager.createNamedQuery("User.all", User.class).getResultList();
    }

    @PostMapping("erase/{id}")
    @Transactional
    public String eraseBook(@PathVariable long id, Model model, HttpSession session) {

        log.info("En funcion BORRAR DE BD");
        
        User user = entityManager.find(
                User.class, ((User)session.getAttribute("u")).getId());
        Book book = entityManager.find(Book.class, id);
        
        PhysicalBook p= entityManager.createNamedQuery("PhysicalBook.allBook", PhysicalBook.class)
        .setParameter("id", id)
        .getSingleResult();

        List<Progress> lprogresoBook = entityManager.createNamedQuery("Progreso.allBook", Progress.class)
        .setParameter("id", id)
        .getResultList();

     /*
        Review reviewBook = entityManager.createNamedQuery("Review.allBook", Review.class)
        .setParameter("id", id)
        .getResultList()
        .stream().findFirst().orElse(null);
        */

        List<Review> reviewBook = entityManager.createNamedQuery("Review.byBook", Review.class)
        .setParameter("id", id)
        .getResultList();
        
        for(Review r : reviewBook){
            if (user.hasRole(Role.ADMIN))
                entityManager.remove(r);
        }

        for(Progress progresoBook: lprogresoBook){
        Library libreria = entityManager
        .createNamedQuery("Library.byOwner", Library.class)
        .setParameter("owner", progresoBook.getUser().getId()).getResultList()
        .stream().findFirst().orElse(null);
        if(libreria!=null && progresoBook!=null && user.hasRole(Role.ADMIN))
            libreria.getBooks().remove(id);

            if (user.hasRole(Role.ADMIN)) {
                log.info("Soy admin");
                
                if (libreria!=null)
                entityManager.persist(libreria);
                entityManager.flush();
            }

            entityManager.remove(progresoBook);
        }

       

        if (user.hasRole(Role.ADMIN)) {
            log.info("Soy admin");
            entityManager.remove(p);
           // if (reviewBook!=null)
           //      entityManager.remove(reviewBook);
            entityManager.remove(book);
            entityManager.flush();
        }

        
        return "redirect:../../admin/";
    }

    @PostMapping("eraseUser/{id}")
    @Transactional
    public String eraseUser(@PathVariable long id, Model model, HttpSession session) {

        log.info("En funcion BORRAR DE BD a User");
        
        User userAdmin = entityManager.find(
                User.class, ((User)session.getAttribute("u")).getId());
        
        User aBorrar = entityManager.find(
                User.class, id);

        List<Message> lr= entityManager.createNamedQuery("Message.allMessagesRUser", Message.class)
        .setParameter("userId", id)
        .getResultList();

        for(Message m : lr){
            if (userAdmin.hasRole(Role.ADMIN))
                entityManager.remove(m);
        }
  
        List<Message> ls = entityManager.createNamedQuery("Message.allMessagesSUser", Message.class)
        .setParameter("userId", id)
        .getResultList();
        for(Message m : ls){
            if (userAdmin.hasRole(Role.ADMIN))
                entityManager.remove(m);
        }
        Library libreria = entityManager
        .createNamedQuery("Library.byOwner", Library.class)
        .setParameter("owner", id).getResultList()
        .stream().findFirst().orElse(null);

        List<PhysicalBook> lp= entityManager.createNamedQuery("PhysicalBook.allBookFromUser", PhysicalBook.class)
        .setParameter("id", id)
        .getResultList();

        for(PhysicalBook p : lp){
            if (userAdmin.hasRole(Role.ADMIN))
            entityManager.remove(p);
        }

        List<PhysicalBook> lp2= entityManager.createNamedQuery("PhysicalBook.allBookToUser", PhysicalBook.class)
        .setParameter("id", id)
        .getResultList();

        for(PhysicalBook p : lp2){
            if (userAdmin.hasRole(Role.ADMIN))
             entityManager.remove(p);
        }

        List<Post> lpost= entityManager.createNamedQuery("PhysicalBook.allPostbyUser", Post.class)
        .setParameter("id", id)
        .getResultList();

        for(Post p : lpost){
            if (userAdmin.hasRole(Role.ADMIN))
                entityManager.remove(p);
        }

        List<Progress> lprogresoBook = entityManager.createNamedQuery("Progreso.byUser", Progress.class)
        .setParameter("user", id)
        .getResultList();
        for(Progress p : lprogresoBook){
            if (userAdmin.hasRole(Role.ADMIN))
                entityManager.remove(p);
        }

        List<Review> reviewBook = entityManager.createNamedQuery("Review.byUser", Review.class)
        .setParameter("id", id)
        .getResultList();
        
        for(Review p : reviewBook){
            if (userAdmin.hasRole(Role.ADMIN))
                entityManager.remove(p);
        }

        if (userAdmin.hasRole(Role.ADMIN)) {
            log.info("Soy admin");
            entityManager.remove(aBorrar);
            if (libreria!=null)
                 entityManager.remove(libreria);
            
            entityManager.flush();
        }

        
        return "redirect:../../admin/";
    }
}
