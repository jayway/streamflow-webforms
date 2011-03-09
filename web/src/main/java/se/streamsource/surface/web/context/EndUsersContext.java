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

import org.qi4j.api.entity.Entity;
import org.qi4j.api.entity.Identity;
import org.qi4j.api.entity.IdentityGenerator;
import org.qi4j.api.injection.scope.Service;
import org.qi4j.api.injection.scope.Structure;
import org.qi4j.api.structure.Module;
import org.qi4j.api.value.ValueBuilder;
import org.restlet.Response;
import org.restlet.data.CharacterSet;
import org.restlet.data.Cookie;
import org.restlet.data.Form;
import org.restlet.data.Status;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;
import se.streamsource.dci.api.RoleMap;
import se.streamsource.dci.restlet.client.CommandQueryClient;
import se.streamsource.dci.value.EntityValue;
import se.streamsource.dci.value.LinkValue;
import se.streamsource.surface.web.rest.CookieResponseHandler;

/**
 */
public class EndUsersContext
{
   public static final String COOKIE_NAME = "ANONYMOUS_USER";

   @Structure
   Module module;

   @Service
   IdentityGenerator identityGenerator;

   public void selectenduser( Response response )
         throws ResourceException
   {
      // for now only user the anonymous end users
      Series<Cookie> cookies = response.getRequest().getCookies();

      Cookie cookie = findCookie( cookies );
      if (cookie == null)
      {
         try
         {
            CommandQueryClient client = RoleMap.current().get( CommandQueryClient.class );
            CookieResponseHandler responseHandler = module.objectBuilderFactory()
                  .newObjectBuilder( CookieResponseHandler.class )
                  .newInstance();
            Form form = new Form();
            form.set("string", identityGenerator.generate(Identity.class));
            client.postCommand( "create", form.getWebRepresentation(CharacterSet.UTF_8), responseHandler );

            response.getCookieSettings().add( responseHandler.getCookieSetting() );
         }
         catch (Throwable e)
         {
            e.printStackTrace();
         }
      }
   }

   private Cookie findCookie( Series<Cookie> cookies )
   {
      for (Cookie cookie : cookies)
      {
         if (cookie.getName().equals( COOKIE_NAME ))
         {
            return cookie;
         }
      }
      return null;
   }

   //This one will re-direct

   public LinkValue viewenduser( Response response )
         throws ResourceException
   {
      EntityValue dto = userreference( response );

      ValueBuilder<LinkValue> builder = module.valueBuilderFactory().newValueBuilder( LinkValue.class );
      builder.prototype().id().set( dto.entity().get() );
      builder.prototype().href().set( dto.entity().get() + "/" );
      builder.prototype().text().set( "ANONYMOUS" );
      return builder.newInstance();
   }

   public EntityValue userreference( Response response )
         throws ResourceException
   {
      Series<Cookie> cookies = response.getRequest().getCookies();
      response.release();

      Cookie cookie = findCookie( cookies );

      if (cookie == null)
      {
         throw new ResourceException( Status.CLIENT_ERROR_UNAUTHORIZED );
      }

      ValueBuilder<EntityValue> builder = module.valueBuilderFactory()
            .newValueBuilder( EntityValue.class );
      builder.prototype().entity().set( cookie.getValue() );
      return builder.newInstance();
   }
}