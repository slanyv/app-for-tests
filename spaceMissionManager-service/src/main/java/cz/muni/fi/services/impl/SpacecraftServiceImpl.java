package cz.muni.fi.services.impl;

import cz.muni.fi.dao.SpacecraftDao;
import cz.muni.fi.entity.Spacecraft;
import cz.muni.fi.services.SpacecraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of the {@link SpacecraftService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 *
 * @author Vojtech Bruza
 */
@Service
public class SpacecraftServiceImpl implements SpacecraftService {
    @Autowired
    private SpacecraftDao spacecraftDao;

    @Override
    public void addSpacecraft(Spacecraft spacecraft) throws DataAccessException {
        if (spacecraft == null){
            throw new IllegalArgumentException("Spacecraft must not be null.");
        }
        try {
            spacecraftDao.addSpacecraft(spacecraft);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not add spacecraft.", e);
        }
    }

    @Override
    public void removeSpacecraft(Spacecraft spacecraft) throws DataAccessException {
        if (spacecraft == null){
            throw new IllegalArgumentException("Spacecraft must not be null.");
        }
        try {
            spacecraftDao.removeSpacecraft(spacecraft);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not remove spacecraft.", e);
        }
    }

    @Override
    public List<Spacecraft> findAllSpacecrafts() throws DataAccessException {
        try {
            return Collections.unmodifiableList(spacecraftDao.findAllSpacecrafts());
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not find spacecrafts.", e);
        }
    }

    @Override
    public Spacecraft findSpacecraftById(Long id) throws DataAccessException {
        if (id == null){
            throw new IllegalArgumentException("Id must not be null.");
        }
        try {
            return spacecraftDao.findSpacecraftById(id);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not find spacecraft.", e);
        }
    }

    @Override
    public void updateSpacecraft(Spacecraft spacecraft) throws DataAccessException {
        if (spacecraft == null){
            throw new IllegalArgumentException("Spacecraft must not be null.");
        }
        try {
            spacecraftDao.updateSpacecraft(spacecraft);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Could not update spacecraft.", e);
        }
    }
}
