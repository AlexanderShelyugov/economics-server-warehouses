./gradlew clean bootJar
eval $(minikube docker-env)
docker build --tag=economics-server-warehouses .
