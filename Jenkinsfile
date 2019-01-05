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
        def AWS_ECS_CLUSTER_NAME = 'EcsCluster'

 		// TODO: finish this method ;)
		def AWS_LB_DNS_NAME = loadBalancerDNSName(AWS_DEFAULT_REGION)

		def branch = env.BRANCH_NAME
		def imageName = 'user-management'
		def imageTag = ''

		stage('PREPARATION') {
			// Delete all images, which have been built before!
			sh returnStatus: true, script: 'docker rmi -f $(docker images -q)'

			// Setup Java
			env.JAVA_HOME="${tool 'Java8'}"
			env.PATH="${env.JAVA_HOME}/bin:${env.PATH}"
			sh 'java -version'

			// Checkout branch
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

			imageTag = buildImageTagFromPomFile(branch)

			// Change build name
			currentBuild.displayName = imageTag
		}

		stage('BUILD PROJECT') {
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
                withDockerRegistry(credentialsId: AWS_ECR_CREDENTIALS_ID, url: AWS_ECR_REPOSITORY_URL) {
   					sh "docker push ${AWS_ECR_REPOSITORY}/$imageName:$imageTag"
 				}

				// TODO: in order to push to Amazon ECR, we need to login to the repository!
				// use 'withDockerRegistry' step, we have constant AWS_ECR_REPOSITORY_URL and AWS_ECR_CREDENTIALS_ID

                withAWS(credentials:'aws') {
					stage('DEPLOY') {
						def taskDefinitionName = "user-management-${branch}-task-definition"
						def serviceName = "user-management-${branch}-service"

 						// TODO: finish this method ;)
						def taskRevision = registerTaskRevision(taskDefinitionName, AWS_LB_DNS_NAME, branch, AWS_ECR_REPOSITORY, AWS_DEFAULT_REGION, imageTag)

 						if (serviceExists(serviceName, AWS_DEFAULT_REGION, AWS_ECS_CLUSTER_NAME)) {
							def updateServiceCmd = "aws ecs update-service " +
									"--cluster ${AWS_ECS_CLUSTER_NAME} " +
									"--service ${serviceName} " +
									"--task-definition ${taskDefinitionName}:${taskRevision} " +
									'--desired-count 1 ' +
									"--region ${AWS_DEFAULT_REGION}"

 							println "Executing update service cmd: ${updateServiceCmd}"

 							// TODO: use Shell Sript to execute updateServiceCmd
						} else {
							def createServiceCmd = "aws ecs create-service " +
									"--cluster ${AWS_ECS_CLUSTER_NAME} " +
									"--service-name ${serviceName} " +
									"--task-definition ${taskDefinitionName}:${taskRevision} " +
									'--desired-count 1 ' +
									"--region ${AWS_DEFAULT_REGION}"

 							println "Executing create service cmd: ${createServiceCmd}"

 							// TODO: use Shell Sript to execute createServiceCmd
						}
					}

 				}
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

String loadBalancerDNSName(String region) {
	def describeLoadBalancersCmd = "aws elbv2 describe-load-balancers " +
			"--region ${region}"
	println "Executing describe load balancers cmd: ${describeLoadBalancersCmd}"

 	// TODO: use Shell Sript to execute describeLoadBalancersCmd

 	// TODO: use readJSON to parse response

 	// TODO: verify that response must contain only one Load Balancer

 	// TODO: return something likes describeLoadBalancersJson.LoadBalancers[0].DNSName
}

 String registerTaskRevision(String taskDefinitionName, String lbDNSName, String branch, String repository, String region, String imageTag) {
	sh "sed -e \"s;%TASK_DEFINITION_NAME%;${taskDefinitionName};g\" -e \"s;%AWS_ERC_REPOSITORY%;${repository};g\" " +
		"-e \"s;%LB_DNS_NAME%;${lbDNSName};g\" " +
		"-e \"s;%BRANCH%;${branch};g\" " +
		"-e \"s;%IMAGE_TAG%;${imageTag};g\" task-definition.json > \"task-definition-${imageTag}.json\""
	def taskDefinitionJson = readJSON file: "./task-definition-${imageTag}.json"
	println "Task definition: ${taskDefinitionJson}"

 	def registerTaskDefinitionCmd = "aws ecs register-task-definition " +
			"--cli-input-json file://task-definition-${imageTag}.json " +
			"--region ${region}"
	println "Executing register task definition cmd: ${registerTaskDefinitionCmd}"

 	// TODO: use Shell Sript to execute registerTaskDefinitionCmd, parse response

 	// TODO: return registerTaskDefinitionJson.taskDefinition.revision

 }

 boolean serviceExists(String serviceName, String region, String clusterName) {
	def describeServicesCmd = "aws ecs describe-services " +
			"--cluster ${clusterName} " +
			"--services ${serviceName} " +
			"--region ${region}"
	println "Executing describe services cmd: ${describeServicesCmd}"

 	def describeServicesResponse = sh returnStdout: true, script: describeServicesCmd
	println "Response of describe services cmd: ${describeServicesResponse}"

 	def describeServicesJson = readJSON text: describeServicesResponse
	def describeServicesFailures = describeServicesJson.failures
	if (describeServicesFailures.size() == 0) {
		def statusOfService = describeServicesJson.services[0].status as String
		println "Service: ${serviceName} is existed in cluster: ${clusterName} region: ${region} with status: ${statusOfService}"
		return statusOfService == 'ACTIVE'
	}

 	println "Service: ${serviceName} is not existed in cluster: ${clusterName} region: ${region}"
	return false
} 	