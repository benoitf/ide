/*
 * Copyright (C) 2010 eXo Platform SAS.
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
package org.exoplatform.ide.client.framework.editor.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
 *
 */
public class EditorGoToPositionEvent extends GwtEvent<EditorGoToPositionHandler>
{

   public static final GwtEvent.Type<EditorGoToPositionHandler> TYPE = new GwtEvent.Type<EditorGoToPositionHandler>();

   private int lineNumber;
   
   private int columnNumber;
   
   private String fileHref;
   
   public EditorGoToPositionEvent(String fileHref, int lineNumber, int columnNumber)
   {
      this.fileHref = fileHref;
      this.lineNumber = lineNumber;
      this.columnNumber = columnNumber;
   }
   
   /**
    * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
    */
   @Override
   protected void dispatch(EditorGoToPositionHandler handler)
   {
      handler.onEditorGoToPosition(this);
   }

   /**
    * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
    */
   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<EditorGoToPositionHandler> getAssociatedType()
   {
      return TYPE;
   }

   /**
    * Get the number of line, where to go.
    * 
    * @return lineNumber
    */
   public int getLineNumber()
   {
      return lineNumber;
   }
   
   /**
    * Get the number of column, where to go.
    * 
    * @return columnNumber
    */
   public int getColumnNumber()
   {
      return columnNumber;
   }
   
   /**
    * Get href of file, on which to go.
    * 
    * @return String
    */
   public String getFileHref()
   {
      return fileHref;
   }

}
