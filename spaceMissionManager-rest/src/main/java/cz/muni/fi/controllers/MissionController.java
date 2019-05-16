package cz.muni.fi.controllers;

import cz.muni.fi.ApiUris;
import cz.muni.fi.dto.MissionCreateDTO;
import cz.muni.fi.dto.MissionDTO;
import cz.muni.fi.exceptions.ResourceAlreadyExistsException;
import cz.muni.fi.exceptions.ResourceNotFoundException;
import cz.muni.fi.facade.MissionFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(ApiUris.ROOT_URI_MISSIONS)
public class MissionController {

    private final static Logger logger = Logger.getLogger(MissionController.class.getName());

    private final MissionFacade missionFacade;

    @Autowired
    public MissionController(MissionFacade missionFacade) {
        this.missionFacade = missionFacade;
    }

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Collection<MissionDTO> findAllMissions(@RequestParam(value="active", required=false) Boolean active) {
        logger.log(Level.INFO, "[REST] Finding all missions...");
        if (active == null) {
            return missionFacade.findAllMissions();
        }
        return missionFacade.findAllMissions(active);
    }

    @RolesAllowed({"MANAGER", "USER"})
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public MissionDTO findMissionById(@PathVariable("id") Long id){
        logger.log(Level.INFO, "[REST] Finding mission "+id+"...");
        MissionDTO missionDTO =  missionFacade.findMissionById(id);
        if (missionDTO == null){
            throw new ResourceNotFoundException();
        }
        return missionDTO;
    }

    @RolesAllowed("MANAGER")
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MissionDTO createMission(@RequestBody MissionCreateDTO missionCreateDTO) {
        logger.log(Level.INFO, "[REST] Creating new mission...");
        try {
            Long id = missionFacade.createMission(missionCreateDTO);
            return missionFacade.findMissionById(id);
        } catch (Exception e){
            logger.log(Level.WARNING, e.getMessage());
            throw new ResourceAlreadyExistsException();
        }
    }

    @RolesAllowed({"MANAGER"})
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public MissionDTO updateMission(@RequestBody MissionDTO missionDTO){
        logger.log(Level.INFO, "[REST] Updating mission "+missionDTO.getId()+"...");
        try {
            missionFacade.updateMission(missionDTO);
            return missionFacade.findMissionById(missionDTO.getId());
        } catch (Exception e){
            logger.log(Level.WARNING, e.getMessage());
            throw new ResourceNotFoundException();
        }
    }

    @RolesAllowed({"MANAGER"})
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MissionDTO> removeMission(@PathVariable("id") long id) {
        logger.log(Level.INFO, "[REST] Canceling mission "+id+"...");
        MissionDTO mission = missionFacade.findMissionById(id);
        if (mission == null){
            throw new ResourceNotFoundException();
        }
        missionFacade.cancelMission(mission);
        return missionFacade.findAllMissions();
    }

    @RolesAllowed({"MANAGER"})
    @RequestMapping(value = "/{id}/archive", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public MissionDTO archiveMission(@PathVariable("id") long id) {
        logger.log(Level.INFO, "[REST] Archiving mission "+id+"...");
        MissionDTO mission = missionFacade.findMissionById(id);
        if (mission == null){
            throw new ResourceNotFoundException();
        }
        missionFacade.archive(mission, LocalDate.now());
        return missionFacade.findMissionById(id);
    }

}
