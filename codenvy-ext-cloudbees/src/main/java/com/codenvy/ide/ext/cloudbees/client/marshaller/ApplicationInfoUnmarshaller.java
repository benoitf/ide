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
package com.codenvy.ide.ext.cloudbees.client.marshaller;

import com.codenvy.ide.commons.exception.UnmarshallerException;
import com.codenvy.ide.ext.cloudbees.dto.client.DtoClientImpls;
import com.codenvy.ide.ext.cloudbees.shared.ApplicationInfo;
import com.codenvy.ide.rest.Unmarshallable;
import com.google.gwt.http.client.Response;

/** @author <a href="mailto:aplotnikov@codenvy.com">Andrey Plotnikov</a> */
public class ApplicationInfoUnmarshaller implements Unmarshallable<ApplicationInfo> {
    private DtoClientImpls.ApplicationInfoImpl applicationInfo;

    /** {@inheritDoc} */
    @Override
    public void unmarshal(Response response) throws UnmarshallerException {
        String text = response.getText();

        if (text == null || text.isEmpty()) {
            return;
        }

        applicationInfo = DtoClientImpls.ApplicationInfoImpl.deserialize(text);
    }

    /** {@inheritDoc} */
    @Override
    public ApplicationInfo getPayload() {
        return applicationInfo;
    }
}