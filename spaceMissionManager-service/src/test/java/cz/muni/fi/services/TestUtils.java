package cz.muni.fi.services;

import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.entity.Mission;
import cz.muni.fi.entity.Spacecraft;
import cz.muni.fi.entity.User;

import java.time.LocalDate;

/**
 * Created by jsmadis
 *
 * Usage:
 * Mission mission = TestUtils.createMission("mission");
 * User user = TestUtils.createUser("user", mission);
 * Spacecraft spacecraft = TestUtils.createSpacecraft("spacecraft", mission);
 * CraftComponent craftComponent = TestUtils.createCraftComponent("craft component", spacecraft);
 *
 *
 *
 * @author jsmadis
 */

public class TestUtils {

    static User createUser(String name) {
        User user = new User();
        user.setName(name);
        user.setBirthDate(LocalDate.now().minusYears(20));
        user.setEmail(name + "@example.com");
        user.setPassword(name);
        return user;
    }

    static Mission createMission(String name) {
        Mission mission = new Mission();
        mission.setName(name);
        return mission;
    }

    static Spacecraft createSpacecraft(String name) {
        Spacecraft spacecraft = new Spacecraft();
        spacecraft.setName(name);
        return spacecraft;
    }

    static CraftComponent createCraftComponent(String name) {
        CraftComponent craftComponent = new CraftComponent();
        craftComponent.setName(name);
        return craftComponent;
    }


    /**
     * Creates a user and sets his mission
     *
     * @param name    User name
     * @param mission Mission to set
     * @return User instance
     */
    static User createUser(String name, Mission mission) {
        User user = createUser(name);
        mission.addAstronaut(user);
//        user.setMission(mission);
        return user;
    }


    /**
     * Creates a CraftComponent and sets its spacecraft
     *
     * @param name       CraftComponent name
     * @param spacecraft Spacecraft to set
     * @return CraftComponent instance
     */
    static CraftComponent createCraftComponent(String name, Spacecraft spacecraft) {
        CraftComponent craftComponent = createCraftComponent(name);
        spacecraft.addComponent(craftComponent);
//        craftComponent.setSpacecraft(spacecraft);
        return craftComponent;
    }


    /**
     * Creates a Spacecraft and sets its Mission
     *
     * @param name Spacecraft name
     * @param mission Mission to set
     * @return Spacecraft instance
     */
    static Spacecraft createSpacecraft(String name, Mission mission) {
        Spacecraft spacecraft = createSpacecraft(name);
        mission.addSpacecraft(spacecraft);
//        spacecraft.setMission(mission);
        return spacecraft;
    }
}
