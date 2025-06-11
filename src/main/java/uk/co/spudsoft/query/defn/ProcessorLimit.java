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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Processor that curtails the output after the configured number of rows.
 * @author jtalbut
 */
@JsonDeserialize(builder = ProcessorLimit.Builder.class)
@Schema(description = """
                      Processor that curtails the output after the configured number of rows.
                      """
)
public class ProcessorLimit implements Processor {
  
  private final ProcessorType type;
  private final String name;
  private final int limit;

  @Override
  public ProcessorType getType() {
    return type;
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  /**
   * The limit on the number of rows that will be output by this processor.
   * @return the limit on the number of rows that will be output by this processor.
   */
  @Schema(description = """
                        The limit on the number of rows that will be output by this processor.
                        """
  )
  public int getLimit() {
    return limit;
  }
  
  /**
   * Builder class for ProcessorLimit.
   */
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class Builder {

    private ProcessorType type = ProcessorType.LIMIT;
    private String name;
    private int limit;

    private Builder() {
    }
    
    /**
     * Set the {@link ProcessorLimit#type} value in the builder.
     * @param value The value for the {@link ProcessorLimit#type}, must be {@link ProcessorType#DYNAMIC_FIELD}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder type(final ProcessorType value) {
      this.type = value;
      return this;
    }
    
    /**
     * Set the {@link ProcessorLimit#name} value in the builder.
     * @param value The value for the {@link ProcessorLimit#name}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder name(final String value) {
      this.name = value;
      return this;
    }

    /**
     * Set the {@link ProcessorLimit#limit} value in the builder.
     * @param value The value for the {@link ProcessorLimit#limit}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder limit(final int value) {
      this.limit = value;
      return this;
    }

    /**
     * Construct a new instance of the ProcessorLimit class.
     * @return a new instance of the ProcessorLimit class.
     */
    public ProcessorLimit build() {
      ProcessorLimit result = new ProcessorLimit(type, name, limit);
      result.validateType(ProcessorType.LIMIT, type);
      return result;
    }
  }

    /**
     * Construct a new instance of the ProcessorLimit.Builder class.
     * @return a new instance of the ProcessorLimit.Builder class.
     */
  public static ProcessorLimit.Builder builder() {
    return new ProcessorLimit.Builder();
  }

  private ProcessorLimit(final ProcessorType type, final String name, final int limit) {
    this.type = type;
    this.name = name;
    this.limit = limit;
  }
  
  
  
  
}
