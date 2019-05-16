package cz.muni.fi.services;


import cz.muni.fi.config.ServiceConfiguration;
import cz.muni.fi.dao.MissionDao;
import cz.muni.fi.entity.Mission;
import cz.muni.fi.services.impl.MissionServiceImpl;
import org.hibernate.service.spi.ServiceException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;


/**
 * Created by jsmadis
 *
 * @author jsmadis
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
public class MissionServiceTest extends AbstractTestNGSpringContextTests {
    @Mock
    private MissionDao missionDao;

    @Autowired
    @InjectMocks
    private MissionService missionService = new MissionServiceImpl();

    private Mission mission1;
    private Mission mission2;
    private Mission mission3;
    private Mission mission4;


    private Long counter = 10L;
    private Map<Long, Mission> missions = new HashMap<>();

    @BeforeClass
    public void beforeClass() throws ServiceException {
        MockitoAnnotations.initMocks(this);

        when(missionDao.createMission(any(Mission.class))).then(invoke -> {
            Mission mockedMission = invoke.getArgumentAt(0, Mission.class);
            if (mockedMission.getId() != null) {
                throw new IllegalArgumentException("Mission already exist");
            }
            if (mockedMission.getName() == null) {
                throw new IllegalArgumentException("Mission name can't be null");
            }
            if (checkMissionsNameDuplicity(mockedMission.getName(), -1L)) {
                throw new IllegalArgumentException("Mission name already exist");
            }
            long index = counter;
            mockedMission.setId(index);
            missions.put(index, mockedMission);
            counter++;
            return mockedMission;
        });

        when(missionDao.updateMission(any(Mission.class))).then(invoke -> {
            Mission mockedMission = invoke.getArgumentAt(0, Mission.class);
            if (mockedMission.getId() == null) {
                throw new IllegalArgumentException("Mission was not created yet.");
            }
            if (mockedMission.getName() == null) {
                throw new IllegalArgumentException("Mission name can't be null");
            }
            if (checkMissionsNameDuplicity(mockedMission.getName(), mockedMission.getId())) {
                throw new IllegalArgumentException("Mission name already exist");
            }
            missions.replace(mockedMission.getId(), mockedMission);
            return mockedMission;
        });

        when(missionDao.cancelMission(any(Mission.class))).then(invoke -> {
            Mission mockedMission = invoke.getArgumentAt(0, Mission.class);
            if (mockedMission.getId() == null) {
                throw new IllegalArgumentException("Mission is not in Database.");
            }
            missions.remove(mockedMission.getId(), mockedMission);
            return mockedMission;
        });

        when(missionDao.findAllMissions())
                .then(invoke -> Collections.unmodifiableList(new ArrayList<>(missions.values())));


        when(missionDao.findMissionById(anyLong())).then(invoke -> {
            Long index = invoke.getArgumentAt(0, Long.class);
            if (index == null) {
                throw new IllegalArgumentException("id is null");
            }
            return missions.get(index);
        });

        when(missionDao.findAllMissions(anyBoolean())).then(invoke -> {
            boolean bool = invoke.getArgumentAt(0, Boolean.class);
            List<Mission> missionList = new ArrayList<>();

            for (Mission m : missions.values()) {
                if (m.isActive() == bool) {
                    missionList.add(m);
                }
            }
            return Collections.unmodifiableList(missionList);
        });

    }


    @BeforeMethod
    public void beforeTest() {
        missions.clear();
        mission1 = TestUtils.createMission("mission1");
        mission2 = TestUtils.createMission("mission2");
        mission3 = TestUtils.createMission("mission3");
        mission4 = TestUtils.createMission("mission4");

        mission1.setId(1L);
        mission2.setId(2L);
        mission3.setId(3L);
        mission4.setId(4L);

        mission1.setActive(true);
        mission2.setActive(true);
        mission3.setActive(false);

        missions.put(1L, mission1);
        missions.put(2L, mission2);
        missions.put(3L, mission3);
        missions.put(4L, mission4);
    }


    @Test
    public void createNewMission() throws DataAccessException {
        int sizeBefore = missions.size();
        Mission mission = TestUtils.createMission("mission");
        missionService.createMission(mission);
        assertThat(missions.values()).hasSize(sizeBefore + 1)
                .contains(mission);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNullMission() {
        missionService.createMission(null);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createMissionNullName() {
        missionService.createMission(new Mission());
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createExistingMission() {
        Mission mission = TestUtils.createMission("mission");
        Mission anotherMission = TestUtils.createMission("mission");
        missionService.createMission(mission);
        missionService.createMission(anotherMission);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createMissionWithIdNotNull() {
        Mission mission = TestUtils.createMission("mission");
        mission.setId(1L);
        missionService.createMission(mission);
    }

    @Test
    public void updateMission() throws DataAccessException {
        mission1.setName("updated mission");
        missionService.updateMission(mission1);

        Mission updated = missions.get(mission1.getId());

        assertThat(updated.getName()).isEqualTo("updated mission");
        assertThat(updated).isEqualToComparingFieldByField(mission1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateNullMission() {
        missionService.updateMission(null);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void updateMissionNullName() {
        mission1.setName(null);
        missionService.updateMission(mission1);
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateMissionWithNullId() {
        Mission mission = TestUtils.createMission("mission");
        mission.setActive(true);
        missionService.updateMission(mission);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void updateMissionWithDuplicateName() {
        mission1.setName(mission2.getName());
        missionService.updateMission(mission1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateArchivedMission() {
        missionService.archive(mission3, LocalDate.now().minusDays(2));

        missionService.updateMission(mission3);
    }

    @Test
    public void deleteMission() throws DataAccessException {
        int sizeBefore = missions.values().size();
        missionService.cancelMission(mission1);

        assertThat(missions.values()).hasSize(sizeBefore - 1)
                .doesNotContain(mission1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void deleteNullMission() {
        missionService.cancelMission(null);
    }


    @Test(expectedExceptions = DataAccessException.class)
    public void deleteMissionWithNullId() {
        Mission mission = TestUtils.createMission("mission");
        missionService.cancelMission(mission);
    }

    @Test
    public void deleteMissionNotInDB() throws DataAccessException {
        int sizeBefore = missions.values().size();
        Mission mission = TestUtils.createMission("mission");
        mission.setId(counter * 2L);
        missionService.cancelMission(mission);

        assertThat(missions.values()).hasSize(sizeBefore)
                .doesNotContain(mission);

    }

    @Test
    public void findAllMissions() throws DataAccessException {
        assertThat(missionService.findAllMissions())
                .containsExactlyInAnyOrder(mission1, mission2, mission3, mission4);
    }

    @Test
    public void findAllActiveMissions() {
        assertThat(missionService.findAllMissions(true))
                .containsExactlyInAnyOrder(mission1, mission2);
        assertThat(missionService.findAllMissions(false))
                .containsExactlyInAnyOrder(mission3, mission4);
    }

    @Test
    public void findMissionById() throws DataAccessException {
        assertThat(missionService.findMissionById(mission1.getId()))
                .isEqualToComparingFieldByField(mission1);
    }

    @Test
    public void findMissionByIdNotInDB() throws DataAccessException {
        assertThat(missionService.findMissionById(10000L)).isNull();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findMissionByNullId() {
        missionService.findMissionById(null);
    }

    @Test
    public void archiveMission() {
        missionService.archive(mission1, LocalDate.now().minusDays(20));

        assertThat(mission1.isActive()).isEqualTo(false);
        assertThat(mission1.getResult()).isNotNull()
                .isNotEmpty()
                .contains(mission1.getName());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void archiveMissionWithNullIdArgument() {
        missionService.archive(null, LocalDate.now().minusDays(20));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void archiveMissionWithNullDateArgument() {
        missionService.archive(mission1, null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void archiveMissionWithDateInThePast() {
        missionService.archive(mission1, LocalDate.now().plusDays(1));
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void archiveMissionWithIdNotInDB() {
        Mission mission = TestUtils.createMission("name");
        missionService.archive(mission, LocalDate.now().minusDays(20));
    }


    private boolean checkMissionsNameDuplicity(String name, long id) {
        for (Mission m : missions.values()) {
            if (m.getName().equals(name) && id != m.getId()) {
                return true;
            }
        }
        return false;
    }
}



