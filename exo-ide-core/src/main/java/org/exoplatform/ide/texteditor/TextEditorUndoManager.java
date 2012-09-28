/*******************************************************************************
 * Copyright (c) 2006, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.exoplatform.ide.texteditor;

import com.google.gwt.event.dom.client.KeyCodes;

import org.exoplatform.ide.commands.ExecutionException;
import org.exoplatform.ide.commands.operations.IUndoContext;
import org.exoplatform.ide.text.BadLocationException;
import org.exoplatform.ide.text.Document;
import org.exoplatform.ide.text.DocumentImpl;
import org.exoplatform.ide.text.store.LineInfo;
import org.exoplatform.ide.text.undo.DocumentUndoEvent;
import org.exoplatform.ide.text.undo.DocumentUndoManagerRegistry;
import org.exoplatform.ide.text.undo.DocumentUndoListener;
import org.exoplatform.ide.text.undo.DocumentUndoManager;
import org.exoplatform.ide.texteditor.api.KeyListener;
import org.exoplatform.ide.texteditor.api.TextEditorPartView;
import org.exoplatform.ide.util.SignalEvent;

/**
 * <p>
 * It registers with the connected text viewer as text input listener, and obtains
 * its undo manager from the current document.  It also monitors mouse and keyboard
 * activities in order to partition the stream of text changes into undo-able
 * edit commands.
 * <p>
 * This class is not intended to be subclassed.
 * </p>
 *
 * @noextend This class is not intended to be subclassed by clients.
 */
public class TextEditorUndoManager
{

   //	/**
   //	 * Internal listener to mouse and key events.
   //	 */
   //	private class KeyAndMouseListener implements MouseListener, KeyListener {
   //
   //		/*
   //		 * @see MouseListener#mouseDoubleClick
   //		 */
   //		public void mouseDoubleClick(MouseEvent e) {
   //		}
   //
   //		/*
   //		 * If the right mouse button is pressed, the current editing command is closed
   //		 * @see MouseListener#mouseDown
   //		 */
   //		public void mouseDown(MouseEvent e) {
   //			if (e.button == 1)
   //				if (isConnected())
   //					fDocumentUndoManager.commit();
   //		}
   //
   //		/*
   //		 * @see MouseListener#mouseUp
   //		 */
   //		public void mouseUp(MouseEvent e) {
   //		}
   //
   //		/*
   //		 * @see KeyListener#keyPressed
   //		 */
   //		public void keyReleased(KeyEvent e) {
   //		}
   //
   //		/*
   //		 * On cursor keys, the current editing command is closed
   //		 * @see KeyListener#keyPressed
   //		 */
   //		public void keyPressed(KeyEvent e) {
   //			switch (e.keyCode) {
   //				case SWT.ARROW_UP:
   //				case SWT.ARROW_DOWN:
   //				case SWT.ARROW_LEFT:
   //				case SWT.ARROW_RIGHT:
   //					if (isConnected()) {
   //						fDocumentUndoManager.commit();
   //					}
   //					break;
   //			}
   //		}
   //	}

   //
   //	/**
   //	 * Internal text input listener.
   //	 */
   //	private class TextInputListener implements ITextInputListener {
   //
   //		/*
   //		 * @see org.eclipse.jface.text.ITextInputListener#inputDocumentAboutToBeChanged(org.eclipse.jface.text.IDocument, org.eclipse.jface.text.IDocument)
   //		 */
   //		public void inputDocumentAboutToBeChanged(IDocument oldInput, IDocument newInput) {
   //			disconnectDocumentUndoManager();
   //		}
   //
   //		/*
   //		 * @see org.eclipse.jface.text.ITextInputListener#inputDocumentChanged(org.eclipse.jface.text.IDocument, org.eclipse.jface.text.IDocument)
   //		 */
   //		public void inputDocumentChanged(IDocument oldInput, IDocument newInput) {
   //			connectDocumentUndoManager(newInput);
   //		}
   //	}

   private class KeyListenerImpl implements KeyListener
   {

      /**
       * @see org.exoplatform.ide.texteditor.api.KeyListener#onKeyPress(org.exoplatform.ide.util.SignalEvent)
       */
      @Override
      public boolean onKeyPress(SignalEvent event)
      {
         switch (event.getKeyCode())
         {
            case KeyCodes.KEY_DOWN :
            case KeyCodes.KEY_UP :
            case KeyCodes.KEY_LEFT :
            case KeyCodes.KEY_RIGHT :
               if (isConnected())
               {
                  fDocumentUndoManager.commit();
               }
               break;
         }
         return false;
      }

   }

   /**
    * Internal document undo listener.
    */
   private class DocumentUndoListenerImpl implements DocumentUndoListener
   {

      /*
       * @see org.eclipse.jface.text.IDocumentUndoListener#documentUndoNotification(DocumentUndoEvent)
       */
      public void documentUndoNotification(DocumentUndoEvent event)
      {
         if (!isConnected())
            return;

         int eventType = event.getEventType();
         if (((eventType & DocumentUndoEvent.ABOUT_TO_UNDO) != 0)
            || ((eventType & DocumentUndoEvent.ABOUT_TO_REDO) != 0))
         {
            //            if (event.isCompound())
            //            {
            //               ITextViewerExtension extension = null;
            //               if (fTextViewer instanceof ITextViewerExtension)
            //                  extension = (ITextViewerExtension)fTextViewer;
            //
            //               if (extension != null)
            //                  extension.setRedraw(false);
            //            }
            //            fTextViewer.getTextWidget().getDisplay().syncExec(new Runnable()
            //            {
            //               public void run()
            //               {
            //                  if (fTextViewer instanceof TextViewer)
            //                     ((TextViewer)fTextViewer).ignoreAutoEditStrategies(true);
            //               }
            //            });
            //
         }
         else if (((eventType & DocumentUndoEvent.UNDONE) != 0) || ((eventType & DocumentUndoEvent.REDONE) != 0))
         {
            //            fTextViewer.getTextWidget().getDisplay().syncExec(new Runnable()
            //            {
            //               public void run()
            //               {
            //                  if (fTextViewer instanceof TextViewer)
            //                     ((TextViewer)fTextViewer).ignoreAutoEditStrategies(false);
            //               }
            //            });
            //            if (event.isCompound())
            //            {
            //               ITextViewerExtension extension = null;
            //               if (fTextViewer instanceof ITextViewerExtension)
            //                  extension = (ITextViewerExtension)fTextViewer;
            //
            //               if (extension != null)
            //                  extension.setRedraw(true);
            //            }
            //
            //            // Reveal the change if this manager's viewer has the focus.
            if (textViewer != null)
            {
               //               StyledText widget = fTextViewer.getTextWidget();
               //               if (widget != null && !widget.isDisposed() && (widget.isFocusControl()))// || fTextViewer.getTextWidget() == control))
               selectAndReveal(event.getOffset(), event.getText() == null ? 0 : event.getText().length());
            }
         }
      }

   }

   //
   //	/** The internal key and mouse event listener */
   //	private KeyAndMouseListener fKeyAndMouseListener;
   //	/** The internal text input listener */
   //	private TextInputListener fTextInputListener;

   private KeyListener inputListener;

   /** The text viewer the undo manager is connected to */
   private TextEditorPartView textViewer;

   /** The undo level */
   private int fUndoLevel;

   /** The document undo manager that is active. */
   private DocumentUndoManager fDocumentUndoManager;

   /** The document that is active. */
   private Document fDocument;

   /** The document undo listener */
   private DocumentUndoListener fDocumentUndoListener;

   /**
    * Creates a new undo manager who remembers the specified number of edit commands.
    *
    * @param undoLevel the length of this manager's history
    */
   public TextEditorUndoManager(int undoLevel)
   {
      fUndoLevel = undoLevel;
      inputListener = new KeyListenerImpl();
   }

   /**
    * Returns whether this undo manager is connected to a text viewer.
    *
    * @return <code>true</code> if connected, <code>false</code> otherwise
    */
   private boolean isConnected()
   {
      return textViewer != null && fDocumentUndoManager != null;
   }

   /*
    * @see IUndoManager#beginCompoundChange
    */
   public void beginCompoundChange()
   {
      if (isConnected())
      {
         fDocumentUndoManager.beginCompoundChange();
      }
   }

   /*
    * @see IUndoManager#endCompoundChange
    */
   public void endCompoundChange()
   {
      if (isConnected())
      {
         fDocumentUndoManager.endCompoundChange();
      }
   }

   /**
    * Registers all necessary listeners with the text viewer.
    */
   private void addListeners()
   {
      //      StyledText text = fTextViewer.getTextWidget();
      //      if (text != null)
      //      {
      //         fKeyAndMouseListener = new KeyAndMouseListener();
      //         text.addMouseListener(fKeyAndMouseListener);
      //         text.addKeyListener(fKeyAndMouseListener);
      //         fTextInputListener = new TextInputListener();
      //         fTextViewer.addTextInputListener(fTextInputListener);
      //      }
      textViewer.getKeyListenerRegistrar().add(inputListener);
   }

   /**
    * Unregister all previously installed listeners from the text viewer.
    */
   private void removeListeners()
   {
      //      StyledText text = fTextViewer.getTextWidget();
      //      if (text != null)
      //      {
      //         if (fKeyAndMouseListener != null)
      //         {
      //            text.removeMouseListener(fKeyAndMouseListener);
      //            text.removeKeyListener(fKeyAndMouseListener);
      //            fKeyAndMouseListener = null;
      //         }
      //         if (fTextInputListener != null)
      //         {
      //            fTextViewer.removeTextInputListener(fTextInputListener);
      //            fTextInputListener = null;
      //         }
      //      }
      textViewer.getKeyListenerRegistrar().remove(inputListener);
   }

   /**
    * Shows the given exception in an error dialog.
    *
    * @param title the dialog title
    * @param ex the exception
    */
   private void openErrorDialog(final String title, final Exception ex)
   {
      //      Shell shell = null;
      //      if (isConnected())
      //      {
      //         StyledText st = fTextViewer.getTextWidget();
      //         if (st != null && !st.isDisposed())
      //            shell = st.getShell();
      //      }
      //      if (Display.getCurrent() != null)
      //         MessageDialog.openError(shell, title, ex.getLocalizedMessage());
      //      else
      //      {
      //         Display display;
      //         final Shell finalShell = shell;
      //         if (finalShell != null)
      //            display = finalShell.getDisplay();
      //         else
      //            display = Display.getDefault();
      //         display.syncExec(new Runnable()
      //         {
      //            public void run()
      //            {
      //               MessageDialog.openError(finalShell, title, ex.getLocalizedMessage());
      //            }
      //         });
      //      }
      ex.printStackTrace();
   }

   /*
    * @see org.eclipse.jface.text.IUndoManager#setMaximalUndoLevel(int)
    */
   public void setMaximalUndoLevel(int undoLevel)
   {
      fUndoLevel = Math.max(0, undoLevel);
      if (isConnected())
      {
         fDocumentUndoManager.setMaximalUndoLevel(fUndoLevel);
      }
   }

   /*
    * @see org.eclipse.jface.text.IUndoManager#connect(org.eclipse.jface.text.ITextViewer)
    */
   public void connect(TextEditorPartView textViewer)
   {
      if (this.textViewer == null && textViewer != null)
      {
         this.textViewer = textViewer;
         addListeners();
      }
      Document doc = textViewer.getDocument();
      connectDocumentUndoManager(doc);
   }

   /*
    * @see org.eclipse.jface.text.IUndoManager#disconnect()
    */
   public void disconnect()
   {
      if (textViewer != null)
      {
         removeListeners();
         textViewer = null;
      }
      disconnectDocumentUndoManager();
   }

   /*
    * @see org.eclipse.jface.text.IUndoManager#reset()
    */
   public void reset()
   {
      if (isConnected())
         fDocumentUndoManager.reset();

   }

   /*
    * @see org.eclipse.jface.text.IUndoManager#redoable()
    */
   public boolean redoable()
   {
      if (isConnected())
         return fDocumentUndoManager.redoable();
      return false;
   }

   /*
    * @see org.eclipse.jface.text.IUndoManager#undoable()
    */
   public boolean undoable()
   {
      if (isConnected())
         return fDocumentUndoManager.undoable();
      return false;
   }

   /*
    * @see org.eclipse.jface.text.IUndoManager#redo()
    */
   public void redo()
   {
      if (isConnected())
      {
         try
         {
            fDocumentUndoManager.redo();
         }
         catch (ExecutionException ex)
         {
            openErrorDialog("Redo failed", ex);
         }
      }
   }

   /*
    * @see org.eclipse.jface.text.IUndoManager#undo()
    */
   public void undo()
   {
      if (isConnected())
      {
         try
         {
            fDocumentUndoManager.undo();
         }
         catch (ExecutionException ex)
         {
            openErrorDialog("Undo failed", ex); //$NON-NLS-1$
         }
      }
   }

   /**
    * Selects and reveals the specified range.
    *
    * @param offset the offset of the range
    * @param length the length of the range
    */
   private void selectAndReveal(int offset, int length)
   {
      DocumentImpl document = (DocumentImpl)textViewer.getDocument();
      try
      {
         int startLine = document.getLineOfOffset(offset);
         int lineOffset = document.getLineOffset(startLine);
         LineInfo baseLine = document.getTextStore().getLineFinder().findLine(startLine);
         int endOffset = offset + length;
         int endLine = document.getLineOfOffset(endOffset);
         LineInfo endLineInfo = document.getTextStore().getLineFinder().findLine(endLine);
         int endLineOffset = document.getLineOffset(endLine);
         textViewer.getSelection().setSelection(baseLine, offset - lineOffset, endLineInfo, endOffset - endLineOffset);

      }
      catch (BadLocationException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      //      if (fTextViewer instanceof ITextViewerExtension5)
      //      {
      //         ITextViewerExtension5 extension = (ITextViewerExtension5)fTextViewer;
      //         extension.exposeModelRange(new Region(offset, length));
      //      }
      //      else if (!fTextViewer.overlapsWithVisibleRegion(offset, length))
      //         fTextViewer.resetVisibleRegion();
      //
      //      fTextViewer.setSelectedRange(offset, length);
      //      fTextViewer.revealRange(offset, length);
   }

   /*
    * @see org.eclipse.jface.text.IUndoManagerExtension#getUndoContext()
    */
   public IUndoContext getUndoContext()
   {
      if (isConnected())
      {
         return fDocumentUndoManager.getUndoContext();
      }
      return null;
   }

   private void connectDocumentUndoManager(Document document)
   {
      disconnectDocumentUndoManager();
      if (document != null)
      {
         fDocument = document;
         DocumentUndoManagerRegistry.connect(fDocument);
         fDocumentUndoManager = DocumentUndoManagerRegistry.getDocumentUndoManager(fDocument);
         fDocumentUndoManager.connect(this);
         setMaximalUndoLevel(fUndoLevel);
         fDocumentUndoListener = new DocumentUndoListenerImpl();
         fDocumentUndoManager.addDocumentUndoListener(fDocumentUndoListener);
      }
   }

   private void disconnectDocumentUndoManager()
   {
      if (fDocumentUndoManager != null)
      {
         fDocumentUndoManager.disconnect(this);
         DocumentUndoManagerRegistry.disconnect(fDocument);
         fDocumentUndoManager.removeDocumentUndoListener(fDocumentUndoListener);
         fDocumentUndoListener = null;
         fDocumentUndoManager = null;
      }
   }
}
