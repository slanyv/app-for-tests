package cz.muni.fi.dao;

import cz.muni.fi.ApplicationContext;
import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.entity.Mission;
import cz.muni.fi.entity.Spacecraft;
import cz.muni.fi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by jsmadis
 *
 * @author jsmadis
 */

@ContextConfiguration(classes = ApplicationContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class MissionDaoTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private MissionDao missionDao;

    @Autowired
    private SpacecraftDao spacecraftDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private CraftComponentDao craftComponentDao;

    private Mission mission1;
    private Mission mission2;
    private Mission inActiveMission;
    private Mission missionWithOutUser;

    private User user1;
    private User user2;
    private User user3;

    private Spacecraft spacecraft1;
    private Spacecraft spacecraft2;
    private Spacecraft spacecraft3;
    private Spacecraft spacecraft4;

    private CraftComponent craftComponent1;
    private CraftComponent craftComponent2;
    private CraftComponent craftComponent3;
    private CraftComponent craftComponent4;

    @BeforeMethod
    public void createMissions() {
        mission1 = getMission("Apollo", true);
        mission2 = getMission("Aura", true);
        inActiveMission = getMission("Pluto is a Planet!", false);
        missionWithOutUser = getMission("Curiosity", true);

        user1 = getUser("Ezio");
        user2 = getUser("Altair");
        user3 = getUser("Victor");

        spacecraft1 = getSpacecraft("Mars rover");
        spacecraft2 = getSpacecraft("TIE Fighter");
        spacecraft3 = getSpacecraft("TIE Bomber");
        spacecraft4 = getSpacecraft("Millennium Falcon");

        craftComponent1 = getCraftComponent("Lightsaber");
        craftComponent2 = getCraftComponent("Hyperdrive");
        craftComponent3 = getCraftComponent("Warpdrive");
        craftComponent4 = getCraftComponent("Teleport");


        spacecraft1.addComponent(craftComponent1);
        spacecraft2.addComponent(craftComponent2);
        spacecraft3.addComponent(craftComponent3);
        spacecraft4.addComponent(craftComponent4);


        mission1.addAstronaut(user1);
        mission2.addAstronaut(user2);
        inActiveMission.addAstronaut(user3);

        mission1.addSpacecraft(spacecraft1);
        mission2.addSpacecraft(spacecraft2);
        inActiveMission.addSpacecraft(spacecraft3);
        missionWithOutUser.addSpacecraft(spacecraft4);


        craftComponentDao.addComponent(craftComponent2);
        spacecraftDao.addSpacecraft(spacecraft2);
        userDao.addUser(user2);
        missionDao.createMission(mission2);

        assertThat(missionDao.findAllMissions())
                .contains(mission2);
        assertThat(userDao.findAllUsers())
                .contains(user2);
        assertThat(spacecraftDao.findAllSpacecrafts())
                .contains(spacecraft2);
    }

    @Test
    public void createMission() {
        missionDao.createMission(mission1);
        Mission createdMission = missionDao.findMissionById(mission1.getId());

        assertThat(createdMission)
                .isEqualToComparingFieldByField(mission1);
        assertThat(missionDao.findAllMissions())
                .contains(mission1);
        assertThat(missionDao.findAllMissions(true))
                .contains(mission1);
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createExistingMission() {
        missionDao.createMission(mission1);
        missionDao.createMission(mission1);

    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createMissionWithIdNotNull() {
        mission1.setId(100l);
        missionDao.createMission(mission1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNullMission() {
        missionDao.createMission(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createMissionWithNullName() {
        mission1.setName(null);
        missionDao.createMission(mission1);
    }

    @Test(expectedExceptions = javax.persistence.PersistenceException.class)
    public void createMissionWithDuplicatedName() {
        mission1.setName(mission2.getName());
        missionDao.createMission(mission1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createMissionWithETAInPast() {
        mission1.setEta(ZonedDateTime.now().minusMinutes(9));
        missionDao.createMission(mission1);
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createMissionWithEmptySpacecrafts() {
        mission1.removeSpacecraft(spacecraft1);
        missionDao.createMission(mission1);
    }

    @Test
    public void createMissionWithoutAstronauts() {
        missionDao.createMission(missionWithOutUser);

        Mission mission = missionDao.findMissionById(missionWithOutUser.getId());

        assertThat(mission)
                .isEqualToComparingFieldByField(missionWithOutUser);
    }

    @Test
    public void updateMission() {
        Mission mission = missionDao.findMissionById(mission2.getId());
        mission.setName("updated");
        missionDao.updateMission(mission);
        Mission updated = missionDao.findMissionById(mission2.getId());

        assertThat(updated.getName())
                .isEqualTo("updated");
        assertThat(updated)
                .isEqualToComparingFieldByField(mission);
    }

    @Test(expectedExceptions = PersistenceException.class)
    public void updateMissionWithDuplicatedName() {
        missionDao.createMission(missionWithOutUser);

        Mission mission = missionDao.findMissionById(mission2.getId());
        mission.setName(missionWithOutUser.getName());

        missionDao.updateMission(mission);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateMissionWithETAInPast() {
        Mission mission = missionDao.findMissionById(mission2.getId());
        mission.setEta(ZonedDateTime.now().minusMinutes(9));
        missionDao.updateMission(mission);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateMissionWithNullId() {
        missionDao.updateMission(mission1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateMissionWithEmptySpacecrafts() {
        Mission updated = missionDao.findMissionById(mission2.getId());
        updated.removeSpacecraft(spacecraft2);

        missionDao.updateMission(updated);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateMissionWithNullName() {
        Mission mission = missionDao.findMissionById(mission2.getId());
        mission.setName(null);
        missionDao.updateMission(mission);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateNullMission() {
        missionDao.updateMission(null);
    }


    @Test
    public void deleteMission() {
//        missionDao.createMission(mission1);
        missionDao.cancelMission(mission2);

        assertThat(missionDao.findMissionById(mission2.getId()))
                .isNull();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void deleteNullMission() {
        missionDao.cancelMission(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void deleteMissionWithNullId() {
        missionDao.cancelMission(mission1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findMissionByNullId() {
        missionDao.findMissionById(null);
    }

    @Test
    public void findAllActiveMission() {
        missionDao.createMission(inActiveMission);

        List<Mission> activeMissions = missionDao.findAllMissions(true);
        List<Mission> inActiveMissions = missionDao.findAllMissions(false);

        assertThat(activeMissions)
                .containsOnly(mission2);
        assertThat(inActiveMissions)
                .containsOnly(inActiveMission);
    }


    private static Mission getMission(String name, boolean active) {
        Mission mission = new Mission();
        mission.setName(name);
        mission.setActive(active);
        mission.setDestination("Earth");
        mission.setEta(ZonedDateTime.now().plusDays(5));
        return mission;
    }

    private static User getUser(String name) {
        User user = new User();
        user.setName(name);
        user.setBirthDate(LocalDate.of(1988, 10, 10));
        user.setEmail(name + "@example.com");
        user.setPassword(name);
        return user;
    }

    private static Spacecraft getSpacecraft(String name) {
        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName(name);
        spacecraft.setType("probe");
        return spacecraft;
    }

    private static CraftComponent getCraftComponent(String name) {
        CraftComponent component = new CraftComponent();
        component.setName(name);
        component.setReadyDate(ZonedDateTime.now().plusDays(2));
        component.setReadyToUse(true);
        return component;
    }
}
