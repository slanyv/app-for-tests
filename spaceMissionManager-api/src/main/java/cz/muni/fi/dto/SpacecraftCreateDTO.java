package cz.muni.fi.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SpacecraftCreateDTO {

	private String type;

	@NotNull
	@Size(min = 3, max = 50)
	private String name;

	@NotNull
	@Size(min = 1)
	private Set<CraftComponentDTO> components = new HashSet<>();

	private MissionDTO mission;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Set<CraftComponentDTO> getComponents() {
		return Collections.unmodifiableSet(components);
	}

	public MissionDTO getMission() {
		return mission;
	}

	public void setMission(MissionDTO mission) {
		this.mission = mission;
	}

	public void setComponents(Set<CraftComponentDTO> components) {
		this.components = components;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
