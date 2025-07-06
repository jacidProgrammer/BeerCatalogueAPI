# ▶️ Start Minikube (will do nothing if already started)
Write-Host '▶️ Starting Minikube...'
minikube start

# 🔁 Set Docker environment to Minikube's internal Docker daemon
Write-Host '🔁 Setting Docker environment to Minikube...'
$envVars = & minikube docker-env --shell powershell | Out-String
Invoke-Expression $envVars
# 🔐 Overwrite DOCKER_CERT_PATH manually to avoid issues with Minikube my user Jose Antonio with spaces in the folder had issues, so moved certs to a path without spaces
$Env:DOCKER_CERT_PATH = 'C:\MinikubeCerts'

# 🧼 Delete existing beer-catalogue pods (optional but ensures fresh deployment)
Write-Host '🧼 Deleting existing pods for beer-catalogue...'
kubectl delete pod -l app=beer-catalogue --ignore-not-found

# 🐳 Build Docker image without cache to ensure freshness
Write-Host '🐳 Building Docker image beer-catalogue:latest...'
docker build --no-cache -t beer-catalogue:latest .

# ⚙️ Use Minikube context
Write-Host '⚙️ Switching to Minikube context...'
kubectl config use-context minikube

# 🚀 Apply Kubernetes manifests (deployment, service, secrets, etc.)
Write-Host '🚀 Applying Kubernetes manifests...'
kubectl apply -f .\k8s\

# ⏳ Wait until the deployment is ready
Write-Host '⏳ Waiting for deployment to be available...'
kubectl wait --for=condition=available --timeout=120s deployment/beer-catalogue

# 📦 List pods and services to confirm deployment
Write-Host '📦 Listing pods and services...'
kubectl get pods
kubectl get svc

# 🌐 Open the service in the browser (NodePort)
Write-Host '🌐 Opening beer-catalogue service in browser...'
minikube service beer-catalogue-service
