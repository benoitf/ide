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
package org.exoplatform.ide.codeassistant.storage;

import org.exoplatform.ide.codeassistant.jvm.shared.TypeInfo;
import org.exoplatform.ide.codeassistant.storage.api.DataWriter;
import org.exoplatform.ide.codeassistant.storage.lucene.SaveDataIndexException;
import org.exoplatform.ide.codeassistant.storage.lucene.writer.LuceneDataWriter;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version $Id:
 *
 */
public class LocalDataWriter implements DataWriter
{

   private LuceneDataWriter dataWriter;

   /**
    * @param dataWriter
    */
   public LocalDataWriter(LuceneDataWriter dataWriter)
   {
      super();
      this.dataWriter = dataWriter;
   }

   /**
    * @see org.exoplatform.ide.codeassistant.storage.api.DataWriter#addTypeInfo(java.util.List, java.lang.String)
    */
   @Override
   public void addTypeInfo(List<TypeInfo> typeInfos, String artifact)
   {
      try
      {
         dataWriter.addTypeInfo(typeInfos);
      }
      catch (SaveDataIndexException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   /**
    * @see org.exoplatform.ide.codeassistant.storage.api.DataWriter#addPackages(java.util.Set, java.lang.String)
    */
   @Override
   public void addPackages(Set<String> packages, String artifact)
   {
      try
      {
         dataWriter.addPackages(packages);
      }
      catch (SaveDataIndexException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

}
