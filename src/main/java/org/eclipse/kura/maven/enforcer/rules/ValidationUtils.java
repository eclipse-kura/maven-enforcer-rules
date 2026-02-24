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

import java.util.List;

public class ValidationUtils {
    public static void requireNonNullOrEmpty(String paramName, String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("Parameter '" + paramName + "' cannot be null or empty");
        }
    }

    public static void requireNonNullOrEmpty(String listName, List<?> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List '" + listName + "' cannot be null or empty");
        }
    }
}
