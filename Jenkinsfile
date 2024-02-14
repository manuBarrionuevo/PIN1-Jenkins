@Library('pinVars') _

def pinVarsInstance = pinVars()

pipeline {
  agent any

  options {
    timeout(time: 2, unit: 'MINUTES')
  }

  environment {
    VERSION_PATTERN = '"version": "\\d+\\.\\d+\\.\\d+"'
    VERSION_FILE = 'package.json'
  }

  stages {
    stage('Building image') {
      steps {
        script {
          try {
            echo "Buscando el patrón ${VERSION_PATTERN} en ${VERSION_FILE}"

            // Cambios aquí: se agregan comillas dobles alrededor del patrón
            def versionLine = sh(script: "grep -E \"${VERSION_PATTERN}\" ${VERSION_FILE} | head -n 1", returnStdout: true).trim()

            if (!versionLine) {
              error 'No se encontró la versión en el package.json.'
            }

            // Cambios aquí: se agregan comillas dobles alrededor del patrón
            def version = sh(script: "echo \"${versionLine}\" | grep -oE '\\d+\\.\\d+\\.\\d+'", returnStdout: true).trim()
            echo "Versión encontrada en el package.json: ${version}"

            env.VERSION = version

            // Docker login
            if (pinVarsInstance.dockerLogin('https://registry.example.com')) {
              pinVarsInstance.buildDockerImage("${DOCKER_USER}/AppPin1", "${version}", '.')
            }
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
