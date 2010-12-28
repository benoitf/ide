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
package org.exoplatform.ide.operation.file.autocompletion;

import org.exoplatform.ide.operation.file.autocompletion.groovy.GroovyAnnotationAutocompleteTest;
import org.exoplatform.ide.operation.file.autocompletion.groovy.GroovyClassMethodsCompletionTest;
import org.exoplatform.ide.operation.file.autocompletion.groovy.GroovyClassNameCompletionTest;
import org.exoplatform.ide.operation.file.autocompletion.groovy.GroovyJavaDocTest;
import org.exoplatform.ide.operation.file.autocompletion.groovy.GroovyKeywordsAutocompletionTest;
import org.exoplatform.ide.operation.file.autocompletion.groovy.GroovyLocalVariableTest;
import org.exoplatform.ide.operation.file.autocompletion.groovy.GroovyObjectCompletionTest;
import org.exoplatform.ide.operation.file.autocompletion.groovy.ImportStatementInsertionTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author <a href="mailto:tnemov@gmail.com">Evgen Vidolob</a>
 * @version $Id: $
 *
 */
@RunWith(Suite.class)
@SuiteClasses({AutoCompletionCSSTest.class, AutoCompletionHTMLTest.class,
   AutoCompletionJavaScriptDuplicationTest.class, AutoCompletionJavaScriptTest.class, AutoCompletionXMLTest.class,
   GroovyObjectCompletionTest.class, GroovyClassMethodsCompletionTest.class, GroovyClassNameCompletionTest.class,
   GroovyLocalVariableTest.class, GroovyAnnotationAutocompleteTest.class, GroovyKeywordsAutocompletionTest.class,
   ImportStatementInsertionTest.class, GroovyJavaDocTest.class})
public class AutoCompletionTestSuite
{

}
