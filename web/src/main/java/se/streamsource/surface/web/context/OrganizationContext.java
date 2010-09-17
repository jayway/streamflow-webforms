/**
 *
 * Copyright 2009-2010 Streamsource AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.streamsource.surface.web.context;

import org.qi4j.api.mixin.Mixins;
import se.streamsource.dci.api.Context;
import se.streamsource.dci.api.ContextMixin;
import se.streamsource.dci.api.IndexContext;
import se.streamsource.surface.web.context.accesspoints.AccessPointsContext;
import se.streamsource.dci.api.SubContext;
import se.streamsource.dci.restlet.client.CommandQueryClient;
import se.streamsource.dci.value.StringValue;

/**
 */
@Mixins(OrganizationContext.Mixin.class)
public interface OrganizationContext
      extends Context, IndexContext<StringValue>
{

   //@SubContext
   //ProjectsContext projects();

   @SubContext
   AccessPointsContext accesspoints();

   //@SubContext
   //ProxyUsersContext proxyusers();

   abstract class Mixin
         extends ContextMixin
         implements OrganizationContext
   {
      /*public ProjectsContext projects()
      {
         context.set( context.get( CommandQueryClient.class ).getSubClient( "projects" ));
         return subContext( ProjectsContext.class );
      } */

      public AccessPointsContext accesspoints()
      {
         roleMap.set( roleMap.get( CommandQueryClient.class ).getSubClient( "accesspoints" ));
         return subContext( AccessPointsContext.class );
      }

      /*public ProxyUsersContext proxyusers()
      {
         context.set( context.get( CommandQueryClient.class ).getSubClient( "proxyusers" ));
         return subContext( ProxyUsersContext.class );
      } */

      public StringValue index()
      {
         CommandQueryClient client = roleMap.get( CommandQueryClient.class );

         try
         {
            return client.query( "index", StringValue.class );
         } catch (Throwable e)
         {
            e.printStackTrace();
         }
         return null;
      }
   }

}
