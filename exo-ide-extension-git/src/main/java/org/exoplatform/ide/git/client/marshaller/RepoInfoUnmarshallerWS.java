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
package org.exoplatform.ide.git.client.marshaller;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;

import org.exoplatform.gwtframework.commons.exception.UnmarshallerException;
import org.exoplatform.ide.client.framework.websocket.rest.ResponseMessage;
import org.exoplatform.ide.client.framework.websocket.rest.Unmarshallable;
import org.exoplatform.ide.git.shared.RepoInfo;

/**
 * @author <a href="mailto:azatsarynnyy@exoplatfrom.com">Artem Zatsarynnyy</a>
 * @version $Id: RepoInfoUnmarshallerWS.java Nov 21, 2012 3:02:52 PM azatsarynnyy $
 */
public class RepoInfoUnmarshallerWS implements Unmarshallable<RepoInfo> {
    private final RepoInfo repoInfo;

    public RepoInfoUnmarshallerWS(RepoInfo repoInfo) {
        this.repoInfo = repoInfo;
    }

    @Override
    public void unmarshal(ResponseMessage response) throws UnmarshallerException {
        JSONObject jsonObject = JSONParser.parseLenient(response.getBody()).isObject();
        JSONString jsonString = jsonObject.get("remoteUri").isString();
        if (jsonString != null) {
            repoInfo.setRemoteUri(jsonString.stringValue());
        }

    }

    @Override
    public RepoInfo getPayload() {
        return repoInfo;
    }

}
