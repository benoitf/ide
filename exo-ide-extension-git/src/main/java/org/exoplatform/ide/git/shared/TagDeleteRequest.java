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
package org.exoplatform.ide.git.shared;

/**
 * Request to delete named tag.
 * 
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: TagDeleteRequest.java 22811 2011-03-22 07:28:35Z andrew00x $
 */
public class TagDeleteRequest extends GitRequest {
    /** Tag to delete. */
    private String name;

    /**
     * @param name name of tag to delete
     */
    public TagDeleteRequest(String name) {
        this.name = name;
    }

    public TagDeleteRequest() {
    }

    /** @return name of tag to delete */
    public String getName() {
        return name;
    }

    /**
     * @param name name of tag to delete
     */
    public void setName(String name) {
        this.name = name;
    }
}