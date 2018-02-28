# Static Code Analysis #

Here is a growing collection of techniques for ensuring your software development team is continuously analyzing their
code, taking heed of the feedback from the tools and shifting the quality improvements to earlier in the development 
process. It's advisable you 
[Shift left](https://martinfowler.com/articles/rise-test-impact-analysis.html#ShiftLeftAndRight)
your static code analysis to reduce inferior, embarrassing and insecure solutions delivered to your customers.

Currently, this repository addresses two techniques oriented near the Java ecosystem.

* Leveraging SonarQube on Kubernetes.
* Leveraging Gradle plugins for analysis reports during build steps and stages.


# SonarQube with Kubernetes #

Setting up your SonarQube services(s) as fragile [snowflakes](https://martinfowler.com/bliki/SnowflakeServer.html) is
both common and not a recommended technique. Any developer should be able to quickly start a service or rely on a
team service that matches the same behaviors. The SonarQube version, it's plugins and it's configurations should also
be easily adjustable. Your software development lifecycle processes (SDLC) should embrace the versioned configuration 
and deployment of SonarQube across a variety of 
[cattle (not pets)](http://cloudscaling.com/blog/cloud-computing/the-history-of-pets-vs-cattle/) targets.

Follow these instructions to setup a personal [SonarQube engine and dashboard](https://www.sonarqube.org). With 
this you have a strong static code analysis tool backing your code changes all before you submit your work for 
pull requests. Within SonarQube there are plugins such as for Checkstyle, PMD and Findbugs. The Fingbugs plugin 
includes rules for vulnerabilities such as the [OWASP top 10](http://find-sec-bugs.github.io).

The code analysis tools within your IDE offers similar rules and can find many of the same technical debts, 
violations, vulnerabilities and anti-patterns. SonarQube provides a language and IDE agnostic way of supplying 
cross team analysis rules.

With a running SonarQube the [SonarLint plugin](http://www.sonarlint.org) 
for your IDE can be connected the service's endpoint for the rules and engine. SonarLint, within the IDE, is an effect 
*shift left* technique to ensure debt is reduced ***before*** committing and costly pull requests with peers. Your 
peers will appreciate your work when you preempt and reduce chatter about low level topics in your pull requests.

### How do I get set up? ###

* Install [Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/) (or any other Kubernetes cluster)
* Install kubectl and verify `kubectl version` runs correctly
* Install [Helm](https://docs.helm.sh/using_helm/), a package manager for Kubernetes based applications
* Initialize Helm with: <p>`helm init`
* Install SonarQube using the public, stable Helm chart: <p>`helm install --set image.tag=7.0 stable/sonarqube`. 

The 7.0 image tag defined SonarQube version 7. This works over the default of v6.5. As of this writing, 
there may be a [newer container version](https://hub.docker.com/_/sonarqube/) you may want to use. The first time, it 
will take a few minutes for SonarQube and it backing Postgress datastore to start and respond. 

At the end of the Helm install there is a note section. The SonarQube service by default is exposed with a 
configuration for a cloud LoadBalancer. If you are running this Helm chart on a cloud based cluster then follow the 
note to export the SERVICE_IP. If you are running MiniKube then the exposed service is exposed in a different 
way (more on this [here](https://github.com/kubernetes/minikube/issues/384)). Create the SERVICE_IP export a 
slightly different way using this command:

Export for a Minikube cluster:

`export SERVICE_IP=$(minikube service adjective-noun-sonarqube --url)`<p>
In the above you replace the random and unique "adjective-noun-sonarqube" with the one noted at the end of the 
Helm Chart installation output instructions. If the adjective-noun concept confuses you, you can use this alternate
form:
`export SERVICE_IP="https://"$(minikube ip)":"$(kubectl get svc --selector=app=sonarqube -o jsonpath='{.items[*].spec.ports[*].nodePort}')`

Given the above the SERVICE_IP should be assigned to something like this: `http://192.168.99.100:30605`. Point your 
browser to this endpoint for the SonarQube service at<p>`echo $CLUSTER_IP`

Now SonarQube is up and running on your cluster and you should be able to get to the dashboard and 
login as administrator with admin/admin.

### How do I analyze my code? ###

In this repository there is an example project called `microservice`. Within is a Gradle build that can build a
microservice and deploy to a Docker registry. The microservice is based on Java, Springboot and MyBatis.

The Gradle build also contains the SonarQube plugin that adds a task `sonarqube`.  This will analyze the source and 
publish the results to a SonarQube service.

* Clone this repository with Git

* Using Gradle, run<p>
`gradlew -Dsonar.host.url=$(echo $SERVICE_IP) sonarqube`

Once the analysis completes, navigate back to the SonarQube dashboard and the project analysis will appear.

Independent of SonarQube, there is a Gradle task `check` will generate an extensive list of example analysis reports
in the build directory. These reports are generated from a variety of analysis plugins included within the 
`microservice/build/reports` directory.


### Technology stack ###

* Kubernetes, it's easy to try with [Minikube](https://kubernetes.io/docs/getting-started-guides/minikube/) running on VirtualBox.
* Gradle with the [Sonarqube plugin](https://plugins.gradle.org/plugin/org.sonarqube)
* [Helm](), a package manager for Kubernetes.
* A preconfigured and stable [Helm chart for provisioning SonarQube](https://github.com/kubernetes/charts/tree/master/stable/sonarqube) on Kubernetes.
* [SonarQube for Docker](https://hub.docker.com/_/sonarqube/)
* [Postgres for Docker](https://hub.docker.com/_/postgres/) as backing store for SonarQube


### Additional information ###

* Visit the No Fluff Just Stuff [tour](https://nofluffjuststuff.com) and see this example in action. [Code Analysis and Team Culture](https://www.nofluffjuststuff.com/conference/speaker/jonathan_johnson)
* [SonarQube integration](https://www.sonarsource.com/why-us/integration/)
* [SonarCloud Nemo](https://sonarcloud.io/projects?sort=-analysis_date), SonarQube continuous analysis reporting of many open source projects.
* Understand more about the stable Helm chart for using [SonarQube on Kubernetes](https://nofluffjuststuff.com)
* Intellij's plugin for SonarQube, [SonarLint plugin](https://www.sonarlint.org/intellij/howto.html)
* [Shift left](https://martinfowler.com/articles/rise-test-impact-analysis.html#ShiftLeftAndRight)
* [Snowflakes](https://martinfowler.com/bliki/SnowflakeServer.html)
* [Cattle not pets](http://cloudscaling.com/blog/cloud-computing/the-history-of-pets-vs-cattle/)
* Point your Intellij IDE at the SonarQube service using the [SonarLint plugin](https://www.sonarlint.org/intellij/howto.html).

### Additional improvements ###

* You can configure your local SonarQube to match your team's SonarQube configuration.
* Create a custom values.yaml file that customizes the setting of the SonarQube Helm chart. check this code in an use 
this *cattle* both locally and with you Continuous Integration pipelines
