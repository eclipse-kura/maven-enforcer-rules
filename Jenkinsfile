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
        timeout(time: 30, unit: 'MINUTES') {
            dir("maven-enforcer-rules") {
                withMaven(
                    jdk: 'temurin-jdk17-latest',
                    maven: 'apache-maven-3.9.11',
                    options: [artifactsPublisher(disabled: true)]
                ) {
                    sh "mvn verify"
                }
            }
        }
    }

    stage('Deploy') {
        if (env.BRANCH_IS_PRIMARY) {
            timeout(time: 15, unit: 'MINUTES') {
                dir("maven-enforcer-rules") {
                    withMaven(
                        jdk: 'temurin-jdk17-latest',
                        maven: 'apache-maven-3.9.11',
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