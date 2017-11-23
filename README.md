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
* Install kubectl and verify `kubectl version` runs correctly
* Clone this project with Git
* Apply the Postgres secret. In the k8s directory run: `kubectl create secret generic postgres-pwd --from-file=./password`
* Change directory to k8s and run the deploy.sh script. This scripts uses KubeCtl to stand up the 
cluster containing SonarQube engine and dashboard. On Windows use the Git Bash command prompt to 
run Bash scripts.

### How do I analyze my code? ###

There is an example project called `microservices`. Within is a Gradle build that will fully build a
microservice to a Docker registry. The microservice is based on java, Springboot and MyBatis.

The Gradle task `sonarqube` will generate and publish results to a SonarQube service.

* Using Gradle, run<p>
`gradlew -Dsonar.host.url=$(minikube service sonar --namespace sonar --url)/sonar sonarqube`

* After the analysis completes, use this next command to navigate to the SonarQube dashboard 
`minikube service sonar --namespace sonar` 

When the browser tab appears, append `/sonar` to the end of the URL.  

Independent of SonarQube, there is a Gradle task `check` will generate an extensive list of example analysis reports
in the build directory. These come from a variety of analysis plugins that have been added to this Gradle project.


### Technology stack ###

* Gradle with the [Sonarqube plugin](https://plugins.gradle.org/plugin/org.sonarqube)
* [SonarQube for Docker](https://hub.docker.com/_/sonarqube/)
* [Postgres for Docker](https://hub.docker.com/_/postgres/) as backing store for SonarQube
* Kubernetes cluster is configures with a PersistentVolume to store the Postgres data in 
your local operating system user account in the folder "sonarqube-postgres". See improvements 
section below.

### Additional information ###

* Visit the No Fluff Just Stuff tour and see this example in action. [Static Code Analysis and Team Culture](https://www.nofluffjuststuff.com/conference/speaker/jonathan_johnson)
* [SonarQube integration](https://www.sonarsource.com/why-us/integration/)
* [SonarCloud Nemo](https://sonarcloud.io/projects?sort=-analysis_date), SonarQube continuous analysis reporting of many open source projects. 
* [This solution was inspired from this blog, "From Pet to Cattle – Running Sonar on Kubernetes"](http://container-solutions.com/pet-cattle-running-sonar-kubernetes)

### Additional improvements ###

* You can configure your local SonarQube to match our team's SonarQube configuration
* TODO: Fix persistence problem to OS file system of the OS hosting VirtualBox. Currently the database 
is persisted in the virtual machine and not on the OS hosting VirtualBox. This means analysis data 
will be lost if your virtual box machine is restarted. Once we can get the data persisted locally 
then data will be restored with any restarts.