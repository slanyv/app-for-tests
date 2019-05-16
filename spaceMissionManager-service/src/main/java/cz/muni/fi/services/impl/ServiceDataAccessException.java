package cz.muni.fi.services.impl;

import org.springframework.dao.DataAccessException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Thrown when there is a problem with accessing the data on the lower layer.
 * @author Vojtech Bruza
 */
public class ServiceDataAccessException extends DataAccessException {
    private Logger logger = Logger.getLogger(MissionServiceImpl.class.getName());

    public ServiceDataAccessException(String msg) {
        super(msg);
        logger.log(Level.SEVERE, msg);
    }

    public ServiceDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
        logger.log(Level.SEVERE, msg, cause);

    }
}
