package cz.muni.fi.services;

import cz.muni.fi.config.ServiceConfiguration;
import cz.muni.fi.dao.CraftComponentDao;
import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.services.impl.CraftComponentServiceImpl;
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
public class CraftComponentServiceTest {
    @Mock
    private CraftComponentDao craftComponentDao;

    @Autowired
    @InjectMocks
    private CraftComponentService craftComponentService = new CraftComponentServiceImpl();

    private Long counter = 10L;
    private Map<Long, CraftComponent> craftComponents = new HashMap<>();

    private CraftComponent craftComponent1;
    private CraftComponent craftComponent2;
    private CraftComponent craftComponent3;


    @BeforeClass
    public void beforeClass() {
        MockitoAnnotations.initMocks(this);

        when(craftComponentDao.addComponent(any(CraftComponent.class))).then(invoke -> {
            CraftComponent mockedCraftComponent = invoke.getArgumentAt(0, CraftComponent.class);

            if (mockedCraftComponent == null) {
                throw new IllegalArgumentException("CraftComponent is null");
            }
            if (mockedCraftComponent.getId() != null) {
                throw new IllegalArgumentException("CraftComponent is already in DB");
            }
            if (mockedCraftComponent.getName() == null) {
                throw new IllegalArgumentException("CraftComponents name cant be null");
            }
            if (checkCraftComponentsNameDuplicity(mockedCraftComponent.getName(), -1L)) {
                throw new IllegalArgumentException("CraftComponents name already exist");
            }
            long id = counter;
            counter++;
            mockedCraftComponent.setId(id);
            craftComponents.put(id, mockedCraftComponent);
            return mockedCraftComponent;
        });

        when(craftComponentDao.updateComponent(any(CraftComponent.class))).then(invoke -> {
            CraftComponent mockedCraftComponent = invoke.getArgumentAt(0, CraftComponent.class);

            if (mockedCraftComponent == null) {
                throw new IllegalArgumentException("CraftComponent is null");
            }
            if (mockedCraftComponent.getId() == null) {
                throw new IllegalArgumentException("CraftComponent is not in DB");
            }
            if (mockedCraftComponent.getName() == null) {
                throw new IllegalArgumentException("CraftComponents name cant be null");
            }
            if (checkCraftComponentsNameDuplicity(mockedCraftComponent.getName(), mockedCraftComponent.getId())) {
                throw new IllegalArgumentException("CraftComponents name already exist");
            }

            craftComponents.replace(mockedCraftComponent.getId(), mockedCraftComponent);
            return mockedCraftComponent;
        });

        when(craftComponentDao.removeComponent(any(CraftComponent.class))).then(invoke -> {
            CraftComponent mockedCraftComponent = invoke.getArgumentAt(0, CraftComponent.class);
            if (mockedCraftComponent == null) {
                throw new IllegalArgumentException("CraftComponent is null");
            }
            if (mockedCraftComponent.getId() == null) {
                throw new IllegalArgumentException("Craftcomponent is not in DB");
            }
            craftComponents.remove(mockedCraftComponent.getId(), mockedCraftComponent);
            return mockedCraftComponent;
        });

        when(craftComponentDao.findComponentById(anyLong())).then(invoke -> {
            Long id = invoke.getArgumentAt(0, Long.class);
            if (id == null) {
                throw new IllegalArgumentException("id is null");
            }
            return craftComponents.get(id);
        });

        when(craftComponentDao.findAllComponents())
                .then(invoke -> Collections.unmodifiableList(new ArrayList<>(craftComponents.values())));
    }

    @BeforeMethod
    public void beforeTest() {
        craftComponents.clear();
        craftComponent1 = TestUtils.createCraftComponent("craftComponent1");
        craftComponent2 = TestUtils.createCraftComponent("craftComponent2");
        craftComponent3 = TestUtils.createCraftComponent("craftComponent3");

        craftComponent1.setId(1L);
        craftComponent2.setId(2L);
        craftComponent3.setId(3L);

        craftComponents.put(1L, craftComponent1);
        craftComponents.put(2L, craftComponent2);
        craftComponents.put(3L, craftComponent3);
    }

    @Test
    public void createCraftComponent() throws DataAccessException {
        int sizeBefore = craftComponents.values().size();
        CraftComponent craftComponent = TestUtils.createCraftComponent("craftComponent");
        craftComponentService.addComponent(craftComponent);
        assertThat(craftComponents.values()).hasSize(sizeBefore + 1)
                .contains(craftComponent);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createNullCraftComponent() {
        craftComponentService.addComponent(null);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createCraftComponentWithNullName() {
        craftComponentService.addComponent(new CraftComponent());
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createCraftComponentDuplicatedName() {
        CraftComponent craftComponent = TestUtils.createCraftComponent(craftComponent1.getName());
        craftComponentService.addComponent(craftComponent);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void createCraftComponentWithIdNotNull() {
        CraftComponent craftComponent = TestUtils.createCraftComponent("craftComponent");
        craftComponent.setId(counter * 2L);
        craftComponentService.addComponent(craftComponent);
    }

    @Test
    public void updateCraftComponent() {
        craftComponent1.setName("updated craftComponent");
        craftComponentService.updateComponent(craftComponent1);

        CraftComponent updated = craftComponents.get(craftComponent1.getId());

        assertThat(updated.getName()).isEqualTo("updated craftComponent");
        assertThat(updated).isEqualToComparingFieldByField(craftComponent1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void updateNullCraftComponent() {
        craftComponentService.updateComponent(null);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void updateCraftComponentWithNullName() {
        craftComponent1.setName(null);
        craftComponentService.updateComponent(craftComponent1);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void updateCraftComponentWithDuplicatedName() {
        craftComponent1.setName(craftComponent2.getName());
        craftComponentService.updateComponent(craftComponent1);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void updateCraftComponentWithNullId() {
        craftComponent1.setId(null);
        craftComponentService.updateComponent(craftComponent1);
    }

    @Test
    public void deleteCraftComponent() {
        int sizeBefore = craftComponents.values().size();
        craftComponentService.removeComponent(craftComponent1);

        assertThat(craftComponents.values()).hasSize(sizeBefore - 1)
                .doesNotContain(craftComponent1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void deleteNullCraftComponent() {
        craftComponentService.removeComponent(null);
    }

    @Test(expectedExceptions = DataAccessException.class)
    public void deleteCraftComponentWithNullId() {
        craftComponentService.removeComponent(new CraftComponent());
    }

    @Test
    public void deleteCraftComponentNotInDb() {
        int sizeBefore = craftComponents.values().size();
        CraftComponent craftComponent = TestUtils.createCraftComponent("craftComponent");
        craftComponent.setId(100000L);
        craftComponentService.removeComponent(craftComponent);

        assertThat(craftComponents.values()).hasSize(sizeBefore)
                .doesNotContain(craftComponent);
    }

    @Test
    public void findAllCraftComponents() {
        assertThat(craftComponentService.findAllComponents())
                .containsExactlyInAnyOrder(craftComponent1, craftComponent2, craftComponent3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void findCraftComponentsByNullId() {
        craftComponentService.findComponentById(null);
    }

    @Test
    public void findCraftComponentById() {
        CraftComponent craftComponent = craftComponentService.findComponentById(craftComponent1.getId());
        assertThat(craftComponent).isEqualToComparingFieldByField(craftComponent1);
    }

    @Test
    public void findCraftComponentByIdNotInDB() {
        assertThat(craftComponentService.findComponentById(100000L)).isNull();
    }

    private boolean checkCraftComponentsNameDuplicity(String name, long id) {
        for (CraftComponent c : craftComponents.values()) {
            if (c.getName().equals(name) && c.getId() != id) {
                return true;
            }
        }
        return false;
    }

}
