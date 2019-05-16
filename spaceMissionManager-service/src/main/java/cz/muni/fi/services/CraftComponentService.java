package cz.muni.fi.services;

import cz.muni.fi.entity.CraftComponent;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface CraftComponentService {
	/**
	 * Create new entity in the database
	 * @param craftComponent Component to add
	 */
	void addComponent(CraftComponent craftComponent) throws DataAccessException;

	List<CraftComponent> findAllComponents() throws DataAccessException;

	/**
	 * Find entity with given id in the database
	 * @param id id of component to find
	 */
	CraftComponent findComponentById(Long id) throws DataAccessException;

	/**
	 * Update entity in the database
	 * @param craftComponent craft component to update
	 */
	void updateComponent(CraftComponent craftComponent) throws DataAccessException;

	/**
	 * Delete entity from the database
	 * @param craftComponent component to remove
	 */
	void removeComponent(CraftComponent craftComponent) throws DataAccessException;

}
