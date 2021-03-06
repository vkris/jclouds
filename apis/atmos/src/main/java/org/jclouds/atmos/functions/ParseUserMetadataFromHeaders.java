/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
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
 */
package org.jclouds.atmos.functions;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.jclouds.atmos.domain.UserMetadata;
import org.jclouds.atmos.reference.AtmosHeaders;
import org.jclouds.http.HttpResponse;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;

/**
 * @author Adrian Cole
 */
@Singleton
public class ParseUserMetadataFromHeaders implements Function<HttpResponse, UserMetadata> {
   private static final Set<String> sysKeys = ImmutableSet.of("atime", "ctime", "gid", "itime", "mtime", "nlink",
         "policyname", "size", "uid", "content-md5", "objectid", "objname", "type");
   private static final Predicate<String> filter = new Predicate<String>() {

      @Override
      public boolean apply(String arg0) {
         return !sysKeys.contains(arg0);
      }

   };

   public UserMetadata apply(HttpResponse from) {
      checkNotNull(from, "http response");

      Map<String, String> meta = Maps.filterKeys(
            getMetaMap(checkNotNull(from.getFirstHeaderOrNull(AtmosHeaders.META), AtmosHeaders.META)),
            filter);

      Map<String, String> listableMeta = (from.getFirstHeaderOrNull(AtmosHeaders.LISTABLE_META) != null) ? getMetaMap(from
            .getFirstHeaderOrNull(AtmosHeaders.LISTABLE_META)) : ImmutableMap.<String, String> of();

      Iterable<String> tags = (from.getFirstHeaderOrNull(AtmosHeaders.TAGS) != null) ? Splitter.on(", ").split(
            from.getFirstHeaderOrNull(AtmosHeaders.TAGS)) : ImmutableSet.<String> of();

      Iterable<String> listableTags = (from.getFirstHeaderOrNull(AtmosHeaders.LISTABLE_TAGS) != null) ? Splitter
            .on(", ").split(from.getFirstHeaderOrNull(AtmosHeaders.LISTABLE_TAGS)) : ImmutableSet.<String> of();

      return new UserMetadata(meta, listableMeta, tags, listableTags);
   }

   // TODO: change to guava
   private Map<String, String> getMetaMap(String meta) {
      Builder<String, String> metaMap = ImmutableMap.<String, String> builder();
      for (String entry : Splitter.on(", ").split(meta)) {
         String[] entrySplit = entry.split("=");
         metaMap.put(entrySplit[0], entrySplit[1]);
      }
      return metaMap.build();
   }
}
