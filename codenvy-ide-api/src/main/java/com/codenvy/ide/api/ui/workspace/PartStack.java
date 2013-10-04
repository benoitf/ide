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
package com.codenvy.ide.api.ui.workspace;

import com.codenvy.ide.api.mvp.Presenter;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

/**
 * Part Stack is tabbed layout element, containing Parts.
 *
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
public interface PartStack extends Presenter {

    /** {@inheritDoc} */
    @Override
    public void go(AcceptsOneWidget container);

    /**
     * Change the focused state of the PartStack to desired value
     *
     * @param focused
     */
    public void setFocus(boolean focused);

    /**
     * Add part to the PartStack. To immediately show part, you mast call <code>setActivePart()</code>.
     *
     * @param part
     */
    public void addPart(PartPresenter part);

    /**
     * Ask if PartStack contains given Part.
     *
     * @param part
     * @return
     */
    public boolean containsPart(PartPresenter part);

    /**
     * Number of parts in the PartStack
     *
     * @return
     */
    public int getNumberOfParts();

    /**
     * Get active Part. Active is the part that is currently displayed on the screen
     *
     * @return
     */
    public PartPresenter getActivePart();

    /**
     * Activate given part (force show it on the screen). If part wasn't previously added
     * to the PartStack or has been removed, that method has no effect.
     *
     * @param part
     */
    public void setActivePart(PartPresenter part);

    /**
     * Hide given part (remove from the screen). If part not active part that method has no effect.
     *
     * @param part
     */
    public void hidePart(PartPresenter part);

    /**
     * Remove given part from PartStack.
     *
     * @param part
     */
    public void removePart(PartPresenter part);

}