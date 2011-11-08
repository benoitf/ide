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
package org.exoplatform.ide.extension.samples.client.samples.load;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event to show list of repositories with sample projects on GitHub.
 * 
 * @author <a href="oksana.vereshchaka@gmail.com">Oksana Vereshchaka</a>
 * @version $Id: GithubSamplesShowEvent.java Aug 30, 2011 12:07:32 PM vereshchaka $
 */
public class ShowSamplesEvent extends GwtEvent<ShowSamplesHandler>
{
   public static final GwtEvent.Type<ShowSamplesHandler> TYPE = new GwtEvent.Type<ShowSamplesHandler>();
   
   public ShowSamplesEvent()
   {
   }

   @Override
   protected void dispatch(ShowSamplesHandler handler)
   {
      handler.onShowSamples(this);
   }

   /**
    * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
    */
   @Override
   public com.google.gwt.event.shared.GwtEvent.Type<ShowSamplesHandler> getAssociatedType()
   {
      return TYPE;
   }

}