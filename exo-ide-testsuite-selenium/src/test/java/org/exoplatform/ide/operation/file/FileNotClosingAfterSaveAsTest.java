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
package org.exoplatform.ide.operation.file;

import org.exoplatform.ide.BaseTest;
import org.exoplatform.ide.MenuCommands;
import org.exoplatform.ide.TestConstants;
import org.exoplatform.ide.ToolbarCommands;
import org.exoplatform.ide.VirtualFileSystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: Nov 3, 2010 $
 *
 */

public class FileNotClosingAfterSaveAsTest extends BaseTest
{

   private static final String FOLDER_NAME = FileNotClosingAfterSaveAsTest.class.getSimpleName();

   private static final String FILE_NAME_1 = "file-" + FileNotClosingAfterSaveAsTest.class.getSimpleName() + "-"
      + System.currentTimeMillis();

   private static final String FILE_NAME_2 = "file-" + FileNotClosingAfterSaveAsTest.class.getSimpleName() + "-"
      + System.currentTimeMillis() + "5";

   private static final String URL = BASE_URL + REST_CONTEXT + "/" + WEBDAV_CONTEXT + "/" + REPO_NAME + "/" + WS_NAME
      + "/" + FOLDER_NAME + "/";

   @BeforeClass
   public static void setUp()
   {
      try
      {
         VirtualFileSystemUtils.mkcol(URL);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

   //http://jira.exoplatform.com/browse/IDE-404
   @Test
   public void testFileNotClosingAfterSaveAs() throws Exception
   {
      Thread.sleep(TestConstants.SLEEP);
      IDE.TOOLBAR.runCommand(ToolbarCommands.File.REFRESH);
      IDE.WORKSPACE.selectItem(WS_URL + FOLDER_NAME + "/");

      createSaveAndCloseFile(MenuCommands.New.REST_SERVICE_FILE, FILE_NAME_1, 0);

      IDE.NAVIGATION.openFileFromNavigationTreeWithCodeEditor(WS_URL + FOLDER_NAME + "/" + FILE_NAME_1, false);

      IDE.EDITOR.typeTextIntoEditor(0, "test test test");

      //IDE.EDITOR.closeUnsavedFileAndDoNotSave(0);
      IDE.EDITOR.closeTabIgnoringChanges(0);

      IDE.TOOLBAR.runCommandFromNewPopupMenu(MenuCommands.New.HTML_FILE);

      saveAsUsingToolbarButton(FILE_NAME_2);

      IDE.EDITOR.checkCodeEditorOpened(0);

   }

   @AfterClass
   public static void tearDown()
   {
      try
      {
         VirtualFileSystemUtils.delete(BASE_URL + REST_CONTEXT + "/" + WEBDAV_CONTEXT + "/" + REPO_NAME + "/" + WS_NAME
            + "/" + FOLDER_NAME);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }

}
