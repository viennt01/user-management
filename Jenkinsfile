#!/usr/bin/env groovy
node {

	def branch = env.BRANCH_NAME
	def imageName = 'user-management'
	def imageTag = ''

	stage('PREPARATION') {
		sh returnStatus: true, script: 'docker rmi -f $(docker images -q)'
	
		// Setup Java
		env.JAVA_HOME="${tool 'Java8'}"
		env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
		sh 'java -version'
	
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
		
		imageTag = buildImageTagFromPomFile(branch)
	
		currentBuild.displayName = imageTag
	}

}

String buildImageTagFromPomFile(String branch) {
	def artifactVersion = fileExists('pom.xml') ? readMavenPom(file: 'pom.xml').version : ''
	artifactVersion = artifactVersion - '-SNAPSHOT'
	def gitRev = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()

	return "$artifactVersion-$branch-$gitRev"
}
