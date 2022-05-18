package es.ucm.fdi.iw.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.*;
import es.ucm.fdi.iw.model.User.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * User management.
 * <p>
 * Access to this end-point is authenticated.
 */
@Controller()
@RequestMapping("user")
public class UserController {

    private static final Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private LocalData localData;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Generates random tokens. From https://stackoverflow.com/a/44227131/15472
     *
     * @param byteLength
     * @return
     */
    public static String generateRandomBase64Token(int byteLength) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] token = new byte[byteLength];
        secureRandom.nextBytes(token);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(token); //base64 encoding
    }

    /**
     * Returns the default profile pic
     *
     * @return
     */
    private static InputStream defaultPic() {
        return new BufferedInputStream(Objects.requireNonNull(
                UserController.class.getClassLoader().getResourceAsStream(
                        "static/img/default-pic.jpg")));
    }

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

    /**
     * Landing page for a user profile
     * <p>
     * Carga Valores necesarios que queremos ver en la pagina principal: ahora -> libros quiero leer
     */
    @GetMapping("{id}")
    @Transactional
    public String index(@PathVariable long id, @RequestParam(defaultValue = "posts") String tab, Model model, HttpSession session) {
        User target = entityManager.find(User.class, id);

        model.addAttribute("tab", tab); // tab.toUpperCase());

        log.info("USER:" + id);
        Library libreria = entityManager
                .createNamedQuery("Library.byOwner", Library.class)
                .setParameter("owner", id).getResultList()
                .stream().findFirst().orElse(null);

        if (libreria != null) {

            if (target.getLibrary() == null) {
                libreria.setOwner(target);
                target.setLibrary(libreria);
            }

            Progress progress = entityManager
                    .createNamedQuery("Progreso.byUser", Progress.class)
                    .setParameter("user", id).getResultList()
                    .stream().findFirst().orElse(null);

            if (progress != null) {
                log.info("Progreso: " + progress.getId());
                log.info("Book: " + progress.getBook().getTitulo());

                Library l = target.getLibrary();
                l.put(progress.getBook(), progress);
            }
            log.info("paso");
        } else
            log.info("Libreria: vacio");
		/*
		if(libreria!=null){
		Map<Integer, Integer> books_quiero_leer  = entityManager
		//.createQuery(" SELECT l FROM Library l INNER JOIN  LIBRARY_BOOKS_QUIERO_LEER  nl " + "ON nl.LIBRARY_ID.id   <> :LIBRARY_ID   ", Tuple.class)
		.createQuery(" SELECT l FROM Library l JOIN u.books_quiero_leer r  WHERE r.get(key)   ", Tuple.class)
		.setParameter("LIBRARY_ID ", libreria.getId())
		.getResultList()
		.stream()
		.collect(
			Collectors.toMap(
				tuple -> ((Number) tuple.get("BOOKS_QUIERO_LEER_KEY")).intValue(),
				tuple -> ((Number) tuple.get("BOOKS_QUIERO_LEER_ID ")).intValue()
			)
		);
		
		//target.addToLibrary(), p, libreria);
		log.info("Mapa: " + books_quiero_leer.entrySet().toString());
		}
*/
        model.addAttribute("user", target);

        return "user";
    }

    /**
     * Alter or create a user
     */
    @PostMapping("/{id}")
    @Transactional
    public String postUser(
            HttpServletResponse response,
            @PathVariable long id,
            @ModelAttribute User edited,
            @RequestParam(required = false) String pass2,
            Model model, HttpSession session) throws IOException {

        User requester = (User) session.getAttribute("u");
        User target = null;
        if (id == -1 && requester.hasRole(Role.ADMIN)) {
            // create new user with random password
            target = new User();
            target.setPassword(encodePassword(generateRandomBase64Token(12)));
            target.setEnabled(true);
            entityManager.persist(target);
            entityManager.flush(); // forces DB to add user & assign valid id
            id = target.getId();   // retrieve assigned id from DB
        }

        // retrieve requested user
        target = entityManager.find(User.class, id);
        model.addAttribute("user", target);

        if (requester.getId() != target.getId() &&
                !requester.hasRole(Role.ADMIN)) {
            throw new NoEsTuPerfilException();
        }

        if (edited.getPassword() != null) {
            if (!edited.getPassword().equals(pass2)) {
                // FIXME: complain
            } else {
                // save encoded version of password
                target.setPassword(encodePassword(edited.getPassword()));
            }
        }
        target.setUsername(edited.getUsername());
        target.setFirstName(edited.getFirstName());
        target.setLastName(edited.getLastName());

        // update user session so that changes are persisted in the session, too
        if (requester.getId() == target.getId()) {
            session.setAttribute("u", target);
        }

        return "user";
    }

    /**
     * Downloads a profile pic for a user id
     *
     * @param id of user
     * @return image
     * @throws IOException if file is not found
     */
    @GetMapping("{id}/pic")
    public StreamingResponseBody getPic(@PathVariable long id) throws IOException {
        File f = localData.getFile("user/", "" + id + ".jpg");
        InputStream in = new BufferedInputStream(f.exists() ?
                new FileInputStream(f) : UserController.defaultPic());
        return os -> FileCopyUtils.copy(in, os);
    }

    /**
     * Uploads a profile pic for a user id
     *
     * @param id of user
     * @return picture updated
     * @throws IOException if file is not found
     */
    @PostMapping("{id}/pic")
    @ResponseBody
    public String setPic(@RequestParam("photo") MultipartFile photo, @PathVariable long id,
                         HttpServletResponse response, HttpSession session, Model model) throws IOException {

        User target = entityManager.find(User.class, id);
        model.addAttribute("user", target);

        // check permissions
        User requester = (User) session.getAttribute("u");
        if (requester.getId() != target.getId() &&
                !requester.hasRole(Role.ADMIN)) {
            throw new NoEsTuPerfilException();
        }

        log.info("Updating photo for user {}", id);
        File f = localData.getFile("user/", "" + id + ".jpg");
        if (photo.isEmpty()) {
            log.info("failed to upload photo: empty file?");
        } else {
            try (BufferedOutputStream stream =
                         new BufferedOutputStream(new FileOutputStream(f))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
                log.info("Uploaded photo for {} into {}!", id, f.getAbsolutePath());
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                log.warn("Error uploading " + id + " ", e);
            }
        }
        return "{\"status\":\"photo uploaded correctly\"}";
    }

    /**
     * Returns JSON with all received messages
     */
    @GetMapping(path = "received", produces = "application/json")
    @Transactional // para no recibir resultados inconsistentes
    @ResponseBody  // para indicar que no devuelve vista, sino un objeto (jsonizado)
    public List<Message.Transfer> retrieveMessages(HttpSession session) {
        long userId = ((User) session.getAttribute("u")).getId();
        User u = entityManager.find(User.class, userId);
        log.info("Generating message list for user {} ({} messages)",
                u.getUsername(), u.getReceived().size());
        return u.getReceived().stream().map(Transferable::toTransfer).collect(Collectors.toList());
    }

    /**
     * Returns JSON with count of unread messages
     */
    @GetMapping(path = "unread", produces = "application/json")
    @ResponseBody
    public String checkUnread(HttpSession session) {
        long userId = ((User) session.getAttribute("u")).getId();
        long unread = entityManager.createNamedQuery("Message.countUnread", Long.class)
                .setParameter("userId", userId)
                .getSingleResult();
        session.setAttribute("unread", unread);
        return "{\"unread\": " + unread + "}";
    }

    /**
     * Posts a message to a user.
     *
     * @param id of target user (source user is from ID)
     * @param o  JSON-ized message, similar to {"message": "text goes here"}
     * @throws JsonProcessingException if unable to build Json object
     */
    @PostMapping("/{id}/msg")
    @ResponseBody
    @Transactional
    public String postMsg(@PathVariable long id,
                          @RequestBody JsonNode o, Model model, HttpSession session)
            throws JsonProcessingException {

        String text = o.get("message").asText();
        User u = entityManager.find(User.class, id);
        User sender = entityManager.find(
                User.class, ((User) session.getAttribute("u")).getId());
        model.addAttribute("user", u);

        // construye mensaje, lo guarda en BD
        Message m = new Message();
        m.setRecipient(u);
        m.setSender(sender);
        m.setDateSent(LocalDateTime.now());
        m.setText(text);
        entityManager.persist(m);
        entityManager.flush(); // to get Id before commit

        // construye json
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("from", sender.getUsername());
        rootNode.put("to", u.getUsername());
        rootNode.put("text", text);
        rootNode.put("id", m.getId());
        String json = mapper.writeValueAsString(rootNode);

        log.info("Sending a message to {} with contents '{}'", id, json);

        messagingTemplate.convertAndSend("/user/" + u.getUsername() + "/queue/updates", json);
        return "{\"result\": \"message sent.\"}";
    }

    @GetMapping("/{id}/chat")
    @Transactional
    public String getChat(@PathVariable long id, Model model, HttpSession session) {
        log.info("En funcion getChat");
        User u = entityManager.find(User.class, id);
        model.addAttribute("user", u);

        long userId = ((User) session.getAttribute("u")).getId();
        List<Message> l= entityManager.createNamedQuery("Message.byUsers", Message.class)
        .setParameter("userId", userId).setParameter("senderId", id)
        .getResultList();

        l.addAll(entityManager.createNamedQuery("Message.byUsers", Message.class)
        .setParameter("userId", id).setParameter("senderId", userId)
        .getResultList());

        Collections.sort(l);
        model.addAttribute("messages", l);

        return "chat";
    }

    @PostMapping("/{id}/chat")
    @ResponseBody
    @Transactional
    public String getChat2(@PathVariable long id, Model model, HttpSession session) {
        log.info("En funcion getChat2");
        User u = entityManager.find(User.class, id);
        model.addAttribute("user", u);

        long userId = ((User) session.getAttribute("u")).getId();
        List<Message> l= entityManager.createNamedQuery("Message.byUsers", Message.class)
        .setParameter("userId", userId).setParameter("senderId", id)
        .getResultList();

        l.addAll(entityManager.createNamedQuery("Message.byUsers", Message.class)
        .setParameter("userId", id).setParameter("senderId", userId)
        .getResultList());

        Collections.sort(l);
        model.addAttribute("messages", l);
        

        return "chat.html";
    }

    @PostMapping("{id}/follow")
    @ResponseBody
    @Transactional
    public String follow(@PathVariable long id, Model model, HttpSession session) {

        User user = entityManager.find(User.class, id);
        User self = entityManager.find(
                User.class, ((User) session.getAttribute("u")).getId());
//		model.addAttribute("user", user); // ???

        List<User> selfFollowed = self.getFollowed();
        if (!selfFollowed.contains(user)) {
            selfFollowed.add(user);
            List<User> userFollowers = user.getFollowers();
            userFollowers.add(self);
        } else {
            selfFollowed.remove(user);
            List<User> userFollowers = user.getFollowers();
            userFollowers.remove(self);
        }

        entityManager.persist(user);
        entityManager.persist(self);

        log.info("User {} now follows user {}", self.getUsername(), user.getUsername());
        return "{\"result\": \"ok.\"}";
    }

    @PostMapping("{id}/state")
    @ResponseBody
    public String postState(@RequestParam("state") Model state, @PathVariable long id,
                            HttpServletResponse response, HttpSession session, Model model) {
        System.out.println("[Test] - Se ejecuta");
        return "{\"result\": \"state updated.\"}";
    }

    @PostMapping("post")
    @Transactional
    public String publishPost(Model model, HttpSession session,  @RequestParam("query")String texto) {
        User u = entityManager.find(
                User.class, ((User) session.getAttribute("u")).getId());
        model.addAttribute("user", u);

        Post post = new Post();
        post.setAuthor(u);
        post.setTitle("this is a post and you couldn't edit the title");
        post.setText(texto);
        post.setLikes(0);
        post.setDateSent(LocalDateTime.now());
        entityManager.persist(post);
        entityManager.flush();

        log.info("published post with id {} by user with id {}", post.getId(), u.getId());

        return "user";
    }

    @PostMapping("/register")
    @Transactional
    public String registerUser(
            HttpServletResponse response,
            @ModelAttribute User user, 
            Model model) throws IOException {


        log.info("registrando usuario:  " + user.getUsername());

        User target = null;

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
        model.addAttribute("user", target);
        /*
        	User u = entityManager.createNamedQuery("User.byUsername", User.class)
			.setParameter("username", user.getUsername())
			.getSingleResult();			*/
        //User requester = (User)session.getAttribute("u");

        log.info("Registrado usuario:  " + target.getUsername());

        model.addAttribute("user", target);


        return "login";
    }
    
    @ModelAttribute("likesPost")
    public List<Long> getBooksList( Model model, HttpSession session) {

            User self = entityManager.find(
                    User.class, ((User) session.getAttribute("u")).getId());
            
            List<Likes> lLikes= entityManager.createNamedQuery("Likes.byUser", Likes.class)
                .setParameter("userId", self.getId())
                .getResultList();
            List<Long> lIDPost = new ArrayList<Long>();
    
            for(Likes like: lLikes){
                lIDPost.add(like.getPost().getId());
            }
    
           
        return lIDPost;
    }

    @ModelAttribute("prestamosMios")
    public List getPhysicalBooksNoDestList(Model model, HttpSession session) {
        return entityManager.createNamedQuery("PhysicalBook.allBookFromUserSinDest")
        .setParameter("id", ((User) session.getAttribute("u")).getId())
        .setMaxResults(10)
        .getResultList();
    }

    @ModelAttribute("prestamosMiosHechos")
    public List getPhysicalBookHechosMios(Model model, HttpSession session) {
        return entityManager.createNamedQuery("PhysicalBook.allBookFromUserConDest")
        .setParameter("id", ((User) session.getAttribute("u")).getId())
        .setMaxResults(10)
        .getResultList();
    }

    @ModelAttribute("contQuieroLeer")
    public int getcontQuieroLeer(Model model, HttpSession session) {
        int contQuieroLeer=0;
        User self = entityManager.find(
            User.class, ((User) session.getAttribute("u")).getId());

       
        Library libreria = entityManager
                .createNamedQuery("Library.byOwner", Library.class)
                .setParameter("owner", self.getId()).getResultList()
                .stream().findFirst().orElse(null);

        if (libreria != null) {

            if (self.getLibrary() == null) {
                libreria.setOwner(self);
                self.setLibrary(libreria);
            }

           
            Map<Long, Progress> lista = libreria.getBooks();

            for (Map.Entry<Long,Progress> entry : lista.entrySet()) {
                if(entry.getValue().getEstado().equals("quieroLeer"))
                contQuieroLeer = contQuieroLeer+1;
            }

           
        } else
            log.info("Libreria: vacio");
        return contQuieroLeer;
        
    }

    @ModelAttribute("contLeyendo")
    public int getccontLeyendo(Model model, HttpSession session) {
        int contQuieroLeer=0;
        User self = entityManager.find(
            User.class, ((User) session.getAttribute("u")).getId());

       
        Library libreria = entityManager
                .createNamedQuery("Library.byOwner", Library.class)
                .setParameter("owner", self.getId()).getResultList()
                .stream().findFirst().orElse(null);

        if (libreria != null) {

            if (self.getLibrary() == null) {
                libreria.setOwner(self);
                self.setLibrary(libreria);
            }

           
            Map<Long, Progress> lista = libreria.getBooks();

            for (Map.Entry<Long,Progress> entry : lista.entrySet()) {
                if(entry.getValue().getEstado().equals("leyendo"))
                contQuieroLeer = contQuieroLeer+1;
            }

           
        } else
            log.info("Libreria: vacio");
        return contQuieroLeer;
        
    }

    @ModelAttribute("contTerminado")
    public int getcontTerminado(Model model, HttpSession session) {
        int contQuieroLeer=0;
        User self = entityManager.find(
            User.class, ((User) session.getAttribute("u")).getId());

       
        Library libreria = entityManager
                .createNamedQuery("Library.byOwner", Library.class)
                .setParameter("owner", self.getId()).getResultList()
                .stream().findFirst().orElse(null);

        if (libreria != null) {

            if (self.getLibrary() == null) {
                libreria.setOwner(self);
                self.setLibrary(libreria);
            }

           
            Map<Long, Progress> lista = libreria.getBooks();

            for (Map.Entry<Long,Progress> entry : lista.entrySet()) {
                if(entry.getValue().getEstado().equals("terminado"))
                contQuieroLeer = contQuieroLeer+1;
            }

           
        } else
            log.info("Libreria: vacio");
        return contQuieroLeer;
        
    }

    @ModelAttribute("contabandonado")
    public int getcontabandonado(Model model, HttpSession session) {
        int contQuieroLeer=0;
        User self = entityManager.find(
            User.class, ((User) session.getAttribute("u")).getId());

       
        Library libreria = entityManager
                .createNamedQuery("Library.byOwner", Library.class)
                .setParameter("owner", self.getId()).getResultList()
                .stream().findFirst().orElse(null);

        if (libreria != null) {

            if (self.getLibrary() == null) {
                libreria.setOwner(self);
                self.setLibrary(libreria);
            }

           
            Map<Long, Progress> lista = libreria.getBooks();

            for (Map.Entry<Long,Progress> entry : lista.entrySet()) {
                if(entry.getValue().getEstado().equals("abandonado"))
                contQuieroLeer = contQuieroLeer+1;
            }

           
        } else
            log.info("Libreria: vacio");
        return contQuieroLeer;
        
    }

    @ModelAttribute("contpausado")
    public int getcontpausado(Model model, HttpSession session) {
        int contQuieroLeer=0;
        User self = entityManager.find(
            User.class, ((User) session.getAttribute("u")).getId());

       
        Library libreria = entityManager
                .createNamedQuery("Library.byOwner", Library.class)
                .setParameter("owner", self.getId()).getResultList()
                .stream().findFirst().orElse(null);

        if (libreria != null) {

            if (self.getLibrary() == null) {
                libreria.setOwner(self);
                self.setLibrary(libreria);
            }

           
            Map<Long, Progress> lista = libreria.getBooks();

            for (Map.Entry<Long,Progress> entry : lista.entrySet()) {
                if(entry.getValue().getEstado().equals("pausado"))
                contQuieroLeer = contQuieroLeer+1;
            }

           
        } else
            log.info("Libreria: vacio");
        return contQuieroLeer;
        
    }

    /**
     * Exception to use when denying access to unauthorized users.
     * <p>
     * In general, admins are always authorized, but users cannot modify
     * each other's profiles.
     */
    @ResponseStatus(
            value = HttpStatus.FORBIDDEN,
            reason = "No eres administrador, y éste no es tu perfil")  // 403
    public static class NoEsTuPerfilException extends RuntimeException {
    }




	/*
		@GetMapping("/messagebox")
    public String messagebox(Model model, HttpSession session) {
		User requester = (User)session.getAttribute("u");
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("text", requester.getUsername()
		+ " is looking up " + u.getUsername());
		String json = mapper.writeValueAsString(rootNode);
		// { "text": "pepito is looking up juanito" }
		messagingTemplate.convertAndSend("/user/"+u.getUsername() +"/queue/updates", json);
        return "messagebox";
    }




	@PostMapping("/{id}/photo")
    @ResponseBody
    public String postPhoto(@RequestParam("photo") MultipartFile photo,@PathVariable("id") String id){
        File f = localData.getFile("user", id +".jpg");
        if (!photo.isEmpty()) {
            try (BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(f))) {
                byte[] bytes = photo.getBytes();
                stream.write(bytes);
            } catch (Exception e) {
                return "Error uploading " + id + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload a photo for " + id + ": empty?";
        }
        return "You successfully uploaded " + id + " into " + f.getAbsolutePath() + "!";
    }*/
}
