pipeline {
  agent any

  options {
    timeout(time: 10, unit: 'MINUTES')
  }

  environment {
    VERSION_PATTERN = 'version: "[0-9]*\\.[0-9]*\\.[0-9]*"'
    PACKAGE_JSON = 'package.json'
  }
  stages {
        stage('Build') {
      steps {
        script {
          try {
             // Chequeo si el archivo package.json existe
            if (!fileExists(PACKAGE_JSON)) {
              error 'No se encontró el archivo package.json'
            }

            // Leer la versión directamente desde package.json usando jq
            def version = sh(script: "jq -r .version ${PACKAGE_JSON}", returnStdout: true).trim()
            echo "Versión encontrada en el package.json: ${version}"

            env.VERSION = version

            // Docker login
            if (dockerLogin('https://registry.example.com')) {
              buildDockerImage("${DOCKER_USER}/AppPIN1", "${version}", '.')
            }
                    } catch (Exception e) {
            echo "Error en la etapa de Build: ${e.message}"
            currentBuild.result = 'FAILURE'
            error 'Hubo un error durante la etapa de Build.'
          }
        }
      }
        } // fin stage build

  //     stage('Run tests') {
  //       steps {
  //         sh 'docker run testapp npm test'
  //       }
  //     }
  //     stage('Deploy Image') {
  //       steps {
  //         sh '''
  //         docker tag testapp 127.0.0.1:5000/mguazzardo/testapp
  //         docker push 127.0.0.1:5000/mguazzardo/testapp
  //         '''
  //       }
  //     }
  //     stage('Vulnerability Scan - Docker ') {
  //         steps {
  //           sh 'docker run  -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --severity=critical 127.0.0.1:5000/mguazzardo/testapp'
  //         }
  //     }
  }
}

dockerLogin = { registryUrl ->
  withCredentials([usernamePassword(credentialsId: 'dockerHub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
    withDockerRegistry([url: registryUrl]) {
      return true
    }
  }
  return false
}

buildDockerImage = { imageName, version, directory ->
  dir(directory) {
    sh """
                docker build -t $imageName:$version .
            """
  }
}

def fileExists(filePath) {
    return file(filePath).exists()
}