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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Processor that curtails the output after the configured number of rows.
 * @author jtalbut
 */
@JsonDeserialize(builder = ProcessorQuery.Builder.class)
@Schema(description = """
                      Processor that filters output rows.
                      """
)
public class ProcessorQuery implements Processor {
    
  private final ProcessorType type;
  private final Condition condition;
  private final String name;
  private final String expression;
  
  @Override
  public ProcessorType getType() {
    return type;
  }
  
  @Override
  public Condition getCondition() {
    return condition;
  }  

  @Override
  public String getName() {
    return name;
  }

  /**
   * Get the FIQL expression that will be evaluated on each row.
   * @return the FIQL expression that will be evaluated on each row.
   */
  @Schema(description = """
                        A valid FIQL expression that will be evaluated on each row.
                        """
          , maxLength = 1000000
          , requiredMode = Schema.RequiredMode.REQUIRED
  )
  public String getExpression() {
    return expression;
  }
  
  /**
   * Builder class for ProcessorQuery.
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Builder class should result in all instances being immutable when object is built")
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class Builder {

    private ProcessorType type = ProcessorType.QUERY;
    private Condition condition;
    private String name;
    private String expression;

    private Builder() {
    }
    
    /**
     * Set the {@link ProcessorQuery#type} value in the builder.
     * @param value The value for the {@link ProcessorQuery#type}, must be {@link ProcessorType#DYNAMIC_FIELD}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder type(final ProcessorType value) {
      this.type = value;
      return this;
    }
    
    /**
     * Set the {@link ProcessorQuery#condition} value in the builder.
     * @param value The value for the {@link ProcessorQuery#condition}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder condition(final Condition value) {
      this.condition = value;
      return this;
    }

    /**
     * Set the {@link ProcessorQuery#name} value in the builder.
     * @param value The value for the {@link ProcessorQuery#name}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder name(final String value) {
      this.name = value;
      return this;
    }

    /**
     * Set the {@link ProcessorQuery#expression} value in the builder.
     * @param value The value for the {@link ProcessorQuery#expression}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder expression(final String value) {
      this.expression = value;
      return this;
    }

    /**
     * Construct a new instance of the ProcessorQuery class.
     * @return a new instance of the ProcessorQuery class.
     */
    public ProcessorQuery build() {
      ProcessorQuery result = new ProcessorQuery(type, condition, name, expression);
      result.validateType(ProcessorType.QUERY, type);
      return result;
    }
  }

  /**
   * Construct a new instance of the ProcessorQuery.Builder class.
   * @return a new instance of the ProcessorQuery.Builder class.
   */
  public static ProcessorQuery.Builder builder() {
    return new ProcessorQuery.Builder();
  }

  private ProcessorQuery(final ProcessorType type, final Condition condition, final String name, final String expression) {
    this.type = type;
    this.condition = condition;
    this.name = name;
    this.expression = expression;
  }
  
  
  
  
}
