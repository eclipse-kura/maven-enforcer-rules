import org.jenkinsci.plugins.pipeline.modeldefinition.Utils

node {
    properties([
        disableConcurrentBuilds(abortPrevious: true),
        buildDiscarder(logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '1', daysToKeepStr: '', numToKeepStr: '3')),
        gitLabConnection('gitlab.eclipse.org'),
        [$class: 'RebuildSettings', autoRebuild: false, rebuildDisabled: false],
        [$class: 'JobLocalConfiguration', changeReasonComment: '']
    ])

    stage('Preparation') {
        dir("maven-enforcer-rules") {
            checkout scm
            sh "touch /tmp/isJenkins.txt"
        }
    }

    stage('Build') {
        timeout(time: 10, unit: 'MINUTES') {
            dir("maven-enforcer-rules") {
                withMaven(
                    jdk: pipelineParams.toolchain.jdk,
                    maven: pipelineParams.toolchain.maven,
                    options: [artifactsPublisher(disabled: true)]
                ) {
                    sh "mvn verify"
                }
            }
        }
    }

    stage('Deploy') {
        if (env.BRANCH_IS_PRIMARY) {
            timeout(time: 5, unit: 'MINUTES') {
                dir("maven-enforcer-rules") {
                    withMaven(
                        jdk: pipelineParams.toolchain.jdk,
                        maven: pipelineParams.toolchain.maven,
                        options: [artifactsPublisher(disabled: true)]
                    ) {
                        sh "mvn clean deploy -DskipTests"
                    }
                }
            }
        } else {
            Utils.markStageSkippedForConditional(STAGE_NAME)
        }
    }

}