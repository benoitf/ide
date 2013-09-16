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
package org.exoplatform.ide.extension.aws.shared.s3;

/**
 * Information about S3 bucket owner
 *
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public interface S3Owner {
    /**
     * Get owner ID
     *
     * @return id of S3 bucket owner
     */
    String getId();

    /**
     * Set owner ID
     *
     * @param id
     *         id of S3 bucket owner
     */
    void setId(String id);

    /**
     * Get S3 bucket owner display name
     *
     * @return display name of S3 bucket owner
     */
    String getName();


    /**
     * Set S3 bucket owner display name
     *
     * @param name
     *         display name of S3 bucket owner
     */
    void setName(String name);
}
