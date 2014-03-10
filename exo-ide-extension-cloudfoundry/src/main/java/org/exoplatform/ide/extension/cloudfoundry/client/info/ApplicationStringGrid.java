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
package org.exoplatform.ide.extension.cloudfoundry.client.info;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.user.cellview.client.Column;

import org.exoplatform.gwtframework.ui.client.component.ListGrid;

/**
 * Grid for displaying application information.
 *
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id:  Jun 1, 2011 3:02:57 PM anya $
 */
public class ApplicationStringGrid extends ListGrid<String> {
    Column<String, String> valueColumn = new Column<String, String>(new TextCell()) {
        @Override
        public String getValue(String value) {
            return value;
        }
    };

    public ApplicationStringGrid() {
        super();
    }

    /**
     * Add one column of list grid.
     * This two actions moved to separate method, because
     * there is need to set the header of column.
     *
     * @param header
     */
    public void addColumn(String header) {
        getCellTable().addColumn(valueColumn, header);
        getCellTable().setColumnWidth(valueColumn, "100%");
    }
}