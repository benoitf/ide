/*
 * CODENVY CONFIDENTIAL
 * __________________
 * 
 * [2012] - [$today.year] Codenvy, S.A. 
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
package com.codenvy.ide.api.ui;

import com.google.gwt.user.client.ui.Image;

import java.util.Map;

/**
 * Provide  possibility to define images for UI in IDE extensions.
 *
 * @author Vitaly Parfonov
 */
public interface IconRegistry {

    /**
     * @param iconId
     * @return Image GWT widget
     */
    Image getIcon(String iconId);

    /**
     * @return default icon, can be useful if don't find icon by id
     */
    Image getDefaultIcon();

    /**
     * @param iconRegistry
     */
    void registerIcons(Map<String, String> iconRegistry);

    /**
     * @param iconId some id
     * @param iconPath path to the image
     */
    void registerIcon(String iconId, String iconPath);
}
