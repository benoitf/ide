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
package org.exoplatform.ide.extension.gadget.server.opensocial.service;

import org.exoplatform.ide.extension.gadget.server.opensocial.model.Album;

import java.util.List;

/**
 * Service to manipulate with OpenSocial Albums data (collections of media items).
 * 
 * @author <a href="mailto:zhulevaanna@gmail.com">Ann Zhuleva</a>
 * @version $Id: Nov 19, 2010 $
 *
 */
public interface AlbumService
{
   /**
    * Request to retrieve Albums.
    * 
    * @param userId user ID of the person whose Albums are to be returned
    * @param groupId group ID of the group of users whose Albums are to be returned
    * @param appId specifies that the response should only contain Albums generated by the given appId (optional)
    * @param id list of Album IDs specifying the Albums to retrieve
    * @return
    */
   List<Album> getAlbums(String userId, String groupId, String appId, List<String> id);
   
   /**
    * Create new album.
    * 
    * @param userId user ID of the person to associate the Album with
    * @param album album to create
    * @return {@link Album} created album
    */
   Album createAlbum(String userId, Album album);
   
   /**
    * Update an Album.
    * 
    * @param userId ser ID of the person the Album is associated with
    * @param album album to update
    * @return {@link Album} album
    */
   Album updateAlbum(String userId, Album album);
   
   /**
    * Delete album.
    * 
    * @param userId user ID of the person the Album is associated with
    * @param id  ID of the Album to delete
    */
   void deleteAlbum(String userId, String id);
}
