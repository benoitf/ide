// Copyright 2012 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.codenvy.ide;

import com.codenvy.ide.api.parts.PartStackUIResources;
import com.codenvy.ide.menu.MenuResources;
import com.codenvy.ide.notification.NotificationResources;
import com.codenvy.ide.texteditor.EditableContentArea;
import com.codenvy.ide.texteditor.TextEditorViewImpl;
import com.codenvy.ide.texteditor.renderer.LineNumberRenderer;
import com.codenvy.ide.tree.FileTreeNodeRenderer;
import com.codenvy.ide.ui.DialogBoxResources;
import com.codenvy.ide.ui.cellview.CellTableResources;
import com.codenvy.ide.ui.cellview.DataGridResources;
import com.codenvy.ide.ui.list.SimpleList;
import com.codenvy.ide.ui.tree.Tree;
import com.codenvy.ide.welcome.WelcomePageResources;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;

import org.vectomatic.dom.svg.ui.SVGResource;


/**
 * Interface for resources, e.g., css, images, text files, etc.
 * <p/>
 * Tree.Resources,
 * FileTreeNodeRenderer.Resources,
 * Editor.Resources,
 * LineNumberRenderer.Resources,
 * EditableContentArea.Resources,
 * PartStackUIResources,
 * impleList.Resources
 */
public interface Resources extends Tree.Resources, FileTreeNodeRenderer.Resources, TextEditorViewImpl.Resources,
                                   LineNumberRenderer.Resources, EditableContentArea.Resources, PartStackUIResources, SimpleList.Resources,
                                   MenuResources, DialogBoxResources, WelcomePageResources, NotificationResources , DataGridResources,
                                   CellTableResources{

    /** Interface for css resources. */
    public interface CoreCss extends CssResource {
        String simpleListContainer();

        String mainText();

        // wizard's styles
        String mainFont();

        String mainBoldFont();

        String errorFont();

        String greyFontColor();

        String cursorPointer();

        String line();
    }

    @Source({"Core.css", "com/codenvy/ide/common/constants.css", "com/codenvy/ide/api/ui/style.css"})
    @NotStrict
    CoreCss coreCss();

    @Source("tree/project_explorer.png")
    ImageResource projectExplorer();

    @Source("tree/project-closed.png")
    ImageResource projectClosed();

    @Source("wizard/new_project_icon.png")
    ImageResource newProjectIcon();

    @Source("wizard/new_project_icon.png")
    ImageResource newResourceIcon();

    @Source("wizard/new_resource_icon.png")
    ImageResource templateIcon();

    @Source("wizard/back.png")
    ImageResource back();

    @Source("wizard/next.png")
    ImageResource next();

    @Source("wizard/cancel.png")
    ImageResource cancel();

    @Source("wizard/ok.png")
    ImageResource ok();

    @Source("wizard/question.png")
    ImageResource question();

    @Source("extension/extention.png")
    ImageResource extension();

    @Source("texteditor/save-all.png")
    ImageResource saveAll();
    
    @Source("texteditor/open-list.png")
    ImageResource listOpenedEditors();

    @Source("xml/xml_file.png")
    ImageResource xmlFile();
    
    @Source("about/logo.png")
    ImageResource logo();
    
    @Source("about/logoBg.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource logoBg();
    
    @Source("console/clear.png")
    ImageResource clear();
    
    @Source("actions/close-project.svg")
    SVGResource closeProject();
    
    @Source("actions/delete.svg")
    SVGResource delete();
    
    @Source("actions/new-resource.svg")
    SVGResource newResource();
    
    @Source("actions/save.svg")
    SVGResource save();
}