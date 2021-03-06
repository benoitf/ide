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
package com.codenvy.ide.theme;

import com.codenvy.ide.api.mvp.View;
import com.codenvy.ide.api.ui.theme.Theme;
import com.codenvy.ide.collections.Array;

/**
 * @author Evgen Vidolob
 */
public interface AppearanceView extends View<AppearanceView.ActionDelegate>{

    void setThemes(Array<Theme> themes, String currentThemeId);

    public interface ActionDelegate{

        void themeSelected(String themeId);
    }
}
