/*
 * Copyright (C) 2023 jtalbut
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.co.spudsoft.query.main;

import io.swagger.v3.core.util.Json;
import io.swagger.v3.core.util.Json31;
import io.swagger.v3.jaxrs2.integration.JaxrsOpenApiContextBuilder;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.integration.api.OpenAPIConfiguration;
import io.swagger.v3.oas.integration.api.OpenApiContext;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.ws.rs.core.Application;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jtalbut
 */
@OpenAPIDefinition
public class OpenApiModelTest extends Application {

  private static final Logger logger = LoggerFactory.getLogger(OpenApiModelTest.class);

  static OpenAPIConfiguration createOpenapiConfiguration(List<Object> resources, Object application) {
    return new SwaggerConfiguration()
            .resourceClasses(Stream.concat(resources.stream(), Stream.of(application)).map(r -> r.getClass().getCanonicalName())
                    .collect(Collectors.toSet()))
            .prettyPrint(true)
            .filterClass("uk.co.spudsoft.query.main.OpenApiFilterClass")
            .openAPI31(Boolean.TRUE)
            .openAPI(
                    new OpenAPI()
                            .info(
                                    new Info()
                                            .title("Swagger-core #4852 Reproducer")
                                            .version("0.0.0")
                            )
            );
  }

  
  @Test
  public void testModel() throws Throwable {

    // ModelConverters.getInstance(true).addConverter(new OpenApiModelConverter());

    OpenAPIConfiguration openApiConfig = createOpenapiConfiguration(Arrays.asList(new OpenApiTestController()), this);

    JaxrsOpenApiContextBuilder<?> oacb = new JaxrsOpenApiContextBuilder<>()
            .application(this);
    oacb.setOpenApiConfiguration(openApiConfig);
    OpenApiContext ctx = oacb.buildContext(true);

    OpenAPIConfiguration config = ctx.getOpenApiConfiguration();
    if (config == null) {
      config = openApiConfig;
    }

    OpenAPI oas = ctx.read();

    if (config.isOpenAPI31()) {
      logger.error("OpenAPI 3.1: {}", Json31.pretty(oas));
    } else {
      logger.error("OpenAPI 3.0: {}", Json.pretty(oas));
    }
    
    assertNotNull(oas);
    for (int i = 0; i < 100; ++i) {
      Thread.sleep(100);
    }
  }
}
