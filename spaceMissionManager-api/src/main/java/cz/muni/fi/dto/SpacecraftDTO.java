package cz.muni.fi.dto;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SpacecraftDTO {

	private Long id;

	private String type;

	private String name;

	@JsonManagedReference
	private Set<CraftComponentDTO> components = new HashSet<>();

	@JsonBackReference
	private MissionDTO mission;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addComponent(CraftComponentDTO c){
		components.add(c);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SpacecraftDTO)) return false;
		SpacecraftDTO spacecraft = (SpacecraftDTO) o;
		return getName() != null ? getName().equals(spacecraft.getName()) : spacecraft.getName() == null;
	}

	@Override
	public int hashCode() {
		return getName() != null ? getName().hashCode() : 0;
	}
}
