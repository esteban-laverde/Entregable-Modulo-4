pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Selenium Java - BDD Demoblaze') {
            steps {
                dir('SeleniumPomLab') {
                    script {
                        if (isUnix()) {
                            sh '''
                                set -e
                                if [ -f "./gradlew" ]; then
                                    chmod +x ./gradlew || true
                                    ./gradlew testJenkins -Dheadless=true
                                else
                                    gradle testJenkins -Dheadless=true
                                fi
                            '''
                        } else {
                            powershell '''
                                if (Test-Path ".\\gradlew.bat") {
                                    .\\gradlew.bat testJenkins -Dheadless=true
                                } else {
                                    gradle testJenkins -Dheadless=true
                                }
                            '''
                        }
                    }
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'SeleniumPomLab/build/test-results/test/*.xml'

                    archiveArtifacts allowEmptyArchive: true, artifacts: '''
                        SeleniumPomLab/build/reports/**,
                        SeleniumPomLab/build/test-results/**,
                        SeleniumPomLab/build/allure-results/**,
                        SeleniumPomLab/build/evidences/screenshots/**
                    '''

                    allure([
                        includeProperties: false,
                        jdk: '',
                        results: [[path: 'SeleniumPomLab/build/allure-results']]
                    ])
                }
            }
        }

        stage('Playwright Python - BDD Demoblaze') {
            steps {
                dir('PlaywrightPomLab') {
                    script {
                        if (isUnix()) {
                            sh '''
                                set -e
                                python3 -m venv .venv
                                . .venv/bin/activate
                                python -m pip install --upgrade pip

                                # Ejecuta solo 1 de los 2 escenarios BDD de Playwright.
                                # @carrito = agrega Samsung galaxy s6 + Iphone 6 32gb y valida carrito.
                                # Cambiar a "-m compra" si se requiere ejecutar solo el escenario de compra.
                                export PYTEST_ADDOPTS="-m carrito"

                                if [ -f "./gradlew" ]; then
                                    chmod +x ./gradlew || true
                                    ./gradlew testJenkins
                                else
                                    gradle testJenkins
                                fi
                            '''
                        } else {
                            powershell '''
                                py -3.12 -m venv .venv
                                .\\.venv\\Scripts\\Activate.ps1
                                python -m pip install --upgrade pip

                                # Ejecuta solo 1 de los 2 escenarios BDD de Playwright.
                                # @carrito = agrega Samsung galaxy s6 + Iphone 6 32gb y valida carrito.
                                # Cambiar a "-m compra" si se requiere ejecutar solo el escenario de compra.
                                $env:PYTEST_ADDOPTS = "-m carrito"

                                if (Test-Path ".\\gradlew.bat") {
                                    .\\gradlew.bat testJenkins
                                } else {
                                    gradle testJenkins
                                }
                            '''
                        }
                    }
                }
            }
            post {
                always {
                    junit allowEmptyResults: true, testResults: 'PlaywrightPomLab/reports/junit/*.xml'

                    archiveArtifacts allowEmptyArchive: true, artifacts: '''
                        PlaywrightPomLab/reports/**,
                        PlaywrightPomLab/allure-results/**
                    '''

                    allure([
                        includeProperties: false,
                        jdk: '',
                        results: [[path: 'PlaywrightPomLab/allure-results']]
                    ])
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizado. Revisa JUnit, artefactos archivados y Allure Report en Jenkins.'
        }
    }
}
