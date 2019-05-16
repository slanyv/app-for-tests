package cz.muni.fi.services;

import cz.muni.fi.config.ServiceConfiguration;
import cz.muni.fi.dto.CraftComponentDTO;
import cz.muni.fi.dto.MissionDTO;
import cz.muni.fi.dto.SpacecraftDTO;
import cz.muni.fi.dto.UserDTO;
import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.entity.Mission;
import cz.muni.fi.entity.Spacecraft;
import cz.muni.fi.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by jsmadis
 *
 * @author jsmadis
 */

@ContextConfiguration(classes = ServiceConfiguration.class)

public class BeanMappingServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private BeanMappingService beanMappingService;

    private List<User> users = new ArrayList<>();
    private List<Mission> missions = new ArrayList<>();
    private List<Spacecraft> spacecrafts = new ArrayList<>();
    private List<CraftComponent> craftComponents = new ArrayList<>();

    private Mission mission;
    private Spacecraft spacecraft;
    private CraftComponent craftComponent;
    private User user;

    @BeforeMethod
    public void beforeTest() {

        missions.clear();
        users.clear();
        craftComponents.clear();
        spacecrafts.clear();

        mission = TestUtils.createMission("test");
        Mission mission1 = TestUtils.createMission("test1");
        Mission mission2 = TestUtils.createMission("test2");

        user = TestUtils.createUser("name", mission);

        spacecraft = TestUtils.createSpacecraft("spacecraft", mission);
        Spacecraft spacecraft1 = TestUtils.createSpacecraft("spacecraft1", mission1);
        Spacecraft spacecraft2 = TestUtils.createSpacecraft("spacecraft2", mission1);
        Spacecraft spacecraft3 = TestUtils.createSpacecraft("spacecraft3", mission2);

        craftComponent = TestUtils.createCraftComponent("craft", spacecraft);
        CraftComponent craftComponent1 = TestUtils.createCraftComponent("craft1", spacecraft1);
        CraftComponent craftComponent2 = TestUtils.createCraftComponent("craft2", spacecraft2);
        CraftComponent craftComponent3 = TestUtils.createCraftComponent("craft3", spacecraft3);
        CraftComponent craftComponent4 = TestUtils.createCraftComponent("craft4", spacecraft3);

        mission.setId(1L);
        user.setId(1L);
        spacecraft.setId(1L);
        craftComponent.setId(1L);

        users.add(user);

        missions.add(mission1);
        missions.add(mission2);

        spacecrafts.add(spacecraft1);
        spacecrafts.add(spacecraft2);
        spacecrafts.add(spacecraft3);

        craftComponents.add(craftComponent1);
        craftComponents.add(craftComponent2);
        craftComponents.add(craftComponent3);
        craftComponents.add(craftComponent4);

    }

    @Test
    public void shouldMapMission() {
        MissionDTO missionDTO = beanMappingService.mapTo(mission, MissionDTO.class);
        assertThat(missionDTO).isNotNull();
        assertThat(missionDTO.getName()).isEqualTo(mission.getName());
        assertThat(missionDTO.getId()).isEqualTo(mission.getId());
        assertThat(missionDTO.getAstronauts()).hasSize(1);
        assertThat(missionDTO.getSpacecrafts()).hasSize(1);

        UserDTO userDTO = missionDTO.getAstronauts().iterator().next();
        SpacecraftDTO spacecraftDTO = missionDTO.getSpacecrafts().iterator().next();

        assertThat(missionDTO.getAstronauts()).containsExactly(userDTO);
        assertThat(userDTO.getMission()).isEqualToComparingFieldByField(missionDTO);

        assertThat(missionDTO.getSpacecrafts()).containsExactly(spacecraftDTO);
        assertThat(spacecraftDTO.getMission()).isEqualToComparingFieldByField(missionDTO);
    }

    @Test
    public void shouldMapSpacecraft() {
        SpacecraftDTO spacecraftDTO = beanMappingService.mapTo(spacecraft, SpacecraftDTO.class);
        assertThat(spacecraftDTO).isNotNull();
        CraftComponentDTO craftComponentDTO = spacecraftDTO.getComponents().iterator().next();
        assertThat(craftComponentDTO).isNotNull();

        assertThat(spacecraftDTO.getName()).isEqualTo(spacecraft.getName());
        assertThat(spacecraftDTO.getId()).isEqualTo(spacecraft.getId());
        assertThat(spacecraftDTO.getComponents()).hasSize(1)
                .containsExactly(craftComponentDTO);
        assertThat(craftComponentDTO.getSpacecraft()).isEqualToComparingFieldByField(spacecraftDTO);
    }

    @Test
    public void shouldMapCraftComponent() {
        CraftComponentDTO craftComponentDTO = beanMappingService.mapTo(craftComponent, CraftComponentDTO.class);

        assertThat(craftComponentDTO.getName()).isEqualTo(craftComponent.getName());
        assertThat(craftComponentDTO.getId()).isEqualTo(craftComponent.getId());
    }

    @Test
    public void shouldMapUser() {
        UserDTO userDTO = beanMappingService.mapTo(user, UserDTO.class);
        assertThat(userDTO.getName()).isEqualTo(user.getName());
        assertThat(userDTO.getId()).isEqualTo(user.getId());
    }


    @Test
    public void shouldMapInnerMissions() {
        List<MissionDTO> missionDTOList = beanMappingService.mapTo(missions, MissionDTO.class);

        assertThat(missionDTOList).hasSize(2);
        assertThat(missionDTOList.get(0).getName()).isEqualTo("test1");
        assertThat(missionDTOList.get(0).getSpacecrafts()).hasSize(2);
    }

    @Test
    public void shouldMapInnerSpacecrafts() {
        List<SpacecraftDTO> spacecraftDTOList = beanMappingService.mapTo(spacecrafts, SpacecraftDTO.class);

        assertThat(spacecraftDTOList).hasSize(3);
        assertThat(spacecraftDTOList.get(0).getName()).isEqualTo("spacecraft1");
        assertThat(spacecraftDTOList.get(0).getComponents()).hasSize(1);
        assertThat(spacecraftDTOList.get(0).getMission().getName()).isEqualTo("test1");
    }

    @Test
    public void shouldMapInnerCraftComponents() {
        List<CraftComponentDTO> craftComponentDTOList = beanMappingService.mapTo(craftComponents, CraftComponentDTO.class);

        assertThat(craftComponentDTOList.get(0).getName()).isEqualTo("craft1");
        assertThat(craftComponentDTOList.get(0).getSpacecraft().getName()).isEqualTo("spacecraft1");
    }

    @Test
    public void shouldMapInnerUsers() {
        List<UserDTO> userDTOList = beanMappingService.mapTo(users, UserDTO.class);

        assertThat(userDTOList.get(0).getName()).isEqualTo("name");
        assertThat(userDTOList.get(0).getMission().getName()).isEqualTo("test");
    }
}
