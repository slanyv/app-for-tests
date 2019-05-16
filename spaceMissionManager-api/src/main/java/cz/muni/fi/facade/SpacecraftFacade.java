package cz.muni.fi.facade;

import cz.muni.fi.dto.SpacecraftCreateDTO;
import cz.muni.fi.dto.SpacecraftDTO;

import java.util.List;

public interface SpacecraftFacade {

	/**
	 * Create new entity in the database
	 * @param spacecraft entity to be persisted
	 * @return id of created spacecraft
	 */
	Long addSpacecraft(SpacecraftCreateDTO spacecraft);

	/**
	 * Remove entity from the database
	 * @param spacecraft entity to be deleted
	 */
	void removeSpacecraft(SpacecraftDTO spacecraft);

	/**
	 * Get list of all spacecrafts in the database
	 * @return list of all spacecrafts in the database
	 */
	List<SpacecraftDTO> findAllSpacecrafts();

	/**
	 * Find spacecraft by its id
	 * @param id to search for in the database
	 * @return reuquested spacecraft
	 */
	SpacecraftDTO findSpacecraftById(Long id);

	/**
	 * Update this spacecraft in the database
	 * @param spacecraft entity to be updated
	 */
	void updateSpacecraft(SpacecraftDTO spacecraft);
}
