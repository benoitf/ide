/*
 * Copyright (C) 2012 eXo Platform SAS.
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
package org.exoplatform.ide.extension.aws.shared.ec2;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:andrew00x@gmail.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public interface InstanceInfo
{
   String getId();

   void setId(String instanceId);

   String getPublicDNSName();

   void setPublicDNSName(String publicDNSName);

   String getImageId();

   void setImageId(String imageId);

   String getRootDeviceType();

   void setRootDeviceType(String rootDeviceType);

   InstanceState getState();

   void setState(InstanceState instanceState);

   String getImageType();

   void setImageType(String imageType);

   String getAvailabilityZone();

   void setAvailabilityZone(String availabilityZone);

   String getKeyName();

   void setKeyName(String keyName);

   long getLaunchTime();

   void setLaunchTime(long launchTime);

   List<String> getSetSecurityGroupsNames();

   void setSecurityGroupsNames(List<String> securityGroupsIds);

   Map<String, String> getTags();

   void setTags(Map<String, String> tags);
}