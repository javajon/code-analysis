#!/bin/sh
set -v

kubectl delete -f sonar-pv-postgres.yaml
kubectl delete -f sonar-pvc-postgres.yaml 
kubectl delete -f sonar-postgres-deployment.yaml 
kubectl delete -f sonar-postgres-service.yaml 

kubectl delete -f sonar-deployment.yaml 
kubectl delete -f sonar-service.yaml 

kubectl get pods,services,deployments,persistentvolumes,persistentvolumeclaims
