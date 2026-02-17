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

import javax.inject.Inject;
import javax.inject.Named;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.maven.enforcer.rule.api.AbstractEnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.PluginExecution;

@Named("bannedPluginsAdvanced")
public class BannedPluginsAdvanced extends AbstractEnforcerRule {

    private String message;
    private List<BannedPlugin> bannedPlugins;

    // Inject needed Maven components
    private final MavenSession session;

    @Inject
    public BannedPluginsAdvanced(MavenSession session) {
        this.session = Objects.requireNonNull(session);
    }

    public void execute() throws EnforcerRuleException {
        ValidationUtils.requireNonNullOrEmpty("bannedPlugins", bannedPlugins);

        for (Plugin plugin : session.getCurrentProject().getBuildPlugins()) {
            if (isPluginBanned(plugin)) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append(plugin.toString()).append(" is banned.");

                if (this.message != null && !this.message.isEmpty()) {
                    errorMessage.append(" - ").append(this.message);
                }
                throw new EnforcerRuleException(errorMessage.toString());
            }
        }
    }

    private boolean isPluginBanned(Plugin projectPlugin) {
        for (BannedPlugin bannedPlugin : bannedPlugins) {
            boolean isSameGroupId = isSameGroupId(projectPlugin, bannedPlugin.getGroupId());
            boolean isSameArtifactId = isSameArtifactId(projectPlugin, bannedPlugin.getArtifactId());
            boolean isSameVersion = isSameVersion(projectPlugin, bannedPlugin.getVersion());
            boolean isSameGoal = isSameGoal(projectPlugin, bannedPlugin.getGoal());

            if (isSameGroupId && isSameArtifactId && isSameVersion && isSameGoal) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSameGroupId(Plugin plugin, String groupId) {
        return plugin.getGroupId().equals(groupId);
    }

    private static boolean isSameArtifactId(Plugin plugin, String artifactId) {
        return plugin.getArtifactId().equals(artifactId);
    }

    private static boolean isSameVersion(Plugin plugin, Optional<String> version) {
        return version.isPresent() ? plugin.getVersion().equals(version.get()) : true;
    }

    private static boolean isSameGoal(Plugin plugin, Optional<String> goal) {
        if (!goal.isPresent()) {
            return true;
        }

        for (PluginExecution execution : plugin.getExecutions()) {
            if (execution.getGoals().stream().anyMatch(executionGoal -> executionGoal.equals(goal.get()))) {
                return true;
            }
        }

        return false;
    }

    /**
     * A good practice is provided toString method for Enforcer Rule.
     * <p>
     * Output is used in verbose Maven logs, can help during investigate problems.
     *
     * @return rule description
     */
    @Override
    public String toString() {
        return String.format("BannedPluginsAdvanced[%s, %s]", message, bannedPlugins);
    }
}