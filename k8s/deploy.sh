#!/bin/sh
set -v

ns='namespace sonar'

kubectl create ${ns}
kubectl create secret --${ns} generic postgres-pwd --from-file=./password
kubectl create -f . --${ns}
minikube service sonar --${ns}