pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Mostrar código de los stages') {
            steps {
                echo 'Mostrando el código de los stages principales del Jenkinsfile.'

                script {
                    if (isUnix()) {
                        sh '''
                            echo "==================== STAGE 1: SELENIUM JAVA ===================="
                            sed -n "/stage('Selenium Java - BDD Demoblaze')/,/stage('Playwright Python - BDD Demoblaze')/p" Jenkinsfile

                            echo "==================== STAGE 2: PLAYWRIGHT PYTHON ===================="
                            sed -n "/stage('Playwright Python - BDD Demoblaze')/,/post {/p" Jenkinsfile
                        '''
                    } else {
                        powershell '''
                            Write-Host "==================== INICIO JENKINSFILE ===================="
                            Get-Content .\\Jenkinsfile
                            Write-Host "==================== FIN JENKINSFILE ===================="
                        '''
                    }
                }
            }
        }

        stage('Selenium Java - BDD Demoblaze') {
            steps {
                dir('SeleniumPomLab') {
                    script {
                        if (isUnix()) {
                            sh '''
                                set -e

                                echo "Ejecutando Stage Selenium Java - BDD Demoblaze"
                                echo "Este stage ejecuta los 2 escenarios BDD de Selenium."

                                if [ -f "./gradlew" ]; then
                                    chmod +x ./gradlew || true
                                    ./gradlew testJenkins -Dheadless=true
                                else
                                    gradle testJenkins -Dheadless=true
                                fi
                            '''
                        } else {
                            powershell '''
                                Write-Host "Ejecutando Stage Selenium Java - BDD Demoblaze"
                                Write-Host "Este stage ejecuta los 2 escenarios BDD de Selenium."

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

                    script {
                        try {
                            allure([
                                includeProperties: false,
                                jdk: '',
                                results: [[path: 'SeleniumPomLab/build/allure-results']]
                            ])
                        } catch (Exception e) {
                            echo "No fue posible publicar Allure desde Jenkins. Verifica si el plugin Allure está instalado. Los resultados quedaron archivados como artefactos."
                        }
                    }
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

                                echo "Ejecutando Stage Playwright Python - BDD Demoblaze"
                                echo "Este stage ejecuta solo 1 escenario BDD de Playwright."

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
                                Write-Host "Ejecutando Stage Playwright Python - BDD Demoblaze"
                                Write-Host "Este stage ejecuta solo 1 escenario BDD de Playwright."

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

                    script {
                        try {
                            allure([
                                includeProperties: false,
                                jdk: '',
                                results: [[path: 'PlaywrightPomLab/allure-results']]
                            ])
                        } catch (Exception e) {
                            echo "No fue posible publicar Allure desde Jenkins. Verifica si el plugin Allure está instalado. Los resultados quedaron archivados como artefactos."
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline finalizado. Revisa JUnit, artefactos archivados y Allure Report en Jenkins si el plugin está instalado.'
        }
    }
}