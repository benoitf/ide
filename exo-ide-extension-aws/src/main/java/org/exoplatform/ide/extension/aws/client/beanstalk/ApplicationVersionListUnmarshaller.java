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
package org.exoplatform.ide.extension.aws.client.beanstalk;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import org.exoplatform.gwtframework.commons.exception.UnmarshallerException;
import org.exoplatform.gwtframework.commons.rest.Unmarshallable;
import org.exoplatform.ide.extension.aws.client.AWSExtension;
import org.exoplatform.ide.extension.aws.shared.beanstalk.ApplicationVersionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:azhuleva@exoplatform.com">Ann Shumilova</a>
 * @version $Id: Sep 18, 2012 10:33:01 AM anya $
 */
public class ApplicationVersionListUnmarshaller implements Unmarshallable<List<ApplicationVersionInfo>> {
    private List<ApplicationVersionInfo> versionsList = new ArrayList<ApplicationVersionInfo>();

    /** @see org.exoplatform.gwtframework.commons.rest.Unmarshallable#unmarshal(com.google.gwt.http.client.Response) */
    @Override
    public void unmarshal(Response response) throws UnmarshallerException {
        if (response.getText() == null || response.getText().isEmpty()) {
            return;
        }

        JSONArray value = JSONParser.parseLenient(response.getText()).isArray();

        if (value == null) {
            return;
        }

        for (int i = 0; i < value.size(); i++) {
            String payload = value.get(i).isObject().toString();

            AutoBean<ApplicationVersionInfo> applicationVersionInfoBean =
                    AutoBeanCodex.decode(AWSExtension.AUTO_BEAN_FACTORY, ApplicationVersionInfo.class, payload);
            versionsList.add(applicationVersionInfoBean.as());
        }
    }

    /** @see org.exoplatform.gwtframework.commons.rest.Unmarshallable#getPayload() */
    @Override
    public List<ApplicationVersionInfo> getPayload() {
        return versionsList;
    }
}
