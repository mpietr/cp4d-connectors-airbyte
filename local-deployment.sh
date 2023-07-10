# Define variables
POD_NAME="airbyte-connector"
OLD_IMAGE_NAME="wdp-connect-sdk-gen-airbyteflight"
IMAGE_NAME="airbyte-connector"
REGISTRY_IMAGE_NAME="put/${IMAGE_NAME}"

# Setup env
kubectl delete deployment airbyte-connector --ignore-not-found=true --wait=true
eval $(minikube -p minikube docker-env)

# Build image
./gradlew dockerBuild
docker tag $OLD_IMAGE_NAME $REGISTRY_IMAGE_NAME
docker push $REGISTRY_IMAGE_NAME

# Make deployment
kubectl apply -f deployment.yaml
kubectl wait deployment -n default airbyte-connector --for condition=Available=True --timeout=90s
kubectl port-forward svc/$POD_NAME 9443:9443
