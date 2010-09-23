/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ide.client.module.groovy.service.groovy;

import java.util.List;

import org.exoplatform.ide.client.module.groovy.service.SimpleParameterEntry;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version @version $Id: $
 */

public abstract class GroovyService
{

   private static GroovyService instance;

   public static GroovyService getInstance()
   {
      return instance;
   }

   protected GroovyService()
   {
      instance = this;
   }

   /**
    * Validate Groovy script.
    * 
    * @param fileName
    * @param content
    */
   public abstract void validate(String fileName, String fileHref, String fileContent);
   
   /**
    * Deploy Groovy script.
    * 
    * @param path
    */
   public abstract void deploy(String href);
   
   /**
    * Deploy Groovy script.
    * 
    * @param path
    */
   public abstract void deploySandbox(String href);
   
   /**
    * Undeploy deployed Groovy script.
    * 
    * @param path
    */
   public abstract void undeploySandbox(String href);
   
   /**
    * Undeploy deployed Groovy script.
    * 
    * @param path
    */
   public abstract void undeploy(String href);
   
   /**
    * Get Groovy script output
    * 
    * @param url
    */
   public abstract void getOutput(String url, String method, List<SimpleParameterEntry> headers,
      List<SimpleParameterEntry> params, String body);

}
