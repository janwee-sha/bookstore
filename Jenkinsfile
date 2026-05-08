pipeline {
    agent any

    triggers {
        pollSCM('H/5 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Gradle Test') {
            steps {
                sh './gradlew clean test'
            }
        }

        stage('Build Docker Images') {
            steps {
                sh 'docker compose build --pull'
            }
        }

        stage('Deploy') {
            steps {
                sh '''
                    docker compose down --remove-orphans
                    docker compose up -d
                '''
            }
        }
    }

    post {
        always {
            sh 'docker image prune -f || true'
        }

        success {
            echo 'Bookstore deployed successfully: http://<your_server_ip>:8080'
        }

        failure {
            echo 'Pipeline failed. Please check the Jenkins console output.'
        }
    }
}
