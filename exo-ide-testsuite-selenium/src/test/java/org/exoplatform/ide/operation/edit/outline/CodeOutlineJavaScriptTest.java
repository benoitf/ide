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
package org.exoplatform.ide.operation.edit.outline;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.BaseTest;
import org.exoplatform.ide.MenuCommands;
import org.exoplatform.ide.TestConstants;
import org.exoplatform.ide.ToolbarCommands;
import org.exoplatform.ide.VirtualFileSystemUtils;
import org.exoplatform.ide.core.Outline.TokenType;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

/**
 * Test Code Outline panel for javascript file.
 * 
 * That tree is correctly displayed for javascript file.
 * 
 * That if working throught file, than correct node is 
 * highlited in outline tree.
 * 
 * That if click on node in outline tree, than cursor
 * goes to this token in editor.
 * 
 * @author <a href="dnochevnovr@gmail.com">Dmytro Nochevnov</a>
 * @version $Id:
 *
 */
public class CodeOutlineJavaScriptTest extends BaseTest
{
   
   private final static String FILE_NAME = "TestJavaScriptFile.js";

   private final static String FOLDER = CodeOutlineJavaScriptTest.class.getSimpleName();

   private final static String URL = BASE_URL + REST_CONTEXT + "/" + WEBDAV_CONTEXT + "/" + REPO_NAME + "/" + WS_NAME + "/" + FOLDER + "/";
   
   private OulineTreeHelper outlineTreeHelper;
   
   public CodeOutlineJavaScriptTest()
   {
      this.outlineTreeHelper = new OulineTreeHelper();
   }
   
   @BeforeClass
   public static void setUp()
   {
      String filePath = "src/test/resources/org/exoplatform/ide/operation/edit/outline/" + FILE_NAME;
      try
      {
         VirtualFileSystemUtils.mkcol(URL);
         VirtualFileSystemUtils.put(filePath, MimeType.APPLICATION_JAVASCRIPT, "nt:resource", URL + FILE_NAME);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }   
   
   @Test
   public void testCodeOutlineJavaScript() throws Exception
   {
      // Open groovy file with content
      Thread.sleep(TestConstants.IDE_LOAD_PERIOD);

      IDE.WORKSPACE.selectItem(WS_URL);
      IDE.MENU.runCommand(MenuCommands.File.FILE, MenuCommands.File.REFRESH);
      Thread.sleep(TestConstants.SLEEP);
      IDE.WORKSPACE.clickOpenIconOfFolder(URL);
      Thread.sleep(TestConstants.SLEEP);
      IDE.NAVIGATION.openFileFromNavigationTreeWithCodeEditor(URL + FILE_NAME, false);
      Thread.sleep(TestConstants.SLEEP);
      
      // open outline panel
      IDE.TOOLBAR.runCommand(ToolbarCommands.View.SHOW_OUTLINE);
      Thread.sleep(TestConstants.SLEEP);

      // check for presence and visibility of outline tab
      IDE.OUTLINE.assertOutlineTreePresent();
      IDE.OUTLINE.checkOutlinePanelVisibility(true);

      // create initial outline tree map
      outlineTreeHelper.init();
      outlineTreeHelper.addOutlineItem("a : Object", 1, false, TokenType.VARIABLE, "a"); // false, because outline node is not highlighted from test, but highlighted when goto this line manually
      outlineTreeHelper.addOutlineItem("b : Object", 8, TokenType.VARIABLE, "b");
      outlineTreeHelper.addOutlineItem("c()", 10, TokenType.FUNCTION, "c");
      outlineTreeHelper.addOutlineItem("d()", 12, TokenType.FUNCTION, "d");
      outlineTreeHelper.addOutlineItem("g()", 18, TokenType.FUNCTION, "g");
      outlineTreeHelper.addOutlineItem("e", 24, TokenType.VARIABLE, "e");
      outlineTreeHelper.addOutlineItem("f()", 27, false, TokenType.FUNCTION, "f");   // "false" to prevent opening "f()" node 
      outlineTreeHelper.addOutlineItem("a : Object", 30, TokenType.VARIABLE, "a");
      outlineTreeHelper.addOutlineItem("h : Object", 31, TokenType.VARIABLE, "h");
      outlineTreeHelper.addOutlineItem("l : Object", 32, TokenType.VARIABLE, "l");
      outlineTreeHelper.addOutlineItem("b : Number", 35, TokenType.VARIABLE, "b");
      outlineTreeHelper.addOutlineItem("c : Boolean", 36, TokenType.VARIABLE, "c");
      outlineTreeHelper.addOutlineItem("f : null", 37, TokenType.VARIABLE, "f");
      outlineTreeHelper.addOutlineItem("g : String", 37, false, TokenType.VARIABLE, "g");  // false, because with same line in file, like previous node
      outlineTreeHelper.addOutlineItem("e : Object", 39, TokenType.VARIABLE, "e");
      outlineTreeHelper.addOutlineItem("k : Object", 40, TokenType.VARIABLE, "k");
      outlineTreeHelper.addOutlineItem("i : Object", 43, TokenType.VARIABLE, "i");
      outlineTreeHelper.addOutlineItem("j : Array", 44, false, TokenType.VARIABLE, "j"); // false, because outline node is not highlighted from test, but highlighted when goto this line manually

      // check is tree created correctly
      outlineTreeHelper.checkOutlineTree();

      // create opened outline tree map
      outlineTreeHelper.clearOutlineTreeInfo();
      outlineTreeHelper.init();      
      outlineTreeHelper.addOutlineItem("a : Object", 1, false, TokenType.VARIABLE, "a"); // false, because outline node is not highlighted from test, but highlighted when goto this line manually
      outlineTreeHelper.addOutlineItem("b : Object", 8, TokenType.VARIABLE, "b");
      outlineTreeHelper.addOutlineItem("c()", 10, TokenType.FUNCTION, "c");
      outlineTreeHelper.addOutlineItem("d()", 12, TokenType.FUNCTION, "d");
      outlineTreeHelper.addOutlineItem("d1 : Object", 13, TokenType.VARIABLE, "d1");
      outlineTreeHelper.addOutlineItem("d4()", 14, TokenType.FUNCTION, "d4");
      outlineTreeHelper.addOutlineItem("d5()", 15, TokenType.FUNCTION, "d5");      
      outlineTreeHelper.addOutlineItem("g()", 18, TokenType.FUNCTION, "g");
      outlineTreeHelper.addOutlineItem("g1 : Object", 19, TokenType.VARIABLE, "g1");
      outlineTreeHelper.addOutlineItem("g4()", 20, TokenType.FUNCTION, "g4");      
      outlineTreeHelper.addOutlineItem("g5()", 21, TokenType.FUNCTION, "g5");      
      outlineTreeHelper.addOutlineItem("e", 24, TokenType.VARIABLE, "e");
      outlineTreeHelper.addOutlineItem("f()", 27, false, TokenType.FUNCTION, "f");  // false, because with same line in file, like next node
      outlineTreeHelper.addOutlineItem("i : Object", 27, TokenType.VARIABLE, "i");      
      outlineTreeHelper.addOutlineItem("a : Object", 30, TokenType.VARIABLE, "a");
      outlineTreeHelper.addOutlineItem("h : Object", 31, TokenType.VARIABLE, "h");
      outlineTreeHelper.addOutlineItem("l : Object", 32, TokenType.VARIABLE, "l");
      outlineTreeHelper.addOutlineItem("b : Number", 35, TokenType.VARIABLE, "b");
      outlineTreeHelper.addOutlineItem("c : Boolean", 36, TokenType.VARIABLE, "c");
      outlineTreeHelper.addOutlineItem("f : null", 37, TokenType.VARIABLE, "f");
      outlineTreeHelper.addOutlineItem("g : String", 37, false, TokenType.VARIABLE, "g");  // false, because with same line in file, like previous node
      outlineTreeHelper.addOutlineItem("e : Object", 39, TokenType.VARIABLE, "e");
      outlineTreeHelper.addOutlineItem("k : Object", 40, TokenType.VARIABLE, "k");
      outlineTreeHelper.addOutlineItem("i : Object", 43, false, TokenType.VARIABLE, "i"); // false, because outline node is not highlighted from test, but highlighted when goto this line manually
      outlineTreeHelper.addOutlineItem("j : Array", 44, false, TokenType.VARIABLE, "j"); // false, because outline node is not highlighted from test, but highlighted when goto this line manually

      // expand outline tree
      outlineTreeHelper.expandOutlineTree();      
      
      // check is tree created correctly
      outlineTreeHelper.checkOutlineTree();
   }
}