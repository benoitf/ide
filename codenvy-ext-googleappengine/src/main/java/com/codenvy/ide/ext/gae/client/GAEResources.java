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
package com.codenvy.ide.ext.gae.client;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * GAE client resources.
 *
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: May 16, 2012 5:43:05 PM anya $
 */
public interface GAEResources extends ClientBundle {
    @Source("com/codenvy/ide/ext/gae/images/gae.png")
    ImageResource googleAppEngine();

    @Source("com/codenvy/ide/ext/gae/images/gae_logo.png")
    ImageResource googleAppEngineLogo();

    @Source("com/codenvy/ide/ext/gae/images/gae_Disabled.png")
    ImageResource googleAppEngineDisabled();

    @Source("com/codenvy/ide/ext/gae/images/gae_48.png")
    ImageResource googleAppEngine48();

    @Source("com/codenvy/ide/ext/gae/images/gae_48_Disabled.png")
    ImageResource googleAppEngine48Disabled();

    @Source("com/codenvy/ide/ext/gae/images/create_app.png")
    ImageResource createApplicationConrtol();

    @Source("com/codenvy/ide/ext/gae/images/create_app_Disabled.png")
    ImageResource createApplicationConrtolDisabled();

    @Source("com/codenvy/ide/ext/gae/images/general.png")
    ImageResource general();

    @Source("com/codenvy/ide/ext/gae/images/get_logs.png")
    ImageResource getLogs();

    @Source("com/codenvy/ide/ext/gae/images/get_logs_Disabled.png")
    ImageResource getLogsDisabled();

    @Source("com/codenvy/ide/ext/gae/images/logs.png")
    ImageResource logs();

    @Source("com/codenvy/ide/ext/gae/images/rollback_application.png")
    ImageResource rollbackApplication();

    @Source("com/codenvy/ide/ext/gae/images/rollback_application_Disabled.png")
    ImageResource rollbackApplicationDisabled();

    @Source("com/codenvy/ide/ext/gae/images/update_application.png")
    ImageResource updateApplication();

    @Source("com/codenvy/ide/ext/gae/images/update_application_Disabled.png")
    ImageResource updateApplicationDisabled();

    @Source("com/codenvy/ide/ext/gae/images/crons.png")
    ImageResource crons();

    @Source("com/codenvy/ide/ext/gae/images/backends.png")
    ImageResource backends();

    @Source("com/codenvy/ide/ext/gae/images/dos.png")
    ImageResource updateDos();

    @Source("com/codenvy/ide/ext/gae/images/dos_Disabled.png")
    ImageResource updateDosDisabled();

    @Source("com/codenvy/ide/ext/gae/images/update_indexes.png")
    ImageResource updateIndexes();

    @Source("com/codenvy/ide/ext/gae/images/update_indexes_Disabled.png")
    ImageResource updateIndexesDisabled();

    @Source("com/codenvy/ide/ext/gae/images/vacuum_indexes.png")
    ImageResource vacuumIndexes();

    @Source("com/codenvy/ide/ext/gae/images/vacuum_indexes_Disabled.png")
    ImageResource vacuumIndexesDisabled();

    @Source("com/codenvy/ide/ext/gae/images/update_pagespeed.png")
    ImageResource updatePagespeed();

    @Source("com/codenvy/ide/ext/gae/images/update_pagespeed_Disabled.png")
    ImageResource updatePagespeedDisabled();

    @Source("com/codenvy/ide/ext/gae/images/update_queues.png")
    ImageResource updateQueues();

    @Source("com/codenvy/ide/ext/gae/images/update_queues_Disabled.png")
    ImageResource updateQueuesDisabled();

    @Source("com/codenvy/ide/ext/gae/images/update.png")
    ImageResource update();

    @Source("com/codenvy/ide/ext/gae/images/update_Disabled.png")
    ImageResource updateDisabled();

    @Source("com/codenvy/ide/ext/gae/images/start.png")
    ImageResource start();

    @Source("com/codenvy/ide/ext/gae/images/start_Disabled.png")
    ImageResource startDisabled();

    @Source("com/codenvy/ide/ext/gae/images/stop.png")
    ImageResource stop();

    @Source("com/codenvy/ide/ext/gae/images/stop_Disabled.png")
    ImageResource stopDisabled();

    @Source("com/codenvy/ide/ext/gae/images/remove.png")
    ImageResource remove();

    @Source("com/codenvy/ide/ext/gae/images/remove_Disabled.png")
    ImageResource removeDisabled();

    @Source("com/codenvy/ide/ext/gae/images/configure.png")
    ImageResource configure();

    @Source("com/codenvy/ide/ext/gae/images/configure_Disabled.png")
    ImageResource configureDisabled();

    @Source("com/codenvy/ide/ext/gae/images/rollback.png")
    ImageResource rollback();

    @Source("com/codenvy/ide/ext/gae/images/rollback_Disabled.png")
    ImageResource rollbackDisabled();

    @Source("com/codenvy/ide/ext/gae/images/rollback_all.png")
    ImageResource rollbackAll();

    @Source("com/codenvy/ide/ext/gae/images/rollback_all_Disabled.png")
    ImageResource rollbackAllDisabled();

    @Source("com/codenvy/ide/ext/gae/images/resource_limits.png")
    ImageResource resourceLimits();

    @Source("com/codenvy/ide/ext/gae/images/resource_limits_Disabled.png")
    ImageResource resourceLimitsDisabled();

    @Source("com/codenvy/ide/ext/gae/images/login.png")
    ImageResource login();

    @Source("com/codenvy/ide/ext/gae/images/login_Disabled.png")
    ImageResource loginDisabled();

    @Source("com/codenvy/ide/ext/gae/images/logout.png")
    ImageResource logout();

    @Source("com/codenvy/ide/ext/gae/images/logout_Disabled.png")
    ImageResource logoutDisabled();
}