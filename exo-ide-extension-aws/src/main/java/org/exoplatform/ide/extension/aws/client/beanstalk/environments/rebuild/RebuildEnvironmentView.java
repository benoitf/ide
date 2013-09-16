/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 * [2012] - [2013] Codenvy, S.A.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Codenvy S.A. and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Codenvy S.A.
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Codenvy S.A..
 */
package org.exoplatform.ide.extension.aws.client.beanstalk.environments.rebuild;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

import org.exoplatform.gwtframework.ui.client.component.ImageButton;
import org.exoplatform.gwtframework.ui.client.component.Label;
import org.exoplatform.ide.client.framework.ui.impl.ViewImpl;
import org.exoplatform.ide.client.framework.ui.impl.ViewType;
import org.exoplatform.ide.extension.aws.client.AWSExtension;

/**
 * @author <a href="mailto:azatsarynnyy@exoplatform.com">Artem Zatsarynnyy</a>
 * @version $Id: RebuildEnvironmentView.java Sep 28, 2012 4:34:01 PM azatsarynnyy $
 */
public class RebuildEnvironmentView extends ViewImpl implements RebuildEnvironmentPresenter.Display {
    private static final String ID = "ideRebuildEnvironmentView";

    private static final int WIDTH = 460;

    private static final int HEIGHT = 170;

    private static final String REBUILD_BUTTON_ID = "ideRebuildEnvironmentViewRebuildButton";

    private static final String CANCEL_BUTTON_ID = "ideRebuildEnvironmentViewCancelButton";

    @UiField
    Label questionLabel;

    @UiField
    ImageButton rebuildButton;

    @UiField
    ImageButton cancelButton;

    private static StopEnvironmentViewUiBinder uiBinder = GWT.create(StopEnvironmentViewUiBinder.class);

    interface StopEnvironmentViewUiBinder extends UiBinder<Widget, RebuildEnvironmentView> {
    }

    public RebuildEnvironmentView() {
        super(ID, ViewType.MODAL, AWSExtension.LOCALIZATION_CONSTANT.rebuildEnvironmentViewTitle(), null, WIDTH, HEIGHT);
        add(uiBinder.createAndBindUi(this));

        questionLabel.setIsHTML(true);
        rebuildButton.setButtonId(REBUILD_BUTTON_ID);
        cancelButton.setButtonId(CANCEL_BUTTON_ID);
    }

    /** @see org.exoplatform.ide.extension.aws.client.beanstalk.versions.delete.DeleteVersionPresenter.Display#getRebuildButton() */
    @Override
    public HasClickHandlers getRebuildButton() {
        return rebuildButton;
    }

    /** @see org.exoplatform.ide.extension.aws.client.beanstalk.versions.delete.DeleteVersionPresenter.Display#getCancelButton() */
    @Override
    public HasClickHandlers getCancelButton() {
        return cancelButton;
    }

    /** @see org.exoplatform.ide.extension.aws.client.beanstalk.versions.delete.DeleteVersionPresenter.Display#getRebuildQuestion() */
    @Override
    public HasValue<String> getRebuildQuestion() {
        return questionLabel;
    }

}
