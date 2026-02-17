/*******************************************************************************
 * Copyright (c) 2026 Eurotech and/or its affiliates and others
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Eurotech
 ******************************************************************************/
package org.eclipse.kura.maven.enforcer.rules;

import java.util.Optional;

public class BannedPlugin {
    private String groupId;
    private String artifactId;
    private String version;
    private String goal;

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public String getGroupId() {
        ValidationUtils.requireNonNullOrEmpty("groupId", groupId);
        return groupId;
    }

    public String getArtifactId() {
        ValidationUtils.requireNonNullOrEmpty("artifactId", artifactId);
        return artifactId;
    }

    public Optional<String> getVersion() {
        return Optional.ofNullable(version);
    }

    public Optional<String> getGoal() {
        return Optional.ofNullable(goal);
    }

    @Override
    public String toString() {
        return groupId + ":" + artifactId + (version != null ? ":" + version : "") + (goal != null ? ":" + goal : "");
    }
    
}
