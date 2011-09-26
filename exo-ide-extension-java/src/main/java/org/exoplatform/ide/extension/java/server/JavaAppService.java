/*
 * Copyright (C) 2011 eXo Platform SAS.
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
package org.exoplatform.ide.extension.java.server;

import org.exoplatform.ide.maven.TaskService;
import org.exoplatform.ide.vfs.server.VirtualFileSystem;
import org.exoplatform.ide.vfs.server.VirtualFileSystemRegistry;

import java.net.URL;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author <a href="mailto:aparfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: $
 */
@Path("ide/application/java")
public class JavaAppService
{
   //   /** Logger. */
   //   private static final Log LOG = ExoLogger.getLogger(JavaAppService.class);
   //
   //   private static InvocationRequestFactory CLEAN_REQUEST_FACTORY = new InvocationRequestFactory(Arrays.asList("clean"));
   //
   //   private static InvocationRequestFactory PACKAGE_REQUEST_FACTORY = new InvocationRequestFactory(Arrays.asList(
   //      "clean", "package"));

   //   private final TaskService taskService;

   private final VirtualFileSystemRegistry vfsRegistry;

   public JavaAppService(TaskService taskService, VirtualFileSystemRegistry vfsRegistry)
   {
      //this.taskService = taskService;
      this.vfsRegistry = vfsRegistry;
   }

   /**
    * Creates Java project.
    */
   @POST
   @Path("create")
   public Response createApplication(@QueryParam("parentId") String parentId,
      @QueryParam("projectName") String projectName, @QueryParam("projectType") String projectType,
      @QueryParam("groupId") String groupId, @QueryParam("artifactId") String artifactId,
      @QueryParam("version") String version, @Context UriInfo uriInfo, @QueryParam("vfsId") String vfsId)
      throws Exception
   {
      String resource = projectType;
      URL url = Thread.currentThread().getContextClassLoader().getResource(resource);
      VirtualFileSystem vfs = vfsRegistry.getProvider(vfsId).newInstance(null);
      if (vfs == null)
         throw new WebApplicationException(Response.serverError().entity("Virtual file system not initialized").build());
      JavaProjectArchetype archetype =
         new JavaProjectArchetype(projectName, groupId, artifactId, version, parentId, vfs);
      archetype.exportResources(url);
      return Response.ok().build();
   }

   //   @POST
   //   @Path("clean")
   //   @Produces(MediaType.APPLICATION_JSON)
   //   public Response clean(@QueryParam("workdir") FSLocation baseDir, @Context UriInfo uriInfo) throws Exception
   //   {
   //      InvocationRequest request = CLEAN_REQUEST_FACTORY.createRequest();
   //      request.setBaseDirectory(new File(baseDir.getLocalPath(uriInfo)));
   //      MavenResponse mvn = execute(request);
   //      return createResponse(mvn);
   //   }
   //
   //   @POST
   //   @Path("package")
   //   @Produces(MediaType.APPLICATION_JSON)
   //   public Response pack(@QueryParam("workdir") FSLocation baseDir, @Context UriInfo uriInfo) throws Exception
   //   {
   //      InvocationRequest request = PACKAGE_REQUEST_FACTORY.createRequest();
   //      File dir = new File(baseDir.getLocalPath(uriInfo));
   //      request.setBaseDirectory(dir);
   //      MavenResponse mvn = execute(request);
   //      String[] files = new File(dir, "target").list(new FilenameFilter()
   //      {
   //         @Override
   //         public boolean accept(File dir, String name)
   //         {
   //            return name.endsWith(".war"); // Support only web applications at the moment.
   //         }
   //      });
   //      if (files != null && files.length > 0)
   //      {
   //         Map<String, String> result = new HashMap<String, String>(1);
   //         result.put("war", //
   //            baseDir.getURL() + "/target/" + files[0]);
   //         mvn.setResult(result);
   //      }
   //      return createResponse(mvn);
   //   }
   //
   //   /*private Response execute(InvocationRequest request, long timeout ) throws Exception
   //   {
   //      return execute(request, new TaskWatcher(timeout));
   //   }*/
   //
   //   private Response createResponse(MavenResponse mvn)
   //   {
   //      ResponseBuilder b = mvn.getExitCode() == 0 ? Response.ok() : Response.status(500);
   //      return b.entity(mvn).type(MediaType.APPLICATION_JSON).build();
   //   }
   //
   //   private MavenResponse execute(InvocationRequest request) throws Exception
   //   {
   //      return execute(request, null);
   //   }
   //
   //   private MavenResponse execute(InvocationRequest request, TaskWatcher watcher) throws Exception
   //   {
   //      MavenTask task = taskService.add(request, watcher);
   //      InvocationResult result;
   //      try
   //      {
   //         result = task.get(); // Block until task end.
   //      }
   //      finally
   //      {
   //         // Do not store task in pool since we are waiting until it ends and read output from it.
   //         taskService.remove(task.getId());
   //      }
   //
   //      CommandLineException executionException = result.getExecutionException();
   //      if (executionException != null)
   //         throw executionException;
   //
   //      // Send output of maven task to caller.
   //      int exitCode = result.getExitCode();
   //      return new MavenResponse(exitCode, task.getTaskLogger().getLogAsString(), null);
   //   }
}
