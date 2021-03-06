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
package com.codenvy.ide.ext.git.shared;


import com.codenvy.dto.shared.DTO;

import java.util.List;

/**
 * Request to update remote refs using local refs. In other words send changes from local repository to remote one.
 *
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: PushRequest.java 22817 2011-03-22 09:17:52Z andrew00x $
 */
@DTO
public interface PushRequest extends GitRequest {
    /** @return list of refspec to push */
    List<String> getRefSpec();
    
    void setRefSpec(List<String> refSpec);
    
    PushRequest withRefSpec(List<String> refspec);

    /** @return remote repository. URI or name is acceptable. If not specified then 'origin' will be used */
    String getRemote();
    
    void setRemote(String remote);

    PushRequest withRemote(String remote);
    
    /** @return force or not push operation */
    boolean isForce();
    
    void setForce(boolean isForce);
    
    PushRequest withForce(boolean force);

    /** @return time (in seconds) to wait without data transfer occurring before aborting pushing data to remote repository */
    int getTimeout();
    
    void setTimeout(int timeout);
    
    PushRequest withTimeout(int timeout);
}