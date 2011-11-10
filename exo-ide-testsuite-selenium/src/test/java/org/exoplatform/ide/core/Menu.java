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
package org.exoplatform.ide.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.exoplatform.ide.TestConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version $
 */

public class Menu extends AbstractTestModule
{

   interface Locators
   {
      String LOCK_LAYER_CLASS = "exo-lockLayer";

      String TOP_MENU_ITEM_LOCATOR = "//td[@class='exo-menuBarItem' and text()='%s']";

      String MENU_ITEM_LOCATOR = "//td[@class='exo-popupMenuTitleField' and contains(., '%s')]";

      String POPUP_SELECTOR = "table.exo-popupMenuTable";

      String MENU_ITEM_ROW_LOCATOR = "//table[@class='exo-popupMenuTable']//tr[contains(., '%s')]";

      String ENABLED_ATTRIBUTE = "item-enabled";
   }

   @FindBy(className = Locators.LOCK_LAYER_CLASS)
   private WebElement lockLayer;

   /**
    * Open command from top menu.
    * 
    * @param topMenuName name of menu
    * @param commandName command name
    */
   public void runCommand(String topMenuName, String commandName) throws Exception
   {
      String menuItemLocator = "//td[@class='exo-popupMenuTitleField']/nobr[text()='" + commandName + "']";

      selenium().mouseDown("//td[@class='exo-menuBarItem' and text()='" + topMenuName + "']");
      waitForElementPresent(menuItemLocator);
      selenium().click(menuItemLocator);

      Thread.sleep(TestConstants.REDRAW_PERIOD);
   }

   public void runCommand(String menuName, String commandName, String subCommandName) throws Exception
   {
      String menuItemLocator1 = "//td[@class='exo-popupMenuTitleField']/nobr[text()='" + commandName + "']";
      String menuItemLocator2 = "//td[@class='exo-popupMenuTitleField']/nobr[text()='" + subCommandName + "']";

      selenium().mouseDown("//td[@class='exo-menuBarItem' and text()='" + menuName + "']");
      Thread.sleep(TestConstants.ANIMATION_PERIOD);

      selenium().click(menuItemLocator1);
      Thread.sleep(TestConstants.ANIMATION_PERIOD);

      selenium().click(menuItemLocator2);
      Thread.sleep(TestConstants.REDRAW_PERIOD);
   }

   /**
    * Check is command in top menu visible or hidden.
    * 
    * @param topMenuName mane of menu
    * @param commandName command name
    * @param isPresent boolean value
    * 
    * use {@link #isCommandVisible(String, String)}
    */
   @Deprecated
   public void checkCommandVisibility(String topMenuName, String commandName, boolean visible) throws Exception
   {
      selenium().mouseDownAt("//td[@class='exo-menuBarItem' and text()='" + topMenuName + "']", "");

      if (visible)
      {
         assertTrue(selenium().isElementPresent("//td/nobr[text()='" + commandName + "']"));
      }
      else
      {
         assertFalse(selenium().isElementPresent("//td/nobr[text()='" + commandName + "']"));
      }
      lockLayer.click();
      Thread.sleep(TestConstants.ANIMATION_PERIOD);
   }

   /**
    * Returns visibility state of the menu command.
    * 
    * @param topMenuName mane of menu
    * @param commandName command name
    * @return {@link Boolean} command visibility state
    * @throws Exception
    */
   public boolean isCommandVisible(String topMenuName, String commandName) throws Exception
   {
      WebElement topMenuItem =
         driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
      topMenuItem.click();
      waitMenuPopUp();

      try
      {
         return driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_LOCATOR, commandName))) != null;
      }
      catch (NoSuchElementException e)
      {
         return false;
      }
      finally
      {
         lockLayer.click();
      }
   }

   /**
    * Wait for menu popup to draw.
    */
   protected void waitMenuPopUp()
   {
      new WebDriverWait(driver(), 2).until(new ExpectedCondition<Boolean>()
      {

         @Override
         public Boolean apply(WebDriver driver)
         {
            try
            {
               return driver.findElement(By.cssSelector(Locators.POPUP_SELECTOR)) != null;
            }
            catch (NoSuchElementException e)
            {
               return false;
            }
         }
      });
   }

   /**
    * Check is command in top menu enabled or disabled.
    * 
    * @param topMenuName mane of menu
    * @param commandName command name
    * @param enabled boolean value
    */
   @Deprecated
   public void checkCommandEnabled(String topMenuName, String commandName, boolean enabled) throws Exception
   {
      selenium().mouseDownAt("//td[@class='exo-menuBarItem' and text()='" + topMenuName + "']", "");
      waitForElementPresent("//table[@class=\"exo-popupMenuTable\"]");

      //Thread.sleep(TestConstants.ANIMATION_PERIOD);

      if (enabled)
      {
         assertTrue(selenium().isElementPresent(
            "//table[@class=\"exo-popupMenuTable\"]//td[@class=\"exo-popupMenuTitleField\"]/nobr[text()='"
               + commandName + "']"));
      }
      else
      {
         assertTrue(selenium().isElementPresent(
            "//table[@class=\"exo-popupMenuTable\"]//td[@class=\"exo-popupMenuTitleFieldDisabled\"]/nobr[text()='"
               + commandName + "']"));
      }

      selenium().mouseDown("//div[@class='exo-lockLayer']/");
      waitForElementNotPresent("//table[@class=\"exo-popupMenuTable\"]");
      //Thread.sleep(TestConstants.ANIMATION_PERIOD);
   }

   /**
    * Returns enabled state of the menu command. 
    * 
    * @param topMenuName top menu command name
    * @param commandName command name
    * @return {@link Boolean} enabled state of the menu command
    * @throws Exception
    */
   public boolean isCommandEnabled(String topMenuName, String commandName) throws Exception
   {
      WebElement topMenuItem =
         driver().findElement(By.xpath(String.format(Locators.TOP_MENU_ITEM_LOCATOR, topMenuName)));
      topMenuItem.click();
      waitMenuPopUp();

      try
      {
         WebElement command =
            driver().findElement(By.xpath(String.format(Locators.MENU_ITEM_ROW_LOCATOR, commandName)));
         return Boolean.parseBoolean(command.getAttribute(Locators.ENABLED_ATTRIBUTE));
      }
      catch (NoSuchElementException e)
      {
         e.printStackTrace();
         return false;
      }
      finally
      {
         lockLayer.click();
      }
   }

   /**
    * Get the XPATH locator for top menu command.
    * 
    * @param title - the title of top menu command.
    * 
    * @return {@link String}
    */
   public String getMenuLocator(String title)
   {
      return "//td[@class='exo-menuBarItem' and text()='" + title + "']";
   }

   public void waitForMenuItemPresent(String itemName) throws Exception
   {
      waitForElementPresent(getMenuLocator(itemName));
   }

   public void waitForMenuItemPresent(String menuName, String itemName) throws Exception
   {
      final String menuLocator = getMenuLocator(menuName);
      waitForElementPresent(menuLocator);
      selenium().mouseDown(menuLocator);
      Thread.sleep(TestConstants.ANIMATION_PERIOD);
      waitForElementPresent("//td[@class='exo-popupMenuTitleField']/nobr[text()='" + itemName + "']");
      selenium().mouseDown("//div[@class='exo-lockLayer']/");
      Thread.sleep(TestConstants.ANIMATION_PERIOD);
   }

}
