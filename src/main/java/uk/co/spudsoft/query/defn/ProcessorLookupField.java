/*
 * Copyright (C) 2024 jtalbut
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
 * Argument to the LookupProcessor that specifies the field containing the key to be looked up and the field that the value will be written to.
 * @author jtalbut
 */
@JsonDeserialize(builder = ProcessorLookupField.Builder.class)
@Schema(description = """
                      Argument to the LookupProcessor that specifies the field containing the key to be looked up and the field that the value will be written to.
                      """
)
public class ProcessorLookupField {
  
  private final String keyField;
  private final String valueField;
  private final Condition condition;

  /**
   * The name of the field in the primary stream that is to be looked up in the map.
   * @return the name of the field in the primary stream that is to be looked up in the map.
   */
  @Schema(description = """
                        The name of the field in the primary stream that is to be looked up in the map.
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.REQUIRED
  )
  public String getKeyField() {
    return keyField;
  }

  /**
   * The name of the field to be created in the stream that is to be set by the value from the map.
   * @return the name of the field to be created in the stream that is to be set by the value from the map.
   */
  @Schema(description = """
                        The name of the field to be created in the stream that is to be set by the value from the map.
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.REQUIRED
  )
  public String getValueField() {
    return valueField;
  }
  
  /**
   * Get any condition that applies to this field.
   * 
   * This can be used to exclude fields based upon input conditions.
   * The condition will be evaluated once at the beginning of the process and will not have access to each output row.
   * 
   * @return any condition that applies to this field.
   */
  @Schema(description = """
                        Any condition that applies to this field.
                        <P>
                        This can be used to exclude fields based upon input conditions.
                        The condition will be evaluated once at the beginning of the process and will not have access to each output row.
                        """
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  public Condition getCondition() {
    return condition;
  }    

  /**
   * Builder class for ProcessorLookupField.
   */
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class Builder {

    private String keyField;
    private String valueField;
    private Condition condition;

    private Builder() {
    }

    /**
     * Set the {@link ProcessorLookupField#keyField} value in the builder.
     * @param value The value for the {@link ProcessorLookupField#keyField}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder keyField(final String value) {
      this.keyField = value;
      return this;
    }

    /**
     * Set the {@link ProcessorLookupField#valueField} value in the builder.
     * @param value The value for the {@link ProcessorLookupField#valueField}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder valueField(final String value) {
      this.valueField = value;
      return this;
    }

    /**
     * Set the {@link ProcessorLookupField#condition} value in the builder.
     * @param value The value for the {@link ProcessorLookupField#condition}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder condition(final Condition value) {
      this.condition = value;
      return this;
    }

    /**
     * Construct a new instance of the ProcessorLookupField class.
     * @return a new instance of the ProcessorLookupField class.
     */
    public ProcessorLookupField build() {
      return new uk.co.spudsoft.query.defn.ProcessorLookupField(keyField, valueField, condition);
    }
  }

  /**
   * Construct a new instance of the ProcessorLookupField.Builder class.
   * @return a new instance of the ProcessorLookupField.Builder class.
   */
  public static ProcessorLookupField.Builder builder() {
    return new ProcessorLookupField.Builder();
  }

  private ProcessorLookupField(final String keyField, final String valueField, final Condition condition) {
    this.keyField = keyField;
    this.valueField = valueField;
    this.condition = condition;
  }
  
  
  
}
