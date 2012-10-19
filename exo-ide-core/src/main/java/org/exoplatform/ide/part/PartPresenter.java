/*
 * Copyright (C) 2003-2012 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ide.part;

import org.exoplatform.ide.presenter.Presenter;

/**
 * Part is a main UI block of the IDE. 
 *
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a> 
 */
public interface PartPresenter extends Presenter
{
   /**
    * The property id for <code>getTitle</code>
    */
   public static final int PROP_TITLE = 0x001;

   /**
    * Title of the Part
    * 
    * @return
    */
   public String getTitle();

   /**
    * This method is called when Part is opened. 
    * Note: this method is NOT called when part gets focused. It is called when new tab in
    * PartStack created. 
    */
   public void onOpen();

   /**
    * This method is called when part is going to be closed. Part itself can deny
    * blocking, by returning false, i.e. when document is being edited and accidently 
    * close button pressed.
    * 
    * @return allow close
    */
   public boolean onClose();
   
   /**
    * Adds a listener for changes to properties of this part.
    * Has no effect if an identical listener is already registered.
    *
    * @param listener a property listener
    */
   public void addPropertyListener(PropertyListener listener);
   

   /**
    * Removes the given property listener from this part.
    * Has no effect if an identical listener is not registered.
    *
    * @param listener a property listener
    */
   public void removePropertyListener(PropertyListener listener);
}
