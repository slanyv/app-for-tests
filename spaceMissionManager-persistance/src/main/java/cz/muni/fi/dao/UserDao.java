package cz.muni.fi.dao;

import cz.muni.fi.entity.User;

import java.util.List;

/**
 * Created by jsmadis.
 *
 * @author jsmadis
 */

public interface UserDao {

    /**
     * Persist user into database
     *
     * @param user instance of user
     */
    User addUser(User user);

    /**
     * Update user in database
     *
     * @param user Instance of user
     */
    User updateUser(User user);

    /**
     * Delete user from database
     *
     * @param user instance of user
     */
    User deleteUser(User user);

    /**
     * Find all users
     *
     * @return List of all users, null if none
     */
    List<User> findAllUsers();

    /**
     * Find all astronauts
     *
     * @return list of
     */

    List<User> findAllAstronauts();

    /**
     * Find user by id
     *
     * @param id User id
     * @return User instance of user
     */
    User findUserById(Long id);

    /**
     * Find all available astronauts
     *
     * @return List of available astronauts
     */
    List<User> findAllAvailableAstronauts();

    /**
     * Find user by email
     * @param email email
     * @return user
     */

    User findUserByEmail(String email);
}
