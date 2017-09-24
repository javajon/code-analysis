# SonarQube with Kubernetes #

Follow these instructions to setup a personal [SonarQube engine and dashboard](https://www.sonarqube.org). With 
this you have a strong static code analysis tool backing your code changes all before you submit your work for 
pull requests. Within SonarQube there are plugins for Checkstyle, PMD and Findbugs. The Fingbugs plugin 
includes rules for vulnerabilities such as the [OWASP top 10](http://find-sec-bugs.github.io).

The code analysis tools within your IDE offers similar rules and can find many of the same technical debts, 
violations, vulnerabilities and anti-patterns. SonarQube provides additional statistics relates to metrics 
on size and coverage.

This solution is not to be confused with the [SonarLint plugin](http://www.sonarlint.org) 
for your IDE. This is another effective way to interact with SonarQube before a pull request.

### How do I get set up? ###

* Install [Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/) (or any other Kubernetes solution)
* Clone this project with Git
* Apply the Postgres secret. In the k8s directory run: "kubectl create secret generic postgres-pwd --from-file=./password"
* Run the k8s/deploy_sonar.sh script that uses KubeCtl to stand up the 
cluster containing SonarQube engine and dashboard. On Windows use the Git Bash command prompt to run Bash scripts.
* If your Kubernetes cluster is not at 192.168.99.100 you will have to modify the 
externalIP in sonar-service.yaml before running the deploy script. See next section to verify your IP.

### How do I analyze my code? ###

* Using Gradle ensure the [SonarQube plugin](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle) is present and has exposed the task "sonarqube"
* Run "./gradlew -Dsonar.host.url=http://192.168.99.100/sonar sonarqube"
* After the analysis completes navigate to the SonarQube interactive dashboard at http://192.168.99.100/sonar. 
* The IP of Kubernetes may vary, run "minikube ip" or "kubectl cluster-info" to determine the IP for the instructions above

### Technology stack ###

* Gradle with the [Sonarqube plugin](https://plugins.gradle.org/plugin/org.sonarqube)
* [SonarQube for Docker](https://hub.docker.com/_/sonarqube/)
* [Postgres for Docker](https://hub.docker.com/_/postgres/) as backing store for SonarQube
* Kubernetes cluster is configures with a PersistentVolume to store the Postgres data in 
your local operating system user account in the folder "sonarqube-postgres". See improvements 
section below.

### Additional information ###

* Visit the No Fluff Just Stuff tour and see this example in action. [A K-2SO Like Static Code Analyzer for Your Squad](https://www.nofluffjuststuff.com/conference/speaker/jonathan_johnson)
* [SonarQube integration](https://www.sonarsource.com/why-us/integration/)
* [SonarCloud Nemo](https://sonarcloud.io/projects?sort=-analysis_date), SonarQube continuous analysis reporting of many open source projects. 
* [This solution was inspired from this blog, "From Pet to Cattle – Running Sonar on Kubernetes"](http://container-solutions.com/pet-cattle-running-sonar-kubernetes)

### Additional improvements ###

* You can configure your local SonarQube to match our team's SonarQube configuration
* TODO: Fix persistence problem to OS file system of the OS hosting VirtualBox. Currently the database 
is persisted in the virtual machine and not on the OS hosting VirtualBox. This means analysis data 
will be lost if your virtual box machine is restarted. Once we can get the data persisted locally 
then data will be restored with any restarts.