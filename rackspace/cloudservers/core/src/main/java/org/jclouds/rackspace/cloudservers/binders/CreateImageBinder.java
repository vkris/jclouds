/**
 *
 * Copyright (C) 2009 Global Cloud Specialists, Inc. <info@globalcloudspecialists.com>
 *
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 */
package org.jclouds.rackspace.cloudservers.binders;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import org.jclouds.http.HttpRequest;
import org.jclouds.http.binders.JsonBinder;

import com.google.common.collect.ImmutableMap;

/**
 * 
 * @author Adrian Cole
 * 
 */
public class CreateImageBinder extends JsonBinder {

   @SuppressWarnings("unused")
   private class CreateImageRequest {
      final int serverId;
      final String name;

      private CreateImageRequest(int serverId, String name) {
         this.serverId = serverId;
         this.name = name;
      }

   }

   @Override
   public void addEntityToRequest(Map<String, String> postParams, HttpRequest request) {
      CreateImageRequest createRequest = new CreateImageRequest(Integer
               .parseInt(checkNotNull(postParams.get("serverId"))), checkNotNull(postParams
               .get("imageName")));
      super.addEntityToRequest(ImmutableMap.of("image", createRequest), request);
   }

   @Override
   public void addEntityToRequest(Object toBind, HttpRequest request) {
      throw new IllegalArgumentException("image is needs parameters");
   }
}