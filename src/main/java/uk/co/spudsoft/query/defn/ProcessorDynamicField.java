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
import com.google.common.collect.ImmutableList;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import uk.co.spudsoft.query.main.ImmutableCollectionTools;

/**
 * Processor that takes in multiple streams and uses them to dynamically add fields to the primary stream.
 * 
 * This is aimed at converting tables of key/value pairs into fields.
 * 
 * Two child pipelines must be defined:
 * <ul>
 * <li>The definition  pipeline, that is queried in its entirety at the beginning and that defines the columns that will be found.
 * <li>The values pipeline, that is queried in parallel with the main stream and the supplies the data for the dynamic columns.
 * </ul>
 * 
 * The definition pipeline must provide four fields:
 * <ul>
 * <li>The ID for the dynamic column to be added - this value must correspond to the ID from the values pipeline.
 * <li>The name for the dynamic column - this will be the name of the newly created field.
 * <li>The type for the dynamic column - one of the{@link uk.co.spudsoft.query.defn.DataType} values.
 * <li>The name of the column in the values stream that will contain the actual value.
 * </ul>
 * The names of these four fields can be controlled using the field*Column properties on this processor (though they have sensible defaults).
 * 
 * The values pipeline must provide at least three fields:
 * <ul>
 * <li>The parent ID, that matches the ID of the data row in the main pipeline.
 * <li>The field ID, that matches one of the rows returned from the definition pipeline.
 * <li>The value field, whose name must match that defined for the selected field.
 * </ul>
 * 
 * As a streaming processor this processor requires the main pipeline and the values pipeline to be sorted by the same ID (the parent ID from the point of view of this processor).
 * 
 * The processor works by:
 * <ol>
 * <li>If the parent ID is greater than the values ID, skip through values until it isn't.
 * <li>If the values ID is greater than the parent ID, skip through parent rows until it isn't.
 * <li>While the two IDs match:
 * <ol>
 * <li>Find the definition for the current value.
 * <li>Get the name of the value field from the field definition.
 * <li>Add a new field to the parent data row with the name from the field definition and the value from the value field of the value row.
 * </ol>
 * </ol>
 * 
 * 
 * @author jtalbut
 */
@JsonDeserialize(builder = ProcessorDynamicField.Builder.class)
@Schema(description = 
        """
        Processor that takes in multiple streams and uses them to dynamically add fields to the primary stream.

        Two child pipelines must be defined:
        <ul>
        <li>The definition  pipeline, that is queried in its entirety at the beginning and that defines the columns that will be found.
        <li>The values pipeline, that is queried in parallel with the main stream and the supplies the data for the dynamic columns.
        </il>
        The definition pipeline must provide four fields:
        <ul>
        <li>The ID for the dynamic column to be added - this value must correspond to the ID from the values pipeline.
        <li>The name for the dynamic column - this will be the name of the newly created field.
        <li>The type for the dynamic column - one of the{@link uk.co.spudsoft.query.defn.DataType} values.
        <li>The name of the column in the values stream that will contain the actual value.
        </ul>
        The names of these four fields can be controlled using the field*Column properties on this processor (though they have sensible defaults).
        
        The values pipeline must provide at least three fields:
        <ul>
        <li>The parent ID, that matches the ID of the data row in the main pipeline.
        <li>The field ID, that matches one of the rows returned from the definition pipeline.
        <li>The value field, whose name must match that defined for the selected field.
        </ul>
        
        As a streaming processor this processor requires the main pipeline and the values pipeline to be sorted by the same ID (the parent ID from the point of view of this processor).
        
        The processor works by:
        <ol>
        <li>If the parent ID is greater than the values ID, skip through values until it isn't.
        <li>If the values ID is greater than the parent ID, skip through parent rows until it isn't.
        <li>While the two IDs match:
        <ol>
        <li>Find the definition for the current value.
        <li>Get the name of the value field from the field definition.
        <li>Add a new field to the parent data row with the name from the field definition and the value from the value field of the value row.
        </ol>
        </ol>
        """
)
public class ProcessorDynamicField implements Processor {

  private final ProcessorType type;
  private final String name;
  
  private final SourcePipeline fieldDefns;
   
  @Override
  public ProcessorType getType() {
    return type;
  }
  
  @Override
  public String getName() {
    return name;
  }
  
  /**
   * Get the feed for the field definitions.
   * 
   * This data feed should result in four columns:
   * <ul>
   * <li>fieldIdColumn - The ID value that will be used to refer to the field from the values feed.
   * <li>fieldNameColumn - The name that the resultant field will be given.
   * <li>fieldTypeColumn - The type of the resultant field (will be processed using {@link uk.co.spudsoft.query.defn.DataType#valueOf(java.lang.String)}/
   * <li>fieldColumnColumn - The column in the field values feed that contains the actual value for this field.
   * </ul>
   * The fields will be added to the parent feed in the order of the rows returned by this query (regardless of the ordering in the fieldValues feed).
   * 
   * @return a SourcePipeline defining a feed that defines additional fields to add to the parent feed.
   */
  @Schema(description = """
                        Get the feed for the field definitions.
                        
                        This data feed should result in four columns:
                        <ul>
                        <li>fieldIdColumn - The ID value that will be used to refer to the field from the values feed.
                        <li>fieldNameColumn - The name that the resultant field will be given.
                        <li>fieldTypeColumn - The type of the resultant field (will be processed using {@link uk.co.spudsoft.query.defn.DataType#valueOf(java.lang.String)}/
                        <li>fieldColumnColumn - The column in the field values feed that contains the actual value for this field.
                        </ul>
                        The fields will be added to the parent feed in the order of the rows returned by this query (regardless of the ordering in the fieldValues feed).
                        """)
  public SourcePipeline getFieldDefns() {
    return fieldDefns;
  }

  /**
   * Builder class for ProcessorDynamicField.
   */
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Builder class should result in all instances being immutable when object is built")
  public static class Builder {

    private ProcessorType type = ProcessorType.DYNAMIC_FIELD;
    private String name;
    private SourcePipeline fieldDefns;

    private Builder() {
    }
    
    /**
     * Set the {@link ProcessorDynamicField#type} value in the builder.
     * @param value The value for the {@link ProcessorDynamicField#type}, must be {@link ProcessorType#DYNAMIC_FIELD}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder type(final ProcessorType value) {
      this.type = value;
      return this;
    }

    /**
     * Set the {@link ProcessorDynamicField#name} value in the builder.
     * @param value The value for the {@link ProcessorDynamicField#name}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder name(final String value) {
      this.name = value;
      return this;
    }

    /**
     * Set the {@link ProcessorDynamicField#fieldDefns} value in the builder.
     * @param value The value for the {@link ProcessorDynamicField#fieldDefns}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder fieldDefns(final SourcePipeline value) {
      this.fieldDefns = value;
      return this;
    }

    /**
     * Construct a new instance of the ProcessorDynamicField class.
     * @return a new instance of the ProcessorDynamicField class.
     */
    public ProcessorDynamicField build() {
      ProcessorDynamicField result = new ProcessorDynamicField(type, name, fieldDefns);
      result.validateType(ProcessorType.DYNAMIC_FIELD, type);
      return result;
    }
  }

  /**
   * Construct a new instance of the ProcessorDynamicField.Builder class.
   * @return a new instance of the ProcessorDynamicField.Builder class.
   */
  public static ProcessorDynamicField.Builder builder() {
    return new ProcessorDynamicField.Builder();
  }

  private ProcessorDynamicField(final ProcessorType type, final String name
          , final SourcePipeline fieldDefns) {
    this.type = type;
    this.name = name;
    this.fieldDefns = fieldDefns;
  }
}
