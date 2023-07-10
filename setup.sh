# Setup kubernetes account
kubectl apply -f setup.yaml
kubectl create rolebinding airbyte-connector-binding \
    --clusterrole=edit \
    --serviceaccount=default:airbyte-connector \
    --namespace=default
