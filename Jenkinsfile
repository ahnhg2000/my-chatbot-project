pipeline {
    agent any

    triggers {
        // 2분마다 깃허브 저장소를 감시하여 새로운 커밋이 있으면 자동으로 빌드를 실행합니다.
        pollSCM('*/2 * * * *')
    }

    environment {
        PROJECT_NAME = 'spring-chatbot'
    }

    stages {
        stage('Clean Up') {
            steps {
                echo ">>> [STEP 1] 기존 서비스 및 네트워크 정리"
                sh "docker-compose -p ${PROJECT_NAME} down --remove-orphans || true"

                echo ">>> [STEP 2] 미사용 이미지 정리 (용량 최적화)"
                sh "docker image prune -f"
            }
        }

        stage('Inject Environment File') {
            steps {
                echo ">>> [STEP 2.5] 암호화된 자격 증명 파일로부터 .env 생성"
                withCredentials([file(credentialsId: 'chatbot-env-file', variable: 'SECRET_ENV')]) {
                    sh "cp \$SECRET_ENV .env"
                }
            }
        }

        stage('Deploy') {
            steps {
                echo ">>> [STEP 3] 최신 소스 기반 빌드 및 컨테이너 실행"
                sh "docker-compose -p ${PROJECT_NAME} up --build -d"
            }
        }

        stage('Verify') {
            steps {
                echo ">>> [STEP 4] 배포 결과 확인"
                sh "docker ps | grep ${PROJECT_NAME}"
            }
        }
    }

    // 빌드 결과에 따른 자동 사후 처리 (슬랙 알림 활성화)
    post {
        always {
            echo ">>> 모든 배포 절차가 완료되었습니다!"
        }
        success {
            slackSend (
                channel: '#deploy-alerts',
                color: '#00FF00',
                message: "SUCCESS: '${env.JOB_NAME} [${env.BUILD_NUMBER}]' 배포가 성공적으로 완료되었습니다. (${env.BUILD_URL})"
            )
        }
        failure {
            slackSend (
                channel: '#deploy-alerts',
                color: '#FF0000',
                message: "FAILURE: '${env.JOB_NAME} [${env.BUILD_NUMBER}]' 배포 중 오류가 발생했습니다. 로그를 확인해 주세요. (${env.BUILD_URL})"
            )
        }
    }
}