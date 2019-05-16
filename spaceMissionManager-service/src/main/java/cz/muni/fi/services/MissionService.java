package cz.muni.fi.services;

import cz.muni.fi.entity.Mission;
import org.springframework.dao.DataAccessException;

import java.time.LocalDate;
import java.util.List;

public interface MissionService {
	/**
	 * Archives given mission.
	 * Sets the mission's end date and result - saving attributes of the mission object.
	 * Result string will contain all details about the completed mission.
	 * @param mission mission to archive
	 * @param endDate end date of the mission. Must be in the past, not in the future
	 */
	void archive(Mission mission, LocalDate endDate);

	/**
	 * Create entity in the database
	 * @param mission mission to be saved in the database
	 */
	void createMission(Mission mission) throws DataAccessException;

	/**
	 * Delete entity from the database
	 * @param mission mission to be deleted from the database
	 */
	void cancelMission(Mission mission) throws DataAccessException;

	/**
	 * Lists all mission in the database
	 * @return list of all missions in the database
	 */
	List<Mission> findAllMissions() throws DataAccessException;

	/**
	 * Find all active or inactive missions
	 * @param active state of the mission
	 * @return list of the missions with given status
	 */
	List<Mission> findAllMissions(boolean active) throws DataAccessException;

	/**
	 * Find mission with given id in the database
	 * @param id id of desired mission
	 * @return mission with selected id or null
	 */
	Mission findMissionById(Long id) throws DataAccessException;

	/**
	 * Update entity in the database
	 * @param mission mission to be updated
	 */
	void updateMission(Mission mission) throws DataAccessException;

}
