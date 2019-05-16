package cz.muni.fi.dao;

import cz.muni.fi.entity.CraftComponent;

import java.util.List;

public interface CraftComponentDao {

	/**
	 * 
	 * @param craftComponent componnent to add
	 */
	CraftComponent addComponent(CraftComponent craftComponent);

	List<CraftComponent> findAllComponents();

	/**
	 * 
	 * @param id id of craft component
	 */
	CraftComponent findComponentById(Long id);

	/**
	 * 
	 * @param craftComponent component to update
	 */
	CraftComponent updateComponent(CraftComponent craftComponent);

	/**
	 * 
	 * @param craftComponent component to remove
	 */
	CraftComponent removeComponent(CraftComponent craftComponent);

}