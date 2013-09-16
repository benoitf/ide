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
package org.exoplatform.ide.client.edit.event;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Generated by pressing on Lock/Unlock button on toolbar.
 * <p/>
 * Listening this event, IDE locks or unlocks active file.
 * <p/>
 *
 * @author <a href="mailto:oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id:
 */
public class LockFileEvent extends GwtEvent<LockFileHandler> {

    public static final GwtEvent.Type<LockFileHandler> TYPE = new GwtEvent.Type<LockFileHandler>();

    private boolean lockFile;

    /**
     * Creates new event to lock or unlock file.
     *
     * @param lockFile
     *         - if true - lock file, if false - unlock file
     */
    public LockFileEvent(boolean lockFile) {
        this.lockFile = lockFile;
    }

    @Override
    protected void dispatch(LockFileHandler handler) {
        handler.onLockFile(this);
    }

    @Override
    public com.google.gwt.event.shared.GwtEvent.Type<LockFileHandler> getAssociatedType() {
        return TYPE;
    }

    /**
     * Is lock or unlock file.
     *
     * @return if true - file must be locked, false - file must be unlocked
     */
    public boolean isLockFile() {
        return lockFile;
    }

}
