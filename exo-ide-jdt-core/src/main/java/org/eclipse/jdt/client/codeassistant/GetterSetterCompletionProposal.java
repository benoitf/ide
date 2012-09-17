/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.client.codeassistant;

import com.google.gwt.user.client.ui.Image;

import org.eclipse.jdt.client.JavaPreferencesSettings;
import org.eclipse.jdt.client.JdtClientBundle;
import org.eclipse.jdt.client.codeassistant.api.IJavaCompletionProposal;
import org.eclipse.jdt.client.codeassistant.ui.StyledString;
import org.eclipse.jdt.client.core.Flags;
import org.eclipse.jdt.client.core.Messages;
import org.eclipse.jdt.client.core.Signature;
import org.eclipse.jdt.client.core.dom.IMethodBinding;
import org.eclipse.jdt.client.core.dom.ITypeBinding;
import org.eclipse.jdt.client.core.dom.IVariableBinding;
import org.eclipse.jdt.client.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.client.core.formatter.CodeFormatter;
import org.eclipse.jdt.client.internal.corext.codemanipulation.CodeGenerationSettings;
import org.eclipse.jdt.client.internal.corext.codemanipulation.GetterSetterUtil;
import org.eclipse.jdt.client.internal.corext.util.CodeFormatterUtil;
import org.eclipse.jdt.client.internal.corext.util.JdtFlags;
import org.eclipse.jdt.client.internal.corext.util.Strings;
import org.eclipse.jdt.client.runtime.CoreException;
import org.exoplatform.ide.editor.runtime.Assert;
import org.exoplatform.ide.editor.text.BadLocationException;
import org.exoplatform.ide.editor.text.IDocument;
import org.exoplatform.ide.editor.text.IRegion;
import org.exoplatform.ide.editor.text.TextUtilities;

import java.util.Collection;
import java.util.Set;

public class GetterSetterCompletionProposal extends JavaTypeCompletionProposal
{

   public static void evaluateProposals(ITypeBinding type, String prefix, int offset, int length, int relevance,
      Set<String> suggestedMethods, Collection<IJavaCompletionProposal> result,
      JavaContentAssistInvocationContext context)
   {
      if (prefix.length() == 0)
      {
         relevance--;
      }

      IVariableBinding[] fields = type.getDeclaredFields();
      IMethodBinding[] methods = type.getDeclaredMethods();
      for (int i = 0; i < fields.length; i++)
      {
         IVariableBinding curr = fields[i];
         if (!JdtFlags.isEnum(curr))
         {
            String getterName = GetterSetterUtil.getGetterName(curr, null);
            if (Strings.startsWithIgnoreCase(getterName, prefix) && !hasMethod(methods, getterName))
            {
               suggestedMethods.add(getterName);
               int getterRelevance = relevance;
               if (JdtFlags.isStatic(curr) && JdtFlags.isFinal(curr))
                  getterRelevance = relevance - 1;
               result.add(new GetterSetterCompletionProposal(curr, offset, length, true, getterRelevance, context));
            }

            if (!JdtFlags.isFinal(curr))
            {
               String setterName = GetterSetterUtil.getSetterName(curr, null);
               if (Strings.startsWithIgnoreCase(setterName, prefix) && !hasMethod(methods, setterName))
               {
                  suggestedMethods.add(setterName);
                  result.add(new GetterSetterCompletionProposal(curr, offset, length, false, relevance, context));
               }
            }
         }
      }
   }

   private static boolean hasMethod(IMethodBinding[] methods, String name)
   {
      for (int i = 0; i < methods.length; i++)
      {
         if (methods[i].getName().equals(name))
         {
            return true;
         }
      }
      return false;
   }

   private final IVariableBinding fField;

   private final boolean fIsGetter;

   public GetterSetterCompletionProposal(IVariableBinding field, int start, int length, boolean isGetter,
      int relevance, JavaContentAssistInvocationContext context)
   {
      super(
         "", start, length, new Image(JdtClientBundle.INSTANCE.publicMethod()), getDisplayName(field, isGetter), relevance, null, context); //$NON-NLS-1$
      Assert.isNotNull(field);

      fField = field;
      fIsGetter = isGetter;
      //		setProposalInfo(new ProposalInfo(field));
   }

   private static StyledString getDisplayName(IVariableBinding field, boolean isGetter)
   {
      StyledString buf = new StyledString();
      String fieldTypeName = Signature.getSimpleName(Signature.toString(field.getType().getKey().replaceAll("/", ".")));
      String fieldNameLabel = field.getName();
      if (isGetter)
      {
         buf.append(GetterSetterUtil.getGetterName(field, null) + "() : " + StyledString.htmlEncode(fieldTypeName)); //$NON-NLS-1$
         buf.append(" - ", StyledString.QUALIFIER_STYLER); //$NON-NLS-1$
         buf.append(Messages.INSTANCE.GetterSetterCompletionProposal_getter_label(fieldNameLabel),
            StyledString.QUALIFIER_STYLER);
      }
      else
      {
         buf.append(GetterSetterUtil.getSetterName(field, null) + '(' + StyledString.htmlEncode(fieldTypeName) + ") : void"); //$NON-NLS-1$
         buf.append(" - ", StyledString.QUALIFIER_STYLER); //$NON-NLS-1$
         buf.append(Messages.INSTANCE.GetterSetterCompletionProposal_setter_label(fieldNameLabel),
            StyledString.QUALIFIER_STYLER);
      }
      return buf;
   }

   /* (non-Javadoc)
    * @see JavaTypeCompletionProposal#updateReplacementString(IDocument, char, int, ImportRewrite)
    */
   @Override
   protected boolean updateReplacementString(IDocument document, char trigger, int offset, ImportRewrite impRewrite)
      throws CoreException, BadLocationException
   {

      CodeGenerationSettings settings = JavaPreferencesSettings.getCodeGenerationSettings();
      boolean addComments = settings.createComments;
      int flags = Flags.AccPublic | (fField.getModifiers() & Flags.AccStatic);

      String stub;
      if (fIsGetter)
      {
         String getterName = GetterSetterUtil.getGetterName(fField, null);
         stub = GetterSetterUtil.getGetterStub(fField, getterName, addComments, flags);
      }
      else
      {
         String setterName = GetterSetterUtil.getSetterName(fField, null);
         stub = GetterSetterUtil.getSetterStub(fField, setterName, addComments, flags);
      }

      // use the code formatter
      String lineDelim = TextUtilities.getDefaultLineDelimiter(document);

      IRegion region = document.getLineInformationOfOffset(getReplacementOffset());
      int lineStart = region.getOffset();
      int indent =
         Strings.computeIndentUnits(document.get(lineStart, getReplacementOffset() - lineStart), settings.tabWidth,
            settings.indentWidth);

      String replacement =
         CodeFormatterUtil.format(CodeFormatter.K_CLASS_BODY_DECLARATIONS, stub, indent, lineDelim);

      if (replacement.endsWith(lineDelim))
      {
         replacement = replacement.substring(0, replacement.length() - lineDelim.length());
      }

      setReplacementString(Strings.trimLeadingTabsAndSpaces(replacement));
      return true;
   }

   /*
    * @see org.eclipse.jface.text.contentassist.ICompletionProposalExtension4#isAutoInsertable()
    */
   public boolean isAutoInsertable()
   {
      return false;
   }
}
