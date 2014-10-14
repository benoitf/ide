/*******************************************************************************
 * Copyright (c) 2012-2014 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/

package com.codenvy.ide.extension.runner.client.wizard;

import com.codenvy.api.project.shared.dto.RunnerEnvironment;
import com.codenvy.api.project.shared.dto.RunnerEnvironmentTree;
import com.codenvy.ide.collections.Array;
import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.ui.tree.NodeDataAdapter;
import com.codenvy.ide.ui.tree.TreeNodeElement;

import java.util.HashMap;

/**
 * @author Evgen Vidolob
 */
public class RunnersDataAdapter implements NodeDataAdapter<Object> {
    private HashMap<Object, TreeNodeElement<Object>> treeNodeElements = new HashMap<>();

    @Override
    public int compare(Object a, Object b) {
        return 0;
    }

    @Override
    public boolean hasChildren(Object data) {
        if(data instanceof RunnerEnvironmentTree){
            RunnerEnvironmentTree environmentTree = (RunnerEnvironmentTree)data;
            if(!environmentTree.getChildren().isEmpty() || (environmentTree.getEnvironments() != null && !environmentTree.getEnvironments().isEmpty()))
                return true;
        }
        return false;


    }

    @Override
    public Array<Object> getChildren(Object data) {
        Array<Object> res = Collections.createArray();
        if(data instanceof RunnerEnvironmentTree){
            RunnerEnvironmentTree environmentTree = (RunnerEnvironmentTree)data;
            for (RunnerEnvironmentTree runnerEnvironmentTree : environmentTree.getChildren()) {
                res.add(runnerEnvironmentTree);
            }

            if(environmentTree.getEnvironments() != null){
                for (RunnerEnvironment environment : environmentTree.getEnvironments()) {
                    res.add(environment);
                }
            }

        }
        return res;
    }

    @Override
    public String getNodeId(Object data) {
        return null;
    }

    @Override
    public String getNodeName(Object data) {
        return null;
    }

    @Override
    public Object getParent(Object data) {
        return null;
    }

    @Override
    public TreeNodeElement<Object> getRenderedTreeNode(Object data) {
        return treeNodeElements.get(data);
    }

    @Override
    public void setNodeName(Object data, String name) {

    }

    @Override
    public void setRenderedTreeNode(Object data, TreeNodeElement<Object> renderedNode) {
        treeNodeElements.put(data, renderedNode);
    }

    @Override
    public Object getDragDropTarget(Object data) {
        return null;
    }

    @Override
    public Array<String> getNodePath(Object data) {
        return null;
    }

    @Override
    public Object getNodeByPath(Object root, Array<String> relativeNodePath) {
        return null;
    }
}