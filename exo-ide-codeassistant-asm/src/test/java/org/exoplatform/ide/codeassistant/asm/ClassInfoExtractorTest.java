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
package org.exoplatform.ide.codeassistant.asm;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import org.exoplatform.ide.codeassistant.asm.test.A;
import org.exoplatform.ide.codeassistant.asm.test.B;
import org.exoplatform.ide.codeassistant.asm.test.E;
import org.exoplatform.ide.codeassistant.jvm.FieldInfo;
import org.exoplatform.ide.codeassistant.jvm.JavaType;
import org.exoplatform.ide.codeassistant.jvm.MethodInfo;
import org.exoplatform.ide.codeassistant.jvm.TypeInfo;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 */
public class ClassInfoExtractorTest
{
   @Test
   public void testExctractClass() throws ClassFormatError, ClassNotFoundException, IOException
   {
      TypeInfo cd = ClassParser.parse(A.class);
      assertEquals(9, cd.getMethods().size());
      assertEquals(3, cd.getFields().size());
      assertEquals(A.class.getCanonicalName(), cd.getName());
   }

   @Test
   public void testExctractField() throws IOException
   {
      TypeInfo cd = ClassParser.parse(A.class);
      Field[] fields = A.class.getFields();
      List<FieldInfo> fds = cd.getFields();
      for (Field field : fields)
      {
         FieldInfo fd = getFieldInfo(fds, field);
         if (fd == null)
         {
            fail();
         }
         assertEquals(field.getModifiers(), fd.getModifiers());
         assertEquals(field.getType().getSimpleName(), fd.getType());
      }
   }

   private FieldInfo getFieldInfo(List<FieldInfo> fds, Field field)
   {

      for (FieldInfo fd : fds)
      {
         if (fd.getName().equals(field.getName()))
         {
            return fd;
         }
      }
      return null;
   }

   @Test
   public void testExctractMethod() throws IOException
   {
      TypeInfo cd = ClassParser.parse(B.class);
      List<MethodInfo> mds = cd.getMethods();
      Method[] methods = B.class.getDeclaredMethods();
      for (Method method : methods)
      {
         MethodInfo md = getMethodInfo(mds, method.getName());
         if (md == null)
         {
            fail();
         }
         assertEquals(method.getModifiers(), md.getModifiers());
      }
   }

   @Test
   public void testEnumExtract() throws IOException
   {
      TypeInfo en = ClassParser.parse(E.class);
      assertEquals(JavaType.ENUM.name(), en.getType());
      assertEquals("ONE", en.getFields().get(0).getName());
   }

   private MethodInfo getMethodInfo(List<MethodInfo> mds, String name)
   {
      for (MethodInfo md : mds)
      {
         if (md.getName().equals(name))
         {
            return md;
         }
      }
      return null;
   }
}