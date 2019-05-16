package cz.muni.fi.dao;

import cz.muni.fi.entity.CraftComponent;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by jcizmar
 *
 * @author jcizmar
 */
@Repository
@Transactional
public class CraftComponentDaoImpl implements CraftComponentDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CraftComponent addComponent(CraftComponent craftComponent) {
		if (craftComponent == null) {
			throw new IllegalArgumentException("craftComponent is null");
		}
		if (craftComponent.getName() == null) {
			throw new IllegalArgumentException("name is null");
		}
		if (craftComponent.getId() != null) {
			throw new IllegalArgumentException("id is not null");
		}
		this.entityManager.persist(craftComponent);
		return craftComponent;
	}

	@Override
	public List<CraftComponent> findAllComponents() {
		return entityManager.createQuery("select cc from CraftComponent cc", CraftComponent.class).getResultList();
	}

	@Override
	public CraftComponent findComponentById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("id is null");
		}
		try {
			return entityManager.createQuery("select cc from CraftComponent cc fetch all properties where id = :id", CraftComponent.class)
					.setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}

	@Override
	public CraftComponent updateComponent(CraftComponent craftComponent) {
		if (craftComponent == null) {
			throw new IllegalArgumentException("craftComponent is null");
		}
		if (craftComponent.getName() == null) {
			throw new IllegalArgumentException("name is null");
		}
		if (craftComponent.getId() == null) {
			throw new IllegalArgumentException("id is null");
		}
		this.entityManager.merge(craftComponent);
		this.entityManager.flush();
		return craftComponent;
	}

	@Override
	public CraftComponent removeComponent(CraftComponent craftComponent) {
		if (craftComponent == null) {
			throw new IllegalArgumentException("craftComponent is null");
		}
		if (craftComponent.getId() == null) {
			throw new IllegalArgumentException("id is null");
		}
		CraftComponent del = findComponentById(craftComponent.getId());
		if (del == null) {
			throw new IllegalArgumentException("Component does not exist");
		}
		if (del.getSpacecraft() != null && del.getSpacecraft().getComponents().size() == 1) {
			throw new IllegalArgumentException("Can't remove only component in spacecraft");
		}
		entityManager.remove(del);
		return craftComponent;
	}
}
