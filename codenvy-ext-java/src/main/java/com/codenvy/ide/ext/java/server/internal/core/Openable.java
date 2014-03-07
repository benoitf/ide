/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2014] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package com.codenvy.ide.ext.java.server.internal.core;

import org.eclipse.jdt.internal.core.JavaElement;

/**
 * @author Evgen Vidolob
 */
public abstract class Openable extends org.eclipse.jdt.internal.core.Openable {
    protected Openable(JavaElement parent) {
        super(parent);
    }

    @Override
    public String toString() {
        return String.valueOf(hashCode());
    }
}
