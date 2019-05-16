package cz.muni.fi.dao;

import cz.muni.fi.ApplicationContext;
import cz.muni.fi.entity.CraftComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import java.time.*;

import static org.assertj.core.api.Assertions.*;

/**
 * @author Vojtech Bruza
 */

@ContextConfiguration(classes = ApplicationContext.class)
public class CraftComponentDaoTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private CraftComponentDao craftComponentDao;

    private CraftComponent orbitalModule;
    private CraftComponent heatShield;
    private CraftComponent retroRocket;

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @BeforeMethod
	public void setUp() {
        LocalDateTime ldt = LocalDateTime.now().plusMonths(3);
        orbitalModule = new CraftComponent();
        orbitalModule.setName("Orbital Module");
        orbitalModule.setReadyDate(ldt.atZone(ZoneId.of("GMT+2")));
        orbitalModule.setReadyToUse(true);

        heatShield = new CraftComponent();
        heatShield.setName("Heat Shield");
        heatShield.setReadyDate(ldt.atZone(ZoneId.of("UTC-06:00")));
        heatShield.setReadyToUse(false);

        retroRocket = new CraftComponent();
        retroRocket.setName("Retro Rocket");
        retroRocket.setReadyDate(ldt.atZone(ZoneId.of("UTC+08:00")));
        retroRocket.setReadyToUse(true);
    }

    @AfterMethod
	public void tearDown() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.createQuery("delete from CraftComponent ").executeUpdate();
        entityManager.getTransaction().commit();
        entityManager.close();
    }

//    ADD
    @Test
    public void testAddComponent(){
        EntityManager em = entityManagerFactory.createEntityManager();
        craftComponentDao.addComponent(orbitalModule);
        assertThat(em.find(CraftComponent.class, orbitalModule.getId())).isEqualTo(orbitalModule);
        assertThat(em.createQuery("select c from CraftComponent c", CraftComponent.class).getResultList()).hasSize(1);
        craftComponentDao.addComponent(heatShield);
        assertThat(em.find(CraftComponent.class, heatShield.getId())).isEqualTo(heatShield);
        assertThat(em.createQuery("select c from CraftComponent c", CraftComponent.class).getResultList()).hasSize(2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddComponentWithId(){
        orbitalModule.setId(1L);
        craftComponentDao.addComponent(orbitalModule);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAddComponentWithNullName() throws Exception {
        orbitalModule.setName(null);
        craftComponentDao.addComponent(orbitalModule);
    }

//    FIND ALL
    @Test
    public void testFindAllComponents(){
        EntityManager em = entityManagerFactory.createEntityManager();
        assertThat(craftComponentDao.findAllComponents()).hasSize(0);
        em.getTransaction().begin();
        em.persist(orbitalModule);
        em.persist(heatShield);
        em.getTransaction().commit();
        assertThat(craftComponentDao.findAllComponents()).hasSize(2);
        em.getTransaction().begin();
        em.persist(retroRocket);
        em.getTransaction().commit();
        assertThat(craftComponentDao.findAllComponents()).hasSize(3);
    }

//    FIND BY ID
    @Test
	public void testFindComponentById() throws Exception {
        craftComponentDao.addComponent(heatShield);
        assertThat(craftComponentDao.findComponentById(heatShield.getId())).isEqualToComparingFieldByField(heatShield);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
	public void testFindComponentByIdWithNullId() throws Exception {
        craftComponentDao.addComponent(retroRocket);
        craftComponentDao.findComponentById(null);
    }

    @Test
	public void testFindNonexistingComponent() throws Exception {
        craftComponentDao.addComponent(retroRocket);
        assertThat(craftComponentDao.findComponentById(retroRocket.getId()+1L)).isNull();
    }

//    UPDATE
    @Test
    public void testUpdateComponent(){
        craftComponentDao.addComponent(orbitalModule);
        orbitalModule.setName("Large orbital module");
        craftComponentDao.updateComponent(orbitalModule);
        assertThat(craftComponentDao.findComponentById(orbitalModule.getId())).isEqualToComparingFieldByField(orbitalModule);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testUpdateComponentWithNullName() throws Exception {
        craftComponentDao.addComponent(orbitalModule);
        orbitalModule.setName(null);
        craftComponentDao.updateComponent(orbitalModule);
    }


    //    REMOVE
    @Test
    public void testRemoveComponent(){
        craftComponentDao.addComponent(retroRocket);
        assertThat(craftComponentDao.findAllComponents()).contains(retroRocket);
        craftComponentDao.removeComponent(retroRocket);
        assertThat(craftComponentDao.findAllComponents()).isEmpty();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRemoveComponentWithNullId(){
        craftComponentDao.addComponent(retroRocket);
        assertThat(craftComponentDao.findAllComponents()).contains(retroRocket);
        retroRocket.setId(null);
        craftComponentDao.removeComponent(retroRocket);
    }

}
