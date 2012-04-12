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

package org.exoplatform.ide.client.project.resource;

import com.google.gwt.uibinder.client.UiField;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

import org.exoplatform.gwtframework.ui.client.component.ImageButton;
import org.exoplatform.ide.client.IDEImageBundle;
import org.exoplatform.ide.client.framework.ui.impl.ViewImpl;

/**
 * 
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class OpenResourceView extends ViewImpl implements org.exoplatform.ide.client.project.resource.OpenResourcePresenter.Display
{

   private static OpenResourceViewUiBinder uiBinder = GWT.create(OpenResourceViewUiBinder.class);

   interface OpenResourceViewUiBinder extends UiBinder<Widget, OpenResourceView>
   {
   }
   
   /**
    * Initial width of this view
    */
   private static final int WIDTH = 550;

   /**
    * Initial height of this view
    */
   private static final int HEIGHT = 320;   
   
   public static final String ID = "ideOpenResourceView";
   
   public static final String TITLE = "Open Resource";
   
   @UiField
   ImageButton openButton;
   
   @UiField
   ImageButton cancelButton;

   public OpenResourceView()
   {
      super(ID, "modal", TITLE, new Image(IDEImageBundle.INSTANCE.openResource()), WIDTH, HEIGHT);
      setCloseOnEscape(true);
      add(uiBinder.createAndBindUi(this));
   }

}
