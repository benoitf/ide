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
package com.codenvy.ide.ext.java.jdt.internal.corext.util;

import com.codenvy.ide.ext.java.jdt.core.dom.CompilationUnit;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id: 12:14:05 PM 34360 2009-07-22 23:58:59Z evgen $
 */
public class ModelUtil {
    public static boolean isImplicitImport(String string, CompilationUnit fCompilationUnit) {
        if ("java.lang".equals(string)) {
            return true;
        }
        return false;
    }
}
