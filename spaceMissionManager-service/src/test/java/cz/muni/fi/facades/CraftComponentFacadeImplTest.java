package cz.muni.fi.facades;

import cz.muni.fi.config.ServiceConfiguration;
import cz.muni.fi.dto.CraftComponentCreateDTO;
import cz.muni.fi.dto.CraftComponentDTO;
import cz.muni.fi.dto.SpacecraftCreateDTO;
import cz.muni.fi.dto.SpacecraftDTO;
import cz.muni.fi.facade.CraftComponentFacade;
import cz.muni.fi.facade.SpacecraftFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@ContextConfiguration(classes = ServiceConfiguration.class)
public class CraftComponentFacadeImplTest extends AbstractTestNGSpringContextTests {


	@Autowired
	private SpacecraftFacade spacecraftFacade;

	@Autowired
	private CraftComponentFacade craftComponentFacade;

	private CraftComponentCreateDTO craftComponentCreateDTO;
	private CraftComponentDTO craftComponent1;
	private CraftComponentDTO craftComponent2;

	@BeforeMethod
	public void setUp() throws Exception {
		craftComponentCreateDTO = TestUtils.getCraftComponentCreateDTO("comp");
		craftComponent1 = craftComponentFacade.findComponentById(craftComponentFacade.addComponent(TestUtils.getCraftComponentCreateDTO("comp1")));
		SpacecraftCreateDTO spacecraftCreateDTO = TestUtils.getSpacecraftCreateDTO("Enterprise");
		spacecraftCreateDTO.setComponents(Collections.singleton(craftComponent1));
		spacecraftFacade.addSpacecraft(spacecraftCreateDTO);
		craftComponent1 = craftComponentFacade.findComponentById(craftComponent1.getId());
		craftComponent2 = craftComponentFacade.findComponentById(craftComponentFacade.addComponent(TestUtils.getCraftComponentCreateDTO("comp2")));
	}


	@AfterMethod
	public void tearDown() throws Exception {
		for (SpacecraftDTO spacecraft :
				spacecraftFacade.findAllSpacecrafts()) {
			spacecraftFacade.removeSpacecraft(spacecraft);
		}
		for (CraftComponentDTO craftComponent :
				craftComponentFacade.findAllComponents()) {
			craftComponentFacade.removeComponent(craftComponent);
		}
		assertThat(spacecraftFacade.findAllSpacecrafts()).isEmpty();
		assertThat(craftComponentFacade.findAllComponents()).isEmpty();
		craftComponentCreateDTO = null;
		craftComponent1 = null;
		craftComponent2 = null;
	}


	@Test
	public void testAddComponent() throws Exception {
		assertThat(craftComponentFacade.findAllComponents()).hasSize(2).contains(craftComponent1, craftComponent2);
		CraftComponentDTO craftComponentDTO = craftComponentFacade.findComponentById(craftComponentFacade.addComponent(craftComponentCreateDTO));
		assertThat(craftComponentDTO).isEqualToIgnoringGivenFields(craftComponentCreateDTO, "id");
		assertThat(craftComponentFacade.findAllComponents()).hasSize(3).contains(craftComponentDTO);
	}

	@Test
	public void testFindAllComponents() throws Exception {
		assertThat(craftComponentFacade.findAllComponents()).hasSize(2).contains(craftComponent1, craftComponent2);
	}

	@Test
	public void testFindComponentById() throws Exception {
		assertThat(craftComponentFacade.findComponentById(craftComponent1.getId())).isEqualTo(craftComponent1);
		assertThat(craftComponentFacade.findComponentById(craftComponent2.getId())).isEqualTo(craftComponent2);
	}

	@Test
	public void testUpdateComponent() throws Exception {
		craftComponent1.setName("UPDATED");
		craftComponentFacade.updateComponent(craftComponent1);
		assertThat(craftComponentFacade.findComponentById(craftComponent1.getId())).isEqualToComparingFieldByField(craftComponent1);
	}

	@Test
	public void testRemoveComponent() throws Exception {
		assertThat(craftComponentFacade.findAllComponents()).hasSize(2).contains(craftComponent1, craftComponent2);
		craftComponentFacade.removeComponent(craftComponent2);
		assertThat(craftComponentFacade.findAllComponents()).hasSize(1).contains(craftComponent1);
	}

}