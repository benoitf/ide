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
package org.exoplatform.ide.miscellaneous;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import junit.framework.Assert;

import com.google.gwt.editor.client.Editor.Ignore;

import org.exoplatform.ide.MenuCommands;
import org.exoplatform.ide.TestConstants;
import org.exoplatform.ide.core.Templates;
import org.junit.Test;

import java.awt.Robot;
import java.awt.event.KeyEvent;

/**
 * IDE-156:HotKeys customization.
 * 
 * Created by The eXo Platform SAS.
 * 
 * @author <a href="mailto:musienko.maxim@gmail.com">Musienko Maxim</a>
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id:   ${date} ${time}
 *
 */
public class HotkeysFormTest extends AbstractHotkeysTest
{

   @Test
   public void testFormAndButtons() throws Exception
   {
      IDE.WORKSPACE.waitForRootItem();
      //----- 1 ------------
      //Call "Customize Hotkeys" window
      IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.CUSTOMIZE_HOTKEYS);
      //      Thread.sleep(TestConstants.SLEEP);

      checkCustomizeHotkeyDialogWindow();

      //----- 2 ------------
      //select row
      selectRow(Commands.NEW_CSS_FILE);

      checkBindButtonEnabled(false);
      checkUnbindButtonEnabled(true);
      checkSaveButtonEnabled(false);
      checkCancelButtonEnabled(true);
      checkTextFieldEnabled(true);

      //----- 3 ------------
      //deselect row
      deselectRow(Commands.NEW_CSS_FILE);

      checkBindButtonEnabled(false);
      //TODO bug
      checkUnbindButtonEnabled(true);
      checkSaveButtonEnabled(false);
      checkCancelButtonEnabled(true);

      //----- 4 ------------
      //select row with binded hotkey
      selectRow(Commands.CREATE_FILE_FROM_TEMPLATE);

      checkBindButtonEnabled(false);
      checkUnbindButtonEnabled(true);
      checkSaveButtonEnabled(false);
      checkCancelButtonEnabled(true);
      checkTextFieldEnabled(true);
      assertEquals("Ctrl+N", getTextFromTextField());

      //----- 5 ------------
      //set another hotkey
      selenium().controlKeyDown();
      selenium().keyDown(TEXT_FIELD_LOCATOR, "" + java.awt.event.KeyEvent.VK_K);
      selenium().keyUp(TEXT_FIELD_LOCATOR, "" + java.awt.event.KeyEvent.VK_K);
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      checkBindButtonEnabled(true);
      checkUnbindButtonEnabled(true);
      checkSaveButtonEnabled(false);
      checkCancelButtonEnabled(true);
      assertEquals("Ctrl+K", getTextFromTextField());

      //----- 6 ------------
      //click Bind button
      clickButton(BIND_BUTTON_LOCATOR);

      checkBindButtonEnabled(false);
      checkUnbindButtonEnabled(true);
      checkSaveButtonEnabled(true);
      checkCancelButtonEnabled(true);
      //      assertEquals("Ctrl+K", getTextFromTextField());

      //close
      closeHotkeysWindow();

   }

   /**
    * IDE-156:HotKeys customization
    * ----- 26-31 ------------
    * @throws Exception
    */
   @Test
   public void testBindingAndUnbindingNewHotkey() throws Exception
   {
      refresh();
      //TODO 1 step not work, shold be fix call hotkey form; see issue 729
      //----- 1 ------------
      //call customize hotkeys form
      IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.CUSTOMIZE_HOTKEYS);

      //----- 2 ------------
      //select row with css file
      selectRow(Commands.NEW_CSS_FILE);

      //----- 3 ------------
      //Press Ctrl+M, press Bind button, press Save button
      selenium().controlKeyDown();
      selenium().keyDown(TEXT_FIELD_LOCATOR, "M");
      selenium().keyUp(TEXT_FIELD_LOCATOR, "M");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      //check no message
      checkNoMessage();
      //check Bind button is enabled
      checkBindButtonEnabled(true);
      //click Bind button
      clickButton(BIND_BUTTON_LOCATOR);
      //click Save button
      clickButton(SAVE_BUTTON_LOCATOR);

      //----- 4 ------------
      //Press Ctrl+M
      selenium().controlKeyDown();
      selenium().keyDown("//", "M");
      selenium().keyUp("//", "M");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      //check new Css file is createed
      assertEquals("Untitled file.css *", IDE.EDITOR.getTabTitle(0));

      //IDE.EDITOR.closeUnsavedFileAndDoNotSave(0);
      IDE.EDITOR.closeTabIgnoringChanges(0);
      //Thread.sleep(TestConstants.SLEEP);

      //----- 5 ------------
      //Call "Customize Hotkeys" window and select "New CSS file". Press Unbind button
      IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.CUSTOMIZE_HOTKEYS);
      selectRow(Commands.NEW_CSS_FILE);

      //check, Ctrl+M text appears near New CSS File in list grid
      assertEquals("Ctrl+M", getTextFromBindColumn(Commands.NEW_CSS_FILE));

      clickButton(UNBIND_BUTTON_LOCATOR);

      assertEquals("", getTextFromBindColumn(Commands.NEW_CSS_FILE));
      //Press Cancel button.
      clickButton(CANCEL_BUTTON_LOCATOR);

      //----- 6 ------------
      //Press Ctrl+M
      selenium().controlKeyDown();
      selenium().keyDown("//", "M");
      selenium().keyUp("//", "M");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      //check new Css file is createed
      assertEquals("Untitled file.css *", IDE.EDITOR.getTabTitle(0));

      //IDE.EDITOR.closeUnsavedFileAndDoNotSave(0);
      IDE.EDITOR.closeTabIgnoringChanges(0);
      //Thread.sleep(TestConstants.SLEEP);

      //----- 7 ------------
      //Call "Customize Hotkeys" window and select "New CSS file". 
      //Press Unbind button and then press Save button.
      IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.CUSTOMIZE_HOTKEYS);

      selectRow(Commands.NEW_CSS_FILE);

      //check, Ctrl+P appears near New CSS File in list grid
      assertEquals("Ctrl+M", getTextFromBindColumn(Commands.NEW_CSS_FILE));

      clickButton(UNBIND_BUTTON_LOCATOR);

      assertEquals("", getTextFromBindColumn(Commands.NEW_CSS_FILE));

      //click Save button
      clickButton(SAVE_BUTTON_LOCATOR);

      //----- 31 ------------
      //Press Ctrl+M
      selenium().controlKeyDown();
      selenium().keyDown("//", "M");
      selenium().keyUp("//", "M");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);
      //Nothing or default browser event works out
      Assert.assertFalse(IDE.EDITOR.isTabPresentInEditorTabset("Untitled file.css"));
      Thread.sleep(TestConstants.SLEEP);
   }

   /**
    * IDE-156:HotKeys customization
    * ----- 21-25 ------------
    * @throws Exception
    */

   @Test
   public void testTryToBindForbiddenHotkeys() throws Exception
   {
      refresh();
      //----- 1 ------------
      //TODO 1 step not work, shold be fix call hotkey form; see issue 729
      //Call "Customize Hotkeys" window
      IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.CUSTOMIZE_HOTKEYS);
      waitForElementPresent(CUSTOMIZE_HOTKEYS_FORM_LOCATOR);

      //----- 2 ------------
      //Binding forbidden hotkeys
      //Select "New Text file" command and try to bind Shift+N. 
      //Then try to bind ordinal keys Y, 8, PrintScreen and simmilar ordinal keys

      //After pressing forbidden keys (Shift, alphabet, digital, PrintScreen) 
      //Bind button is disabled and under text field appears error message 
      //(remember, that you can't bind ordinal keys such as N, Y, 8, PrintScreen, and keys, 
      //that start with Shift, but Ctrl+<digital> and Alt+<digital> will work)

      //click "New TEXT File" row
      selectRow(Commands.NEW_TEXT_FILE);

      //Double set focus to TEXT_FIELD_LOCATOR to test work :(
      selenium().focus(TEXT_FIELD_LOCATOR);
      Thread.sleep(TestConstants.SLEEP_SHORT);
      selenium().focus(TEXT_FIELD_LOCATOR);
      Thread.sleep(TestConstants.SLEEP_SHORT);
      selenium().click(TEXT_FIELD_LOCATOR);
      Thread.sleep(TestConstants.SLEEP_SHORT);
      selenium().click(TEXT_FIELD_LOCATOR);

      //press Shift+N
      Robot bot = new Robot();
      bot.keyPress(KeyEvent.VK_SHIFT);
      selenium().keyPressNative("" + java.awt.event.KeyEvent.VK_N);
      bot.keyRelease(KeyEvent.VK_SHIFT);

      checkMessage(ERROR_MESSAGE_STYLE, "First key should be Ctrl or Alt", true);

      //press Y
      selenium().keyPressNative("" + java.awt.event.KeyEvent.VK_Y);
      Thread.sleep(TestConstants.SLEEP_SHORT);
      checkMessage(ERROR_MESSAGE_STYLE, "First key should be Ctrl or Alt", true);

      //press 8
      selenium().keyPressNative("" + java.awt.event.KeyEvent.VK_8);
      Thread.sleep(TestConstants.SLEEP_SHORT);
      checkMessage(ERROR_MESSAGE_STYLE, "First key should be Ctrl or Alt", true);

      //----- 3 ------------
      //check, that after pressing Ctrl or Alt info message is displayed:
      //Holt Ctrl or Alt, then press key

      //select row with Css file
      selectRow(Commands.NEW_CSS_FILE);
      //press Ctrl
      selenium().keyPressNative("" + java.awt.event.KeyEvent.VK_CONTROL);
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      //check message
      checkMessage(INFO_MESSAGE_STYLE, "Holt Ctrl or Alt, then press key", true);
      //get text from text field
      assertEquals("Ctrl+", getTextFromTextField());
      //check Bind button is disabled
      checkBindButtonEnabled(false);

      //press alt
      selenium().keyPressNative("" + java.awt.event.KeyEvent.VK_ALT);
      Thread.sleep(TestConstants.REDRAW_PERIOD);
      //check message
      checkMessage(INFO_MESSAGE_STYLE, "Holt Ctrl or Alt, then press key", true);
      //get text from text field
      assertEquals("Alt+", getTextFromTextField());
      //check Bind button is disabled
      checkBindButtonEnabled(false);
      checkSaveButtonEnabled(false);

      //----- 4 ------------
      //Binding of hotkeys, that are reserved by editors

      //Try to bind Ctrl+C
      //select row with Css file
      selectRow(Commands.NEW_CSS_FILE);
      Thread.sleep(TestConstants.ANIMATION_PERIOD);
      //Press Ctrl+C
      selenium().controlKeyDown();
      selenium().keyDown("//", "C");
      selenium().keyUp("//", "C");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      //check text in text field
      assertEquals("Ctrl+C", getTextFromTextField());
      //check message
      checkMessage(ERROR_MESSAGE_STYLE, "This hotkey is used by Code or WYSIWYG Editors", true);
      //check Bind button is disabled
      checkBindButtonEnabled(false);
      checkSaveButtonEnabled(false);

      //----- 5 ------------
      //Binding hotkeys, that are bound to another commands

      //Press Ctrl+D
      selenium().controlKeyDown();
      selenium().keyDown("//", "D");
      selenium().keyUp("//", "D");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);
      //check text in text field
      Thread.sleep(2000);
      assertEquals("Ctrl+D", getTextFromTextField());
      //check message
      checkMessage(ERROR_MESSAGE_STYLE, "Such hotkey already bound to another command", true);
      //check Bind button is disabled
      checkBindButtonEnabled(false);
      checkSaveButtonEnabled(false);

      //Press Ctrl+P, check Bind button is enabled and no error message
      selenium().controlKeyDown();
      selenium().keyDown("//", "P");
      selenium().keyUp("//", "P");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      //check no message
      checkNoMessage();
      //check Bind button is enabled
      checkBindButtonEnabled(true);
      checkSaveButtonEnabled(false);

      //press bind button
      clickButton(BIND_BUTTON_LOCATOR);
      checkSaveButtonEnabled(true);

      //Try to bind the same hotkeys
      //      selectRow(Commands.NEW_CSS_FILE);
      //press Ctrl+P
      selenium().controlKeyDown();
      selenium().keyDown("//", "P");
      selenium().keyUp("//", "P");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);
      //check hotkeys in text field
      assertEquals("Ctrl+P", getTextFromTextField());
      checkBindButtonEnabled(false);
      checkMessage(ERROR_MESSAGE_STYLE, "Such hotkey already bound to this command", true);

      //close
      closeHotkeysWindow();

   }

   @Test
   public void testUnbindingDefaultHotkey() throws Exception
   {
      refresh();
      //----- 1 ------------
      //TODO 1 step not work, shold be fix call hotkey form; see issue 729
      //check Ctrl+N calls Create File From Template window
      //press Ctrl+N
      selenium().controlKeyDown();
      selenium().keyDown("//", "N");
      selenium().keyUp("//", "N");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      checkCreateFileFromTemplateFormAndClose();

      //----- 2 ------------
      //Call "Customize Hotkeys" window
      IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.CUSTOMIZE_HOTKEYS);
      Thread.sleep(TestConstants.SLEEP);

      //----- 3 ------------
      //select row with Create File From Template... command
      selectRow(Commands.CREATE_FILE_FROM_TEMPLATE);
      //unbind
      clickButton(UNBIND_BUTTON_LOCATOR);
      clickButton(SAVE_BUTTON_LOCATOR);

      //----- 4 ------------
      //press Ctrl+N
      selenium().controlKeyDown();
      selenium().keyDown("//", "N");
      selenium().keyUp("//", "N");
      selenium().controlKeyUp();
      Thread.sleep(TestConstants.REDRAW_PERIOD);

      //check no Crate File From Template form
      assertFalse(selenium().isElementPresent(Templates.FILE_FROM_TEMPLATE_FORM_ID));

      //----- 5 ------------
      //Call "Customize Hotkeys" window
      IDE.MENU.runCommand(MenuCommands.Window.WINDOW, MenuCommands.Window.CUSTOMIZE_HOTKEYS);
      Thread.sleep(TestConstants.SLEEP);

      selectRow(Commands.CREATE_FILE_FROM_TEMPLATE);
      assertEquals("", getTextFromBindColumn(Commands.CREATE_FILE_FROM_TEMPLATE));

      //close
      closeHotkeysWindow();

   }

}