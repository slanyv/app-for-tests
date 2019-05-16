package cz.muni.fi;


import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.entity.Mission;
import cz.muni.fi.entity.Spacecraft;
import cz.muni.fi.entity.User;
import cz.muni.fi.services.CraftComponentService;
import cz.muni.fi.services.MissionService;
import cz.muni.fi.services.SpacecraftService;
import cz.muni.fi.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class SampleDataLoadingFacadeImpl implements SampleDataLoadingFacade {

	final static Logger log = LoggerFactory.getLogger(SampleDataLoadingFacadeImpl.class);

	@Autowired
	private UserService userService;
	@Autowired
	private CraftComponentService craftComponentService;
	@Autowired
	private MissionService missionService;
	@Autowired
	private SpacecraftService spacecraftService;

	@Autowired
	private PasswordEncoder encoder;


	@Override
	public void loadData() throws IOException {
		loadCC();
		loadUsers();
		loadSpacecrafts();
		loadMissions();
	}


	private void loadUsers() {

		User user = new User();
		user.setName("ADMIN");
		user.setBirthDate(LocalDate.now().minusYears(20));
		user.setEmail("admin@admin.com");
		user.setPassword(encoder.encode("password"));
		user.setManager(true);
		userService.addUser(user);

		User gagarin = new User();
		gagarin.setName("Gagarin");
		gagarin.setBirthDate(LocalDate.of(1934, Month.MARCH, 9));
		gagarin.setEmail("gagarin@gmail.com");
		gagarin.setPassword(encoder.encode("gagarin"));
		gagarin.setManager(false);
		gagarin.setExperienceLevel(10);
		userService.addUser(gagarin);

		gagarin = new User();
		gagarin.setName("Han Solo");
		gagarin.setBirthDate(LocalDate.of(1978, Month.MARCH, 9));
		gagarin.setEmail("solo@gmail.com");
		gagarin.setPassword(encoder.encode("SOLO1"));
		gagarin.setManager(false);
		gagarin.setExperienceLevel(35);
		userService.addUser(gagarin);

	}


	private void loadCC() {
		CraftComponent craftComponent = new CraftComponent();
		craftComponent.setName("Wing");
		craftComponent.setReadyToUse(false);
		craftComponent.setReadyDate(ZonedDateTime.now().plusDays(5));
		craftComponentService.addComponent(craftComponent);

		craftComponent = new CraftComponent();
		craftComponent.setName("Fuel tank");
		craftComponent.setReadyToUse(true);
		//craftComponent.setReadyDate(ZonedDateTime.now().plusDays(5));
		craftComponentService.addComponent(craftComponent);

		craftComponent = new CraftComponent();
		craftComponent.setName("Engine");
		craftComponent.setReadyToUse(false);
		craftComponent.setReadyDate(ZonedDateTime.now().plusDays(40));
		craftComponentService.addComponent(craftComponent);

		craftComponent = new CraftComponent();
		craftComponent.setName("Cockpit");
		craftComponent.setReadyToUse(true);
//		craftComponent.setReadyDate(ZonedDateTime.now().plusDays(5));
		craftComponentService.addComponent(craftComponent);

		craftComponent = new CraftComponent();
		craftComponent.setName("TestDelete");
		craftComponent.setReadyToUse(true);
//		craftComponent.setReadyDate(ZonedDateTime.now().plusDays(5));
		craftComponentService.addComponent(craftComponent);

	}

	private void loadSpacecrafts() {
		List<CraftComponent> ccs = craftComponentService.findAllComponents();
		Spacecraft spacecraft = new Spacecraft();
		spacecraft.setName("Apollo 11");
		spacecraft.setType("Manned spacecraft");
		spacecraft.addComponent(ccs.get(0));
		spacecraft.addComponent(ccs.get(1));
		spacecraftService.addSpacecraft(spacecraft);

		spacecraft = new Spacecraft();
		spacecraft.setName("Falcon 9");
		spacecraft.setType("Supply rocket");
		spacecraft.addComponent(ccs.get(2));
		spacecraftService.addSpacecraft(spacecraft);

		spacecraft = new Spacecraft();
		spacecraft.setName("Millenium falcon");
		spacecraft.setType("Smuggler ship");
		spacecraft.addComponent(ccs.get(3));
		spacecraftService.addSpacecraft(spacecraft);
	}

	private void loadMissions(){
		List<Spacecraft> spacecrafts = spacecraftService.findAllSpacecrafts();
		List<User> users = userService.findAllAvailableAstronauts();

		Mission mission = new Mission();
		mission.setName("Mars mission");
		mission.setDestination("Mars");
		mission.setEta(ZonedDateTime.now().plusDays(50));
		mission.setMissionDescription("Simple mission to colonize Mars");
		mission.setActive(true);
		mission.addSpacecraft(spacecrafts.get(0));
		mission.addAstronaut(users.get(0));
		missionService.createMission(mission);

		mission = new Mission();
		mission.setName("Supply Mission");
		mission.setDestination("ISS");
		mission.setEta(ZonedDateTime.now().plusDays(40));
		mission.setMissionDescription("Simple mission to send supplies to ISS");
		mission.setActive(true);
		mission.addSpacecraft(spacecrafts.get(1));
		missionService.createMission(mission);

		mission = new Mission();
		mission.setName("Kessel run");
		mission.setDestination("Galaxy far far away");
		mission.setEta(ZonedDateTime.now().plusDays(12));
		mission.setMissionDescription("Test to see how fast can han do a kessel run");
		mission.addAstronaut(users.get(1));
		mission.addSpacecraft(spacecrafts.get(2));
		mission.setActive(true);
		missionService.createMission(mission);
	}

}
