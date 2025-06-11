/*
 * Copyright (C) 2022 jtalbut
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
package uk.co.spudsoft.query.defn;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.DiscriminatorMapping;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 *
 * Base class for all Processors.
 * 
 * Processors modify the data stream in flight.
 * 
 * @author jtalbut
 */
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME, 
  include = JsonTypeInfo.As.EXISTING_PROPERTY, 
  property = "type")
@JsonSubTypes({ 
  @JsonSubTypes.Type(value = ProcessorLimit.class, name = "LIMIT") 
  , @JsonSubTypes.Type(value = ProcessorDynamicField.class, name = "DYNAMIC_FIELD") 
})
@Schema(
        discriminatorProperty = "type"
        , discriminatorMapping = {
          @DiscriminatorMapping(schema = ProcessorLimit.class, value = "LIMIT") 
          , @DiscriminatorMapping(schema = ProcessorDynamicField.class, value = "DYNAMIC_FIELD") 
        }
        , description = """
                      Processors modify the data stream in flight.
                      """
)
public interface Processor {
  
  /**
   * The type of Processor being configured.
   * @return the type of Processor being configured.
   */
  @Schema(description = """
                        <P>The type of Processor being configured.</P>
                        """
          , requiredMode = Schema.RequiredMode.REQUIRED
  )
  ProcessorType getType();
  
  /**
   * Name that uniquely identifies this processor within the pipeline.
   * @return a name that uniquely identifies this processor within the pipeline.
   */
  @Schema(description = """
                        <P>Name that uniquely idenfities this processor within the pipeline.</P>
                        """
          , minLength = 1
          , maxLength = 60
          , requiredMode = Schema.RequiredMode.REQUIRED
  )
  String getName();
  
  /**
   * Validate that the {@link ProcessorType} configured is correct.
   * @param required the required {@link ProcessorType}.
   * @param actual the configured {@link ProcessorType}.
   * @throws IllegalArgumentException if the two types do not match.
   */
  default void validateType(ProcessorType required, ProcessorType actual) throws IllegalArgumentException {
    if (required != actual) {
      throw new IllegalArgumentException("Processor of type " + required + " configured with type " + actual);
    }
  }
  
}
