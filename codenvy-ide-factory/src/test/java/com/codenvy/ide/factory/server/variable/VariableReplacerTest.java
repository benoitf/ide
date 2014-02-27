package com.codenvy.ide.factory.server.variable;


import com.codenvy.api.factory.dto.Replacement;
import com.codenvy.api.factory.dto.Variable;
import com.codenvy.commons.lang.IoUtil;
import com.codenvy.commons.lang.ZipUtils;
import com.codenvy.dto.server.DtoFactory;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.util.*;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

/**
 * @author vzhukovskii@codenvy.com
 */
public class VariableReplacerTest {

    private Path sourceProjectUnpacked;
    private Path resultProjectUnpacked;

    Map<String, String> sourceFileContents = new HashMap<>();
    Map<String, String> resultFileContents = new HashMap<>();

    private static final String TEXT_MULTIPASS_MODE      = "text_multipass";
    private static final String VARIABLE_SINGLEPASS_MODE = "variable_singlepass";

    @BeforeTest
    public void setUpTest() throws Exception {
        URI testRootPath = Thread.currentThread().getContextClassLoader().getResource(".").toURI();
        sourceProjectUnpacked = Files.createTempDirectory(Paths.get(testRootPath), "VariableReplacerTest_", new FileAttribute[]{});
        resultProjectUnpacked = Files.createTempDirectory(Paths.get(testRootPath), "VariableReplacerTest_", new FileAttribute[]{});

        Path sourceProjectZip = Paths.get(testRootPath.getPath(), "sourceProject.zip");
        Path resultProjectZip = Paths.get(testRootPath.getPath(), "resultProject.zip");

        //be sure that our archive exists
        assertNotNull(sourceProjectZip);
        assertNotNull(resultProjectZip);
        assertEquals(sourceProjectZip.toFile().exists(), true);
        assertEquals(sourceProjectZip.toFile().isFile(), true);
        assertEquals(resultProjectZip.toFile().exists(), true);
        assertEquals(resultProjectZip.toFile().isFile(), true);

        ZipUtils.unzip(sourceProjectZip.toFile(), sourceProjectUnpacked.toFile());
        ZipUtils.unzip(resultProjectZip.toFile(), resultProjectUnpacked.toFile());

        Files.walkFileTree(resultProjectUnpacked, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                resultFileContents.put(file.toFile().getName(), IoUtil.readAndCloseQuietly(new FileInputStream(file.toFile())));
                return FileVisitResult.CONTINUE;
            }
        });
    }

    @AfterTest
    public void tearDownTest() throws Exception {
        IoUtil.deleteRecursive(sourceProjectUnpacked.toFile());
        IoUtil.deleteRecursive(resultProjectUnpacked.toFile());
        sourceFileContents.clear();
        resultFileContents.clear();
    }

    @Test
    public void shouldReplaceVariables() throws Exception {
        Variable v1 = DtoFactory.getInstance().createDto(Variable.class);
        List<Replacement> r1Entries = new ArrayList<>(4);
        r1Entries.add(getReplacement("GROUP_ID", "Codenvy"));
        r1Entries.add(getReplacement("ARTIFACT_ID", "com.codenvy.factory.sample"));
        r1Entries.add(getReplacement("PROJECT_VERSION", "0.1-SNAPSHOT"));
        r1Entries.add(getReplacement("THIS IS TEST", "REPLACED", TEXT_MULTIPASS_MODE));
        v1.setFiles(Arrays.asList("{pom.xm*,test.*}", "test_file.txt"));
        v1.setEntries(r1Entries);

        Variable v2 = DtoFactory.getInstance().createDto(Variable.class);
        List<Replacement> r2Entries = new ArrayList<>(2);
        r2Entries.add(getReplacement("REPLACE_ME", "REPLACED_JAVA", VARIABLE_SINGLEPASS_MODE));
        r2Entries.add(getReplacement("12345", "9876", TEXT_MULTIPASS_MODE));
        v2.setFiles(Arrays.asList("src/main/java/helloworld/GreetingController.java"));
        v2.setEntries(r2Entries);

        Variable v3 = DtoFactory.getInstance().createDto(Variable.class);
        List<Replacement> r3Entries = new ArrayList<>(1);
        r3Entries.add(getReplacement("factoryUrlRepo", "this_is_replaced", TEXT_MULTIPASS_MODE));
        v3.setFiles(Arrays.asList("README*[1-2].md"));
        v3.setEntries(r3Entries);

        new VariableReplacer(sourceProjectUnpacked).performReplacement(Arrays.asList(v1, v2, v3));

        Files.walkFileTree(sourceProjectUnpacked, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                sourceFileContents.put(file.toFile().getName(), IoUtil.readAndCloseQuietly(new FileInputStream(file.toFile())));
                return FileVisitResult.CONTINUE;
            }
        });

        assertEquals(sourceFileContents.equals(resultFileContents), true);
    }

    private Replacement getReplacement(String find, String replace, String... replaceMode) {
        Replacement replacement = DtoFactory.getInstance().createDto(Replacement.class);
        replacement.setFind(find);
        replacement.setReplace(replace);
        replacement.setReplacemode(replaceMode.length > 0 ? replaceMode[0] : VARIABLE_SINGLEPASS_MODE);

        return replacement;
    }

}
