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
package org.exoplatform.ide.client.module.groovy.service.codeassistant;

/**
 * This service for auto-complete feature.
 * Service need for retrieve information about Groovy classes.      
 * <br>
 * Created by The eXo Platform SAS.
 *
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: Nov 17, 2010 2:44:13 PM evgen $
 *
 */
public abstract class CodeAssistantService
{

   private static CodeAssistantService instance;
   
   protected CodeAssistantService()
   {
      instance = this;
   }

   /**
    * @return the instance of service
    */
   public static CodeAssistantService getInstance()
   {
      return instance;
   }
     
   /**
    * Get Classes FQN by name.
    *   
    * @param className
    */
   public abstract void findClass(String className);
   
   /**
    * Get Class description (methods, fields etc.) by class FQN
    * 
    * @param fqn
    */
   public abstract void getClassDescription(String fqn);
   
   /**
    * @param prefix the first letters of class name 
    */
   public abstract void findClassesByPrefix(String prefix);
   
}
