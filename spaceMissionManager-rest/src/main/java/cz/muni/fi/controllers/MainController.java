package cz.muni.fi.controllers;

import cz.muni.fi.ApiUris;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
public class MainController {
	private static final Logger logger = Logger.getLogger(MainController.class.getName());



	@RequestMapping( method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public final Map<String, String> getResources() {

		Map<String,String> resourcesMap = new HashMap<>();

		resourcesMap.put("users_uri", ApiUris.ROOT_URI_USERS);
		resourcesMap.put("components_uri", ApiUris.ROOT_URI_CRAFTCOMPONENTS);
		resourcesMap.put("auth_uri", ApiUris.ROOT_URI_AUTH);
		resourcesMap.put("missions_uri", ApiUris.ROOT_URI_MISSIONS);

		return Collections.unmodifiableMap(resourcesMap);

	}
}
