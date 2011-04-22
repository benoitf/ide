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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.exoplatform.common.http.client.ModuleException;
import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.BaseTest;
import org.exoplatform.ide.Locators;
import org.exoplatform.ide.TestConstants;
import org.exoplatform.ide.VirtualFileSystemUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for code outline for html files.
 * 
 * @author <a href="mailto:njusha.exo@gmail.com">Nadia Zavalko</a>
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id:   ${date} ${time}
 *
 */
public class CodeOutLineHtmlTest extends BaseTest
{
   
   private final static String FILE_NAME = "HtmlCodeOutline.html";
   
   private final static String FOLDER_NAME = CodeOutLineHtmlTest.class.getSimpleName();

   private final static String URL = BASE_URL + REST_CONTEXT + "/" + WEBDAV_CONTEXT + "/" + REPO_NAME + "/" + WS_NAME + "/";
   
   @BeforeClass
   public static void setUp()
   {
      String filePath ="src/test/resources/org/exoplatform/ide/operation/edit/outline/HtmlCodeOutline.html";
      try
      {
         VirtualFileSystemUtils.mkcol(URL + FOLDER_NAME);
         VirtualFileSystemUtils.put(filePath, MimeType.TEXT_HTML, URL + FOLDER_NAME + "/" + FILE_NAME);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      catch (ModuleException e)
      {
         e.printStackTrace();
      }
   }
   
   @AfterClass
   public static void tearDown()
   {
      try
      {
         VirtualFileSystemUtils.delete(URL + FOLDER_NAME);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      catch (ModuleException e)
      {
         e.printStackTrace();
      }
   }

   // IDE-175:Html Code Outline
   @Test
   public void testCodeOutLineHtml() throws Exception
   {
      //---- 1-3 -----------------
      //open file with text
      Thread.sleep(TestConstants.SLEEP);
      IDE.NAVIGATION.clickOpenIconOfFolder(WS_URL + FOLDER_NAME + "/");
      Thread.sleep(TestConstants.REDRAW_PERIOD);
      IDE.NAVIGATION.openFileFromNavigationTreeWithCodeEditor(FILE_NAME, false);

      //---- 4 ----
      //show Outline
      Thread.sleep(TestConstants.SLEEP);
      IDE.TOOLBAR.runCommand("Show Outline");
      Thread.sleep(TestConstants.SLEEP);

      //---- 5 ----
      //check Outline tree
      checkTreeCorrectlyCreated();
      
      //---- 6 ----
      //check navigation in tree
      //click on td tag from tbody of first table
      IDE.OUTLINE.select(15);
      assertEquals("21 : 1", getCursorPositionUsingStatusBar());
      
      //close tr tag
      IDE.OUTLINE.clickOpenImg(13, 1);
      assertEquals("21 : 1", getCursorPositionUsingStatusBar());
      
      //open tr tag
      IDE.OUTLINE.clickOpenImg(13, 1);
      assertEquals("21 : 1", getCursorPositionUsingStatusBar());
      IDE.OUTLINE.checkOutlineTreeNodeSelected(15, "td", true);
      
      //click on another td tab from the second table
//      selenium.click("scLocator=//TreeGrid[ID=\"ideOutlineTreeGrid\"]/body/row[28]/col[1]/open");
//      Thread.sleep(TestConstants.SLEEP_SHORT);
//      assertEquals("46 : 1", getCursorPositionUsingStatusBar());
      
      //click on first table node in Outlite tree
      IDE.OUTLINE.select(8);
      assertEquals("14 : 1", getCursorPositionUsingStatusBar());
      
      //---- 7 ----
      //check, that tree will correctly updated, after changing content
      
      //delete table tag from html file
      //press Ctrl+D to delete lines
      for (int i = 0; i < 11; i++)
      {
        IDE.EDITOR.runHotkeyWithinEditor(0, true, false, 68);
         Thread.sleep(TestConstants.SLEEP_SHORT);
      }
      Thread.sleep(TestConstants.SLEEP);
      
      //check updated tree
      assertEquals("html", IDE.OUTLINE.getTitle(0, 0));
      assertEquals("head", IDE.OUTLINE.getTitle(1, 0));
      assertEquals("body", IDE.OUTLINE.getTitle(2, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(3, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(4, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(5, 0));
      assertEquals("style", IDE.OUTLINE.getTitle(6, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(7, 0));
      
      //check, that first br tag is selected
      IDE.OUTLINE.checkOutlineTreeNodeSelected(3, "br", true);
      assertEquals("14 : 1", getCursorPositionUsingStatusBar());
      
      //click on editor
     IDE.EDITOR.clickOnEditor();
      selenium.clickAt("//body[@class='editbox']", "5,5");
      //press key DOWN to navigate in editor
      for (int i = 0; i < 18; i++){
         selenium.keyPressNative("" + java.awt.event.KeyEvent.VK_DOWN);
         Thread.sleep(TestConstants.SLEEP_SHORT);
      }
      Thread.sleep(TestConstants.SLEEP);
      assertEquals("19 : 1", getCursorPositionUsingStatusBar());
      //check updated tree
      //node script must be opened and displayGreeting selected (and opened too)
      assertEquals("html", IDE.OUTLINE.getTitle(0, 0));
      assertEquals("head", IDE.OUTLINE.getTitle(1, 0));
      assertEquals("body", IDE.OUTLINE.getTitle(2, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(3, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(4, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(5, 0));
      assertEquals("prefs : gadgets.Prefs", IDE.OUTLINE.getTitle(6, 0));
      assertEquals("displayGreeting()", IDE.OUTLINE.getTitle(7, 0));
      assertEquals("today : Date", IDE.OUTLINE.getTitle(8, 0));
      assertEquals("time : Object", IDE.OUTLINE.getTitle(9, 0));
      assertEquals("html : Object", IDE.OUTLINE.getTitle(10, 0));  
      assertEquals("style", IDE.OUTLINE.getTitle(11, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(12, 0));
      
      Thread.sleep(TestConstants.SLEEP);

      //---- 8 ----
      //close file
     IDE.EDITOR.closeUnsavedFileAndDoNotSave(0);
   }
   
   private void checkTreeCorrectlyCreated() throws Exception
   {
      //check for presence of tab outline
      assertTrue(selenium.isElementPresent(Locators.CodeHelperPanel.SC_OUTLINE_TAB_LOCATOR));
      assertEquals("Outline", selenium.getText(Locators.CodeHelperPanel.SC_CODE_HELPER_TABSET_LOCATOR 
         + "/tab[index=0]/title"));
      
      //check tree correctly created:
      //all nodes closed, except root
      assertEquals("html", IDE.OUTLINE.getTitle(0, 0));
      assertEquals("head", IDE.OUTLINE.getTitle(1, 0));
      assertEquals("body", IDE.OUTLINE.getTitle(2, 0));
      
      //open head node
      IDE.OUTLINE.clickOpenImg(1, 0);
      //check new nodes appeard
      assertEquals("meta", IDE.OUTLINE.getTitle(2, 0));
      assertEquals("link", IDE.OUTLINE.getTitle(3, 0));
      assertEquals("title", IDE.OUTLINE.getTitle(4, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(5, 0));
      assertEquals("style", IDE.OUTLINE.getTitle(6, 0));
      assertEquals("body", IDE.OUTLINE.getTitle(7, 0));
      
      Thread.sleep(TestConstants.SLEEP*2);
      IDE.OUTLINE.clickOpenImg(7, 0);
      Thread.sleep(TestConstants.SLEEP*2);
      
      //check new nodes appeard
      assertEquals("table", IDE.OUTLINE.getTitle(8, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(9, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(10, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(11, 0));
      assertEquals("style", IDE.OUTLINE.getTitle(12, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(13, 0));
      
      //open table node
      IDE.OUTLINE.clickOpenImg(8, 0);
      //check subnodes of table
      assertEquals("thead", IDE.OUTLINE.getTitle(9, 0));
      assertEquals("tbody", IDE.OUTLINE.getTitle(10, 0));
      //check other nodes
      assertEquals("br", IDE.OUTLINE.getTitle(11, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(12, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(13, 0));
      assertEquals("style", IDE.OUTLINE.getTitle(14, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(15, 0));
      
      //open thead node
      IDE.OUTLINE.clickOpenImg(9, 0);
      //check subnodes of thead
      assertEquals("tr", IDE.OUTLINE.getTitle(10, 0));
      //check other nodes
      assertEquals("tbody", IDE.OUTLINE.getTitle(11, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(12, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(13, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(14, 0));
      assertEquals("style", IDE.OUTLINE.getTitle(15, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(16, 0));
      
      //open tr node
      IDE.OUTLINE.clickOpenImg(10, 0);
      //check subnodes of tr
      assertEquals("td", IDE.OUTLINE.getTitle(11, 0));
      //check other nodes
      assertEquals("tbody", IDE.OUTLINE.getTitle(12, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(13, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(14, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(15, 0));
      assertEquals("style", IDE.OUTLINE.getTitle(16, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(17, 0));
      
      //open tbody node
      IDE.OUTLINE.clickOpenImg(12, 0);
      //check subnodes of tbody
      assertEquals("tr", IDE.OUTLINE.getTitle(13, 0));
      //check other nodes
      assertEquals("br", IDE.OUTLINE.getTitle(14, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(15, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(16, 0));
      assertEquals("style", IDE.OUTLINE.getTitle(17, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(18, 0));
      
      //open tr node
      IDE.OUTLINE.clickOpenImg(13, 0);
      //check subnodes of tr
      assertEquals("td", IDE.OUTLINE.getTitle(14, 0));
      assertEquals("td", IDE.OUTLINE.getTitle(15, 0));
      assertEquals("td", IDE.OUTLINE.getTitle(16, 0));
      //check other nodes
      assertEquals("br", IDE.OUTLINE.getTitle(17, 0));
      assertEquals("br", IDE.OUTLINE.getTitle(18, 0));
      assertEquals("script", IDE.OUTLINE.getTitle(19, 0));
      assertEquals("style", IDE.OUTLINE.getTitle(20, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(21, 0));
      
      //open script node
      IDE.OUTLINE.clickOpenImg(19, 0);
      //check subnodes of script
      assertEquals("prefs : gadgets.Prefs", IDE.OUTLINE.getTitle(20, 0));
      assertEquals("displayGreeting()", IDE.OUTLINE.getTitle(21, 0));
      //check other nodes
      assertEquals("style", IDE.OUTLINE.getTitle(22, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(23, 0));
      
      //open displayGreeting node
      IDE.OUTLINE.clickOpenImg(21, 0);
      //check subnodes of displayGreeting
      assertEquals("today : Date", IDE.OUTLINE.getTitle(22, 0));
      assertEquals("time : Object", IDE.OUTLINE.getTitle(23, 0));
      assertEquals("html : Object", IDE.OUTLINE.getTitle(24, 0));      

      //check other nodes
      assertEquals("style", IDE.OUTLINE.getTitle(25, 0));
      assertEquals("table", IDE.OUTLINE.getTitle(26, 0));
      
      Thread.sleep(TestConstants.SLEEP);
   }

}
