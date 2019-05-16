package cz.muni.fi.facade;

import cz.muni.fi.dto.CraftComponentCreateDTO;
import cz.muni.fi.dto.CraftComponentDTO;

import java.util.List;

public interface CraftComponentFacade {
	/**
	 * Add given component
	 * @param craftComponent component to be addded
	 * @return Id of created component
	 */
	Long addComponent(CraftComponentCreateDTO craftComponent);

	/**
	 * Find all components
	 * @return list of all components
	 */
	List<CraftComponentDTO> findAllComponents();

	/**
	 * Find component with given id
	 * @param id id off component to find
	 */
	CraftComponentDTO findComponentById(Long id);

	/**
	 * Update given component
	 * @param craftComponent component to update
	 */
	void updateComponent(CraftComponentDTO craftComponent);

	/**
	 * Remove given component
	 * @param craftComponent component to remove
	 */
	void removeComponent(CraftComponentDTO craftComponent);
}
