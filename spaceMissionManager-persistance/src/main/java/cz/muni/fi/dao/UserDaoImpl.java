package cz.muni.fi.dao;

import cz.muni.fi.entity.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

/**
 * @author jsmadis
 */

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    public User addUser(User user) {
        validateUser(user);
        if(user.getId() != null){
            throw new IllegalArgumentException("User id is not null");
        }
        em.persist(user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        if(user.getId() == null){
            throw new IllegalArgumentException("User id should not be null");
        }
        em.merge(user);
        em.flush();
        return user;
    }

    @Override
    public User deleteUser(User user) {
        if (user == null){
            throw new IllegalArgumentException(User.class.getName());
        }
        em.remove(em.merge(user));
        return user;
    }

    @Override
    public List<User> findAllUsers() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    @Override
    public List<User> findAllAstronauts() {
        return em.createQuery("select u from User u where u.isManager = :bool", User.class)
                .setParameter("bool", false)
                .getResultList();
    }

    @Override
    public User findUserById(Long id) {
        if (id == null){
            throw new IllegalArgumentException("User id is null");
        }
        try {
            return em.createQuery("select u from User u fetch all properties where u.id = :id", User.class)
                    .setParameter("id", id).getSingleResult();
        }
        catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public List<User> findAllAvailableAstronauts() {
        return em.createQuery("select u from User u where u.mission is null and u.isManager = :bool", User.class)
                .setParameter("bool", false)
                .getResultList();
    }

    @Override
    public User findUserByEmail(String email) {
        if (email == null){
            throw new IllegalArgumentException("User email is null");
        }
        if(!email.matches(".+@.+\\....?")){
            throw new IllegalArgumentException("Email has wrong format");
        }
        try {
            return em.createQuery("select u from User u fetch all properties where u.email = :email", User.class)
                    .setParameter("email", email).getSingleResult();
        }
        catch (NoResultException ex){
            return null;
        }
    }

    /**
     * Validation of user
     * @param user User to validate
     */
    private void validateUser(User user){
        if (user == null) {
            throw new IllegalArgumentException("User is null");
        }
        if(user.getName() == null){
            throw new IllegalArgumentException("User name should not be null");
        }
        if(user.getBirthDate() == null){
            throw new IllegalArgumentException("User birthday should not be null");
        }
        if(user.getEmail() == null){
            throw new IllegalArgumentException("User email should not be null");
        }
        if(!user.getEmail().matches(".+@.+\\....?")){
            throw new IllegalArgumentException("User email has wrong format");
        }
        if(user.getBirthDate().isAfter(LocalDate.now())){
            throw new IllegalArgumentException("User birthday should be in the past");
        }
        if(user.getPassword() == null){
            throw new IllegalArgumentException("User password should not be null");
        }
    }

}
