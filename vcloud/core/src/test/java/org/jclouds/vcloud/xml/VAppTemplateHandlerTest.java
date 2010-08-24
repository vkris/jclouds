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

package org.jclouds.vcloud.xml;

import static org.testng.Assert.assertEquals;

import java.io.InputStream;
import java.net.URI;

import org.jclouds.http.functions.ParseSax;
import org.jclouds.http.functions.ParseSax.Factory;
import org.jclouds.http.functions.config.SaxParserModule;
import org.jclouds.vcloud.VCloudMediaType;
import org.jclouds.vcloud.domain.Status;
import org.jclouds.vcloud.domain.VAppTemplate;
import org.jclouds.vcloud.domain.Vm;
import org.jclouds.vcloud.domain.internal.NamedResourceImpl;
import org.testng.annotations.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests behavior of {@code VAppTemplateHandler}
 * 
 * @author Adrian Cole
 */
@Test(groups = "unit", testName = "vcloud.VAppTemplateHandlerTest")
public class VAppTemplateHandlerTest {
   public void testUbuntuTemplate() {
      InputStream is = getClass().getResourceAsStream("/vAppTemplate.xml");
      Injector injector = Guice.createInjector(new SaxParserModule());
      Factory factory = injector.getInstance(ParseSax.Factory.class);
      VAppTemplate result = factory.create(injector.getInstance(VAppTemplateHandler.class)).parse(is);
      assertEquals(result.getName(), "Ubuntu Template");
      assertEquals(result.getId(), URI
               .create("https://vcenterprise.bluelock.com/api/v1.0/vAppTemplate/vappTemplate-1201908921"));
      assertEquals(result.getType(), "application/vnd.vmware.vcloud.vAppTemplate+xml");
      assertEquals(result.getStatus(), Status.OFF);
      assertEquals(result.getVDC(), new NamedResourceImpl(null, VCloudMediaType.VDC_XML, URI
               .create("https://vcenterprise.bluelock.com/api/v1.0/vdc/1014839439")));
      assertEquals(result.getDescription(), null);
      assertEquals(result.getTasks(), ImmutableList.of());
      assertEquals(result.getVAppScopedLocalId(), null);
      assert result.isOvfDescriptorUploaded();
      Vm vm = Iterables.getOnlyElement(result.getChildren());
      assertEquals(vm.getName(), "Ubuntu1004");
      assertEquals(vm.getId(), URI.create("https://vcenterprise.bluelock.com/api/v1.0/vAppTemplate/vm-172837194"));
      // NOTE this is vAppTemplate not VM!
      assertEquals(vm.getType(), "application/vnd.vmware.vcloud.vAppTemplate+xml");
      assertEquals(vm.getStatus(), null);
      assertEquals(vm.getParent(), new NamedResourceImpl(null, VCloudMediaType.VAPPTEMPLATE_XML, URI
               .create("https://vcenterprise.bluelock.com/api/v1.0/vAppTemplate/vappTemplate-1201908921")));
      assertEquals(vm.getDescription(), null);
      assertEquals(vm.getTasks(), ImmutableList.of());
      assertEquals(vm.getVAppScopedLocalId(), null);
   }

}
