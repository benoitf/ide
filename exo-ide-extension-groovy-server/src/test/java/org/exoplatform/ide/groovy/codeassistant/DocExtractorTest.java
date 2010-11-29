/*
 * Copyright (C) 2003-2010 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ide.groovy.codeassistant;

import org.codehaus.groovy.groovydoc.GroovyClassDoc;
import org.codehaus.groovy.groovydoc.GroovyMethodDoc;
import org.codehaus.groovy.groovydoc.GroovyRootDoc;
import org.exoplatform.ide.groovy.codeassistant.extractors.DocExtractor;

import groovyjarjarantlr.RecognitionException;
import groovyjarjarantlr.TokenStreamException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

/**
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:vitaly.parfonov@gmail.com">Vitaly Parfonov</a>
 * @version $Id: $
*/
public class DocExtractorTest extends TestCase
{

   private String javaHome;

   private static final String UUID_RANDOMUUID_COMENT =
      "* Static factory to retrieve a type 4 (pseudo randomly generated) UUID.";

   @Override
   public void setUp() throws Exception
   {
      super.setUp();
      javaHome = System.getProperty("java.home");
      String fileSeparator = System.getProperty("file.separator");
      javaHome = javaHome.substring(0, javaHome.lastIndexOf(fileSeparator) + 1) + "src.zip";
   }

   public void testExtractDocFromJavaSource() throws RecognitionException, TokenStreamException, IOException
   {
      Map<String, GroovyRootDoc> roots = DocExtractor.extract(javaHome, "java/util");
      Set<String> set = roots.keySet();
      assertTrue(roots.containsKey("java.util"));
      GroovyRootDoc doc = roots.get("java.util");
      assertNotNull(doc);
      GroovyClassDoc classDoc = getClassDoc(doc, "UUID");
      assertNotNull(classDoc);
      GroovyMethodDoc methodDoc = getMethodDoc(classDoc, "randomUUID");
      assertNotNull(methodDoc);
      assertTrue(methodDoc.getRawCommentText().contains(UUID_RANDOMUUID_COMENT));
   }

   private GroovyClassDoc getClassDoc(GroovyRootDoc doc, String className)
   {
      GroovyClassDoc[] classDocs = doc.classes();
      for (GroovyClassDoc classDoc : classDocs)
      {
         if (classDoc.name().equals(className))
         {
            return classDoc;
         }
      }
      return null;
   }

   private GroovyMethodDoc getMethodDoc(GroovyClassDoc doc, String method)
   {
      GroovyMethodDoc[] docs = doc.methods();
      for (GroovyMethodDoc methodDoc : docs)
      {
         if (methodDoc.name().equals(method))
         {
            return methodDoc;
         }
      }
      return null;
   }

}
