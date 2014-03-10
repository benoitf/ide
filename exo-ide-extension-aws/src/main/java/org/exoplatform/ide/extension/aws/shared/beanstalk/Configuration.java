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
package org.exoplatform.ide.extension.aws.shared.beanstalk;

import java.util.List;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public interface Configuration {
    String getSolutionStackName();

    void setSolutionStackName(String solutionStackName);

    String getApplicationName();

    void setApplicationName(String applicationName);

    String getTemplateName();

    void setTemplateName(String templateName);

    String getDescription();

    void setDescription(String description);

    String getEnvironmentName();

    void setEnvironmentName(String environmentName);

    ConfigurationTemplateDeploymentStatus getDeploymentStatus();

    void setDeploymentStatus(ConfigurationTemplateDeploymentStatus deploymentStatus);

    long getCreated();

    void setCreated(long created);

    long getUpdated();

    void setUpdated(long updated);

    List<ConfigurationOption> getOptions();

    void setOptions(List<ConfigurationOption> options);
}