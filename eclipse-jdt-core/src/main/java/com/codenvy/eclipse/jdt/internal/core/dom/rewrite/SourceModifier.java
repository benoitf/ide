/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.codenvy.eclipse.jdt.internal.core.dom.rewrite;

import com.codenvy.eclipse.jdt.core.formatter.IndentManipulation;

import org.exoplatform.ide.editor.shared.text.edits.ISourceModifier;
import org.exoplatform.ide.editor.shared.text.edits.ReplaceEdit;

import java.util.ArrayList;
import java.util.List;


public class SourceModifier implements ISourceModifier
{

   private final String destinationIndent;

   private final int sourceIndentLevel;

   private final int tabWidth;

   private final int indentWidth;

   public SourceModifier(int sourceIndentLevel, String destinationIndent, int tabWidth, int indentWidth)
   {
      this.destinationIndent = destinationIndent;
      this.sourceIndentLevel = sourceIndentLevel;
      this.tabWidth = tabWidth;
      this.indentWidth = indentWidth;
   }

   public ISourceModifier copy()
   {
      // We are state less
      return this;
   }

   public ReplaceEdit[] getModifications(String source)
   {
      List result = new ArrayList();
      int destIndentLevel = IndentManipulation.measureIndentUnits(this.destinationIndent, this.tabWidth,
         this.indentWidth);
      if (destIndentLevel == this.sourceIndentLevel)
      {
         return (ReplaceEdit[])result.toArray(new ReplaceEdit[result.size()]);
      }
      return IndentManipulation.getChangeIndentEdits(source, this.sourceIndentLevel, this.tabWidth, this.indentWidth,
         this.destinationIndent);
   }
}