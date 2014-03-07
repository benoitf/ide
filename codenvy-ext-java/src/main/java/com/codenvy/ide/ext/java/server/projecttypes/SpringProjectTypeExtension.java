/*
 * CODENVY CONFIDENTIAL
 * __________________
 *
 *  [2012] - [2014] Codenvy, S.A.
 *  All Rights Reserved.
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
package com.codenvy.ide.ext.java.server.projecttypes;

import com.codenvy.api.project.server.ProjectTypeDescriptionRegistry;
import com.codenvy.api.project.server.ProjectTypeExtension;
import com.codenvy.api.project.shared.Attribute;
import com.codenvy.api.project.shared.ProjectTemplateDescription;
import com.codenvy.api.project.shared.ProjectType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author Artem Zatsarynnyy */
@Singleton
public class SpringProjectTypeExtension implements ProjectTypeExtension {

    private Map<String, String> icons = new HashMap<>();

    @Inject
    public SpringProjectTypeExtension(ProjectTypeDescriptionRegistry registry) {
        icons.put("spring.projecttype.big.icon", "java-extension/Spring-Logo.png");
        icons.put("spring.projecttype.small.icon", "java-extension/Spring-Logo.png");
        icons.put("spring.folder.small.icon", "java-extension/package.gif");
        icons.put("spring/java.file.small.icon", "java-extension/java-class.png");
        icons.put("java.class", "java-extension/java-class.png");
        icons.put("java.package", "java-extension/package.gif");
        registry.registerProjectType(this);
    }

    @Override
    public ProjectType getProjectType() {
        return new ProjectType("spring", "Spring Application");
    }

    @Override
    public List<Attribute> getPredefinedAttributes() {
        final List<Attribute> list = new ArrayList<>(3);
        list.add(new Attribute("language", "java"));
        list.add(new Attribute("framework", "spring"));
        list.add(new Attribute("runner.name", "webapps"));
        //TODO
        list.add(new Attribute("builder.name","maven"));
        return list;
    }

    @Override
    public List<ProjectTemplateDescription> getTemplates() {
        final List<ProjectTemplateDescription> list = new ArrayList<>(2);
        list.add(new ProjectTemplateDescription("zip",
                                                "MAVEN SPRING APPLICATION",
                                                "Simple Spring project which uses Maven build system.",
                                                "templates/MavenSpring.zip"));

//TODO:temporary unregist Ant project
//        list.add(new ProjectTemplateDescription("zip",
//                                                "ANT SPRING APPLICATION",
//                                                "Simple Spring project which uses Ant build system.",
//                                                "templates/AntSpring.zip"));
        return list;
    }

    @Override
    public Map<String, String> getIconRegistry() {
        return icons;
    }


}
