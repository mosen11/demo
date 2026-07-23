pipeline {
    agent any

    environment {
        // Replace with your Docker Hub username
        DOCKER_HUB_USER = 'mosen111'
        IMAGE_NAME = 'demo'
        IMAGE_TAG = 'v4'
        // Replace with the ID of the credentials you created in Jenkins for Docker Hub
        DOCKER_HUB_CREDS = 'docker-hub-credentials'
    }

    stages {
        stage('Build Application') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }

        stage('Create Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_HUB_USER}/${IMAGE_NAME}:${IMAGE_TAG}")
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('', DOCKER_HUB_CREDS) {
                        dockerImage.push()
                        dockerImage.push('latest')
                    }
                }
            }
        }
    }

    post {
        always {
            sh "docker rmi ${DOCKER_HUB_USER}/${IMAGE_NAME}:${IMAGE_TAG} || true"
            sh "docker rmi ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest || true"
        }
    }
}
