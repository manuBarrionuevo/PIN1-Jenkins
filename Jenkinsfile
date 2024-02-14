def funcs

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
    stage('Load Functions') {
      steps {
        node {
          // Agrega un bloque node para proporcionar el contexto necesario
          funcs = load 'funcs.groovy'
        }
      }
    }

    stage('Build') {
      steps {
        script {
          try {
            // Chequeo si el archivo package.json existe
            if (!funcs.fileExists(env.PACKAGE_JSON)) {
              error 'No se encontró el archivo package.json'
            }

            // Leer la versión directamente desde package.json usando jq
            def version = sh(script: "jq -r .version ${env.PACKAGE_JSON}", returnStdout: true).trim()
            echo "Versión encontrada en el package.json: ${version}"

            env.VERSION = version

            // Docker login
            node { // Inicio del bloque node
              if (funcs.dockerLogin('https://registry.example.com')) {
                funcs.buildDockerImage("${DOCKER_USER}/AppPIN1", "${version}", '.')
              }
            } // Fin del bloque node
          } catch (Exception e) {
            echo "Error en la etapa de Build: ${e.message}"
            currentBuild.result = 'FAILURE'
            error 'Hubo un error durante la etapa de Build.'
          }
        }
      }
    } // fin stage build

    // stage('Run tests') {
    //   steps {
    //     sh 'docker run testapp npm test'
    //   }
    // }
    // stage('Deploy Image') {
    //   steps {
    //     sh '''
    //     docker tag testapp 127.0.0.1:5000/mguazzardo/testapp
    //     docker push 127.0.0.1:5000/mguazzardo/testapp
    //     '''
    //   }
    // }
    // stage('Vulnerability Scan - Docker ') {
    //   steps {
    //     sh 'docker run  -v /var/run/docker.sock:/var/run/docker.sock aquasec/trivy image --severity=critical 127.0.0.1:5000/mguazzardo/testapp'
    //   }
    // }
  }
}