/*
 * Copyright (C) 2011 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.ide.editor.client.api.event;

import com.google.gwt.event.shared.GwtEvent;

import org.exoplatform.ide.editor.client.api.Editor;

/**
 * Fires just after opened in editor content had been changed. Created by The eXo Platform SAS .
 *
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version @version $Id: $
 */

public class EditorContentChangedEvent extends GwtEvent<EditorContentChangedHandler> {

    public static final GwtEvent.Type<EditorContentChangedHandler> TYPE =
            new GwtEvent.Type<EditorContentChangedHandler>();

    /** {@link org.exoplatform.ide.editor.client.api.Editor} instance. */
    private Editor editor;

    /**
     * Creates new instance of {@link EditorContentChangedEvent}.
     *
     * @param editor
     */
    public EditorContentChangedEvent(Editor editor) {
        this.editor = editor;
    }

    /**
     * Returns {@link Editor} instance.
     *
     * @return
     */
    public Editor getEditor() {
        return editor;
    }

    @Override
    protected void dispatch(EditorContentChangedHandler handler) {
        handler.onEditorContentChanged(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<EditorContentChangedHandler> getAssociatedType() {
        return TYPE;
    }

}
