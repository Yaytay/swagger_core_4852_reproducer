/*
 * Copyright (C) 2025 jtalbut
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.container.AsyncResponse;
import jakarta.ws.rs.container.Suspended;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import uk.co.spudsoft.query.defn.Pipeline;

/**
 *
 * @author jtalbut
 */
@Path("/info")
public class OpenApiTestController {

    /**
     * Retrieves the source of a pipeline, either in JSON or YAML format, based on the provided path. The pipeline is returned as a response in JSON format. In case of errors, an
     * appropriate error response is returned.
     *
     * @param response the asynchronous response to resume the HTTP request.
     * @param path the path to the pipeline resource. The path is resolved to an absolute file path for retrieval of the pipeline source.
     */
    @GET
    @Path("/pipeline/{path: .*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(description = "Return the source of a pipeline.")
    @ApiResponse(
            responseCode = "200",
            description = "The source of a single pipeline.",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = Pipeline.class)
            )
    )
    public void getPipeline(
            @Suspended final AsyncResponse response,
            @PathParam("path") String path
    ) {
    }
  
}
