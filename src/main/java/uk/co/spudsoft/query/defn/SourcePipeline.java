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
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collections;
import java.util.List;

/**
 * A SourcePipeline is the core part of a Pipeline, without the globally defined elements.
 * 
 * A SourcePipeline cannot be directly referenced externally, but is used within a Pipeline to declare the source and processing of the data.
 * 
 * @author jtalbut
 */
@JsonDeserialize(builder = SourcePipeline.Builder.class)
@Schema(description = """
                      <P>A SourcePipeline is the core part of a Pipeline, without the globally defined elements.</P>
                      <P>
                      A SourcePipeline cannot be directly referenced externally, but is used within a Pipeline to declare the source and processing of the data.
                      </P>
                      <P>
                      Every Pipeline is also a SourcePipeline.
                      </P>
                      """)
public class SourcePipeline {
  
  private final List<Processor> processors;

  /**
   * Processors to run on the data as it flows from the Source.
   * @return processors to run on the data as it flows from the Source.
   */
  @ArraySchema(
          schema = @Schema(
                  implementation = Processor.class
          )
          , arraySchema = @Schema(
                  type = "array"
                  , description = """
                                <P>Processors to run on the data as it flows from the Source.</P>
                                """
          )
          , minItems = 0
          , uniqueItems = true
  )
  public List<Processor> getProcessors() {
    return processors;
  }

  /**
   * Builder class for SourcePipeline.
   * @param <T> Subclass of this to use in fluent return values.
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Builder class should result in all instances being immutable when object is built")
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class Builder<T extends Builder<T>> {

    /**
     * The {@link SourcePipeline#processors} value in the builder.
     */
    protected List<Processor> processors;

    /**
     * Constructor.
     */
    protected Builder() {
    }

    /**
     * This, typed to T.
     * @return this, cast to type T.
     */
    @SuppressWarnings("unchecked")
    final T self() {
        return (T) this;
    }
    
    /**
     * Set the {@link SourcePipeline#processors} value in the builder.
     * @param value The value for the {@link SourcePipeline#processors}
     * @return this, so that this builder may be used in a fluent manner.
     */
    public T processors(final List<Processor> value) {
      this.processors = value;
      return self();
    }

    /**
     * Construct a new instance of the SourcePipeline class.
     * @return a new instance of the SourcePipeline class.
     */
    public SourcePipeline build() {
      return new SourcePipeline(processors);
    }
  }

  /**
   * Construct a new instance of the SourcePipeline.Builder class.
   * @return a new instance of the SourcePipeline.Builder class.
   */
  public static SourcePipeline.Builder<?> builder() {
    return new SourcePipeline.Builder<>();
  }

  /**
   * Constructor.
   * @param processors Processors to run on the data as it flows from the Source.
   */
  protected SourcePipeline(List<Processor> processors) {
    this.processors = processors == null ? Collections.emptyList() : Collections.unmodifiableList(processors);
  }
}
