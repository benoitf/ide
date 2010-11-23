/*
 * Copyright (C) 2010 eXo Platform SAS.
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
package org.exoplatform.ide.client.module.groovy.codeassistant;

import java.util.HashMap;
import java.util.Map;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.client.framework.codeassistant.api.ImportDeclarationTokenCollector;

import com.google.gwt.event.shared.HandlerManager;

/**
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: Nov 22, 2010 3:01:51 PM evgen $
 *
 */
public final class ImportDeclarationsTokenCollectors
{

   private static Map<String, ImportDeclarationTokenCollector> collectors = new HashMap<String, ImportDeclarationTokenCollector>();
   
   private static ImportDeclarationsTokenCollectors instance;
   
   private ImportDeclarationsTokenCollectors(HandlerManager eventBus)
   {
      GroovyImportDeclarationTokenCollector collector = new GroovyImportDeclarationTokenCollector(eventBus);
      collectors.put(MimeType.GROOVY_SERVICE, collector);
      collectors.put(MimeType.APPLICATION_GROOVY, collector);
      collectors.put(MimeType.CHROMATTIC_DATA_OBJECT, collector);
      collectors.put(MimeType.APPLICATION_GROOVY, collector);
   }
   
   
   public static ImportDeclarationTokenCollector getCollector(HandlerManager eventBus, String mimeType)
   {
      if(instance == null)
      {
         instance = new ImportDeclarationsTokenCollectors(eventBus);
      }
      return collectors.get(mimeType);
   }
   
}
