# Space-Mission-Manager
![BuildStatus](https://travis-ci.org/mlynarikj/Space-Mission-Manager.svg?branch=package-reorganization) 

The web application can be started with command `maven jetty:run` on `space-mission-manager-angular` folder. 
Application communication could be changed in `service.js` class located in `\spaceMissionManager-angular\src\main\webapp\` folder. For normally working application set `PORT_NUMBER` to value `8080` and for test with mocking set value to `8081`.

The web application allows the management of a space mission (clearly simplified from real needs!). The managers can mainly track different missions and for each mission the required components. Each mission has a final destination and Estimated Time of Arrival (ETA). They can also track the completion of rocket construction for a mission, based on the several components that are needed. They can assign astronauts with different levels of experience to the missions. There are also exploratory missions in which there are no astronauts, but just probes / pathfinders for planets exploration. Ended missions need to be archived with all the information that was collected. Each astronaut can login to the system and see to which mission he has been assigned. He can confirm / reject the participation to the mission and add their explanation for the decision.
