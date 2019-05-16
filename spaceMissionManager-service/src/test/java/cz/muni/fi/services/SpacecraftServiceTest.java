package cz.muni.fi.services;

import cz.muni.fi.config.ServiceConfiguration;
import cz.muni.fi.dao.SpacecraftDao;
import cz.muni.fi.entity.Spacecraft;
import cz.muni.fi.services.impl.SpacecraftServiceImpl;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

/**
 * Created by jsmadis
 *
 * @author jsmadis
 */

@ContextConfiguration(classes = ServiceConfiguration.class)
public class SpacecraftServiceTest {
    @Mock
    private SpacecraftDao spacecraftDao;

    @Autowired
    @InjectMocks
    private SpacecraftService spacecraftService = new SpacecraftServiceImpl();


    private Long counter = 10L;
    private Map<Long, Spacecraft> spacecrafts = new HashMap<>();


    private Spacecraft spacecraft1;
    private Spacecraft spacecraft2;
    private Spacecraft spacecraft3;


    @BeforeClass
    public void beforeClass() {
        MockitoAnnotations.initMocks(this);

        when(spacecraftDao.addSpacecraft(any(Spacecraft.class))).then(invoke -> {
            Spacecraft mockedSpacecraft = invoke.getArgumentAt(0, Spacecraft.class);
            if (mockedSpacecraft == null) {
                throw new IllegalArgumentException("Spacecraft is null");
            }
            if (mockedSpacecraft.getId() != null) {
                throw new IllegalArgumentException("Spacecraft is already in DB");
            }
            if (mockedSpacecraft.getName() == null) {
                throw new IllegalArgumentException("Spacecraft name is null");
            }
            if (checkSpacecraftsNameDuplicity(mockedSpacecraft.getName(), -1L)) {
                throw new IllegalArgumentException("Spacecraft with this name already exist");
            }
            long index = counter;
            counter++;
            mockedSpacecraft.setId(index);
            spacecrafts.put(index, mockedSpacecraft);
            return mockedSpacecraft;
        });

        when(spacecraftDao.updateSpacecraft(any(Spacecraft.class))).then(invoke -> {
            Spacecraft mockedSpacecraft = invoke.getArgumentAt(0, Spacecraft.class);
            if (mockedSpacecraft == null) {
                throw new IllegalArgumentException("Spacecraft is null");
            }
            if (mockedSpacecraft.getId() == null) {
                throw new IllegalArgumentException("Spacecraft is not in DB");
            }
            if (mockedSpacecraft.getName() == null) {
                throw new IllegalArgumentException("Spacecraft name is null");
            }
            if (checkSpacecraftsNameDuplicity(mockedSpacecraft.getName(), mockedSpacecraft.getId())) {
                throw new IllegalArgumentException("Spacecraft with this name already exist");
            }
            spacecrafts.replace(mockedSpacecraft.getId(), mockedSpacecraft);
            return mockedSpacecraft;
        });

        when(spacecraftDao.removeSpacecraft(any(Spacecraft.class))).then(invoke -> {
            Spacecraft mockedSpacecraft = invoke.getArgumentAt(0, Spacecraft.class);
            if (mockedSpacecraft.getId() == null) {
                throw new IllegalArgumentException("Spacecraft is not in DB");
            }
            spacecrafts.remove(mockedSpacecraft.getId(), mockedSpacecraft);
            return mockedSpacecraft;
        });

        when(spacecraftDao.findSpacecraftById(anyLong())).then(invoke -> {
            Long id = invoke.getArgumentAt(0, Long.class);
            if (id == null) {
                throw new IllegalArgumentException("id is null");
            }
            return spacecrafts.get(id);
        });

        when(spacecraftDao.findAllSpacecrafts())
                .then(invoke -> Collections.unmodifiableList(new ArrayList<>(spacecrafts.values())));
    }

    @BeforeMethod
    public void beforeTest() {
        spacecrafts.clear();
        spacecraft1 = TestUtils.createSpacecraft("spacecraft1");
        spacecraft2 = TestUtils.createSpacecraft("spacecraft2");
        spacecraft3 = TestUtils.createSpacecraft("spacecraft3");

        spacecraft1.setId(1L);
        spacecraft2.setId(2L);
        spacecraft3.setId(3L);

        spacecrafts.put(1L, spacecraft1);
        spacecrafts.put(2L, spacecraft2);
        spacecrafts.put(3L, spacecraft3);
    }


    @Test
    public void createSpacecraft() throws DataAccessException {
        int sizeBefore = spacecrafts.values().size();
        Spacecraft spacecraft = TestUtils.createSpacecraft("spacecraft");
        spacecraftService.addSpacecraft(spacecraft);
        assertThat(spacecrafts.values()).hasSize(sizeBefore + 1)
                .contains(spacecraft);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNullSpacecraft() {
        spacecraftService.addSpacecraft(null);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createSpacecraftNullName() {
        spacecraftService.addSpacecraft(new Spacecraft());
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createSpacecraftDuplicateName() {
        Spacecraft spacecraft = TestUtils.createSpacecraft(spacecraft1.getName());
        spacecraftService.addSpacecraft(spacecraft);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createSpacecraftWithIdNotNull() {
        Spacecraft spacecraft = TestUtils.createSpacecraft("spacecraft");
        spacecraft.setId(1000L);
        spacecraftService.addSpacecraft(spacecraft);
    }

    @Test
    public void updateSpacecraft() throws DataAccessException {
        spacecraft1.setName("updated spacecraft");
        spacecraftService.updateSpacecraft(spacecraft1);

        Spacecraft updatedSpacecraft = spacecrafts.get(spacecraft1.getId());

        assertThat(updatedSpacecraft.getName()).isEqualTo("updated spacecraft");
        assertThat(updatedSpacecraft).isEqualToComparingFieldByField(spacecraft1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateNullSpacecraft() {
        spacecraftService.updateSpacecraft(null);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void updateSpacecraftNullName() {
        spacecraft1.setName(null);
        spacecraftService.updateSpacecraft(spacecraft1);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void updateSpacecraftDuplicatedName() {
        spacecraft2.setName(spacecraft1.getName());
        spacecraftService.updateSpacecraft(spacecraft2);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void updateSpacecraftNullId() {
        spacecraftService.updateSpacecraft(new Spacecraft());
    }

    @Test
    public void deleteSpacecraft() {
        int sizeBefore = spacecrafts.values().size();
        spacecraftService.removeSpacecraft(spacecraft1);

        assertThat(spacecrafts.values()).hasSize(sizeBefore - 1)
                .doesNotContain(spacecraft1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void deleteNullSpacecraft() {
        spacecraftService.removeSpacecraft(null);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void deleteSpacecraftWithNullId() {
        Spacecraft spacecraft = TestUtils.createSpacecraft("spacecraft");
        spacecraftService.removeSpacecraft(spacecraft);
    }

    @Test
    public void deleteSpacecraftNotInDB() {
        int sizeBefore = spacecrafts.values().size();
        Spacecraft spacecraft = TestUtils.createSpacecraft("spacecraft");
        spacecraft.setId(counter * 2L);
        spacecraftService.removeSpacecraft(spacecraft);

        assertThat(spacecrafts.values()).hasSize(sizeBefore)
                .doesNotContain(spacecraft);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findSpacecraftWithNullId() {
        spacecraftService.findSpacecraftById(null);
    }

    @Test
    public void findSpacecraftNotInDB() {
        assertThat(spacecraftService.findSpacecraftById(10000L)).isNull();
    }

    @Test
    public void findSpacecraftById() {
        long id = spacecraft1.getId();
        Spacecraft spacecraft = spacecraftService.findSpacecraftById(id);

        assertThat(spacecraft).isEqualToComparingFieldByField(spacecraft1);
    }

    @Test
    public void findAllSpacecrafts() {
        assertThat(spacecraftService.findAllSpacecrafts())
                .containsOnly(spacecraft1, spacecraft2, spacecraft3);
    }

    private boolean checkSpacecraftsNameDuplicity(String name, long id) {
        for (Spacecraft s : spacecrafts.values()) {
            if (s.getName().equals(name) && s.getId() != id) {
                return true;
            }
        }
        return false;
    }
}
