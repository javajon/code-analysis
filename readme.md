# Static Code Analysis

Within this project is a growing collection of techniques for ensuring your software development team is continuously analyzing their code, taking heed of the feedback from the tools and shifting the quality improvements to earlier in the development process. It's advisable you [Shift left](https://martinfowler.com/articles/rise-test-impact-analysis.html#ShiftLeftAndRight) your static code analysis to reduce inferior, embarrassing and insecure solutions delivered to your customers.

In this repository there is an example project called `microservice`. This simple microservice is based on Java ,Spring Boot and MyBatis. Within Gradle builds a microservice and deploys it to a Docker registry. The Gradle build also contains an `check` and `sonarqube` task and these are described below.

Currently, this repository addresses two techniques oriented near the Java ecosystem.

1. Leveraging Gradle plugins for analysis reporting,
2. Leveraging SonarQube on Kubernetes.

## Analysis reports with Gradle plugins

A few code analysis plugins have been added to the Gradle build. To create the analysis reports run the Gradle task `check`:

``` sh
gradlew check
```

Once this task completes explore the code analysis reports in the build/reports directory. The current list of plugins can be inspected in the gradle/analysis directory.

## SonarQube with Kubernetes

Setting up your SonarQube services(s) as fragile [snowflakes](https://martinfowler.com/bliki/SnowflakeServer.html) is both common and not a recommended technique. Any developer should be able to quickly start a service or rely on a team service that matches the same behaviors. The latest SonarQube version, it's plugins and it's configurations should also be easily adjustable. Your software development lifecycle processes (SDLC) should embrace the versioned configuration and deployment of SonarQube across a variety of [cattle (not pets)](http://cloudscaling.com/blog/cloud-computing/the-history-of-pets-vs-cattle/) targets.

Follow these instructions to setup a personal [SonarQube engine and dashboard](https://www.sonarqube.org). With this you have a strong static code analysis tool backing your code changes all before you submit your work for pull requests. Within SonarQube there are plugins such as for Checkstyle, PMD and Findbugs. The Fingbugs plugin includes rules for vulnerabilities such as the [OWASP top 10](http://find-sec-bugs.github.io).

The code analysis tools within your IDE offers similar rules and can find many of the same technical debts, violations, vulnerabilities and anti-patterns. SonarQube provides a language and IDE agnostic way of supplying cross team analysis rules.

With a running SonarQube the [SonarLint plugin](http://www.sonarlint.org) for your IDE can be connected the service's endpoint for the rules and engine. SonarLint, within the IDE, is an effect *shift left* technique to ensure debt is reduced ***before*** committing and costly pull requests with peers. Your peers will appreciate your work when you preempt and reduce chatter about low level topics in your pull requests.

### Setting up SonarQube on Kubernetes

#### Kubernetes setup

1. Install [Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/) (or any other Kubernetes cluster)
1. Install [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) command line tool for Kubernetes
1. Install [Helm](https://docs.helm.sh/using_helm/), a package manager for Kubernetes based applications
1. Start Minikube: `minikube start --kubernetes-version v1.9.4 --cpus 4 --memory 8000 --disk-size 80g -p minikube-sonarqube`
1. Use profile specificed above: `minikube profile minikube-sonarqube`
1. Verify `kubectl version` runs correctly
1. Initialize Helm with: `helm init`

#### SonarQube setup

Install SonarQube using the public, stable Helm chart:

``` sh
helm install --name my-sonar --namespace sonarqube stable/sonarqube -f ./sonarqube-values.yaml
```

Inside the sonarqube-values.yaml file is a varety of settings that will superceed the defaults of the chart. The image tag specifies the SonarQube version where the tag can be found [here](https://hub.docker.com/r/library/sonarqube/tags/). As of this writing, there may be newer container tag and plugin versions. The first time, it will take a few minutes for SonarQube and its backing Postgres datastore to start and respond.

#### Sonarqube access

At the end of the Helm install there is a note section. The SonarQube service by default is exposed with a configuration for a cloud LoadBalancer. If you are running this Helm chart on a cloud based cluster then follow the note to export the SERVICE_IP. If you are running MiniKube then the exposed service is exposed in a different way (more on this [here](https://github.com/kubernetes/minikube/issues/384)). 

Access to the service:

``` sh
export NODE_PORT=$(kubectl get --namespace sonarqube -o jsonpath="{.spec.ports[0].nodePort}" services my-sonar-sonarqube)
export NODE_IP=$(kubectl get nodes --namespace sonarqube -o jsonpath="{.items[0].status.addresses[0].address}")
export SERVICE_IP=$(echo http://$NODE_IP:$NODE_PORT)
```

An equivalent, shorter form using Minikube is:

``` sh
export SERVICE_IP=$(minikube service my-sonar-sonarqube -n sonarqube --url)
```

Given the above, the SERVICE_IP should be assigned to something like this: `http://192.168.99.10x:3xxxx`. Point your browser to this endpoint to access  SonarQube at `echo $SERVICE_IP`.

Now SonarQube is up and running on your cluster and you should be able to get to the dashboard and login as administrator with admin/admin.

### Analyze with SonarQube

The Gradle build within the microservice folder also contains the SonarQube plugin that adds a Gradle task `sonarqube`. This will analyze the source and publish the results to a SonarQube service.

* Clone this repository with Git
* Using Gradle in the microservices folder run:

``` sh
gradlew -Dsonar.host.url=$(echo $SERVICE_IP) sonarqube
```

Once the analysis completes, navigate back to the SonarQube dashboard and the project analysis will appear.

### Technology stack

* VirtualBox 5.2.8
* [Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/) 0.25.2 (Kubernetes 1.9.0 + Docker) (avoid versions 0.26.0 and 0.26.1)
* Kubectl 1.9.0
* Helm 2.8.2, a package manager for Kubernetes. (avoid version 2.9.0)
* Java 1.8
* Spring Boot 2.0.1
* Gradle 4.7 and a few helpful plugins for building and deploying containers
* A collection of Gradle static code analysis plugins in gradle/analysis folder
* Gradle [Sonarqube plugin](https://plugins.gradle.org/plugin/org.sonarqube)
* Preconfigured and stable [Helm chart for provisioning SonarQube](https://github.com/kubernetes/charts/tree/master/stable/sonarqube) on Kubernetes.
* [SonarQube Docker container 6.7.3-alpine](https://hub.docker.com/_/sonarqube/)
* [Postgres for Docker](https://hub.docker.com/_/postgres/) as backing store for SonarQube

### Presentation setup

1. Use VirtualBox pre-configured image with `minikube start -p minikube-sonarqube`
1. Use profile specificed above: `minikube profile minikube-sonarqube`
1. Generate reports: `gradlew check`
1. Publish sonarqube results: 

``` sh
export SERVICE_IP=$(minikube service my-sonar-sonarqube -n sonarqube --url)
gradlew -Dsonar.host.url=$(echo $SERVICE_IP) sonarqube
```

### Additional information

* Visit the No Fluff Just Stuff [tour](https://nofluffjuststuff.com) and see this example in action. [Code Analysis and Team Culture](https://www.nofluffjuststuff.com/conference/speaker/jonathan_johnson)
* [SonarQube integration](https://www.sonarsource.com/why-us/integration/)
* [SonarCloud Nemo](https://sonarcloud.io/projects?sort=-analysis_date), SonarQube continuous analysis reporting of many open source projects.
* Understand more about the stable Helm chart for using [SonarQube on Kubernetes](https://github.com/kubernetes/charts/tree/master/stable/sonarqube)
* Intellij's plugin for SonarQube, [SonarLint plugin](https://www.sonarlint.org/intellij/howto.html)
* [Shift left](https://martinfowler.com/articles/rise-test-impact-analysis.html#ShiftLeftAndRight)
* [Snowflakes](https://martinfowler.com/bliki/SnowflakeServer.html)
* [Cattle not pets](http://cloudscaling.com/blog/cloud-computing/the-history-of-pets-vs-cattle/)
* Point your Intellij IDE at the SonarQube service using the [SonarLint plugin](https://www.sonarlint.org/intellij/howto.html).
* Careful when upgrading SonarQube tag images or its plugins. It can be dependency hell with a complex matrix. If your
container is failing it's due to mismatched versions.
* Roadmap: Adjust settings in sonarqube-values.yaml to adjust rules for team preferences
* Roadmap: Review some of the commented out analysis plugins
