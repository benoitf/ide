/*
 * Copyright (C) 2012 eXo Platform SAS.
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
package org.exoplatform.ide.codeassistant.storage.lucene;

import org.exoplatform.ide.codeassistant.asm.JarParser;
import org.exoplatform.ide.codeassistant.jvm.shared.TypeInfo;
import org.exoplatform.ide.codeassistant.storage.extractors.QDoxJavaDocExtractor;
import org.exoplatform.ide.codeassistant.storage.lucene.writer.LuceneDataWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataStorageWriter
{

   private final String pathToIndex;

   public DataStorageWriter(String pathToIndex)
   {
      this.pathToIndex = pathToIndex;
   }

   public void writeBinaryJarsToIndex(List<String> jars) throws IOException, SaveDataIndexException
   {
      LuceneInfoStorage luceneInfoStorage = null;
      try
      {
         luceneInfoStorage = new LuceneInfoStorage(pathToIndex);
         LuceneDataWriter writer = new LuceneDataWriter(luceneInfoStorage);

         for (String jar : jars)
         {
            File jarFile = new File(jar);
            List<TypeInfo> typeInfos = JarParser.parse(jarFile);
            writer.addTypeInfo(typeInfos);
         }
      }
      finally
      {
         if (luceneInfoStorage != null)
         {
            luceneInfoStorage.closeIndexes();
         }
      }
   }

   public void writeSourceJarsToIndex(List<String> sourceJars) throws IOException, SaveDataIndexException
   {
      LuceneInfoStorage luceneInfoStorage = null;
      try
      {
         luceneInfoStorage = new LuceneInfoStorage(pathToIndex);
         LuceneDataWriter writer = new LuceneDataWriter(luceneInfoStorage);

         for (String jar : sourceJars)
         {
            File jarFile = new File(jar);
            Map<String, String> javaDocs = QDoxJavaDocExtractor.extractZip(new FileInputStream(jarFile));
            writer.addJavaDocs(javaDocs);
         }
      }
      finally
      {
         if (luceneInfoStorage != null)
         {
            luceneInfoStorage.closeIndexes();
         }
      }
   }

}