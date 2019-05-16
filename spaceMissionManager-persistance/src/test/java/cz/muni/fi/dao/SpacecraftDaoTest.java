package cz.muni.fi.dao;

import cz.muni.fi.ApplicationContext;
import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.entity.Spacecraft;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by jcizmar
 *
 * @author jcizmar
 */
@ContextConfiguration(classes = ApplicationContext.class)
public class SpacecraftDaoTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private SpacecraftDao spacecraftDao;

    @Autowired
    private MissionDao missionDao;

    @Autowired
    private CraftComponentDao componentDao;

    private Spacecraft spacecraft1;
    private Spacecraft spacecraft2;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @BeforeMethod
    public void setUp(){
        CraftComponent craftComponent = new CraftComponent();
        craftComponent.setName("Fast Motor");
        componentDao.addComponent(craftComponent);

        this.spacecraft1 = new Spacecraft();
        this.spacecraft1.setName("Spacecraft 1");
        this.spacecraft1.setType("Type 1");
        this.spacecraft1.addComponent(craftComponent);

        CraftComponent craftComponent2 = new CraftComponent();
        craftComponent2.setName("Slow Motor");
        componentDao.addComponent(craftComponent2);


        this.spacecraft2 = new Spacecraft();
        this.spacecraft2.setName("Spacecraft 2");
        this.spacecraft2.setType("Type 1");
        this.spacecraft2.addComponent(craftComponent2);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
	    entityManager.createQuery("delete from CraftComponent").executeUpdate();
	    entityManager.createQuery("delete from Spacecraft").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void createSpacecraft(){
        spacecraftDao.addSpacecraft(spacecraft1);
        assertThat(spacecraftDao.findAllSpacecrafts()).hasSize(1);

        spacecraftDao.addSpacecraft(spacecraft2);
        assertThat(spacecraftDao.findAllSpacecrafts()).hasSize(2);
        assertThat(spacecraftDao.findSpacecraftById(spacecraft1.getId())).isEqualTo(spacecraft1);
        assertThat(spacecraftDao.findSpacecraftById(spacecraft1.getId())).isNotEqualTo(spacecraft2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddSpacecraftWithId() throws Exception {
        spacecraft1.setId(1L);
        spacecraftDao.addSpacecraft(spacecraft1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddSpacecraftWithNullName() throws Exception {
        spacecraft1.setName(null);
        spacecraftDao.addSpacecraft(spacecraft1);
    }

    @Test
    public void testUpdateSpacecraft(){
        spacecraftDao.addSpacecraft(spacecraft1);
        spacecraft1.setName("New spacecraft name");
        spacecraft1.setType("New spacecraft type");
        spacecraftDao.updateSpacecraft(spacecraft1);

        assertThat(spacecraftDao.findSpacecraftById(spacecraft1.getId())).isEqualToComparingFieldByField(spacecraft1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateSpacecraftWithNullId(){
        spacecraftDao.addSpacecraft(spacecraft1);
        spacecraft1.setId(null);
        spacecraftDao.updateSpacecraft(spacecraft1);
    }


    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateSpacecraftWithNullName(){
        spacecraftDao.addSpacecraft(spacecraft1);
        spacecraft1.setName(null);
        spacecraftDao.updateSpacecraft(spacecraft1);
    }

    @Test
    public void testDeleteSpacecraft(){
        spacecraftDao.addSpacecraft(spacecraft1);
        assertThat(spacecraftDao.findSpacecraftById(spacecraft1.getId())).isEqualTo(spacecraft1);
        spacecraftDao.removeSpacecraft(spacecraft1);
        assertThat(spacecraftDao.findSpacecraftById(spacecraft1.getId())).isNull();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDeleteNotExistingSpacecraft(){
        spacecraft1.setId(1L);
        spacecraftDao.removeSpacecraft(spacecraft1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testDeleteSpacecraftWithNullId(){
        spacecraftDao.removeSpacecraft(spacecraft1);
    }

    @Test
    public void testFindAllSpacecraft(){
        spacecraftDao.addSpacecraft(spacecraft1);
        assertThat(spacecraftDao.findAllSpacecrafts().get(0)).isEqualTo(spacecraft1);
        spacecraftDao.addSpacecraft(spacecraft2);
        assertThat(spacecraftDao.findAllSpacecrafts()).hasSize(2);
        assertThat(spacecraftDao.findAllSpacecrafts()).contains(spacecraft1).contains(spacecraft2);
    }

    @Test
    public void testFindSpacecraftById(){
        spacecraftDao.addSpacecraft(spacecraft1);
        spacecraftDao.addSpacecraft(spacecraft2);
        assertThat(spacecraftDao.findSpacecraftById(spacecraft1.getId())).isEqualTo(spacecraft1);
        assertThat(spacecraftDao.findSpacecraftById(spacecraft2.getId())).isEqualTo(spacecraft2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testFindSpacecraftByNullId(){
        spacecraftDao.findSpacecraftById(null);
    }
}
