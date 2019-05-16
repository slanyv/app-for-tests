package cz.muni.fi.facades;

import cz.muni.fi.config.ServiceConfiguration;
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
public class SpacecraftFacadeImplTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private SpacecraftFacade spacecraftFacade;

	@Autowired
	private CraftComponentFacade craftComponentFacade;

	private SpacecraftCreateDTO spacecraftCreateDTO;
	private SpacecraftDTO spacecraftDTO1;
	private SpacecraftDTO spacecraftDTO2;


	@BeforeMethod
	public void setUp() throws Exception {
		spacecraftCreateDTO = TestUtils.getSpacecraftCreateDTO("Enterprise");
		spacecraftCreateDTO.setComponents(Collections.singleton(getCraftComponentDTO("Enterprise")));
		SpacecraftCreateDTO createDTO = TestUtils.getSpacecraftCreateDTO("Glenn");
		createDTO.setComponents(Collections.singleton(getCraftComponentDTO("Glenn")));
		spacecraftDTO1 = spacecraftFacade.findSpacecraftById(spacecraftFacade.addSpacecraft(createDTO));
		createDTO = TestUtils.getSpacecraftCreateDTO("Discovery");
		createDTO.setComponents(Collections.singleton(getCraftComponentDTO("Discovery")));
		spacecraftDTO2 = spacecraftFacade.findSpacecraftById(spacecraftFacade.addSpacecraft(createDTO));
	}

	private CraftComponentDTO getCraftComponentDTO(String name){
		return craftComponentFacade.findComponentById(craftComponentFacade.addComponent(TestUtils.getCraftComponentCreateDTO(name)));
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
		spacecraftCreateDTO = null;
		spacecraftDTO1 = null;
		spacecraftDTO2 = null;
	}

	@Test
	public void testAddSpacecraft() throws Exception {

		assertThat(spacecraftFacade.findAllSpacecrafts()).hasSize(2);
		SpacecraftDTO spacecraftDTO = spacecraftFacade.findSpacecraftById(spacecraftFacade.addSpacecraft(spacecraftCreateDTO));
		assertThat(spacecraftDTO).isEqualToIgnoringGivenFields(spacecraftCreateDTO, "id");
		assertThat(spacecraftFacade.findAllSpacecrafts()).contains(spacecraftDTO);
	}

	@Test
	public void testRemoveSpacecraft() throws Exception {

		assertThat(spacecraftFacade.findAllSpacecrafts()).hasSize(2).contains(spacecraftDTO1, spacecraftDTO2);
		spacecraftFacade.removeSpacecraft(spacecraftDTO1);
		assertThat(spacecraftFacade.findAllSpacecrafts()).hasSize(1).contains(spacecraftDTO2);
	}

	@Test
	public void testFindAllSpacecrafts() throws Exception {
		assertThat(spacecraftFacade.findAllSpacecrafts()).hasSize(2).contains(spacecraftDTO1, spacecraftDTO2);
	}

	@Test
	public void testFindSpacecraftById() throws Exception {
		assertThat(spacecraftFacade.findSpacecraftById(spacecraftDTO1.getId())).isEqualTo(spacecraftDTO1);
		assertThat(spacecraftFacade.findSpacecraftById(spacecraftDTO2.getId())).isEqualTo(spacecraftDTO2);
	}

	@Test
	public void testUpdateSpacecraft() throws Exception {
		spacecraftDTO1.setName("UPDATED");
		spacecraftFacade.updateSpacecraft(spacecraftDTO1);
		assertThat(spacecraftFacade.findSpacecraftById(spacecraftDTO1.getId())).isEqualToComparingFieldByField(spacecraftDTO1);
	}

}