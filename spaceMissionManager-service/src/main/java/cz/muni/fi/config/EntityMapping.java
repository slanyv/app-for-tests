package cz.muni.fi.config;

import cz.muni.fi.dto.*;
import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.entity.Mission;
import cz.muni.fi.entity.Spacecraft;
import cz.muni.fi.entity.User;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.dozer.loader.api.TypeMappingOptions;

public class EntityMapping extends BeanMappingBuilder {
	@Override
	protected void configure() {
		mapping(Mission.class, MissionDTO.class, TypeMappingOptions.mapNull(false))
				.fields(field("astronauts").accessible(true), field("astronauts").accessible(true))
				.fields(field("spacecrafts").accessible(true), field("spacecrafts").accessible(true))
				.fields(field("eta"), field("eta"), FieldsMappingOptions.copyByReference());
		mapping(Mission.class, MissionCreateDTO.class, TypeMappingOptions.mapNull(false))
				.fields(field("astronauts").accessible(true), field("astronauts").accessible(true))
				.fields(field("spacecrafts").accessible(true), field("spacecrafts").accessible(true))
				.fields(field("eta"), field("eta"), FieldsMappingOptions.copyByReference());
		mapping(CraftComponent.class, CraftComponentDTO.class, TypeMappingOptions.mapNull(false))
				.fields(field("spacecraft").accessible(true), field("spacecraft").accessible(true))
				.fields(field("readyDate"), field("readyDate"), FieldsMappingOptions.copyByReference());
		mapping(CraftComponentCreateDTO.class, CraftComponent.class, TypeMappingOptions.mapNull(false))
				.fields(field("spacecraft").accessible(true), field("spacecraft").accessible(true))
				.fields(field("readyDate"), field("readyDate"), FieldsMappingOptions.copyByReference());
		mapping(Spacecraft.class, SpacecraftDTO.class, TypeMappingOptions.mapNull(false))
				.fields(field("mission").accessible(true), field("mission").accessible(true))
				.fields(field("components").accessible(true), field("components").accessible(true));
		mapping(SpacecraftCreateDTO.class, Spacecraft.class, TypeMappingOptions.mapNull(false))
				.fields(field("mission").accessible(true), field("mission").accessible(true))
				.fields(field("components").accessible(true), field("components").accessible(true));
		mapping(User.class, UserDTO.class, TypeMappingOptions.mapNull(false))
				.fields(field("mission").accessible(true), field("mission").accessible(true));
		mapping(User.class, UserCreateDTO.class, TypeMappingOptions.mapNull(false))
				.fields(field("mission").accessible(true), field("mission").accessible(true));
		//TODO add other mapping options in future
	}
}
