FROM maven:3.8.2-jdk-8

WORKDIR /Users/abdelwahidjr/MacBookDisk/code/Java/java-task
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run
