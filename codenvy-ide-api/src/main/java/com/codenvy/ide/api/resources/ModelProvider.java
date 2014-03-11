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
package com.codenvy.ide.api.resources;

import com.codenvy.ide.resources.model.Project;

/**
 * Model provider, is an entity responsible for creating a nature-specific project model.
 * Class itself creates an empty project of desired type.
 *
 * @author Nikolay Zamosenchuk
 */
public interface ModelProvider {
    /**
     * Creates empty project instance of corresponding class
     *
     * @return Project instance
     */
    public Project createProjectInstance();
}
