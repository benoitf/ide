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
package org.exoplatform.ide.extension.groovy.server;

import org.exoplatform.ide.codeassistant.jvm.CodeAssistantException;
import org.exoplatform.ide.codeassistant.jvm.JavaType;
import org.exoplatform.ide.codeassistant.jvm.ShortTypeInfo;
import org.exoplatform.ide.codeassistant.jvm.TypeInfo;
import org.exoplatform.ide.vfs.server.exceptions.VirtualFileSystemException;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Service provide Autocomplete of source code is also known as code completion feature. 
 * In a source code editor autocomplete is greatly simplified by the regular structure 
 * of the programming languages. 
 * At current moment implemented the search class FQN,
 * by Simple Class Name and a prefix (the lead characters in the name of the package or class).
 * 
 * Created by The eXo Platform SAS.
 * @author <a href="mailto:vitaly.parfonov@gmail.com">Vitaly Parfonov</a>
 * @version $Id: $
*/
@Path("/ide/code-assistant/groovy")
public class RestCodeAssistantGroovy
{

   @Inject
   private GroovyCodeAssistant codeAssistant;

   public RestCodeAssistantGroovy()
   {
   }

   /**
    * Returns the Class object associated with the class or interface with the given string name.
    * 
    * @param fqn the Full Qualified Name
    * @return {@link TypeInfo} 
    * @throws CodeAssistantException
    * @throws VirtualFileSystemException 
    */
   @GET
   @Path("/class-description")
   @Produces(MediaType.APPLICATION_JSON)
   public TypeInfo getClassByFQN(@QueryParam("fqn") String fqn,
                                 @QueryParam("projectid") String projectId,
                                 @QueryParam("vfsid") String vfsId) throws CodeAssistantException, VirtualFileSystemException
   {
      return codeAssistant.getClassByFQN(fqn, projectId, vfsId);
   }

   /**
    * Returns set of FQNs matched to prefix (means FQN begin on {prefix} or Class simple name)
    * Example :
    * if prefix = "java.util.c"
    * set must content:
    *  {
    *   java.util.Comparator<T>
    *   java.util.Calendar
    *   java.util.Collection<E>
    *   java.util.Collections
    *   java.util.ConcurrentModificationException
    *   java.util.Currency
    *   java.util.concurrent
    *   java.util.concurrent.atomic
    *   java.util.concurrent.locks
    *  }
    * 
    * @param prefix the string for matching FQNs
    * @param where the string that indicate where find (must be "className" or "fqn")
    * @throws VirtualFileSystemException 
    */
   @GET
   @Path("/find-by-prefix/{prefix}")
   @Produces(MediaType.APPLICATION_JSON)
   public List<ShortTypeInfo> findFQNsByPrefix(@PathParam("prefix") String prefix,
                                               @QueryParam("where") String where,
                                               @QueryParam("projectid") String projectId,
                                               @QueryParam("vfsid") String vfsId) throws CodeAssistantException,
      VirtualFileSystemException
   {
      if ("fqn".equalsIgnoreCase(where))
      {
         return codeAssistant.getTypesByFqnPrefix(prefix, projectId, vfsId);
      }
      return codeAssistant.getTypesByNamePrefix(prefix, projectId, vfsId);
   }

   /**
    * Find all classes or annotations or interfaces
    *   
    * @param type the string that represent one of Java class type (i.e. CLASS, INTERFACE, ANNOTATION) 
    * @param prefix optional parameter that matching first letter of type name
    * @return Returns set of FQNs matched to class type
    * @throws CodeAssistantException
    * @throws VirtualFileSystemException 
    */
   @GET
   @Path("/find-by-type/{type}")
   @Produces(MediaType.APPLICATION_JSON)
   public List<ShortTypeInfo> findByType(@PathParam("type") String type,
                                         @QueryParam("prefix") String prefix,
                                         @QueryParam("projectid") String projectId,
                                         @QueryParam("vfsid") String vfsId) throws CodeAssistantException,
      VirtualFileSystemException
   {
      return codeAssistant.getByType(JavaType.valueOf(type.toUpperCase()), prefix, projectId, vfsId);
   }

   @GET
   @Path("/class-doc")
   @Produces(MediaType.TEXT_HTML)
   public String getClassDoc(@QueryParam("fqn") String fqn,
                             @QueryParam("projectid") String projectId,
                             @QueryParam("vfsid") String vfsId,
                             @QueryParam("isclass") @DefaultValue("true") boolean isClass)
      throws CodeAssistantException, VirtualFileSystemException
   {
      if (isClass)
      {
         return "<html><head></head><body style=\"font-family: monospace;font-size: 12px;\">"
            + codeAssistant.getClassJavaDoc(fqn, projectId, vfsId) + "</body></html>";
      }
      return "<html><head></head><body style=\"font-family: monospace;font-size: 12px;\">"
         + codeAssistant.getMemberJavaDoc(fqn, projectId, vfsId) + "</body></html>";
   }
}
