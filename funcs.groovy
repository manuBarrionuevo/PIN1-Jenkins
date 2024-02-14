// funcs.groovy
def fileExists(filePath) {
  return file(filePath).exists()
}

def dockerLogin(registryUrl) {
  withCredentials([usernamePassword(credentialsId: 'dockerHub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASSWORD')]) {
    withDockerRegistry([url: registryUrl]) {
      return true
    }
  }
  return false
}

def buildDockerImage(imageName, version, directory) {
  dir(directory) {
    sh """
      docker build -t $imageName:$version .
    """
  }
}
