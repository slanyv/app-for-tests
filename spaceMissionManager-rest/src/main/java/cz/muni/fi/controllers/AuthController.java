package cz.muni.fi.controllers;

import cz.muni.fi.ApiUris;
import cz.muni.fi.config.MyUserPrincipal;
import cz.muni.fi.dto.UserDTO;
import cz.muni.fi.dto.UserLoginDTO;
import cz.muni.fi.dto.UserUpdatePwdDTO;
import cz.muni.fi.exceptions.ResourceNotFoundException;
import cz.muni.fi.facade.UserFacade;
import cz.muni.fi.services.impl.ServiceDataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jsmadis
 *
 * @author jsmadis
 */

@RestController
@RequestMapping(ApiUris.ROOT_URI_AUTH)
public class AuthController {

	private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserFacade userFacade;

	@Autowired
	private PasswordEncoder encoder;

	/**
	 * Authenticates user
	 *
	 * @param user UserLoginDTO
	 * @return UserDTO
	 */

	@RolesAllowed({"MANAGER", "USER"})
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public UserDTO authenticate(@RequestBody UserLoginDTO user, HttpServletResponse response, Authentication authentication) {
		logger.debug("[REST] authenticate()");
		if (user == null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return null;
		}
		user.setPassword(((MyUserPrincipal) authentication.getPrincipal()).getPassword());
		try {
			if (userFacade.authenticate(user.getEmail(), user.getPassword())) {
				return userFacade.findUserByEmail(user.getEmail());
			}
		} catch (ServiceDataAccessException e) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		return null;
	}

	/**
	 * Updates password of user
	 *
	 * @param user UserUpdatePwdDTO
	 * @return updated UserDTO
	 */

	@RolesAllowed({"MANAGER", "USER"})
	@RequestMapping(value = "/update", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public UserDTO updatePassword(@RequestBody UserUpdatePwdDTO user) {
		logger.debug("[REST] update password");

		if (user == null) {
			throw new ResourceNotFoundException();
		}
		user.setCurrentPassword(encoder.encode(user.getCurrentPassword()));
		user.setNewPassword(encoder.encode(user.getNewPassword()));
		return userFacade.updatePassword(user);
	}
}
