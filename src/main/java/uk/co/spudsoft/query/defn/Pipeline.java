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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Duration;
import java.util.List;
import uk.co.spudsoft.query.main.ImmutableCollectionTools;

/**
 * The Pipeline is the fundamental unit of processing in QueryEngine.
 * A single Pipeline takes data from a single {@link Source}, passes it through any number of {@link Processor}s and finally delivers it to a {@link Format}.
 * The {@link Processor}s themselves may pull in data from other {@link Source}s.
 * @author jtalbut
 */
@JsonDeserialize(builder = Pipeline.Builder.class)
@Schema(description = """
                      <P>The Pipeline is the fundamental unit of processing in QueryEngine.</P>
                      <P>
                       A single Pipeline takes data from a single Source, passes it through any number of Processors and finally delivers it to a Format.
                       The Processors within a Pipeline may pull in data from other Sources.
                      </P>
                      <P>
                      A Source usually requires an Endpoint to tell it where to get the data from.
                      This separation allows the same query to be used against multiple databases (potentially dynamically defined).
                      </P>
                      <P>
                      A minimal Pipeline, therefore, must consist of at least a Source and a Format, and usually an Endpoint (unless using the Test Source).
                      </P>
                      <P>
                      Pipelines may be considered either Interactive or Non-Interactive.
                      The user of an Interactive Pipeline always runs the Pipeline via a form, and does not need to consider the actual URL being used at all.
                      A Non-Interactive Pipeline is either used programatically or by being configured in some client system (such as PowerBI).
                      A Non-Interactive Pipeline can be distinguished by the user having to know or construct the URL for it at some point.
                      The distinctino is irrelevant to the Query Engine itself, but can help when configuring Pipelines.
                      """
)
public final class Pipeline extends SourcePipeline {

  /**
   * Builder class.
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Builder class should result in all instances being immutable when object is built")
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class Builder extends SourcePipeline.Builder<Pipeline.Builder> {

    private Builder() {
    }
    
    /**
     * Create a new instance of the {@link Pipeline}.
     * @return a new instance of the {@link Pipeline}.
     */
    @Override
    public Pipeline build() {
      return new Pipeline(source, processors);
    }

    /**
     * Set the {@link SourcePipeline#source} value in the builder.
     * @param value The value for the {@link SourcePipeline#source}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    @Override
    public Builder source(final Source value) {
      this.source = value;
      return this;
    }

    /**
     * Set the {@link SourcePipeline#processors} value in the builder.
     * @param value The value for the {@link SourcePipeline#processors}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    @Override
    public Builder processors(final List<Processor> value) {
      this.processors = value;
      return this;
    }
    
  }
/**
 * Construct a new Builder object.
 * @return a newly created Builder object.
 */
  public static Pipeline.Builder builder() {
    return new Pipeline.Builder();
  }

  private Pipeline(Source source, List<Processor> processors) {
    super(source, processors);
  }  
  
}
