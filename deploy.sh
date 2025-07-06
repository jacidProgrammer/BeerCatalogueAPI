#!/bin/bash

echo "▶️ Starting Minikube..."
minikube start

echo "🔁 Setting Docker environment to Minikube..."
eval $(minikube docker-env)

echo "🧼 Deleting existing pods for beer-catalogue..."
kubectl delete pod -l app=beer-catalogue --ignore-not-found

echo "🐳 Building Docker image beer-catalogue:latest..."
docker build --no-cache -t beer-catalogue:latest .

echo "⚙️ Using Minikube context..."
kubectl config use-context minikube

echo "🚀 Applying Kubernetes manifests..."
kubectl apply -f ./k8s/

echo "⏳ Waiting for deployment to be available..."
kubectl wait --for=condition=available --timeout=120s deployment/beer-catalogue

echo "📦 Listing pods and services..."
kubectl get pods
kubectl get svc

echo "🌐 Opening beer-catalogue service in browser..."
minikube service beer-catalogue-service