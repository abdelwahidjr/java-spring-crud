## Starting Guide
- clone the repo
- change the .env "build path" for docker and "WORKDIR" same for custom setup if needed
- docker-compose up -d
- import postman collection [JavaTask.postman_collection.json](JavaTask.postman_collection.json)
- import postman environment [JAVA TASK ENV.postman_environment.json](JAVA%20TASK%20ENV.postman_environment.json)
- you can sign up , signin you will get a token and injected automatically in postman env then you can CRUD or BREAD operations other requests
- change the mail credentials to receive an email I kept the mail fun out of registration to not crack the flow for testing purpose
  - due to time limitation, so we can enhance this small app by adding UI,pagination,complete module for API Error Handling and standard messaging response full api docs on swagger or postman etc...