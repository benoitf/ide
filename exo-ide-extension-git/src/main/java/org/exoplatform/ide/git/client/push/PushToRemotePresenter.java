/*
 * Copyright (C) 2011 eXo Platform SAS.
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
package org.exoplatform.ide.git.client.push;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasValue;

import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.ide.client.framework.module.IDE;
import org.exoplatform.ide.client.framework.output.event.OutputEvent;
import org.exoplatform.ide.client.framework.output.event.OutputMessage.Type;
import org.exoplatform.ide.client.framework.ui.api.IsView;
import org.exoplatform.ide.git.client.GitClientService;
import org.exoplatform.ide.git.client.Messages;
import org.exoplatform.ide.git.client.remote.HasBranchesPresenter;
import org.exoplatform.ide.git.shared.Branch;
import org.exoplatform.ide.git.shared.Remote;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * Presenter of view for pushing changes to remote repository.
 * The view is pointed in Views.gwt.xml file.
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:  Apr 4, 2011 9:53:07 AM anya $
 *
 */
public class PushToRemotePresenter extends HasBranchesPresenter implements PushToRemoteHandler
{
   public interface Display extends IsView
   {
      /**
       * Get the push button click handler.
       * 
       * @return {@link HasClickHandlers} push button
       */
      HasClickHandlers getPushButton();

      /**
       * Get the cancel button click handler.
       * 
       * @return {@link HasClickHandlers} cancel button
       */
      HasClickHandlers getCancelButton();

      /**
       * Get remote repository field value.
       * 
       * @return {@link HasValue} field
       */
      HasValue<String> getRemoteValue();

      /**
       * Get remote branches field value.
       * 
       * @return {@link HasValue} field
       */
      HasValue<String> getRemoteBranchesValue();

      /**
       * Get local branches field value.
       * 
       * @return {@link HasValue} field
       */
      HasValue<String> getLocalBranchesValue();

      /**
       * Set values of remote repositories. 
       * 
       * @param values values to set
       */
      void setRemoteValues(LinkedHashMap<String, String> values);

      String getRemoteDisplayValue();

      /**
       * Set values of remote repository branches. 
       * 
       * @param values values to set
       */
      void setRemoteBranches(String[] values);

      /**
       * Set values of local repository branches. 
       * 
       * @param values values to set
       */
      void setLocalBranches(String[] values);

      /**
       * Change the enable state of the push button.
       * 
       * @param enable enable state
       */
      void enablePushButton(boolean enable);

   }

   /**
    * Presenter's display.
    */
   private Display display;

   /**
    * @param eventBus events handler
    */
   public PushToRemotePresenter(HandlerManager eventBus)
   {
      super(eventBus);
      eventBus.addHandler(PushToRemoteEvent.TYPE, this);
   }

   /**
    * Bind pointed display with presenter.
    * 
    * @param d display
    */
   public void bindDisplay(Display d)
   {
      this.display = d;

      display.getCancelButton().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(ClickEvent event)
         {
            IDE.getInstance().closeView(display.asView().getId());
         }
      });

      display.getPushButton().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(ClickEvent event)
         {
            doPush();
         }
      });

      display.getRemoteValue().addValueChangeHandler(new ValueChangeHandler<String>()
      {

         @Override
         public void onValueChange(ValueChangeEvent<String> event)
         {
            display.setRemoteBranches(getRemoteBranchesToDisplay(display.getRemoteDisplayValue()));
         }
      });

      display.getRemoteBranchesValue().addValueChangeHandler(new ValueChangeHandler<String>()
      {

         @Override
         public void onValueChange(ValueChangeEvent<String> event)
         {
            boolean empty = (event.getValue() == null || event.getValue().length() <= 0);
            display.enablePushButton(!empty);
         }
      });
   }

   /**
    * @see org.exoplatform.ide.git.client.push.PushToRemoteHandler#onPushToRemote(org.exoplatform.ide.git.client.push.PushToRemoteEvent)
    */
   @Override
   public void onPushToRemote(PushToRemoteEvent event)
   {
      getWorkDir();
   }

   /**
    * Push changes to remote repository.
    */
   public void doPush()
   {
      if (workDir == null)
         return;

      final String remote = display.getRemoteValue().getValue();
      String localBranch = display.getLocalBranchesValue().getValue();
      String remoteBranch = display.getRemoteBranchesValue().getValue();

      GitClientService.getInstance().push(workDir, new String[]{localBranch + ":" + remoteBranch}, remote, false,
         new AsyncRequestCallback<String>()
         {

            @Override
            protected void onSuccess(String result)
            {
               eventBus.fireEvent(new OutputEvent(Messages.PUSH_SUCCESS + "<b>" + remote + "</b>", Type.INFO));
            }

            @Override
            protected void onFailure(Throwable exception)
            {
               String errorMessage = (exception.getMessage() != null) ? exception.getMessage() : Messages.PUSH_FAIL;
               eventBus.fireEvent(new OutputEvent(errorMessage, Type.ERROR));
            }
         });
      IDE.getInstance().closeView(display.asView().getId());
   }

   /**
    * @see org.exoplatform.ide.git.client.remote.HasBranchesPresenter#onRemotesReceived(java.util.List)
    */
   @Override
   public void onRemotesReceived(List<Remote> remotes)
   {
      Display d = GWT.create(Display.class);
      IDE.getInstance().openView(d.asView());
      bindDisplay(d);
      display.enablePushButton(false);
      LinkedHashMap<String, String> remoteValues = new LinkedHashMap<String, String>();
      for (Remote remote : remotes)
      {
         remoteValues.put(remote.getUrl(), remote.getName());
      }

      display.setRemoteValues(remoteValues);

      getBranches(workDir, false);
      getBranches(workDir, true);
   }

   /**
    * @see org.exoplatform.ide.git.client.remote.HasBranchesPresenter#setRemoteBranches(java.util.List)
    */
   @Override
   protected void setRemoteBranches(List<Branch> result)
   {
      display.setRemoteBranches(getRemoteBranchesToDisplay(display.getRemoteDisplayValue()));
   }

   /**
    * @see org.exoplatform.ide.git.client.remote.HasBranchesPresenter#setLocalBranches(java.lang.String[])
    */
   @Override
   protected void setLocalBranches(List<Branch> branches)
   {
      String[] values = new String[branches.size()];
      for (int i = 0; i < branches.size(); i++)
      {
         values[i] = branches.get(i).getName();
      }
      display.setLocalBranches(values);
   }
}
