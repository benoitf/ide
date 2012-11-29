/*
 * Copyright (C) 2012 eXo Platform SAS.
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
package org.exoplatform.ide.wizard;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

/**
 * Wizard presenter manages wizard pages. It's responsible for the 
 * communication user and wizard page.
 * In typical usage, the client instantiates this class with a particular
 * wizard page. The wizard serves as the wizard page container and orchestrates the
 * presentation of its pages.
 * 
 * @author <a href="mailto:aplotnikov@exoplatform.com">Andrey Plotnikov</a>
 *
 */
public class WizardPresenter implements WizardPagePresenter.WizardUpdateDelegate, WizardView.ActionDelegate
{
   private WizardPagePresenter currentPage;

   private WizardView display;

   /**
    * Creates WizardPresenter with given instance of view and current wizard page
    * 
    * For Unit Tests
    * 
    * @param currentPage
    * @param display
    */
   protected WizardPresenter(WizardPagePresenter currentPage, WizardView display)
   {
      this.display = display;
      currentPage.setUpdateDelegate(this);
      setPage(currentPage);
   }

   /**
    * Creates WizardPresenter with given current wizard page, resources, wizard's title
    * 
    * @param currentPage
    * @param resources
    * @param title
    */
   public WizardPresenter(WizardPagePresenter currentPage, WizardResource resources, String title)
   {
      this.display = new WizardViewImpl(resources, title);
      display.setDelegate(this);

      currentPage.setUpdateDelegate(this);
      setPage(currentPage);
   }

   
   /**
    * {@inheritDoc}
    */
   public void onNextClicked()
   {
      currentPage = currentPage.flipToNext();
      updateControls();
      currentPage.go(display.getContentPanel());
   }

   /**
    * {@inheritDoc}
    */
   public void onBackClicked()
   {
      currentPage = currentPage.flipToPrevious();
      // update buttons, once the page changed
      updateControls();
      currentPage.go(display.getContentPanel());
   }

   /**
    * {@inheritDoc}
    */
   public void onFinishClicked()
   {
      currentPage.doFinish();
      display.close();
   }

   /**
    * {@inheritDoc}
    */
   public void onCancelClicked()
   {
      currentPage.doCancel();
      display.close();
   }

   /**
    * {@inheritDoc}
    */
   public void setPage(WizardPagePresenter page)
   {
      page.setPrevious(currentPage);
      currentPage = page;
      updateControls();
      currentPage.go(display.getContentPanel());
   }

   /**
    * {@inheritDoc}
    */
   public void updateControls()
   {
      // read the state of the buttons from current page
      display.setBackButtonVisible(currentPage.hasPrevious());
      display.setNextButtonVisible(currentPage.hasNext());
      display.setNextButtonEnabled(currentPage.isCompleted());
      display.setFinishButtonEnabled(currentPage.canFinish());

      display.setCaption(currentPage.getCaption());
      display.setNotice(currentPage.getNotice());
      ImageResource image = currentPage.getImage();
      display.setImage(image == null ? null : new Image(image));
   }

   /**
    * Show wizard
    */
   public void showWizard()
   {
      display.showWizard();
   }
}