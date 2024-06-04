# Guide - Airbyte Connector - Windows
## Requirements
- `git` and `git bash`
- `docker`
- `minikube`
- `java 11`

## Java version
Command line `java -version` must return java version 11 (in `git bash`). 
If the command returns different version, than after 4th step execute following commands:
- `export JAVA_HOME="/c/Users/tmp_user/.jdks/temurin-11.0.22" ` - path to main directory with Java, replace `\` with `/` and replace begining `/c/` with `C:\`
- `export PATH=$JAVA_HOME/bin:$PATH`

## Steps
1. Start minikube: `minikube start`
2. Clone the repository: `git clone` https://github.com/IBM/cp4d-connectors-airbyte.git
3. Open build.gradle file and go to line 189. 
Change `kernel-java11-openj9-ubi` to `22.0.0.12-kernel-java11-openj9-ubi`
4. Start `git bash` (Start menu or Windows Terminal)
5. Go to your project directory
6. Run command: `eval $(minikube -p minikube --shell bash docker-env)`
7. Run this commands:

```
POD_NAME="airbyte-connector"
OLD_IMAGE_NAME="wdp-connect-sdk-gen-airbyteflight"
IMAGE_NAME="airbyte-connector"
REGISTRY_IMAGE_NAME="put/${IMAGE_NAME}"
kubectl delete deployment airbyte-connector --ignore-not-found=true --wait=true
./gradlew assemble
./gradlew dockerBuild
docker tag $OLD_IMAGE_NAME $REGISTRY_IMAGE_NAME
kubectl apply -f setup.yaml
kubectl create rolebinding airbyte-connector-binding --clusterrole=edit --serviceaccount=default:airbyte-connector --namespace=default
kubectl apply -f deployment.yaml
kubectl wait deployment -n default airbyte-connector --for condition=Available=True --timeout=90s
kubectl port-forward svc/$POD_NAME 9443:9443
```

8. You can see the pod's status using `minikube dashboard`
9. To avoid repetitie building of project, you can use `minikube stop` to pause and `minikube start` to resume
10. To completly delete local cluster, use `minikube delete`