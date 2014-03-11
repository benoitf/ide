/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
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
package com.codenvy.ide.ext.java.client.projectmodel;

import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.StringSet;
import com.codenvy.ide.ext.java.shared.Constants;
import com.codenvy.ide.resources.model.ProjectDescription;

/** @author Nikolay Zamosenchuk */
public class JavaProjectDescription extends ProjectDescription {

    /** @param project */
    public JavaProjectDescription(JavaProject project) {
        super(project);
    }

    /** @return The set of Project's source folders or empty set. */
    public StringSet getSourceFolders() {
        final String builderName = project.getAttributeValue(Constants.BUILDER_NAME);
        if (builderName != null) {
            final String sourceFolders = Constants.BUILDER_SOURCE_FOLDERS.replace("${builder}", builderName);
            return asStringSet(sourceFolders);
        }
        return Collections.createStringSet();
    }
}
