@Library('pinVars') _package.json

def pinVarsInstance = pinVars()

pipeline {
  agent any

  options {
    timeout(time: 2, unit: 'MINUTES')
  }

  environment {
    VERSION_PATTERN = 'version:\\[[0-9]*\\.[0-9]*\\.[0-9]*\\]'
    VERSION_FILE = 'package.json'
  }

  stages {
    stage('Building image') {
      steps {
        script {
          try {
             // Chequeo si la versión existe en package.json
            def versionLine = sh(script: "grep -E \"${VERSION_PATTERN}\" \"${VERSION_FILE}\" | head -n 1", returnStdout: true).trim()

            if (!versionLine) {
              error 'No se encontró la versión en el package.json.'
            }

            // Definir versión
            def version = sh(script: "echo \"${versionLine}\" | grep -oE \"[0-9]*\\.[0-9]*\\.[0-9]*\"", returnStdout: true).trim()
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
