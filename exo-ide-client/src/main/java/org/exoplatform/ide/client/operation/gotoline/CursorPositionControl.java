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
package org.exoplatform.ide.client.operation.gotoline;

import com.google.gwt.user.client.Timer;

import org.exoplatform.gwtframework.ui.client.command.StatusTextControl;
import org.exoplatform.gwtframework.ui.client.component.TextButton.TextAlignment;
import org.exoplatform.ide.client.framework.annotation.RolesAllowed;
import org.exoplatform.ide.client.framework.control.IDEControl;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent;
import org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.editor.api.Editor;
import org.exoplatform.ide.editor.api.event.EditorCursorActivityEvent;
import org.exoplatform.ide.editor.api.event.EditorCursorActivityHandler;
import org.exoplatform.ide.vfs.client.model.FileModel;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
 *
 */
@RolesAllowed({"administrators", "developers"})
public class CursorPositionControl extends StatusTextControl implements IDEControl, EditorCursorActivityHandler,
   EditorActiveFileChangedHandler
{
   public static final String ID = "__editor_cursor_position";

   /**
    * 
    */
   public CursorPositionControl()
   {
      super(ID);

      setSize(70);
      setFireEventOnSingleClick(true);
      setText("&nbsp;");
      setTextAlignment(TextAlignment.CENTER);
      setEvent(new GoToLineEvent());
      IDE.addHandler(EditorCursorActivityEvent.TYPE, this);
      IDE.addHandler(EditorActiveFileChangedEvent.TYPE, this);
   }

   /**
    * @see org.exoplatform.ide.client.framework.control.IDEControl#initialize()
    */
   @Override
   public void initialize()
   {
      setVisible(true);
      setEnabled(true);
   }

   /**
    * @param row
    * @param column
    */
   private void setCursorPosition(int row, int column)
   {
      setText("<nobr>" + row + " : " + column + "</nobr>");
   }

   /**
    * @see org.exoplatform.gwtframework.editor.event.EditorActivityHandler#onEditorActivity(org.exoplatform.gwtframework.editor.event.EditorActivityEvent)
    */
   @Override
   public void onEditorCursorActivity(EditorCursorActivityEvent event)
   {
      if (event.getRow() > 0 && event.getColumn() > 0)
      {
         setEvent(new GoToLineEvent());
         setCursorPosition(event.getRow(), event.getColumn());
      }
      else
      {
         setEvent(null);
         setText("&nbsp;");
      }
   }

   private FileModel file;

   private Editor editor;

   private Timer updateCursorPositionTimer = new Timer()
   {

      @Override
      public void run()
      {
         updateCursorPosition();
      }

   };

   /**
    * 
    */
   private void updateCursorPosition()
   {
      try
      {
         if (file == null || editor == null)
         {
            setText("&nbsp;");
            setEvent(null);
            setVisible(false);
            return;
         }

         if (editor.getCursorRow() > 0 && editor.getCursorCol() > 0)
         {
            setEvent(new GoToLineEvent());
            setCursorPosition(editor.getCursorRow(), editor.getCursorCol());
         }
         else
         {
            setEvent(null);
            setText("&nbsp;");
         }

         setVisible(true);
      }
      catch (Throwable e)
      {
         e.printStackTrace();
      }
   }

   /**
    * @see org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedHandler#onEditorActiveFileChanged(org.exoplatform.ide.client.framework.editor.event.EditorActiveFileChangedEvent)
    */
   @Override
   public void onEditorActiveFileChanged(EditorActiveFileChangedEvent event)
   {
      file = event.getFile();
      editor = event.getEditor();

      updateCursorPositionTimer.cancel();
      updateCursorPositionTimer.schedule(500);
   }

}