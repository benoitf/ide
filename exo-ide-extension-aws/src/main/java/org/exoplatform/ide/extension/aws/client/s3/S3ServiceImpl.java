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

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestException;

import org.exoplatform.gwtframework.commons.loader.Loader;
import org.exoplatform.gwtframework.commons.rest.AsyncRequest;
import org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback;
import org.exoplatform.gwtframework.commons.rest.HTTPHeader;
import org.exoplatform.gwtframework.commons.rest.MimeType;
import org.exoplatform.ide.extension.aws.client.AwsAsyncRequestCallback;
import org.exoplatform.ide.extension.aws.shared.s3.S3Bucket;
import org.exoplatform.ide.extension.aws.shared.s3.S3ObjectsList;

import java.util.List;

/**
 * @author <a href="mailto:vparfonov@exoplatform.com">Vitaly Parfonov</a>
 * @version $Id: S3ServiceImpl.java Sep 19, 2012 vetal $
 *
 */
public class S3ServiceImpl extends S3Service
{

   private static final String BASE_URL = "/ide/aws/s3";

   private static final String BUCKETS = BASE_URL + "/buckets";
   
   private static final String OBJECTS = BASE_URL + "/objects/";

   private static final String OBJECT_DELETE = BASE_URL + "/objects/delete/";

   private static final String BUCKETS_DELETE = BASE_URL + "/buckets/delete/";
   
   private static final String BUCKETS_CREATE = BASE_URL + "/buckets/create";
//
//
//   private static final String OBJECT_PUT = BASE_URL + "/objects/put";
//
//   private static final String OBJECT_UPLOAD = BASE_URL + "/objects/upload";
//
   
//

   
   /**
    * REST service context.
    */
   private String restServiceContext;

   /**
    * Loader to be displayed.
    */
   private Loader loader;

   public S3ServiceImpl(String restContext, Loader loader)
   {
      this.loader = loader;
      this.restServiceContext = restContext;
   }

   /**
    * @see org.exoplatform.ide.extension.aws.client.beanstalk.BeanstalkClientService#getAvailableSolutionStacks(org.exoplatform.gwtframework.commons.rest.AsyncRequestCallback)
    */
   @Override
   public void getBuckets(AwsAsyncRequestCallback<List<S3Bucket>> callback) throws RequestException
   {
      String url = restServiceContext + BUCKETS;
      AsyncRequest.build(RequestBuilder.GET, url).loader(loader).header(HTTPHeader.ACCEPT, MimeType.APPLICATION_JSON)
         .send(callback);
   }
   
   @Override
   public void getS3ObjectsList(AsyncRequestCallback<S3ObjectsList> callback, String s3Bucket, String nextMarker) throws RequestException
   {
      String url = restServiceContext + OBJECTS + s3Bucket + "?maxkeys=18";
      if (nextMarker != null)
         url += "&nextmarker=" + nextMarker;
      
      AsyncRequest.build(RequestBuilder.POST, url).loader(loader).header(HTTPHeader.ACCEPT, MimeType.APPLICATION_JSON)
         .send(callback);
   }
   
   @Override
   public void deleteObject(AsyncRequestCallback<String> callback, String s3Bucket, String s3key)
      throws RequestException
   {
      String url = restServiceContext + OBJECT_DELETE + s3Bucket + "?s3key=" + s3key;
      AsyncRequest.build(RequestBuilder.POST, url).loader(loader).send(callback);
      
   }
   
   
   @Override
   public void deleteBucket(AsyncRequestCallback<String> callback, String bucketId)
      throws RequestException
   {
      String url = restServiceContext + BUCKETS_DELETE + bucketId;
      AsyncRequest.build(RequestBuilder.POST, url).loader(loader).send(callback);
   }
   
   
   @Override
   public void createBucket(AsyncRequestCallback<String> callback, String name, String region)
      throws RequestException
   {
      String url = restServiceContext + BUCKETS_CREATE + "?name=" + name + "&region=" + region;
      AsyncRequest.build(RequestBuilder.POST, url).loader(loader).send(callback);
      
   }

  
}