package cz.muni.fi.services;

import org.dozer.Mapper;

import java.util.Collection;
import java.util.List;

public interface BeanMappingService {
	/**
	 * Maps collection of objects to given class.
	 * Used for example to map DTO objects to entities and vice-versa
	 * @param objects collection of objects to be mapped
	 * @param mapToClass class to map to
	 * @param <T> class to map to
	 * @return list of mapped objects
	 */
	<T> List<T> mapTo(Collection<?> objects, Class<T> mapToClass);

	/**
	 * Maps single object to given class
	 * @param u object to be mapped
	 * @param mapToClass taget class to map to
	 * @param <T> class to map to
	 * @return mapped object
	 */
	<T> T mapTo(Object u, Class<T> mapToClass);

	/**
	 * Returns instance of the mapper
	 * @return mapper instance
	 */
	Mapper getMapper();
}
