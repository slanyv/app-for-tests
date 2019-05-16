package cz.muni.fi.facades;

import cz.muni.fi.dto.CraftComponentCreateDTO;
import cz.muni.fi.dto.CraftComponentDTO;
import cz.muni.fi.entity.CraftComponent;
import cz.muni.fi.facade.CraftComponentFacade;
import cz.muni.fi.services.BeanMappingService;
import cz.muni.fi.services.CraftComponentService;
import cz.muni.fi.services.SpacecraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CraftComponentFacadeImpl implements CraftComponentFacade {

    @Autowired
    CraftComponentService craftComponentService;

    @Autowired
    SpacecraftService spacecraftService;

    @Autowired
    BeanMappingService beanMappingService;

    @Override
    public Long addComponent(CraftComponentCreateDTO craftComponent) {
        CraftComponent mappedCraftComponent = beanMappingService.mapTo(craftComponent, CraftComponent.class);
        craftComponentService.addComponent(mappedCraftComponent);
        return mappedCraftComponent.getId();
    }

    @Override
    public List<CraftComponentDTO> findAllComponents() {
        return beanMappingService.mapTo(craftComponentService.findAllComponents(), CraftComponentDTO.class);
    }

    @Override
    public CraftComponentDTO findComponentById(Long id) {
        CraftComponent c = craftComponentService.findComponentById(id);
        return (c == null) ? null : beanMappingService.mapTo(c, CraftComponentDTO.class);
    }

    @Override
    public void updateComponent(CraftComponentDTO craftComponent) {
        craftComponentService.updateComponent(beanMappingService.mapTo(craftComponent, CraftComponent.class));
    }

    @Override
    public void removeComponent(CraftComponentDTO craftComponent) {
        craftComponentService.removeComponent(beanMappingService.mapTo(craftComponent, CraftComponent.class));
    }
}
