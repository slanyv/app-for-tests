package cz.muni.fi.controllers;


import cz.muni.fi.ApiUris;
import cz.muni.fi.dto.MissionDTO;
import cz.muni.fi.dto.UserCreateDTO;
import cz.muni.fi.dto.UserDTO;
import cz.muni.fi.exceptions.ResourceAlreadyExistsException;
import cz.muni.fi.exceptions.ResourceNotFoundException;
import cz.muni.fi.facade.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.Collection;

/**
 * Created by jsmadis
 *
 * @author jsmadis
 */

@RestController
@RequestMapping(ApiUris.ROOT_URI_USERS)
public class UsersController {

    private final static Logger logger = LoggerFactory.getLogger(UsersController.class);


    @Autowired
    private UserFacade userFacade;

    @Autowired
    private PasswordEncoder encoder;

    /**
     * Returns all users
     *
     * @return Collection of UserDTOs
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<UserDTO> getUsers() {
        logger.debug("[REST] getUsers()");
        return userFacade.findAllUsers();

    }

    /**
     * Returns user by ID
     *
     * @param id id of user
     * @return UserDTO
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO getUser(@PathVariable("id") long id) {
        logger.debug("[REST] getUser() id=" + id);
        UserDTO userDTO = userFacade.findUserById(id);
        if (userDTO == null) {
            throw new ResourceNotFoundException();
        }
        return userDTO;
    }

    /**
     * Creates and returns user
     *
     * @param user UserCreateDTO
     * @return UserDTO
     */

    @RolesAllowed("MANAGER")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO createUser(@RequestBody UserCreateDTO user) {

        logger.debug("[REST] createUser()");

        try {
            user.setPassword(encoder.encode(user.getPassword()));
            Long id = userFacade.addUser(user);
            return userFacade.findUserById(id);
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new ResourceAlreadyExistsException();
        }
    }

    /**
     * Deletes user and returns all remaining users
     *
     * @param id
     * @return
     */

    @RolesAllowed("MANAGER")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Collection<UserDTO> deleteUser(@PathVariable Long id) {
        logger.debug("[REST] deleteUser()");

        UserDTO user = userFacade.findUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        userFacade.deleteUser(user);
        return userFacade.findAllUsers();
    }

    /**
     * Returns all astronauts
     *
     * @return collection of UserDTOs
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/astronauts", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<UserDTO> getAstronauts() {
        logger.debug("[REST] getAstronauts()");

        return userFacade.findAllAstronauts();
    }

    /**
     * Returns all available astronauts
     *
     * @return collection of UserDTOs
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/astronauts/available", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<UserDTO> getAvailableAstronauts() {
        logger.debug("[REST] getAvailableAstronauts()");

        return userFacade.findAllAvailableAstronauts();
    }

    /**
     * Updates and returns user
     *
     * @param user UserDTO
     * @return UserDTO
     */

    @RolesAllowed("MANAGER")
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO updateUser(@RequestBody UserDTO user) {
        logger.debug("[REST] updateUser()");

        try {
            user.setMission(userFacade.findUserById(user.getId()).getMission());
            user.setPassword(encoder.encode(user.getPassword()));
            userFacade.updateUser(user);
            return user;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new ResourceAlreadyExistsException();
        }
    }

    /**
     * Updates and returns user
     *
     * @param user UserDTO
     * @return UserDTO
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/profile", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO updateUserSelf(@RequestBody UserDTO user, Authentication authentication) {
        logger.debug("[REST] updateUserSelf()");
        logger.debug(authentication.getName());

        try {
            UserDTO storedUser = userFacade.findUserByEmail(authentication.getName());



            logger.debug("[REST] password length: "+user.getPassword().length());

            if (user.getPassword().length() >= 3 && user.getPassword().length() <= 150) {
                storedUser.setPassword(encoder.encode(user.getPassword()));
            }

            storedUser.setEmail(user.getEmail());
            storedUser.setName(user.getName());
            userFacade.updateUser(storedUser);
            return user;
        } catch (Exception e) {
            logger.warn(e.getMessage());
            throw new ResourceAlreadyExistsException();
        }
    }


    /**
     * Accepts mission for user
     *
     * @param id id of user
     * @return UserDTO
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/{id}/acceptMission", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO acceptMission(@PathVariable Long id) {
        logger.debug("[REST] acceptMission()");

        UserDTO user = userFacade.findUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException();
        }

        userFacade.acceptAssignedMission(user);
        user = userFacade.findUserById(id);
        return user;
    }

    /**
     * Rejects mission for user
     *
     * @param id          id of user
     * @param explanation explanation of user
     * @return UserDTO
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "{id}/rejectMission", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO rejectMission(@PathVariable Long id, @RequestBody String explanation) {
        logger.debug("[REST] rejectMission");

        UserDTO user = userFacade.findUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        userFacade.rejectAssignedMission(user, explanation);
        user = userFacade.findUserById(id);
        return user;
    }

    /**
     * Returns if user is manager
     *
     * @param user user
     * @return true if successful, false otherwise
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/manager", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean isManager(@RequestBody UserDTO user) {
        logger.debug("[REST] is manager");
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        return userFacade.isManager(user);
    }

    /**
     * Finds user by email
     *
     * @param email email
     * @return userDTO
     */

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/email", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO findByEmail(@RequestBody String email) {
        logger.debug("[REST] find by email");
        UserDTO user = userFacade.findUserByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        return user;
    }

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/mission/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public MissionDTO getMissionForUser(@PathVariable Long id) {
        logger.debug("[REST] getMission");

        UserDTO user = userFacade.findUserById(id);
        if (user == null) {
            throw new ResourceNotFoundException();
        }
        return user.getMission();
    }
}
