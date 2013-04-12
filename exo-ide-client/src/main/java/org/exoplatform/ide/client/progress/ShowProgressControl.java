/*
 * Copyright (C) 2011 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.ide.client.progress;

import org.exoplatform.gwtframework.ui.client.command.SimpleControl;
import org.exoplatform.ide.client.IDE;
import org.exoplatform.ide.client.IDEImageBundle;
import org.exoplatform.ide.client.framework.annotation.RolesAllowed;
import org.exoplatform.ide.client.framework.control.IDEControl;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id: Sep 20, 2011 evgen $
 */
@RolesAllowed({"developer"})
public class ShowProgressControl extends SimpleControl implements IDEControl {

    /**
     *
     */
    public ShowProgressControl() {
        super(IDE.IDE_LOCALIZATION_CONSTANT.progressControlId());
        setTitle(IDE.IDE_LOCALIZATION_CONSTANT.progressControlTitle());
        setPrompt(IDE.IDE_LOCALIZATION_CONSTANT.progressControlPrompt());
        setImages(IDEImageBundle.INSTANCE.progresImage(), IDEImageBundle.INSTANCE.progresImage());
        setDelimiterBefore(true);
        setEvent(new ShowProgressEvent());
    }

    /** @see org.exoplatform.ide.client.framework.control.IDEControl#initialize() */
    @Override
    public void initialize() {
        setVisible(true);
        setEnabled(true);
    }

}