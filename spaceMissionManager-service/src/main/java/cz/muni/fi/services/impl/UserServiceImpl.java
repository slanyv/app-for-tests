package cz.muni.fi.services.impl;

import cz.muni.fi.dao.UserDao;
import cz.muni.fi.entity.User;
import cz.muni.fi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of the {@link UserService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 *
 * Added auth methods - jsmadis
 * @author Vojtech Bruza
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    private Logger logger = Logger.getLogger(UserServiceImpl.class.getName());

    @Override
    public void acceptAssignedMission(User user){
        if(user == null){
            throw new IllegalArgumentException("User must not be null.");
        }
        if(user.missionStatusPending()){
            user.setAcceptedMission(true);
        } else {
            throw new IllegalArgumentException("This user does not have pending mission status.");
        }
        try {
            userDao.updateUser(user);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not update user.", e);
        }
    }

    @Override
    public void rejectAssignedMission(User user, String explanation) throws IllegalArgumentException{
        if(user == null){
            throw new IllegalArgumentException("User must not be null.");
        }
        if(explanation == null){
            throw new IllegalArgumentException("Explanation must not be null.");
        }
        if(explanation.isEmpty()){
            throw new IllegalArgumentException("Explanation must not be empty.");
        }
        if(user.missionStatusPending()){
            user.setExplanation(explanation);
            user.setMission(null);
        } else {
            throw new IllegalArgumentException("This user does not have pending mission status.");
        }
        try {
            userDao.updateUser(user);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not update user.", e);
        }
    }

    @Override
    public User addUser(User user) throws DataAccessException {
        if(user == null){
            throw new IllegalArgumentException("User is null");
        }
//        String unencryptedPassword = user.getPassword();
        try{
//            user.setPassword(createHash(unencryptedPassword));
            return userDao.addUser(user);
        }catch (Throwable t) {
            throw new ServiceDataAccessException("Can not create user: " + user.getEmail(), t);
        }
    }

    @Override
    public void updateUser(User user) throws DataAccessException {
        if(user == null){
            throw new IllegalArgumentException("User must not be null.");
        }
        try {
            userDao.updateUser(user);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not update user.", e);
        }
    }

    @Override
    public void deleteUser(User user) throws DataAccessException {
        if(user == null){
            throw new IllegalArgumentException("User must not be null.");
        }
        try {
            userDao.deleteUser(user);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not delete user.", e);
        }
    }

    @Override
    public List<User> findAllUsers() throws DataAccessException {
        try {
            return Collections.unmodifiableList(userDao.findAllUsers());
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not find users.", e);
        }
    }

    @Override
    public List<User> findAllAstronauts() throws DataAccessException {
        try {
            return Collections.unmodifiableList(userDao.findAllAstronauts());
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not find astronauts.", e);
        }
    }

    @Override
    public User findUserById(Long id) throws DataAccessException {
        if(id == null){
            throw new IllegalArgumentException("Id must not be null.");
        }
        try {
            return userDao.findUserById(id);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not find user.", e);
        }
    }

    @Override
    public List<User> findAllAvailableAstronauts() throws DataAccessException {
        try {
            return Collections.unmodifiableList(userDao.findAllAvailableAstronauts());
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not find astronauts.", e);
        }
    }

    @Override
    public User findUserByEmail(String email) throws DataAccessException {
        if (email == null){
            throw new IllegalArgumentException("Email is null");
        }
        try{
            return userDao.findUserByEmail(email);
        }catch (Throwable t){
            throw new ServiceDataAccessException("Could not find user by email: "+ email, t);
        }
    }

    @Override
    public boolean isManager(User user) throws DataAccessException {
        if(user == null){
            throw new IllegalArgumentException("User is null");
        }

        try{
            return findUserById(user.getId()).isManager();
        }catch (Throwable t){
            throw new ServiceDataAccessException("Could not find user when checking if is admin");
        }
    }


    @Override
    public boolean updatePassword(User user, String oldPassword, String newPassword) throws DataAccessException {
        if(user == null){
            throw new IllegalArgumentException("User is null");
        }
        User u = userDao.findUserById(user.getId());
        if(u == null){
            throw new IllegalArgumentException("User is null");
        }
        try{
            if(oldPassword.equals(u.getPassword())){
                user.setPassword(newPassword);
                userDao.updateUser(user);
                return true;
            }
            return false;
        }catch (Throwable t){
            throw new ServiceDataAccessException("Can not update user: " + user.getId() + "password", t);
        }
    }

    @Override
    public boolean authenticate(User user, String password) throws DataAccessException {
        if(user == null){
            throw new IllegalArgumentException("User is null");
        }

        User u = findUserById(user.getId());
        //return encoder.matches(password, u.getPassword());
        return u.getPassword().equals(password);
    }

}
