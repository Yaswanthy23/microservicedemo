pipeline {

    agent any

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                sh '''
                    mvn clean package \
                      -pl auth-service,gateway-service,user-service,admin-service,employee-service,customer-service,hr-service,task-service \
                      -am \
                      -DskipTests
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                   sudo  docker build -t auth-service:qa-${BUILD_NUMBER} ./auth-service
                    sudo docker build -t gateway-service:qa-${BUILD_NUMBER} ./gateway-service
                    sudo docker build -t user-service:qa-${BUILD_NUMBER} ./user-service
                    sudo docker build -t admin-service:qa-${BUILD_NUMBER} ./admin-service
                    sudo docker build -t employee-service:qa-${BUILD_NUMBER} ./employee-service
                    sudo docker build -t customer-service:qa-${BUILD_NUMBER} ./customer-service
                    sudo docker build -t hr-service:qa-${BUILD_NUMBER} ./hr-service
                    sudo docker build -t task-service:qa-${BUILD_NUMBER} ./task-service
                '''
            }
        }

        stage('Trivy Scan') {
            steps {
                sh '''
                   trivy image --exit-code 1 --severity HIGH,CRITICAL auth-service:qa-${BUILD_NUMBER}
                    trivy image --exit-code 1 --severity HIGH,CRITICAL gateway-service:qa-${BUILD_NUMBER}
                    trivy image --exit-code 1 --severity HIGH,CRITICAL user-service:qa-${BUILD_NUMBER}
                    trivy image --exit-code 1 --severity HIGH,CRITICAL admin-service:qa-${BUILD_NUMBER}
                    trivy image --exit-code 1 --severity HIGH,CRITICAL employee-service:qa-${BUILD_NUMBER}
                    trivy image --exit-code 1 --severity HIGH,CRITICAL customer-service:qa-${BUILD_NUMBER}
                    trivy image --exit-code 1 --severity HIGH,CRITICAL hr-service:qa-${BUILD_NUMBER}
                    trivy image --exit-code 1 --severity HIGH,CRITICAL task-service:qa-${BUILD_NUMBER}
                '''
            }
        }

        stage('ECR Login') {
            steps {
                sh '''
                    aws ecr get-login-password --region "${AWS_REGION}" | \
                    docker login \
                      --username AWS \
                      --password-stdin \
                      "$(aws sts get-caller-identity --query Account --output text).dkr.ecr.${AWS_REGION}.amazonaws.com"
                '''
            }
        }

        stage('Push Images') {
            steps {
                sh '''
                    ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
                    ECR_REGISTRY="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

                    docker tag auth-service:qa-${BUILD_NUMBER} \
                        ${ECR_REGISTRY}/auth-service:qa-${BUILD_NUMBER}

                    docker tag gateway-service:qa-${BUILD_NUMBER} \
                        ${ECR_REGISTRY}/gateway-service:qa-${BUILD_NUMBER}

                    docker tag user-service:qa-${BUILD_NUMBER} \
                        ${ECR_REGISTRY}/user-service:qa-${BUILD_NUMBER}

                    docker tag admin-service:qa-${BUILD_NUMBER} \
                        ${ECR_REGISTRY}/admin-service:qa-${BUILD_NUMBER}

                    docker tag employee-service:qa-${BUILD_NUMBER} \
                        ${ECR_REGISTRY}/employee-service:qa-${BUILD_NUMBER}

                    docker tag customer-service:qa-${BUILD_NUMBER} \
                        ${ECR_REGISTRY}/customer-service:qa-${BUILD_NUMBER}

                    docker tag hr-service:qa-${BUILD_NUMBER} \
                        ${ECR_REGISTRY}/hr-service:qa-${BUILD_NUMBER}

                    docker tag task-service:qa-${BUILD_NUMBER} \
                        ${ECR_REGISTRY}/task-service:qa-${BUILD_NUMBER}

                    docker push ${ECR_REGISTRY}/auth-service:qa-${BUILD_NUMBER}
                    docker push ${ECR_REGISTRY}/gateway-service:qa-${BUILD_NUMBER}
                    docker push ${ECR_REGISTRY}/user-service:qa-${BUILD_NUMBER}
                    docker push ${ECR_REGISTRY}/admin-service:qa-${BUILD_NUMBER}
                    docker push ${ECR_REGISTRY}/employee-service:qa-${BUILD_NUMBER}
                    docker push ${ECR_REGISTRY}/customer-service:qa-${BUILD_NUMBER}
                    docker push ${ECR_REGISTRY}/hr-service:qa-${BUILD_NUMBER}
                    docker push ${ECR_REGISTRY}/task-service:qa-${BUILD_NUMBER}
                '''
            }
        }

        stage('Configure EKS') {
            steps {
                sh '''
                    aws eks update-kubeconfig \
                      --region "${AWS_REGION}" \
                      --name "${EKS_CLUSTER_NAME}"

                    kubectl get nodes
                '''
            }
        }

        stage('Helm Deploy') {
            steps {
                sh '''
                    ACCOUNT_ID=$(aws sts get-caller-identity --query Account --output text)
                    ECR_REGISTRY="${ACCOUNT_ID}.dkr.ecr.${AWS_REGION}.amazonaws.com"

                    helm upgrade --install crm ./helm/crm \
                      --set global.imageRegistry="${ECR_REGISTRY}" \
                      --set services.auth.tag="qa-${BUILD_NUMBER}" \
                      --set services.gateway.tag="qa-${BUILD_NUMBER}" \
                      --set services.user.tag="qa-${BUILD_NUMBER}" \
                      --set services.admin.tag="qa-${BUILD_NUMBER}" \
                      --set services.employee.tag="qa-${BUILD_NUMBER}" \
                      --set services.customer.tag="qa-${BUILD_NUMBER}" \
                      --set services.hr.tag="qa-${BUILD_NUMBER}" \
                      --set services.task.tag="qa-${BUILD_NUMBER}"
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                sh '''
                    kubectl rollout status deployment/auth-service \
                      -n auth \
                      --timeout=180s

                    kubectl rollout status deployment/gateway-service \
                      -n gateway \
                      --timeout=180s

                    kubectl rollout status deployment/user-service \
                      -n user \
                      --timeout=180s

                    kubectl rollout status deployment/admin-service \
                      -n admin \
                      --timeout=180s

                    kubectl rollout status deployment/employee-service \
                      -n employee \
                      --timeout=180s

                    kubectl rollout status deployment/customer-service \
                      -n customer \
                      --timeout=180s

                    kubectl rollout status deployment/hr-service \
                      -n hr \
                      --timeout=180s

                    kubectl rollout status deployment/task-service \
                      -n task \
                      --timeout=180s
                '''
            }
        }
    }

    post {
        always {
            sh '''
                echo "Jenkins Build: ${BUILD_NUMBER}"
                echo "Image Tag: qa-${BUILD_NUMBER}"
            '''
        }
    }
}
