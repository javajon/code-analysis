#!/bin/sh
set -v

kubectl create -f sonar-pv-postgres.yaml
kubectl create -f sonar-pvc-postgres.yaml 
kubectl create -f sonar-postgres-deployment.yaml 
kubectl create -f sonar-postgres-service.yaml 

kubectl create -f sonar-deployment.yaml 
kubectl create -f sonar-service.yaml 

