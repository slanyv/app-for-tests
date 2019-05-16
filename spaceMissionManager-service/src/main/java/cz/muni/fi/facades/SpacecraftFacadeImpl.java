package cz.muni.fi.facades;

import cz.muni.fi.dto.SpacecraftCreateDTO;
import cz.muni.fi.dto.SpacecraftDTO;
import cz.muni.fi.entity.Spacecraft;
import cz.muni.fi.facade.SpacecraftFacade;
import cz.muni.fi.services.BeanMappingService;
import cz.muni.fi.services.SpacecraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SpacecraftFacadeImpl implements SpacecraftFacade {
    @Autowired
    BeanMappingService beanMappingService;

    @Autowired
    SpacecraftService spacecraftService;

    @Override
    public Long addSpacecraft(SpacecraftCreateDTO spacecraft) {
        Spacecraft mappedSpacecraft = beanMappingService.mapTo(spacecraft, Spacecraft.class);
        spacecraftService.addSpacecraft(mappedSpacecraft);
        return mappedSpacecraft.getId();
    }

    @Override
    public void removeSpacecraft(SpacecraftDTO spacecraft) {
        Spacecraft mappedSpacecraft = beanMappingService.mapTo(spacecraft, Spacecraft.class);
        spacecraftService.removeSpacecraft(mappedSpacecraft);
    }

    @Override
    public List<SpacecraftDTO> findAllSpacecrafts() {
        return beanMappingService.mapTo(spacecraftService.findAllSpacecrafts(), SpacecraftDTO.class);
    }

    @Override
    public SpacecraftDTO findSpacecraftById(Long id) {
        Spacecraft spacecraft = spacecraftService.findSpacecraftById(id);
        return spacecraft == null ? null : beanMappingService.mapTo(spacecraftService.findSpacecraftById(id), SpacecraftDTO.class);
    }

    @Override
    public void updateSpacecraft(SpacecraftDTO spacecraft) {
        Spacecraft mappedSpacecraft = beanMappingService.mapTo(spacecraft, Spacecraft.class);
        spacecraftService.updateSpacecraft(mappedSpacecraft);
    }
}
