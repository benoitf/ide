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
package com.codenvy.ide.wizard;

import com.codenvy.ide.annotations.NotNull;
import com.codenvy.ide.annotations.Nullable;
import com.codenvy.ide.api.mvp.View;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Image;


/**
 * Interface of Wizard view.
 *
 * @author <a href="mailto:aplotnikov@exoplatform.com">Andrey Plotnikov</a>
 */
public interface WizardView extends View<WizardView.ActionDelegate> {
    /** Needs for delegate some function into Wizard view. */
    public interface ActionDelegate {
        /** Performs any actions appropriate in response to the user having pressed the Next button */
        void onNextClicked();

        /** Performs any actions appropriate in response to the user having pressed the Back button */
        void onBackClicked();

        /** Performs any actions appropriate in response to the user having pressed the Finish button */
        void onFinishClicked();

        /** Performs any actions appropriate in response to the user having pressed the Cancel button */
        void onCancelClicked();
    }

    /**
     * Sets whether Next button is visible.
     *
     * @param isVisible
     *         <code>true</code> to visible the button, <code>false</code>
     *         to disable it
     */
    void setNextButtonVisible(boolean isVisible);

    /**
     * Sets whether Next button is enabled.
     *
     * @param isEnabled
     *         <code>true</code> to enable the button, <code>false</code>
     *         to disable it
     */
    void setNextButtonEnabled(boolean isEnabled);

    /**
     * Sets whether Back button is visible.
     *
     * @param isVisible
     *         <code>true</code> to visible the button, <code>false</code>
     *         to disable it
     */
    void setBackButtonVisible(boolean isVisible);

    /**
     * Sets whether Finish button is enabled.
     *
     * @param isEnabled
     *         <code>true</code> to enable the button, <code>false</code>
     *         to disable it
     */
    void setFinishButtonEnabled(boolean isEnabled);

    /**
     * Sets new caption of wizard's page
     *
     * @param caption
     */
    void setCaption(@NotNull String caption);

    /**
     * Sets new notice of wizard's page
     *
     * @param notice
     */
    void setNotice(@Nullable String notice);

    /**
     * Sets new image of wizard's page
     *
     * @param image
     */
    void setImage(@Nullable Image image);

    /** Close wizard */
    void close();

    /** Show wizard */
    void showWizard();

    /**
     * Sets whether animation for change page is enabled.
     *
     * @param isEnabled
     *         <code>true</code> to enable the animation, <code>false</code>
     *         to disable it
     */
    void setChangePageAnimationEnabled(boolean isEnabled);

    /**
     * Returns place of main form where will be shown current wizard page.
     *
     * @return place of main form
     */
    AcceptsOneWidget getContentPanel();
}