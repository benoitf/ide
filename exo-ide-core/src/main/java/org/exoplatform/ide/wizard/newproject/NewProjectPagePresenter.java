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
package org.exoplatform.ide.wizard.newproject;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.AcceptsOneWidget;

import org.exoplatform.ide.json.JsonArray;
import org.exoplatform.ide.paas.PaaS;
import org.exoplatform.ide.paas.PaaSAgentImpl;
import org.exoplatform.ide.wizard.AbstractWizardPagePresenter;
import org.exoplatform.ide.wizard.WizardAgentImpl;
import org.exoplatform.ide.wizard.WizardPagePresenter;

/**
 * Provides selecting kind of project which user wish to create.
 * 
 * @author <a href="mailto:aplotnikov@exoplatform.com">Andrey Plotnikov</a>
 */
public class NewProjectPagePresenter extends AbstractWizardPagePresenter implements NewProjectPageView.ActionDelegate
{
   private WizardPagePresenter next;

   private NewProjectPageView view;

   private JsonArray<NewProjectWizardData> wizards;

   private PaaS selectedPaaS;

   private JsonArray<PaaS> paases;

   /**
    * Create presenter
    * 
    * @param wizardAgent
    * @param resources
    * @param paasAgent
    */
   public NewProjectPagePresenter(WizardAgentImpl wizardAgent, NewProjectWizardResource resources,
      PaaSAgentImpl paasAgent)
   {
      this(wizardAgent, resources.newProjectIcon(), new NewProjectPageViewImpl(wizardAgent.getNewProjectWizards(),
         paasAgent.getPaaSes()), paasAgent);
   }

   /**
    * Create presenter
    * 
    * For tests
    * 
    * @param wizardAgent
    * @param image
    * @param view
    */
   protected NewProjectPagePresenter(WizardAgentImpl wizardAgent, ImageResource image, NewProjectPageView view,
      PaaSAgentImpl paasAgent)
   {
      super("Select a wizard", image);
      this.view = view;
      view.setDelegate(this);
      this.wizards = wizardAgent.getNewProjectWizards();
      this.paases = paasAgent.getPaaSes();
   }

   /**
    * {@inheritDoc}
    */
   public WizardPagePresenter flipToNext()
   {
      next.setPrevious(this);
      next.setUpdateDelegate(delegate);
      return next;
   }

   /**
    * {@inheritDoc}
    */
   public boolean canFinish()
   {
      return false;
   }

   /**
    * {@inheritDoc}
    */
   public boolean hasNext()
   {
      return next != null;
   }

   /**
    * {@inheritDoc}
    */
   public boolean isCompleted()
   {
      return next != null && selectedPaaS != null;
   }

   /**
    * {@inheritDoc}
    */
   public String getNotice()
   {
      if (next ==null)
      {
         return "Please, choose technology";
      }
      else if (selectedPaaS == null)
      {
         return "Please, choose PaaS";
      }
      
      return null;
   }

   /**
    * {@inheritDoc}
    */
   public void go(AcceptsOneWidget container)
   {
      container.setWidget(view);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void onProjectTypeSelected(int id)
   {
      next = wizards.get(id).getWizardPage();
      selectedPaaS = null;
      delegate.updateControls();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void onPaaSSelected(int id)
   {
      selectedPaaS = paases.get(id);
      delegate.updateControls();
   }
}