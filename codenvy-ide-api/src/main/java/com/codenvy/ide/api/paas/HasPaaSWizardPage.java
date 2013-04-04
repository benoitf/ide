/*
 * Copyright (C) 2013 eXo Platform SAS.
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
package com.codenvy.ide.api.paas;

/**
 * General interface for all classes which contains PaaS's wizard page.
 *
 * @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a>
 */
public interface HasPaaSWizardPage {
    /**
     * Returns PaaS's wizard page.
     *
     * @return PaaS
     */
    public AbstractPaasWizardPagePresenter getPaaSWizardPage();

    /**
     * Sets PaaS's wizard page.
     *
     * @param paasPage
     */
    public void setPaaSWizardPage(AbstractPaasWizardPagePresenter paasPage);
}