package cz.muni.fi.services;

import cz.muni.fi.entity.Spacecraft;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface SpacecraftService {
	/**
	 * Create new entity in the database
	 * @param spacecraft entity to be persisted
	 */
	void addSpacecraft(Spacecraft spacecraft) throws DataAccessException;

	/**
	 * Remove entity from the database
	 * @param spacecraft entity to be deleted
	 */
	void removeSpacecraft(Spacecraft spacecraft) throws DataAccessException;

	/**
	 * Get list of all spacecrafts in the database
	 * @return list of all spacecrafts in the database
	 */
	List<Spacecraft> findAllSpacecrafts() throws DataAccessException;

	/**
	 * Find spacecraft by its id
	 * @param id to search for in the database
	 * @return reuquested spacecraft
	 */
	Spacecraft findSpacecraftById(Long id) throws DataAccessException;

	/**
	 * Update this spacecraft in the database
	 * @param spacecraft entity to be updated
	 */
	void updateSpacecraft(Spacecraft spacecraft) throws DataAccessException;
}
