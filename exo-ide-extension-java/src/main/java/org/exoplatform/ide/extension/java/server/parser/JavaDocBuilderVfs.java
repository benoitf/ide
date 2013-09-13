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
package org.exoplatform.ide.extension.java.server.parser;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaSource;

import org.exoplatform.ide.extension.java.server.parser.scanner.FileSuffixFilter;
import org.exoplatform.ide.extension.java.server.parser.scanner.FolderScanner;
import org.exoplatform.ide.vfs.server.VirtualFileSystem;
import org.exoplatform.ide.vfs.server.exceptions.VirtualFileSystemException;
import org.exoplatform.ide.vfs.shared.Folder;
import org.exoplatform.ide.vfs.shared.Item;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version ${Id}: Nov 28, 2011 3:07:14 PM evgen $
 */
public class JavaDocBuilderVfs extends JavaDocBuilder {
    /**
     *
     */
    private static final long serialVersionUID = 2801488236934185900L;

    private VirtualFileSystem vfs;

    /** Logger. */
    private static final Log LOG = ExoLogger.getLogger(JavaDocBuilderVfs.class);

    /** @param vfs */
    public JavaDocBuilderVfs(VirtualFileSystem vfs, VfsClassLibrary library) {
        super(library);
        this.vfs = vfs;
    }

    @Override
    protected JavaClass createSourceClass(String name) {
        InputStream sourceFile = ((VfsClassLibrary)getClassLibrary()).getSourceFileContent(name);
        if (sourceFile != null) {
            try {


                JavaSource source = addSource(new InputStreamReader(sourceFile), name);
                for (int index = 0; index < source.getClasses().length; index++) {
                    JavaClass clazz = source.getClasses()[index];
                    if (name.equals(clazz.getFullyQualifiedName())) {
                        return clazz;
                    }
                }
                return source.getNestedClassByName(name);
            } catch (IndexOutOfBoundsException e) {
                LOG.error(e);
            }
        }
        return null;
    }

    public void addSourceTree(Folder folder) throws VirtualFileSystemException {
        FolderScanner scanner = new FolderScanner(folder, vfs);
        scanner.addFilter(new FileSuffixFilter(".java"));
        List<Item> list = scanner.scan();
        for (Item i : list) {
            try {
                addSource(new InputStreamReader(vfs.getContent(i.getId()).getStream()), i.getId());
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e);
                }
            }
        }
    }

    /** @see com.thoughtworks.qdox.JavaDocBuilder#getClassByName(java.lang.String) */
    @Override
    public JavaClass getClassByName(String name) {
        for (JavaClass clazz : getClasses()) {
            if (clazz.getFullyQualifiedName().equals(name)) {
                return clazz;
            }
        }
        return null;
    }
}
