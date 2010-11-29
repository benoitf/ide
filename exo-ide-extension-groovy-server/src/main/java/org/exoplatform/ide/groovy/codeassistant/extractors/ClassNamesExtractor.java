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
package org.exoplatform.ide.groovy.codeassistant.extractors;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extracting class names from jar's or from jdk source.
 * 
 * 
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:vitaly.parfonov@gmail.com">Vitaly Parfonov</a>
 * @version $Id: $
*/

public class ClassNamesExtractor
{

   /**
    * Extract all class names from jar
    * 
    * @param jarPath the path to jar
    * @return set of canonical names 
    * @throws IOException
    */
   public static List<String> getClassesNamesInJar(String jarPath) throws IOException
   {
     return getClassesNamesInJar(jarPath, null);
   }
   
   /**
    * Extract all class names from jar in given package
    * 
    * @param jarPath the path to jar
    * @param packageName the package name for filtering class names
    * @return set of canonical names
    * @throws IOException
    */
   public static List<String> getClassesNamesInJar(String jarPath,  String packageName) throws IOException
   {
      return extract(jarPath, packageName, ".class");
   }

   /**
    * Extract all information from zip archive of java source 
    * 
    * @param javaSrcPath the path to source archive
    * @return set of canonical names
    * @throws IOException
    */
   public static List<String> getClassesNamesFromJavaSrc(String javaSrcPath) throws IOException
   {
      
      return getClassesNamesFromJavaSrc(javaSrcPath, null);
   }

   /**
    *  Extract all information from zip archive of java source in given package 
    * 
    * @param javaSrcPath path to source archive
    * @param packageName the package for filtering class names
    * @return set of canonical names
    * @throws IOException
    */
   public static List<String> getClassesNamesFromJavaSrc(String javaSrcPath, String packageName) throws IOException
   {
      return extract(javaSrcPath, packageName, ".java");
   }

   private static List<String> extract(String archath, String packageName, String fileExtension) throws FileNotFoundException, IOException
   {
      ArrayList<String> classes = new ArrayList<String>();
      ZipInputStream zipFile = new ZipInputStream(new FileInputStream(archath));
      ZipEntry zipEntry;
      while (true)
      {
         zipEntry = zipFile.getNextEntry();
         if (zipEntry == null)
         {
            break;
         }
         if (zipEntry.getName().endsWith(fileExtension))
         {
            String fqn = zipEntry.getName();
            fqn = fqn.substring(0, fqn.lastIndexOf("."));
            fqn = fqn.replaceAll("/", "\\.");
            if (packageName != null)
            {
               if (fqn.startsWith(packageName))
               {
                  classes.add(fqn);
               }
            }
            else
            {
               classes.add(fqn);
            }
         }
      }
      return classes;
   }
   
  
     }
