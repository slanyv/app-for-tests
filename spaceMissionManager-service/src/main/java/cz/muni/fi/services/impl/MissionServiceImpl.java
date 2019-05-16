package cz.muni.fi.services.impl;

import cz.muni.fi.dao.MissionDao;
import cz.muni.fi.entity.Mission;
import cz.muni.fi.services.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the {@link MissionService}. This class is part of the
 * service module of the application that provides the implementation of the
 * business logic (main logic of the application).
 *
 * @author Vojtech Bruza
 */
@Service
public class MissionServiceImpl implements MissionService {
	@Autowired
	private MissionDao missionDao;

	@Override
	public void archive(Mission mission, LocalDate endDate) {
		if (mission == null) {
			throw new IllegalArgumentException("Mission must not be null.");
		}
		if (endDate == null) {
			throw new IllegalArgumentException("End date must not be null.");
		}
		if (endDate.isAfter(LocalDate.now())) {
			throw new IllegalArgumentException("Mission end date must be in the past.");
		}
		mission.setEndDate(endDate);
		mission.setActive(false);
		mission.getAstronauts().forEach(p->{
			if(!p.getAcceptedMission()){
				mission.removeAstronaut(p);
			}
		});
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder
				.append("Name of mission: ").append(mission.getName())
				.append("\nDestination: ").append(mission.getDestination())
				.append("\nEta: ").append(mission.getEta())
				.append("\nMission description: ").append(mission.getMissionDescription())
				.append("\nEnd date: ").append(mission.getEndDate()).append('\n');
		mission.getAstronauts().forEach(p -> {
			stringBuilder.append(p.toString());
			mission.removeAstronaut(p);
		});
		mission.getSpacecrafts().forEach(p -> stringBuilder.append(p.toString()));
		mission.setResult(stringBuilder.toString());
		try {
			missionDao.updateMission(mission);
		} catch (Throwable e) {
			throw new ServiceDataAccessException("Could not archive mission.", e);
		}
	}

	@Override
	public void createMission(Mission mission) throws DataAccessException {
		if (mission == null) {
			throw new IllegalArgumentException("Mission must not be null.");
		}
		try {
			missionDao.createMission(mission);
		} catch (Throwable e) {
			throw new ServiceDataAccessException("Could not create mission.", e);
		}
	}

	@Override
	public void cancelMission(Mission mission) throws DataAccessException {
		if (mission == null) {
			throw new IllegalArgumentException("Mission must not be null.");
		}
		try {
			missionDao.cancelMission(mission);
		} catch (Throwable e) {
			throw new ServiceDataAccessException("Could not cancel mission.", e);
		}
	}

	@Override
	public List<Mission> findAllMissions() throws DataAccessException {
		try {
			return Collections.unmodifiableList(missionDao.findAllMissions());
		} catch (Throwable e) {
			throw new ServiceDataAccessException("Could not find missions.", e);
		}
	}

	@Override
	public List<Mission> findAllMissions(boolean active) throws DataAccessException {
		try {
			return Collections.unmodifiableList(missionDao.findAllMissions(active));
		} catch (Throwable e) {
			throw new ServiceDataAccessException("Could not find missions.", e);
		}
	}

	@Override
	public Mission findMissionById(Long id) throws DataAccessException {
		if (id == null) {
			throw new IllegalArgumentException("Id must not be null.");
		}
		try {
			return missionDao.findMissionById(id);
		} catch (Throwable e) {
			throw new ServiceDataAccessException("Could not find mission.", e);
		}
	}

	@Override
	public void updateMission(Mission mission) throws DataAccessException {
		if (mission == null) {
			throw new IllegalArgumentException("Mission must not be null.");
		}
		if (mission.getId() == null) {
			throw new IllegalArgumentException("Mission ID is null, could not update.");
		}

		Mission foundMission = missionDao.findMissionById(mission.getId());
		if (foundMission == null) {
			throw new ServiceDataAccessException("Could not find mission.");
		}

		boolean missionActive = mission.isActive() && foundMission.isActive();

		if (missionActive) {
			try {
				missionDao.updateMission(mission);
			} catch (Throwable e) {
				throw new ServiceDataAccessException("Could not update mission.", e);
			}
		} else throw new IllegalArgumentException("Cannot modify already archived mission");
	}
}
