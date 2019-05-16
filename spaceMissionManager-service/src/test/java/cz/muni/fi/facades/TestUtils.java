package cz.muni.fi.facades;

import cz.muni.fi.dto.CraftComponentCreateDTO;
import cz.muni.fi.dto.MissionCreateDTO;
import cz.muni.fi.dto.SpacecraftCreateDTO;

import java.time.ZonedDateTime;

public class TestUtils {

	public static CraftComponentCreateDTO getCraftComponentCreateDTO(String name){

		CraftComponentCreateDTO craftComponentCreateDTO = new CraftComponentCreateDTO();
		craftComponentCreateDTO.setName(name);
		craftComponentCreateDTO.setReadyDate(ZonedDateTime.now().plusDays(20));
		return craftComponentCreateDTO;
	}

	public static SpacecraftCreateDTO getSpacecraftCreateDTO(String name){
		SpacecraftCreateDTO spacecraftCreateDTO = new SpacecraftCreateDTO();
		spacecraftCreateDTO.setName(name);
		return spacecraftCreateDTO;
	}

	public static MissionCreateDTO getMissionCreateDTO(String name){

		MissionCreateDTO missionCreateDTO = new MissionCreateDTO();
		missionCreateDTO.setName(name);
		missionCreateDTO.setDestination(name);
		missionCreateDTO.setEta(ZonedDateTime.now().plusDays(100));
		missionCreateDTO.setMissionDescription(name);
		return missionCreateDTO;
	}

}
