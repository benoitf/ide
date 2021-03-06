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
package com.codenvy.ide.api.parts;

import com.codenvy.ide.api.ui.workspace.PartPresenter;

import javax.validation.constraints.NotNull;

/**
 * Part containing Welcome Page
 *
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
public interface WelcomePart extends PartPresenter {
    /**
     * Add item on welcome page.
     *
     * @param action
     *         action what need to execute when current item is clicked
     */
    void addItem(@NotNull WelcomeItemAction action);
}