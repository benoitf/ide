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
package org.exoplatform.ide.editor.codemirror.parser;

import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.editor.api.codeassitant.TokenBeenImpl;
import org.exoplatform.ide.editor.api.codeassitant.TokenType;
import org.exoplatform.ide.editor.codemirror.Node;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * @author <a href="mailto:dmitry.ndp@gmail.com">Dmytro Nochevnov</a>
 * @version $Id: $
 */
public class XmlParser extends CodeMirrorParserImpl
{
   private String lastNodeContent;
   
   private String lastNodeType;
   
   @Override
   TokenBeenImpl parseLine(JavaScriptObject node, int lineNumber, TokenBeenImpl currentToken, boolean hasParentParser)
   {
      // interrupt at the end of the line or content
      if ((node == null) || Node.getName(node).equals("BR"))
         return currentToken;

      String nodeContent = Node.getContent(node).trim(); // returns text without ended space " " in the text
      String nodeType = Node.getType(node);

      // recognize CDATA open tag "<![CDATA[" not in the CDATA section
      if (XmlParser.isCDATAOpenNode(nodeContent))
      {
         TokenBeenImpl newToken = new TokenBeenImpl("CDATA", TokenType.CDATA, lineNumber, MimeType.TEXT_XML);
         if (currentToken != null)
         {
            currentToken.addSubToken(newToken);
         }

         currentToken = newToken;
      }

      // recognize CDATA close tag "]]>" into the CDATA section
      else if (XmlParser.isCDATACloseNode(nodeContent))
      {
         if (currentToken != null)
         {
            currentToken = currentToken.getParentToken();
         }
      }

      // recognize tag node not in the CDATA section
      if (isTagNode(nodeType))
      {
         // recognize open tag starting with "<"
         if (isOpenTagNode(lastNodeType, lastNodeContent))
         {
            currentToken = addTag(currentToken, nodeContent, lineNumber, MimeType.TEXT_XML);
         }

         // recognize close tag starting with "</"
         else if (isCloseStartTagNode(lastNodeType, lastNodeContent))
         {
            currentToken = addTagBreak(lineNumber, currentToken, MimeType.TEXT_XML);
         }
      }

      // recognize close tag starting with "/>" out of 
      else if (isCloseFinishTagNode(nodeType, nodeContent))
      {
         currentToken = addTagBreak(lineNumber, currentToken, MimeType.TEXT_XML);
      }

      lastNodeContent = nodeContent;
      lastNodeType = nodeType;

      if (hasParentParser)
      {
         return currentToken; // return current token to parent parser
      }

      return parseLine(Node.getNext(node), lineNumber, currentToken, false);
   }
 
   
   /**
    * recognize "</" node
    * @param nodeType
    * @param nodeContent
    * @return
    */
   static boolean isCloseStartTagNode(String nodeType, String nodeContent)
   {
      return (nodeType != null) && (nodeContent != null) && (nodeType.equals("xml-punctuation") && nodeContent.equals("&lt;/"));
   }

   /**
    * recognize "/>" node
    * @param nodeType
    * @param nodeContent
    * @return
    */
   static boolean isCloseFinishTagNode(String nodeType, String nodeContent)
   {
      return (nodeType != null) && (nodeContent != null) && (nodeType.equals("xml-punctuation") && nodeContent.equals("/&gt;"));
   }
   
   /**
    * recognize "<" node
    * @param nodeType
    * @param nodeContent
    * @return
    */
   static boolean isOpenTagNode(String nodeType, String nodeContent)
   {
      return (nodeType != null) && (nodeContent != null) && nodeType.equals("xml-punctuation") && nodeContent.equals("&lt;");
   };

   /**
    * recognize "<" node
    * @param nodeType
    * @return
    */
   static boolean isTagNode(String nodeType)
   {
      return (nodeType != null) && nodeType.equals("xml-tagname");
   };
   
   static boolean isCDATAOpenNode(String nodeContent)
   {
      return nodeContent.matches("&lt;!\\[CDATA\\[.*(\\n)*.*");
   };
   
   static boolean isCDATACloseNode(String nodeContent)
   {
      return nodeContent.matches("\\]\\]&gt;.*");
   };   

   static TokenBeenImpl addTag(TokenBeenImpl currentToken, String tagName, int lineNumber, String contentMimeType)
   {
      TokenBeenImpl newToken = new TokenBeenImpl(tagName, TokenType.TAG, lineNumber, contentMimeType);
      if (currentToken != null)
      {
         currentToken.addSubToken(newToken);
      }

      return newToken;
   }   
   
   // it is required for the correct recognizing of currentLineMimeType and selection of current token in the outline panel   
   static TokenBeenImpl addTagBreak(int lineNumber, TokenBeenImpl currentToken, String nextContentMimeType)
   {
      TokenBeenImpl newToken = new TokenBeenImpl(null, TokenType.TAG_BREAK, lineNumber, nextContentMimeType);      

      if (currentToken != null) 
      {
         currentToken.addSubToken(newToken);
         currentToken.setLastLineNumber(lineNumber);
         currentToken = currentToken.getParentToken();
      } 
      else
      {
         currentToken = newToken;
      }
      
      return currentToken;
   }     
}