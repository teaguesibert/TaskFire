gradlew clean build
docker build -t taskfire .
docker run -it --rm -p 8080:8080 --name taskfireserver taskfire java -jar ./jars/taskfireapi.jar --inMemory