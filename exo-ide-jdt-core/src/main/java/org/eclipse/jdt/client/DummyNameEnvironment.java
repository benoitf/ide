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
package org.eclipse.jdt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;
import com.google.web.bindery.autobean.shared.AutoBean;
import com.google.web.bindery.autobean.shared.AutoBeanCodex;
import com.google.web.bindery.autobean.shared.Splittable;
import com.google.web.bindery.autobean.shared.impl.StringQuoter;

import org.eclipse.jdt.client.core.Signature;
import org.eclipse.jdt.client.core.compiler.CharOperation;
import org.eclipse.jdt.client.env.BinaryTypeImpl;
import org.eclipse.jdt.client.internal.codeassist.ISearchRequestor;
import org.eclipse.jdt.client.internal.compiler.env.IBinaryMethod;
import org.eclipse.jdt.client.internal.compiler.env.INameEnvironment;
import org.eclipse.jdt.client.internal.compiler.env.NameEnvironmentAnswer;
import org.eclipse.jdt.client.runtime.IProgressMonitor;
import org.exoplatform.gwtframework.commons.exception.UnmarshallerException;
import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.gwtframework.commons.rest.Unmarshallable;
import org.exoplatform.ide.codeassistant.jvm.shared.ShortTypeInfo;
import org.exoplatform.ide.codeassistant.jvm.shared.TypeInfo;
import org.exoplatform.ide.codeassistant.jvm.shared.TypesList;
import org.exoplatform.ide.editor.java.client.JavaEditorExtension;
import org.exoplatform.ide.editor.java.client.codeassistant.services.JavaCodeAssistantService;
import org.exoplatform.ide.vfs.client.VirtualFileSystem;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:evidolob@exoplatform.com">Evgen Vidolob</a>
 * @version ${Id}: Jan 13, 2012 3:10:43 PM evgen $
 */
public class DummyNameEnvironment implements INameEnvironment
{

   protected interface JsonClasses extends ClientBundle
   {
      @Source("org/eclipse/jdt/client/object.json")
      TextResource object();

      @Source("org/eclipse/jdt/client/string.json")
      TextResource string();

      @Source("org/eclipse/jdt/client/system.json")
      TextResource system();

      @Source("org/eclipse/jdt/client/boolean.json")
      TextResource booleanClass();

      @Source("org/eclipse/jdt/client/byte.json")
      TextResource byteClass();

      @Source("org/eclipse/jdt/client/character.json")
      TextResource character();

      @Source("org/eclipse/jdt/client/class.json")
      TextResource clazz();

      @Source("org/eclipse/jdt/client/cloneable.json")
      TextResource cloneable();

      @Source("org/eclipse/jdt/client/double.json")
      TextResource doubleClass();

      @Source("org/eclipse/jdt/client/error.json")
      TextResource error();

      @Source("org/eclipse/jdt/client/exception.json")
      TextResource exception();

      @Source("org/eclipse/jdt/client/float.json")
      TextResource floatClass();

      @Source("org/eclipse/jdt/client/integer.json")
      TextResource integer();

      @Source("org/eclipse/jdt/client/long.json")
      TextResource longClass();

      @Source("org/eclipse/jdt/client/runtimeexception.json")
      TextResource runtimeException();

      @Source("org/eclipse/jdt/client/serializible.json")
      TextResource serializible();

      @Source("org/eclipse/jdt/client/short.json")
      TextResource shortClass();

      @Source("org/eclipse/jdt/client/stringbuffer.json")
      TextResource stringBuffer();

      @Source("org/eclipse/jdt/client/throwable.json")
      TextResource throwable();

      @Source("org/eclipse/jdt/client/void.json")
      TextResource voidClass();
   }

   private static Set<String> packages;

   private String projectId;

   static
   {

      JsonClasses classes = GWT.create(JsonClasses.class);
      JSONObject parseLenient = JSONParser.parseLenient(classes.object().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Object", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.string().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.String", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.system().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.System", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.booleanClass().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Boolean", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.byteClass().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Byte", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.character().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Character", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.clazz().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Class", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.cloneable().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Cloneable", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.doubleClass().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Double", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.error().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Error", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.exception().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Exception", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.floatClass().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Float", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.integer().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Integer", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.longClass().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Long", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.runtimeException().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.RuntimeException", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.serializible().getText()).isObject();
      TypeInfoStorage.get().putType("java.io.Serializable", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.shortClass().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Short", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.stringBuffer().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.StringBuffer", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.throwable().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Throwable", parseLenient.toString());

      parseLenient = JSONParser.parseLenient(classes.voidClass().getText()).isObject();
      TypeInfoStorage.get().putType("java.lang.Void", parseLenient.toString());

      packages = new HashSet<String>();
      packages.add("java");
      packages.add("java.lang");
      packages.add("java.util");
      packages.add("java.io");
   }

   /**
    * 
    */
   public DummyNameEnvironment(String projectId)
   {
      this.projectId = projectId;
   }

   /** @see org.eclipse.jdt.client.internal.compiler.env.INameEnvironment#findType(char[][]) */
   @Override
   public NameEnvironmentAnswer findType(char[][] compoundTypeName)
   {
      StringBuilder b = new StringBuilder();
      for (char[] c : compoundTypeName)
      {
         b.append(c).append('.');
      }
      b.deleteCharAt(b.length() - 1);

      final String key = validateFqn(b);
      if (TypeInfoStorage.get().containsKey(key))
      {
         return new NameEnvironmentAnswer(new BinaryTypeImpl(JSONParser
            .parseLenient(TypeInfoStorage.get().getType(key)).isObject()), null);
      }
      if (projectId != null)
      {

         loadClassInfo(key);
      }
      return null;
   }

   private String validateFqn(StringBuilder builder)
   {
      if (builder.indexOf("<") != -1)
      {
         builder.setLength(builder.indexOf("<"));
      }
      return builder.toString();
   }

   /** @param key */
   private void loadClassInfo(final String key)
   {
      final JSONTypeInfoUnmarshaller jsonTypeInfoUnmarshaller = new JSONTypeInfoUnmarshaller();
      JavaCodeAssistantService.get().getClassDescription(key, projectId,
         new AsyncRequestCallback<TypeInfo>(jsonTypeInfoUnmarshaller)

         {

            @Override
            protected void onSuccess(TypeInfo result)
            {
               if (jsonTypeInfoUnmarshaller.typeInfo != null)
               {
                  TypeInfoStorage.get().putType(key, jsonTypeInfoUnmarshaller.typeInfo.toString());
                  packages.add(key.substring(0, key.lastIndexOf('.')));
               }
            }

            @Override
            protected void onFailure(Throwable exception)
            {
               // TODO
               exception.printStackTrace();
            }
         });
   }

   /**
    * @see org.eclipse.jdt.client.internal.compiler.env.INameEnvironment#findType(char[], char[][])
    */
   @Override
   public NameEnvironmentAnswer findType(char[] typeName, char[][] packageName)
   {
      if ("package-info".equals(new String(typeName)))
         return null;

      StringBuilder b = new StringBuilder();
      for (char[] c : packageName)
      {
         b.append(c).append('.');
      }
      b.append(typeName);
      final String key = validateFqn(b);
      if (TypeInfoStorage.get().containsKey(key))
      {
         return new NameEnvironmentAnswer(new BinaryTypeImpl(JSONParser
            .parseLenient(TypeInfoStorage.get().getType(key)).isObject()), null);
      }
      if (projectId != null)
      {
         loadClassInfo(key);
      }
      return null;
   }

   /**
    * @see org.eclipse.jdt.client.internal.compiler.env.INameEnvironment#isPackage(char[][], char[])
    */
   @Override
   public boolean isPackage(char[][] parentPackageName, char[] packageName)
   {
      if (parentPackageName == null)
         return true;

      if (Character.isUpperCase(packageName[0]))
         return false;
      else
         return true;

      // StringBuilder b = new StringBuilder();
      // for (char[] c : parentPackageName)
      // {
      // b.append(c).append('.');
      // }
      // b.append(packageName);
      //
      // String pack = b.toString();
      // // for (String fqn : typeStorage.keySet())
      // // {
      // // if (fqn.startsWith(pack))
      // // return true;
      // // }
      // return packages.contains(pack)

      // return packages.contains(pack);

      // return false;
   }

   /** @see org.eclipse.jdt.client.internal.compiler.env.INameEnvironment#cleanup() */
   @Override
   public void cleanup()
   {
   }

   /**
    * Must be used only by CompletionEngine. The progress monitor is used to be able to cancel completion operations
    * 
    * Find constructor declarations that are defined in the current environment and whose name starts with the given prefix. The
    * prefix is a qualified name separated by periods or a simple name (ex. java.util.V or V).
    * 
    * The constructors found are passed to one of the following methods: ISearchRequestor.acceptConstructor(...)
    */
   public void findConstructorDeclarations(char[] prefix, boolean camelCaseMatch, final ISearchRequestor requestor,
      IProgressMonitor monitor)
   {
      int lastDotIndex = CharOperation.lastIndexOf('.', prefix);
      char[] qualification, simpleName;
      if (lastDotIndex < 0)
      {
         qualification = null;
         if (camelCaseMatch)
         {
            simpleName = prefix;
         }
         else
         {
            simpleName = CharOperation.toLowerCase(prefix);
         }
      }
      else
      {
         qualification = CharOperation.subarray(prefix, 0, lastDotIndex);
         if (camelCaseMatch)
         {
            simpleName = CharOperation.subarray(prefix, lastDotIndex + 1, prefix.length);
         }
         else
         {
            simpleName = CharOperation.toLowerCase(CharOperation.subarray(prefix, lastDotIndex + 1, prefix.length));
         }
      }
      String url =
         "/rest/private" + "/ide/code-assistant/java/classes-by-prefix" + "?prefix=" + new String(simpleName)
            + "&projectid=" + projectId + "&vfsid=" + VirtualFileSystem.getInstance().getInfo().getId();
      try
      {
         List<JSONObject> typesByNamePrefix =
            TypeInfoStorage.get().getTypesByNamePrefix(new String(prefix), qualification != null);
         for (JSONObject object : typesByNamePrefix)
         {
            addConstructor(new BinaryTypeImpl(object), requestor);
         }
         String typesJson = findTypes(url);
         JSONArray typesFromServer = null;
         if (typesJson != null)
         {
            typesFromServer = JSONParser.parseLenient(typesJson).isArray();
            for (int i = 0; i < typesFromServer.size(); i++)
            {
               JSONObject object = typesFromServer.get(i).isObject();
               BinaryTypeImpl type = new BinaryTypeImpl(object);
               TypeInfoStorage.get().putType(new String(type.getSourceName()), type.toJsonString());
               addConstructor(type, requestor);
            }
         }

      }
      catch (Exception e)
      {
         // TODO: handle exception
         e.printStackTrace();
      }
   }

   private void addConstructor(BinaryTypeImpl type, final ISearchRequestor requestor)
   {
      for (IBinaryMethod method : type.getMethods())
      {
         if (!method.isConstructor())
            continue;
         int parameterCount = Signature.getParameterCount(method.getMethodDescriptor());
         char[][] parameterTypes = Signature.getParameterTypes(method.getMethodDescriptor());
         requestor.acceptConstructor(method.getModifiers(), type.getSourceName(), parameterCount,
            method.getMethodDescriptor(), parameterTypes, method.getArgumentNames(), type.getModifiers(),
            Signature.getQualifier(type.getFqn()), 0, new String(type.getSourceName()), null);
      }
   }

   /**
    * Find the packages that start with the given prefix. A valid prefix is a qualified name separated by periods (ex. java.util).
    * The packages found are passed to: ISearchRequestor.acceptPackage(char[][] packageName)
    */
   public void findPackages(char[] qualifiedName, ISearchRequestor requestor)
   {
      // TODO Auto-generated method stub
   }

   private native String findTypes(String url)/*-{
		var xmlhttp = new XMLHttpRequest();
		xmlhttp.open("GET", url, false);
		xmlhttp.send();
		return xmlhttp.responseText;
   }-*/;

   /**
    * @param qualifiedName
    * @param b
    * @param camelCaseMatch
    * @param searchFor
    * @param completionEngine
    * @param monitor
    */
   public void findTypes(char[] qualifiedName, boolean b, boolean camelCaseMatch, int searchFor,
      final ISearchRequestor requestor, IProgressMonitor monitor)
   {
      AutoBean<TypesList> autoBean = JavaEditorExtension.AUTO_BEAN_FACTORY.types();
      // AutoBeanUnmarshaller<TypesList> unmarshaller = new AutoBeanUnmarshaller<TypesList>(autoBean);
      String url =
         "/rest/private" + "/ide/code-assistant/java/find-by-prefix/" + new String(qualifiedName) + "?where=className"
            + "&projectid=" + projectId + "&vfsid=" + VirtualFileSystem.getInstance().getInfo().getId();
      try
      {

         String typesJson = findTypes(url);
         Splittable data = StringQuoter.split(typesJson);
         AutoBeanCodex.decodeInto(data, autoBean);

         for (ShortTypeInfo info : autoBean.as().getTypes())
         {

            requestor
               .acceptType(info.getName().substring(0, info.getName().lastIndexOf(".")).toCharArray(), info.getName()
                  .substring(info.getName().lastIndexOf(".") + 1).toCharArray(), null, info.getModifiers(), null);
         }
      }
      catch (Throwable e)
      {
         e.printStackTrace();
      }
   }

   /**
    * @param missingSimpleName
    * @param b
    * @param type
    * @param storage
    */
   public void findExactTypes(char[] missingSimpleName, boolean b, int type, ISearchRequestor storage)
   {
      // TODO Auto-generated method stub

   }

   public static class JSONTypeInfoUnmarshaller implements Unmarshallable<TypeInfo>
   {

      private JSONObject typeInfo;

      /** @see org.exoplatform.gwtframework.commons.rest.copy.Unmarshallable#unmarshal(com.google.gwt.http.client.Response) */
      @Override
      public void unmarshal(Response response) throws UnmarshallerException
      {
         if (response.getStatusCode() != 204)
         {
            typeInfo = JSONParser.parseLenient(response.getText()).isObject();
         }
      }

      /** @see org.exoplatform.gwtframework.commons.rest.copy.Unmarshallable#getPayload() */
      @Override
      public TypeInfo getPayload()
      {
         return null;
      }

   }
}