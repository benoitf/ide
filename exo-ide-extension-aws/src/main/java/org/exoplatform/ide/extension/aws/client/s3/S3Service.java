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
package org.exoplatform.ide.extension.aws.client.s3;

import com.google.gwt.http.client.RequestException;

import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.ide.extension.aws.client.AwsAsyncRequestCallback;
import org.exoplatform.ide.extension.aws.shared.s3.S3Bucket;
import org.exoplatform.ide.extension.aws.shared.s3.S3ObjectsList;

import java.util.List;

/**
 * @author <a href="mailto:vparfonov@exoplatform.com">Vitaly Parfonov</a>
 * @version $Id: S3Service.java Sep 19, 2012 vetal $
 *
 */
public abstract class S3Service
{
   private static S3Service instance;

   public static S3Service getInstance()
   {
      return instance;
   }

   protected S3Service()
   {
      instance = this;
   }

   /**
    * Returns available buckets.
    * 
    * @param callback
    * @throws RequestException
    */
   public abstract void getBuckets(AwsAsyncRequestCallback<List<S3Bucket>> callback) throws RequestException;

   /**
    * Returns available buckets.
    * 
    * @param asyncRequestCallback
    * @throws RequestException
    */
   public abstract void createBucket(AsyncRequestCallback<String> asyncRequestCallback, String name, String region)
      throws RequestException;

   /**
    * Returns object list in the bucket.
    * 
    * @param callback
    * @param nextMarker 
    * @throws RequestException
    */
   public abstract void getS3ObjectsList(AsyncRequestCallback<S3ObjectsList> callback, String s3Bucket,
      String nextMarker) throws RequestException;

   /**
    * Delete object.
    * 
    * @param callback
    * @param nextMarker 
    * @throws RequestException
    */
   public abstract void deleteObject(AsyncRequestCallback<String> callback, String s3Bucket, String s3Key)
      throws RequestException;

   /**
    * @param asyncRequestCallback
    * @param bucketId
    * @throws RequestException
    */
   public abstract void deleteBucket(AsyncRequestCallback<String> asyncRequestCallback, String bucketId)
      throws RequestException;

}