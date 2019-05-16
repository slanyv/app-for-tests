package cz.muni.fi.entity;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Spacecraft {

	//ATTRIBUTES
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String type; //just probes or astronauts...

    @Column(nullable = false, unique = true)
	private String name;

	@OneToMany(mappedBy = "spacecraft")
	private Set<CraftComponent> components = new HashSet<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mission_id")
	private Mission mission;


	//METHODS
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

	public void addComponent(CraftComponent c){
	    if(components.contains(c)) return;
		components.add(c);
		c.setSpacecraft(this);
	}

	public void removeComponent(CraftComponent c){
		components.remove(c);
		c.setSpacecraft(null);
	}

	public Set<CraftComponent> getComponents() {
		return Collections.unmodifiableSet(components);
	}

	public Mission getMission() {
		return mission;
	}

	public void setMission(Mission mission) {
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
		if (!(o instanceof Spacecraft)) return false;
		Spacecraft spacecraft = (Spacecraft) o;
		return getName() != null ? getName().equals(spacecraft.getName()) : spacecraft.getName() == null;
	}

	@Override
	public int hashCode() {
		return getName() != null ? getName().hashCode() : 0;
	}

	@Override
	public String toString() {
		return "Spacecraft:\n" +
				"Name " + name + '\n' +
				"Type " + type + '\n' +
				"Components: \n" + components+'\n';
	}
}