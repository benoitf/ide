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
package org.exoplatform.ide.codeassistant.asm;

import org.exoplatform.ide.codeassistant.jvm.MethodInfo;

/**
 * This class used for building MethodInfo objects
 */
public class MethodInfoBuilder extends RoutineInfoBuilder
{

   MethodInfoBuilder(int access, String methodName, String[] exceptions, String desc, String declaredClass)
   {
      super(access, methodName, exceptions, desc, declaredClass);
   }

   public MethodInfo buildMethodInfo()
   {
      MethodInfo methodInfo = new MethodInfo();

      fillRoutineInfo(methodInfo);

      String genericReturnType = transformTypeFormat(desc.substring(desc.lastIndexOf(')') + 1));
      methodInfo.setGenericReturnType(genericReturnType);
      methodInfo.setReturnType(toShortName(genericReturnType));

      StringBuilder genericBuilder = new StringBuilder();
      String stringModifier = methodInfo.modifierToString();
      if (!stringModifier.isEmpty())
      {
         genericBuilder.append(stringModifier);
         genericBuilder.append(" ");
      }
      genericBuilder.append(methodInfo.getGenericReturnType());
      genericBuilder.append(" ");
      genericBuilder.append(methodInfo.getDeclaringClass());
      genericBuilder.append(".");
      genericBuilder.append(methodInfo.getName());
      genericBuilder.append(methodInfo.getGenericParameterTypes());
      if (exceptions != null && exceptions.length > 0)
      {
         genericBuilder.append(" throws ");
         for (int i = 0; i < exceptions.length; i++)
         {
            if (i != 0)
            {
               genericBuilder.append(", ");
            }
            genericBuilder.append(exceptions[i]);
         }
      }
      methodInfo.setGeneric(genericBuilder.toString());

      return methodInfo;
   }

}