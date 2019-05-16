package cz.muni.fi.facades;

import cz.muni.fi.dto.UserCreateDTO;
import cz.muni.fi.dto.UserDTO;
import cz.muni.fi.dto.UserUpdatePwdDTO;
import cz.muni.fi.entity.User;
import cz.muni.fi.facade.UserFacade;
import cz.muni.fi.services.BeanMappingService;
import cz.muni.fi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserFacadeImpl implements UserFacade {
	@Autowired
	BeanMappingService beanMappingService;

	@Autowired
	UserService userService;

	@Override
	public void acceptAssignedMission(UserDTO user) {
		userService.acceptAssignedMission(beanMappingService.mapTo(user, User.class));
	}

	@Override
	public void rejectAssignedMission(UserDTO user, String explanation) {
		userService.rejectAssignedMission(beanMappingService.mapTo(user, User.class), explanation);
	}

	@Override
	public Long addUser(UserCreateDTO user) {
		User mappedUser = beanMappingService.mapTo(user, User.class);
		userService.addUser(mappedUser);
		return mappedUser.getId();
	}

	@Override
	public void updateUser(UserDTO user) {
		User mappedUser = beanMappingService.mapTo(user, User.class);
		userService.updateUser(mappedUser);
	}

	@Override
	public void deleteUser(UserDTO user) {
		User mappedUser = beanMappingService.mapTo(user, User.class);
		userService.deleteUser(mappedUser);
	}

	@Override
	public List<UserDTO> findAllUsers() {
		return beanMappingService.mapTo(userService.findAllUsers(), UserDTO.class);
	}

	@Override
	public List<UserDTO> findAllAstronauts() {
		return beanMappingService.mapTo(userService.findAllAstronauts(), UserDTO.class);
	}

	@Override
	public UserDTO findUserById(Long id) {
		User user = userService.findUserById(id);
		return user == null ? null : beanMappingService.mapTo(user, UserDTO.class);
	}

	@Override
	public List<UserDTO> findAllAvailableAstronauts() {
		return beanMappingService.mapTo(userService.findAllAvailableAstronauts(), UserDTO.class);
	}

	@Override
	public UserDTO findUserByEmail(String email) {
		User user = userService.findUserByEmail(email);
		return user == null ? null : beanMappingService.mapTo(user, UserDTO.class);
	}

	@Override
	public boolean authenticate(String email, String unencryptedPassword) {
		User user = userService.findUserByEmail(email);
		return user != null && userService.authenticate(user, unencryptedPassword);
	}

	@Override
	public UserDTO updatePassword(UserUpdatePwdDTO user) {
		User userEntity = beanMappingService.mapTo(findUserById(user.getId()), User.class);

		if (!userService.updatePassword(userEntity, user.getCurrentPassword(), user.getNewPassword())) {
			throw new IllegalArgumentException("Invalid password");
		}

		return findUserById(user.getId());
	}

	@Override
	public boolean isManager(UserDTO user) {
		return userService.isManager(beanMappingService.mapTo(user, User.class));
	}
}
