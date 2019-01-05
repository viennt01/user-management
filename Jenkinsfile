#!/usr/bin/env groovy

node {

	withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
		accessKeyVariable: 'AWS_ACCESS_KEY_ID',
		credentialsId: 'aws',
		secretKeyVariable: 'AWS_SECRET_ACCESS_KEY']]) {

		// CONSTANTS
		def AWS_ACCOUNT_ID = accountId()
		def AWS_DEFAULT_REGION = 'ap-southeast-1'
		def AWS_ECR_REPOSITORY = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com"
		def AWS_ECR_REPOSITORY_URL = "https://${AWS_ECR_REPOSITORY}"
		def AWS_ECR_CREDENTIALS_ID = "ecr:${AWS_DEFAULT_REGION}:aws"

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
				sh "docker build -t $imageName:$imageTag ./docker"
				sh "docker tag $imageName:$imageTag ${AWS_ECR_REPOSITORY}/$imageName:$imageTag"
			}

			stage('PUSH IMAGE') {
				// TODO: push image to Amazon ECR
				// IDEA: use 'Shell Script' step to execute docker command

				// TODO: in order to push to Amazon ECR, we need to login to the repository!
				// use 'withDockerRegistry' step, we have constant AWS_ECR_REPOSITORY_URL and AWS_ECR_CREDENTIALS_ID
			}
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

// AMAZON HELPERS
String accountId() {
	def getCallerIdentityCmd = "aws sts get-caller-identity"
	println "Executing get caller identity cmd: ${getCallerIdentityCmd}"

	def getCallerIdentityResponse = sh returnStdout: true, script: getCallerIdentityCmd
	println "Response of get caller identity cmd: ${getCallerIdentityResponse}"

	def getCallerIdentityJson = readJSON text: getCallerIdentityResponse

	return getCallerIdentityJson.Account
}