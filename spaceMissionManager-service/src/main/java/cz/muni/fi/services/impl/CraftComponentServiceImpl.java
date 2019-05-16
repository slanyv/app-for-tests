package cz.muni.fi.services.impl;

import cz.muni.fi.dao.CraftComponentDao;
import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.services.CraftComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Implementation of the {@link CraftComponentService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 *
 * @author Vojtech Bruza
 */
@Service
public class CraftComponentServiceImpl implements CraftComponentService {
    @Autowired
    private CraftComponentDao craftComponentDao;

    @Override
    public void addComponent(CraftComponent craftComponent) throws DataAccessException {
        if(craftComponent == null){
            throw new IllegalArgumentException("Component must not be null.");
        }
        try {
            craftComponentDao.addComponent(craftComponent);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Error when adding component.", e);
        }
    }

    @Override
    public List<CraftComponent> findAllComponents() throws DataAccessException {
        try {
            return Collections.unmodifiableList(craftComponentDao.findAllComponents());
        }  catch (Throwable e) {
            throw new ServiceDataAccessException("Could not find all components.", e);
        }
    }

    @Override
    public CraftComponent findComponentById(Long id) throws DataAccessException {
        if(id == null){
            throw new IllegalArgumentException("Id must not be null.");
        }
        try{
            return craftComponentDao.findComponentById(id);
        } catch (Throwable e) {
            throw new ServiceDataAccessException("Error when finding component.", e);
        }
    }

    @Override
    public void updateComponent(CraftComponent craftComponent) throws DataAccessException {
        if(craftComponent == null){
            throw new IllegalArgumentException("Component must not be null.");
        }
        try {
            craftComponentDao.updateComponent(craftComponent);
        }  catch (Throwable e) {
            throw new ServiceDataAccessException("Error when updating component.", e);
        }
    }

    @Override
    public void removeComponent(CraftComponent craftComponent) throws DataAccessException {
        if(craftComponent == null){
            throw new IllegalArgumentException("Component must not be null.");
        }
        try {
            craftComponentDao.removeComponent(craftComponent);
        }  catch (Throwable e) {
            throw new ServiceDataAccessException("Error when removing component.", e);
        }
    }
}
