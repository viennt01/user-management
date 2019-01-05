#!/usr/bin/env groovy

node {

	def branch = env.BRANCH_NAME
	def imageName = 'user-management'
	def imageTag = ''

	stage('PREPARATION') {
		// TODO: clean up docker images which were built before
		// IDEA: use 'Shell Script' step to remove all docker images
		//sh returnStatus: true, script: 'docker rmi -f $(docker images -q)'

		// TODO: setup tools: Java, Maven...
		// IDEA: use 'Tool' step to get path of installed Java, then set Java path into env.PATH

		env.JAVA_HOME="${tool 'Java8'}"
		env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
		sh 'java -version'

		// TODO: checkout project, please remember to checkout only your branch!
		// IDEA: use 'Checkout' step

		// Need to set LocalBranch extension, since: https://stackoverflow.com/questions/44006070/jenkins-gitscm-finishes-the-clone-in-a-detached-head-state-how-can-i-make-sure
		checkout([$class: 'GitSCM',
			branches: [
				[
					name: "*/$branch"
				]
			],
			doGenerateSubmoduleConfigurations: false,
			extensions: [],
			extensions: [
				[$class: 'CleanBeforeCheckout'],
				[$class: 'LocalBranch', localBranch: "**"]
			],
			submoduleCfg: [],
			userRemoteConfigs: [
				[
					credentialsId: 'ci-user-ssh',
					url: 'git@github.com:tranductrinh/user-management.git'
				]
			]
		])

		// TODO: build image tag, later we will use this tag to tag docker image in this build
		// IDEA: some of global variables that might interesting!

		imageTag = buildImageTagFromPomFile(branch)

		// TODO: may be change display name of this build to display image tab

		currentBuild.displayName = imageTag
	}

	stage('BUILD PROJECT') {
		// TODO: execute maven build
		// IDEA: use 'Shell Script' step, and also see README.md - how to build project

		sh './mvnw clean install -P prod'
	}

	if (!currentBuild.result) {
		stage('BUILD IMAGE') {
			// TODO: write Dockerfile or you could use any plugin to build an image
			// IDEA: if you like Dockerfile https://spring.io/guides/gs/spring-boot-docker/

			// TODO: enable info endpoint (Spring Actuator), we will use later to verify that application is deployed successfully
			// IDEA: https://www.baeldung.com/spring-boot-actuators

			// TODO: build and tag an image
			// IDEA: use 'Shell Script' step to execute docker command
		}

	}
}

// GENERAL HELPERS

String buildImageTagFromPomFile(String branch) {
	def artifactVersion = fileExists('pom.xml') ? readMavenPom(file: 'pom.xml').version : ''
	artifactVersion = artifactVersion - '-SNAPSHOT'
	def gitRev = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()

	return "$artifactVersion-$branch-$gitRev"
}