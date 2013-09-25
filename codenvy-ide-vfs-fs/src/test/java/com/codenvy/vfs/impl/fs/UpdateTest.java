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
package com.codenvy.vfs.impl.fs;

import com.codenvy.api.vfs.shared.Folder;
import com.codenvy.api.vfs.shared.Item;
import com.codenvy.api.vfs.shared.ItemType;
import com.codenvy.api.vfs.shared.Principal;
import com.codenvy.api.vfs.shared.PrincipalImpl;
import com.codenvy.api.vfs.shared.Project;

import org.everrest.core.impl.ContainerResponse;
import org.everrest.core.tools.ByteArrayContainerResponseWriter;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.security.Identity;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.codenvy.api.vfs.shared.VirtualFileSystemInfo.BasicPermissions;

public class UpdateTest extends LocalFileSystemTest {
    private final String lockToken = "01234567890abcdef";

    private String fileId;
    private String filePath;

    private String folderId;
    private String folderPath;

    private String lockedFilePath;
    private String lockedFileId;

    private String protectedFileId;
    private String protectedFilePath;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        filePath = createFile(testRootPath, "UpdateTest_File", DEFAULT_CONTENT_BYTES);
        folderPath = createDirectory(testRootPath, "UpdateTest_Folder");
        lockedFilePath = createFile(testRootPath, "UpdateTest_LockedFile", DEFAULT_CONTENT_BYTES);
        protectedFilePath = createFile(testRootPath, "UpdateTest_ProtectedFile", DEFAULT_CONTENT_BYTES);

        Map<Principal, Set<BasicPermissions>> permissions = new HashMap<Principal, Set<BasicPermissions>>(2);
        permissions.put(new PrincipalImpl("andrew", Principal.Type.USER), EnumSet.of(BasicPermissions.ALL));
        permissions.put(new PrincipalImpl("admin", Principal.Type.USER), EnumSet.of(BasicPermissions.READ));

        writePermissions(protectedFilePath, permissions);
        createLock(lockedFilePath, lockToken, Long.MAX_VALUE);

        fileId = pathToId(filePath);
        lockedFileId = pathToId(lockedFilePath);
        protectedFileId = pathToId(protectedFilePath);
        folderId = pathToId(folderPath);
    }

    public void testUpdatePropertiesFile() throws Exception {
        String update = "[{\"name\":\"MyProperty\", \"value\":[\"MyValue\"]}]";

        String requestPath = SERVICE_URI + "item/" + fileId;
        Map<String, List<String>> h = new HashMap<String, List<String>>(1);
        h.put("Content-Type", Arrays.asList("application/json"));
        ContainerResponse response = launcher.service("POST", requestPath, BASE_URI, h, update.getBytes(), null);
        assertEquals("Error: " + response.getEntity(), 200, response.getStatus());

        Map<String, String[]> expectedProperties = new HashMap<String, String[]>(1);
        expectedProperties.put("MyProperty", new String[]{"MyValue"});
        validateProperties(filePath, expectedProperties);

        Item item = getItem(fileId);
        assertEquals("MyValue", item.getPropertyValue("MyProperty"));
    }

    public void testUpdatePropertiesFile2() throws Exception {
        Map<String, String[]> props = new HashMap<String, String[]>(3);
        props.put("a", new String[]{"to be or not to be"});
        props.put("b", new String[]{"hello world"});
        props.put("c", new String[]{"test"});
        writeProperties(filePath, props);

        String update = "[{\"name\":\"b\", \"value\":[\"TEST\"]}," //
                        + "{\"name\":\"c\", \"value\":[\"TEST\"]}," //
                        + "{\"name\":\"d\", \"value\":[\"TEST\", \"TEST\"]}]";

        String requestPath = SERVICE_URI + "item/" + fileId;
        Map<String, List<String>> h = new HashMap<String, List<String>>(1);
        h.put("Content-Type", Arrays.asList("application/json"));
        ContainerResponse response = launcher.service("POST", requestPath, BASE_URI, h, update.getBytes(), null);
        assertEquals("Error: " + response.getEntity(), 200, response.getStatus());

        Map<String, String[]> expectedProperties = new HashMap<String, String[]>(4);
        expectedProperties.put("a", new String[]{"to be or not to be"});
        expectedProperties.put("b", new String[]{"TEST"});
        expectedProperties.put("c", new String[]{"TEST"});
        expectedProperties.put("d", new String[]{"TEST", "TEST"});
        validateProperties(filePath, expectedProperties);

        Item item = getItem(fileId);
        assertEquals("to be or not to be", item.getPropertyValue("a")); // not updated
        assertEquals("TEST", item.getPropertyValue("b"));
        assertEquals("TEST", item.getPropertyValue("c"));
        assertEquals(Arrays.asList("TEST", "TEST"), item.getPropertyValues("d"));
    }

    public void testUpdateLockedFile() throws Exception {
        String properties = "[{\"name\":\"MyProperty\", \"value\":[\"MyValue\"]}]";

        String requestPath = SERVICE_URI + "item/" + lockedFileId + '?' + "lockToken=" + lockToken;
        Map<String, List<String>> h = new HashMap<String, List<String>>(1);
        h.put("Content-Type", Arrays.asList("application/json"));
        ContainerResponse response = launcher.service("POST", requestPath, BASE_URI, h, properties.getBytes(), null);
        assertEquals("Error: " + response.getEntity(), 200, response.getStatus());

        Map<String, String[]> expectedProperties = new HashMap<String, String[]>(1);
        expectedProperties.put("MyProperty", new String[]{"MyValue"});
        validateProperties(lockedFilePath, expectedProperties);

        Item item = getItem(lockedFileId);
        assertEquals("MyValue", item.getPropertyValue("MyProperty"));
    }

    public void testUpdateLockedFileNoLockToken() throws Exception {
        String properties = "[{\"name\":\"MyProperty\", \"value\":[\"MyValue\"]}]";

        String requestPath = SERVICE_URI + "item/" + lockedFileId;
        Map<String, List<String>> h = new HashMap<String, List<String>>(1);
        h.put("Content-Type", Arrays.asList("application/json"));
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        ContainerResponse response = launcher.service("POST", requestPath, BASE_URI, h, properties.getBytes(), writer, null);
        assertEquals(423, response.getStatus());
        log.info(new String(writer.getBody()));

        assertNull("Properties must not be updated. ", readProperties(lockedFilePath));

        Item item = getItem(lockedFileId);
        assertNull(item.getPropertyValue("MyProperty"));
    }

    public void testUpdateFileHavePermissions() throws Exception {
        String properties = "[{\"name\":\"MyProperty\", \"value\":[\"MyValue\"]}]";

        String requestPath = SERVICE_URI + "item/" + protectedFileId;
        Map<String, List<String>> h = new HashMap<String, List<String>>(1);
        h.put("Content-Type", Arrays.asList("application/json"));
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        // File is protected and default principal 'admin' has not write permission.
        // Replace default principal by principal who has write permission.
        ConversationState user = new ConversationState(new Identity("andrew"));
        ConversationState.setCurrent(user);
        ContainerResponse response = launcher.service("POST", requestPath, BASE_URI, h, properties.getBytes(), writer, null);
        assertEquals("Error: " + response.getEntity(), 200, response.getStatus());

        Map<String, String[]> expectedProperties = new HashMap<String, String[]>(1);
        expectedProperties.put("MyProperty", new String[]{"MyValue"});
        validateProperties(protectedFilePath, expectedProperties);

        Item item = getItem(protectedFileId);
        assertEquals("MyValue", item.getPropertyValue("MyProperty"));
    }

    public void testUpdateFileNoPermissions() throws Exception {
        String properties = "[{\"name\":\"MyProperty\", \"value\":[\"MyValue\"]}]";

        String requestPath = SERVICE_URI + "item/" + protectedFileId;
        Map<String, List<String>> h = new HashMap<String, List<String>>(1);
        h.put("Content-Type", Arrays.asList("application/json"));
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        ContainerResponse response = launcher.service("POST", requestPath, BASE_URI, h, properties.getBytes(), writer, null);
        assertEquals(403, response.getStatus());
        log.info(new String(writer.getBody()));

        assertNull("Properties must not be updated.", readProperties(protectedFilePath));

        Item item = getItem(protectedFileId);
        assertNull(item.getPropertyValue("MyProperty"));
    }

    public void testUpdatePropertiesAndChangeFolderType() throws Exception {
        String requestPath = SERVICE_URI + "item/" + folderId;
        String properties = "[{\"name\":\"vfs:mimeType\", \"value\":[\"text/vnd.ideproject+directory\"]}]";
        Map<String, List<String>> h = new HashMap<String, List<String>>(1);
        h.put("Content-Type", Arrays.asList("application/json"));
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        ContainerResponse response = launcher.service("POST", requestPath, BASE_URI, h, properties.getBytes(), writer, null);
        assertEquals("Error: " + response.getEntity(), 200, response.getStatus());

        Map<String, String[]> expectedProperties = new HashMap<String, String[]>(1);
        expectedProperties.put("vfs:mimeType", new String[]{Project.PROJECT_MIME_TYPE});
        validateProperties(folderPath, expectedProperties);

        Item item = getItem(folderId);
        assertEquals(ItemType.PROJECT, item.getItemType());
        assertEquals(Project.PROJECT_MIME_TYPE, item.getMimeType());
    }

    public void testUpdatePropertiesAndChangeFolderType2() throws Exception {
        Map<String, String[]> props = new HashMap<String, String[]>(1);
        props.put("vfs:mimeType", new String[]{Project.PROJECT_MIME_TYPE});
        writeProperties(folderPath, props);
        Item item = getItem(folderId);
        assertEquals(ItemType.PROJECT, item.getItemType());
        assertEquals(Project.PROJECT_MIME_TYPE, item.getMimeType());

        String requestPath = SERVICE_URI + "item/" + folderId;
        String properties = "[{\"name\":\"vfs:mimeType\", \"value\":[\"text/directory\"]}]";
        Map<String, List<String>> h = new HashMap<String, List<String>>(1);
        h.put("Content-Type", Arrays.asList("application/json"));
        ByteArrayContainerResponseWriter writer = new ByteArrayContainerResponseWriter();
        ContainerResponse response = launcher.service("POST", requestPath, BASE_URI, h, properties.getBytes(), writer, null);
        assertEquals("Error: " + response.getEntity(), 200, response.getStatus());

        Map<String, String[]> expectedProperties = new HashMap<String, String[]>(1);
        expectedProperties.put("vfs:mimeType", new String[]{Folder.FOLDER_MIME_TYPE});
        validateProperties(folderPath, expectedProperties);

        item = getItem(folderId);
        assertEquals(ItemType.FOLDER, item.getItemType());
        assertEquals(Folder.FOLDER_MIME_TYPE, item.getMimeType());

    }
}
