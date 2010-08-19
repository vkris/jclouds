/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.vcloud.bluelock.compute.functions;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.compute.strategy.PopulateDefaultLoginCredentialsForImageStrategy;
import org.jclouds.vcloud.compute.functions.FindLocationForResource;
import org.jclouds.vcloud.compute.functions.ImageForVAppTemplate;

/**
 * 
 * @author Adrian Cole
 */
@Singleton
public class BlueLockVCloudImageForVAppTemplate extends ImageForVAppTemplate {

   @Inject
   protected BlueLockVCloudImageForVAppTemplate(FindLocationForResource findLocationForResource,
            PopulateDefaultLoginCredentialsForImageStrategy credentialsProvider) {
      super(findLocationForResource, credentialsProvider);
   }

   // Extremely important, as otherwise the size encoded into the name will throw off the
   // template matching, accidentally choosing the largest size by default
   protected String getName(String name) {
      return name.split(" ")[0];
   }
}