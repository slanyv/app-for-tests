CraftComponent Rest testing curl commands

Test findAll:
curl -X GET \
  http://localhost:8080/pa165/rest/craftComponents/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 9ef27618-7713-6c32-60ec-9a0759600164'

Test findById:
curl -X GET \
  http://localhost:8080/pa165/rest/craftComponents/{id} \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 114d6edf-d8ff-bf7c-290c-1ec489fc991d'

Test create:
curl -X POST \
  http://localhost:8080/pa165/rest/craftComponents/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: c53a8050-affa-4db6-762b-19fed285fc54' \
  -d '{
"readyToUse": false,
"name": "Wing4",
"spacecraft": null,
"readyDate": "2017-12-22T16:40:17.629+01:00[Europe/Prague]"
}'

Test update:
curl -X PUT \
  http://localhost:8080/pa165/rest/craftComponents/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: e3b45f7d-038f-fde3-8700-4f7898556b6d' \
  -d '{
"id": "2",
"readyToUse": false,
"name": "Wing120",
"spacecraft": null,
"readyDate": "2017-12-22T16:40:17.629+01:00[Europe/Prague]"
}'

User Rest testing curl commands

Test findAll:
curl -X GET \
  http://localhost:8080/pa165/rest/users/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 42d714f1-97b9-99a9-c75d-4cf71bc0d5a8'

Test findById:
curl -X GET \
  http://localhost:8080/pa165/rest/users/{id} \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 155b34b2-53f6-bb96-39fe-bc7335cfc476'

Test create:
curl -X POST \
  http://localhost:8080/pa165/rest/users/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: ae32cc17-776d-b46d-f925-8ae9dc39d5f4' \
  -d '{
    "name": "Gagarin",
    "email": "gagarin1@gmail.com",
    "password": "1000:7659677b31cfa20fc71876734a58491198ddbbc1c4746f52:0808b9f7fa8cc4d6fd42ef35257e2b0805e9f2ac8de75e42",
    "experienceLevel": 10,
    "acceptedMission": false,
    "explanation": null,
    "mission": null,
    "manager": false,
    "birthDate": "1934-03-09"
}'

Test update:
curl -X PUT \
  http://localhost:8080/pa165/rest/users/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 2c96e5ab-9e6a-da02-dd56-11276047d3b5' \
  -d '{
    "id": "2",
    "name": "Gagarin4",
    "email": "gagarin5@gmail.com",
    "password": "1000:7659677b31cfa20fc71876734a58491198ddbbc1c4746f52:0808b9f7fa8cc4d6fd42ef35257e2b0805e9f2ac8de75e42",
    "experienceLevel": 10,
    "acceptedMission": false,
    "explanation": null,
    "mission": null,
    "manager": false,
    "birthDate": "1934-03-09"
}'

Test delete:
curl -X DELETE \
  http://localhost:8080/pa165/rest/users/{id} \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 155b34b2-53f6-bb96-39fe-bc7335cfc476'


Test findAllAvailableAstronauts:
curl -X GET \
  http://localhost:8080/pa165/rest/users/astronauts/available \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 42d714f1-97b9-99a9-c75d-4cf71bc0d5a8'


Test findAllAstronauts:
curl -X GET \
  http://localhost:8080/pa165/rest/users/astronauts \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 42d714f1-97b9-99a9-c75d-4cf71bc0d5a8'


Test acceptMission:
curl -X GET \
  http://localhost:8080/pa165/rest/users/astronauts/{id}/acceptMission \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 42d714f1-97b9-99a9-c75d-4cf71bc0d5a8'

Test rejectMission:
curl -X POST \
  http://localhost:8080/pa165/rest/users/astronauts/{id}/rejectMission \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 42d714f1-97b9-99a9-c75d-4cf71bc0d5a8'
  -d '{"explanation": "explanation"}'


Test findByEmail:
curl -X GET \
  http://localhost:8080/pa165/rest/users/email \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 42d714f1-97b9-99a9-c75d-4cf71bc0d5a8'
  -d '{"email": "example@example.com"}'


  Spacecraft Rest testing curl commands
  

  Test findAll:
curl -X GET \
  http://localhost:8080/pa165/rest/spacecrafts/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 42d714f1-97b9-99a9-c75d-4cf71bc0d5a8'

Test findById:
curl -X GET \
  http://localhost:8080/pa165/rest/spacecrafts/{id} \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 155b34b2-53f6-bb96-39fe-bc7335cfc476'

Test create:
curl -X POST \
  http://localhost:8080/pa165/rest/spacecrafts/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: ae32cc17-776d-b46d-f925-8ae9dc39d5f4' \
  -d '{
    "type": "Manned spacecraft",
        "name": "Apollo 11",
        "components": [
            {
                "id": 1,
                "readyToUse": false,
                "name": "Wing",
                "readyDate": "2017-12-23T23:33:43.206+01:00[Europe/Prague]"
            },
            {
                "id": 2,
                "readyToUse": true,
                "name": "Fuel tank",
                "readyDate": null
            }
        ]
}'

Test update:
curl -X PUT \
  http://localhost:8080/pa165/rest/spacecrafts/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 2c96e5ab-9e6a-da02-dd56-11276047d3b5' \
  -d '{
    "id" : 1,
    "type": "Manned spacecraft",
        "name": "Apollo 11",
        "components": [
            {
                "id": 1,
                "readyToUse": false,
                "name": "Wing",
                "readyDate": "2017-12-23T23:33:43.206+01:00[Europe/Prague]"
            },
            {
                "id": 2,
                "readyToUse": true,
                "name": "Fuel tank",
                "readyDate": null
            }
        ]
}'

Test delete:
curl -X DELETE \
  http://localhost:8080/pa165/rest/spacecrafts/{id} \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 155b34b2-53f6-bb96-39fe-bc7335cfc476'




  Mission Rest testing curl commands
  

  Test findAll:
curl -X GET \
  http://localhost:8080/pa165/rest/missions/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 42d714f1-97b9-99a9-c75d-4cf71bc0d5a8'

Test findById:
curl -X GET \
  http://localhost:8080/pa165/rest/missions/{id} \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 155b34b2-53f6-bb96-39fe-bc7335cfc476'

Test create:
curl -X POST \
  http://localhost:8080/pa165/rest/missions/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: ae32cc17-776d-b46d-f925-8ae9dc39d5f4' \
  -d '{
        "astronauts": [
            {
                "id": 2,
                "name": "Gagarin",
                "email": "gagarin@gmail.com",
                "password": "1000:9a780b21a3628308cbf4ad0e78ed6e7ebc808fab27c5748d:18e2d5b17579e44801b65b25cdd253cd7ca8cada9dacfd84",
                "experienceLevel": 10,
                "acceptedMission": false,
                "explanation": null,
                "manager": false,
                "birthDate": "1934-03-09"
            }
        ],
        "spacecrafts": [
            {
                "id": 1,
                "type": "Manned spacecraft",
                "name": "Apollo 11",
                "components": [
                    {
                        "id": 1,
                        "readyToUse": false,
                        "name": "Wing",
                        "readyDate": "2017-12-23T23:33:43.206+01:00[Europe/Prague]"
                    },
                    {
                        "id": 2,
                        "readyToUse": true,
                        "name": "Fuel tank",
                        "readyDate": null
                    }
                ]
            }
        ],
        "name": "Mars mission",
        "destination": "Mars",
        "missionDescription": "Simple mission to colonize Mars",
        "active": true,
        "result": null,
        "eta": "2018-02-06T23:33:43.977+01:00[Europe/Prague]",
        "endDate": null
}'

Test update:
curl -X PUT \
  http://localhost:8080/pa165/rest/missions/ \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'postman-token: 2c96e5ab-9e6a-da02-dd56-11276047d3b5' \
  -d '{
    "id": 1,
        "astronauts": [
            {
                "id": 2,
                "name": "Gagarin",
                "email": "gagarin@gmail.com",
                "password": "1000:9a780b21a3628308cbf4ad0e78ed6e7ebc808fab27c5748d:18e2d5b17579e44801b65b25cdd253cd7ca8cada9dacfd84",
                "experienceLevel": 10,
                "acceptedMission": false,
                "explanation": null,
                "manager": false,
                "birthDate": "1934-03-09"
            }
        ],
        "spacecrafts": [
            {
                "id": 1,
                "type": "Manned spacecraft",
                "name": "Apollo 11",
                "components": [
                    {
                        "id": 1,
                        "readyToUse": false,
                        "name": "Wing",
                        "readyDate": "2017-12-23T23:33:43.206+01:00[Europe/Prague]"
                    },
                    {
                        "id": 2,
                        "readyToUse": true,
                        "name": "Fuel tank",
                        "readyDate": null
                    }
                ]
            }
        ],
        "name": "Mars mission",
        "destination": "Mars",
        "missionDescription": "Simple mission to colonize Mars",
        "active": true,
        "result": null,
        "eta": "2018-02-06T23:33:43.977+01:00[Europe/Prague]",
        "endDate": null
}'

Test delete:
curl -X DELETE \
  http://localhost:8080/pa165/rest/missions/{id} \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 155b34b2-53f6-bb96-39fe-bc7335cfc476'


Test archive:
curl -X POST \
  http://localhost:8080/pa165/rest/missions/{id}/archive \
  -H 'authorization: Basic QURNSU46QURNSU4=' \
  -H 'cache-control: no-cache' \
  -H 'postman-token: 155b34b2-53f6-bb96-39fe-bc7335cfc476'
