#!/bin/sh
set -v

# kubectl create secret generic postgres-pwd --from-file=./password
kubectl create namespace sonar
kubectl create secret --namespace sonar generic postgres-pwd --from-file=./password
kubectl create -f . --namespace sonar
minikube service sonar --namespace sonar


