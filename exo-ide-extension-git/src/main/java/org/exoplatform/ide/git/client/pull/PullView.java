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
package org.exoplatform.ide.git.client.pull;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

import org.exoplatform.gwtframework.ui.client.component.ComboBoxField;
import org.exoplatform.gwtframework.ui.client.component.IButton;
import org.exoplatform.gwtframework.ui.client.component.SelectItem;
import org.exoplatform.gwtframework.ui.client.component.TitleOrientation;
import org.exoplatform.ide.client.framework.ui.impl.ViewImpl;
import org.exoplatform.ide.git.client.GitClientBundle;

import java.util.LinkedHashMap;

/**
 * View for pulling changes from remote repository to local one.
 * Point view in Views.gwt.xml.
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:  Apr 20, 2011 4:23:02 PM anya $
 *
 */
public class PullView extends ViewImpl implements PullPresenter.Display
{
   public static final int HEIGHT = 225;

   public static final int WIDTH = 510;

   public static final String ID = "idePullView";

   public static final String TYPE = "modal";

   public static final String TITLE = "Pull from remote repository";

   private static final int BUTTON_HEIGHT = 22;

   private static final int BUTTON_WIDTH = 90;

   private static final String PULL_BUTTON_ID = "idePullViewPullButton";

   private static final String CANCEL_BUTTON_ID = "idePullViewCancelButton";

   private static final String REMOTE_FIELD_ID = "idePullViewRemoteField";

   private static final String REMOTE_BRANCHES_FIELD_ID = "idePullViewRemoteBranchesField";

   private static final String LOCAL_BRANCHES_FIELD_ID = "idePullViewLocalBranchesField";

   /*Elements titles*/

   private static final String PULL_BUTTON_TITLE = "Pull";

   private static final String CANCEL_BUTTON_TITLE = "Cancel";

   private static final String REMOTE_FIELD_TITLE = "Choose remote repository :";

   private static final String REMOTE_BRANCHES_TITLE = "Pull from remote branch:";

   private static final String LOCAL_BRANCHES_TITLE = "To local branch:";

   /**
    * Pull button.
    */
   private IButton pullButton;

   /**
    * Cancel button.
    */
   private IButton cancelButton;

   /**
    * Remote repository field.
    */
   private SelectItem remoteField;

   /**
    * Local branches field
    */
   private ComboBoxField localBranchesField;

   /**
    * Remote branches field.
    */
   private ComboBoxField remoteBranchesField;

   public PullView()
   {
      super(ID, TYPE, TITLE, null, WIDTH, HEIGHT);

      VerticalPanel mainLayout = new VerticalPanel();
      mainLayout.setWidth("100%");
      mainLayout.setHeight("100%");
      mainLayout.setSpacing(10);

      remoteField = new SelectItem(REMOTE_FIELD_ID, REMOTE_FIELD_TITLE);
      remoteField.setWidth(280);
      mainLayout.add(remoteField);
      mainLayout.setCellVerticalAlignment(remoteField, HasVerticalAlignment.ALIGN_MIDDLE);

      addRefsLayout(mainLayout);

      addButtonsLayout(mainLayout);

      add(mainLayout);
   }

   /**
    * Add buttons to the pointed panel.
    * 
    * @param panel
    */
   private void addButtonsLayout(VerticalPanel panel)
   {
      HorizontalPanel buttonsLayout = new HorizontalPanel();
      buttonsLayout.setHeight(BUTTON_HEIGHT + 20 + "px");
      buttonsLayout.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
      buttonsLayout.setSpacing(5);

      pullButton =
         createButton(PULL_BUTTON_ID, PULL_BUTTON_TITLE, GitClientBundle.INSTANCE.ok(),
            GitClientBundle.INSTANCE.okDisabled());
      cancelButton =
         createButton(CANCEL_BUTTON_ID, CANCEL_BUTTON_TITLE, GitClientBundle.INSTANCE.cancel(),
            GitClientBundle.INSTANCE.cancelDisabled());

      buttonsLayout.add(pullButton);
      buttonsLayout.add(cancelButton);

      panel.add(buttonsLayout);
      panel.setCellHorizontalAlignment(buttonsLayout, HasHorizontalAlignment.ALIGN_CENTER);
   }

   /**
    * Creates button.
    * 
    * @param id button's id
    * @param title button's title
    * @param icon button's normal icon
    * @param disabledIcon button's icon in disabled state
    * @return {@link IButton}
    */
   private IButton createButton(String id, String title, ImageResource icon, ImageResource disabledIcon)
   {
      IButton button = new IButton(title);
      button.setID(id);
      button.setIcon(icon.getURL(), disabledIcon.getURL());
      button.setHeight(BUTTON_HEIGHT);
      button.setWidth(BUTTON_WIDTH);
      return button;
   }

   /**
    * Create the layout for displaying refs (local and remote ones).
    * 
    * @param panel parent panel for refs layout 
    */
   private void addRefsLayout(VerticalPanel panel)
   {
      HorizontalPanel refsLayout = new HorizontalPanel();
      refsLayout.setWidth("100%");
      refsLayout.setSpacing(3);

      localBranchesField = createComboBoxField(LOCAL_BRANCHES_FIELD_ID, LOCAL_BRANCHES_TITLE);

      Image arrow = new Image(GitClientBundle.INSTANCE.arrow());
      arrow.setWidth("16px");
      arrow.setHeight("16px");
      DOM.setStyleAttribute(arrow.getElement(), "marginTop", "15px");

      remoteBranchesField = createComboBoxField(REMOTE_BRANCHES_FIELD_ID, REMOTE_BRANCHES_TITLE);

      refsLayout.add(remoteBranchesField);
      refsLayout.add(arrow);
      refsLayout.setCellVerticalAlignment(arrow, HasVerticalAlignment.ALIGN_MIDDLE);
      refsLayout.add(localBranchesField);

      panel.add(refsLayout);
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#getPullButton()
    */
   @Override
   public HasClickHandlers getPullButton()
   {
      return pullButton;
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#getCancelButton()
    */
   @Override
   public HasClickHandlers getCancelButton()
   {
      return cancelButton;
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#getRemoteName()
    */
   @Override
   public HasValue<String> getRemoteName()
   {
      return remoteField;
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#getRemoteBranches()
    */
   @Override
   public HasValue<String> getRemoteBranches()
   {
      return remoteBranchesField;
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#getLocalBranches()
    */
   @Override
   public HasValue<String> getLocalBranches()
   {
      return localBranchesField;
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#setRemoteBranches(java.lang.String[])
    */
   @Override
   public void setRemoteBranches(String[] values)
   {
      remoteBranchesField.setValueMap(values);

      if (values != null && values.length > 0)
      {
         remoteBranchesField.setValue(values[0]);
      }
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#setLocalBranches(java.lang.String[])
    */
   @Override
   public void setLocalBranches(String[] values)
   {
      localBranchesField.setValueMap(values);
      if (values != null && values.length > 0)
      {
         localBranchesField.setValue(values[0]);
      }
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#enablePullButton(boolean)
    */
   @Override
   public void enablePullButton(boolean enable)
   {
      pullButton.setEnabled(enable);
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#setRemoteValues(java.util.LinkedHashMap)
    */
   @Override
   public void setRemoteValues(LinkedHashMap<String, String> values)
   {
      remoteField.setValueMap(values);
   }

   /**
    * @see org.exoplatform.ide.git.client.pull.PullPresenter.Display#getRemoteDisplayValue()
    */
   @Override
   public String getRemoteDisplayValue()
   {
      return remoteField.getDisplayValue();
   }

   /**
    * Creates combobox field.
    * 
    * @param id element's id
    * @param title element's title
    * @return {@link ComboBoxField} created combobox
    */
   private ComboBoxField createComboBoxField(String id, String title)
   {
      ComboBoxField combobox = new ComboBoxField();
      combobox.setTitleOrientation(TitleOrientation.TOP);
      combobox.setShowTitle(true);
      combobox.setTitle(title);
      combobox.setName(id);
      combobox.setWidth(210);
      combobox.setHeight(18);
      combobox.setPickListHeight(100);
      return combobox;
   }
}
