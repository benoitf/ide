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
package com.codenvy.ide.api.resources;

import com.codenvy.api.project.shared.dto.ItemReference;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Event that describes the fact that file is going to be opened.
 *
 * @author Nikolay Zamosenchuk
 */
public class FileEvent extends GwtEvent<FileEventHandler> {

    public static Type<FileEventHandler> TYPE = new Type<>();
    private ItemReference file;
    private FileOperation fileOperation;

    /**
     * Creates new {@link FileEvent}.
     *
     * @param file
     *         {@link ItemReference} that represents an affected file
     * @param fileOperation
     *         file operation
     */
    public FileEvent(ItemReference file, FileOperation fileOperation) {
        this.fileOperation = fileOperation;
        this.file = file;
    }

    /** {@inheritDoc} */
    @Override
    public Type<FileEventHandler> getAssociatedType() {
        return TYPE;
    }

    /** @return {@link ItemReference} that represents an affected file */
    public ItemReference getFile() {
        return file;
    }

    /** @return the type of operation performed with file */
    public FileOperation getOperationType() {
        return fileOperation;
    }

    @Override
    protected void dispatch(FileEventHandler handler) {
        handler.onFileOperation(this);
    }

    public static enum FileOperation {
        OPEN, SAVE, CLOSE
    }
}
