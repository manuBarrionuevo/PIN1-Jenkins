@Library('pinVars') _ //se invoca libreria

def pinVarsInstance = pinVars() //se crea instancia para poder utilizar las funciones

pipeline {
  agent any

  options {
    timeout(time: 2, unit: 'MINUTES')
  }

  environment {
    VERSION_FILE = 'package.json'
    DOCKER_USER = credentials('DockerHub').username
  }

  stages {
    stage('Building image') {
      steps {
        script {
          try {
            echo "Extrayendo la versión de ${VERSION_FILE}"

            // utilizamos jq para extraer la versión
            def version = sh(script: "jq -r '.version' ${VERSION_FILE}", returnStdout: true).trim()

            if (!version) {
              error 'No se encontró la versión en el package.json.'
            }

            echo "Versión encontrada en el package.json: ${version}"

            env.VERSION = version

            echo 'Intentando login en Docker Hub'
            // Docker login
            if (!pinVarsInstance.dockerLogin('https://registry.example.com', credentialsId: 'DockerHub')) {
              error 'Error en el login de Docker Hub'
            }
            pinVarsInstance.buildDockerImage("${DOCKER_USER}/AppPin1", "${version}")
          } catch (Exception e) {
            echo "Error en la etapa de Build: ${e.message}"
            currentBuild.result = 'FAILURE'
            error 'Hubo un error durante la etapa de Build.'
          }
        }
      }
    }

    // stage('Run tests') {
    //   steps {
    //     sh "docker run testapp npm test"
    //   }
    // }

  // stage('Deploy Image') {
  //   steps {
  //     sh '''
  //       docker tag testapp 127.0.0.1:5000/mguazzardo/testapp
  //       docker push 127.0.0.1:5000/mguazzardo/testapp
  //     '''
  //   }
  // }
  }
}
