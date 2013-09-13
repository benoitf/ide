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
package org.exoplatform.ide.extension.aws.client.ec2;

import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;

import org.exoplatform.gwtframework.commons.exception.UnmarshallerException;
import org.exoplatform.gwtframework.commons.rest.Unmarshallable;
import org.exoplatform.ide.extension.aws.client.AWSExtension;
import org.exoplatform.ide.extension.aws.shared.ec2.InstanceInfo;

import java.util.List;

/**
 * @author <a href="mailto:azatsarynnyy@exoplatform.com">Artem Zatsarynnyy</a>
 * @version $Id: InstanceListUnmarshaller.java Sep 21, 2012 4:56:02 PM azatsarynnyy $
 */
public class InstanceListUnmarshaller implements Unmarshallable<List<InstanceInfo>> {

    private List<InstanceInfo> instanceList;

    public InstanceListUnmarshaller(List<InstanceInfo> instanceList) {
        this.instanceList = instanceList;
    }

    /** @see org.exoplatform.gwtframework.commons.rest.Unmarshallable#unmarshal(com.google.gwt.http.client.Response) */
    @Override
    public void unmarshal(Response response) throws UnmarshallerException {
        try {
            if (response.getText() == null || response.getText().isEmpty()) {
                return;
            }
            JSONArray array = JSONParser.parseLenient(response.getText()).isArray();
            if (array == null) {
                return;
            }
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = array.get(i).isObject();
                String value = (jsonObject.isObject() != null) ? jsonObject.isObject().toString() : "";
                AutoBean<InstanceInfo> autoBean =
                        AutoBeanCodex.decode(AWSExtension.AUTO_BEAN_FACTORY, InstanceInfo.class, value);
                instanceList.add(autoBean.as());
            }
        } catch (Exception e) {
            throw new UnmarshallerException("Can't parse security group list." + e.getMessage());
        }
    }

    /** @see org.exoplatform.gwtframework.commons.rest.copy.Unmarshallable#getPayload() */
    @Override
    public List<InstanceInfo> getPayload() {
        return instanceList;
    }

}
