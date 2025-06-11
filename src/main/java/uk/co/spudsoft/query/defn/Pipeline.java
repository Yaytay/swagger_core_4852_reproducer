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
  
  private final String title;
  private final String description;
  private final Condition condition;
  private final Duration cacheDuration;
  private final ImmutableList<RateLimitRule> rateLimitRules;
  private final ImmutableList<ArgumentGroup> argumentGroups;
  private final ImmutableList<Argument> arguments;
  private final ImmutableList<Endpoint> sourceEndpoints;
  private final ImmutableMap<String, Endpoint> sourceEndpointsMap;
  private final ImmutableList<DynamicEndpoint> dynamicEndpoints;

  @JsonIgnore
  private String sha256;

  /**
   * Get the hash of this pipeline.
   * <p>
   * The hash is a SHA256 hash of the raw bytes of the pipeline file after template processing if the entire file is a template.
   * <p>
   * This value is used at part of the keys for locating cached output from previous runs.
   * @return the hash of this pipeline.
   */
  public String getSha256() {
    return sha256;
  }

  /**
   * Set the hash of this pipeline.
   * <p>
   * The hash is a SHA256 hash of the raw bytes of the pipeline file after template processing if the entire file is a template.
   * <p>
   * This value is used at part of the keys for locating cached output from previous runs.
   * @param sha256 the hash of this pipeline.
   */
  public void setSha256(String sha256) {
    this.sha256 = sha256;
  }

  /**
   * The title of the Pipeline that will be used in the UI in preference to the filename.
   * <p>
   * The title is optional, but should usually be provided, particularly for Interactive Pipelines.
   * @return the title of the Pipeline that will be used in the UI in preference to the filename.
   */
  @Schema(description = """
                        <P>
                        The title of the Pipeline that will be used in the UI in preference to the filename.
                        </P>
                        <P>
                        The title is optional, but should usually be provided, particularly for Interactive Pipelines.
                        </P>
                        """
          , maxLength = 100
  )
  public String getTitle() {
    return title;
  }

  /**
   * A description of the Pipeline that will be used in the UI to provide information to the user.
   * <p>
   * The description is optional, but should always be provided for the sanity of your users.
   * <p>
   * The description should be kept relatively short as it will be included, in full, in the parameter gathering form for Interactive Pipelines.
   * @return a description of the Pipeline that will be used in the UI to provide information to the user.
   */
  @Schema(description = """
                        <P>
                        A description of the Pipeline that will be used in the UI to provide information to the user.
                        </P>
                        <P>
                        The description is optional, but should always be provided for the sanity of your users.
                        </P>
                        <P>
                        The description should be kept relatively short as it will be included, in full, in the parameter gathering form for Interactive Pipelines.
                        </P>
                        """
          , implementation = String.class
          , maxLength = 1000000
  )
  public String getDescription() {
    return description;
  }

  /**
   * A condition that constrains who can use the Pipeline.
   * @return a condition that constrains who can use the Pipeline.
   */
  @Schema(description = """
                        <P>
                        A condition that constrains who can use the Pipeline.
                        </P>
                        """
          , implementation = String.class
          , externalDocs = @ExternalDocumentation(description = "Conditions", url = "")
          , maxLength = 1000000
    )
  public Condition getCondition() {
    return condition;
  }

  /**
   * The time for which the results of this pipeline should be cached.
   * <p>
   * The cache key is made of:
   * <UL>
   * <LI>The full request URL.
   * <LI>Headers:
   * <UL>
   * <LI>Accept
   * <LI>Accept-Encoding
   * </UL>
   * <LI>Token fields:
   * <UL>
   * <LI>aud
   * <LI>iss
   * <LI>sub
   * <LI>groups
   * <LI>roles
   * </UL>
   * </UL>
   * Ordering of groups and roles is relevant.
   * <p>
   * Note that the fileHash must also match, but isn't built into the key (should usually match because of the use of the inclusion of full URL).
   * 
   * @return the time for which the results of this pipeline should be cached.
   */
  @Schema(description = """
                        <P>
                        The time for which the results of this pipeline should be cached.
                        </P>
                        <P>
                        The cache key is made of:
                        <UL>
                        <LI>The full request URL.
                        <LI>Headers:
                        <UL>
                        <LI>Accept
                        <LI>Accept-Encoding
                        </UL>
                        <LI>Token fields:
                        <UL>
                        <LI>aud
                        <LI>iss
                        <LI>sub
                        <LI>groups
                        <LI>roles
                        </UL>
                        </UL>
                        Ordering of groups and roles is relevant.
                        </P>
                        <P>
                        Note that the fileHash must also match, but isn't built into the key (should usually match because of the use of the inclusion of full URL).
                        """
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  public Duration getCacheDuration() {
    return cacheDuration;
  }
  
  /**
   * Helper method that returns true if the cacheDuration contains a valid value.
   * @return true if the cacheDuration contains a valid value.
   */
  @JsonIgnore
  public boolean supportsCaching() {
    return cacheDuration != null && cacheDuration.isPositive();
  }
  
  /**
   * Rate limit rules that constrain how frequently pipelines can be run.
   * @return rate limit rules that constrain how frequently pipelines can be run.
   */
  @ArraySchema(
          arraySchema = @Schema(description = """
                        <P>
                        Rate limit rules that constrain how frequently pipelines can be run.
                        </P>
                        """
          )
          , schema = @Schema(
                      implementation = RateLimitRule.class
          )
          , minItems = 0
          , uniqueItems = true
  )
  public List<RateLimitRule> getRateLimitRules() {
    return rateLimitRules;
  }    
    
  /**
   * Declared argument groups to the Pipeline.
   * <p>
   * Arguments may be placed into groups in the user interface, outside of the user interface groups serve no purpose.
   * 
   * @return the declared argument groups to the Pipeline.
   */
  @ArraySchema(
          arraySchema = @Schema(
                  description = """
                          <P>Declared arguments to the Pipeline.</P>
                          <P>
                          Arguments may be placed into groups in the user interface, outside of the user interface groups serve no purpose.
                          </P>
                          """
          )
          , schema = @Schema(
                  implementation = ArgumentGroup.class
          )
          , minItems = 0
          , uniqueItems = true
  )
  public List<ArgumentGroup> getArgumentGroups() {
    return argumentGroups;
  }
    
  /**
   * Declared arguments to the Pipeline.
   * <p>
   * Pipelines can receive arguments via the HTTP query string.
   * Any arguments may be provided and may be processed by the templates of the pipeline, even if they are not
   * declared here.
   * Declare all arguments here, otherwise no-one will know that they exist unless they read the pipeline definition.
   * <p>
   * The order in which Arguments are defined here is relevant as it affects the order in which they will be displayed for
   * Interactive Pipelines.
   * The order in which Arguments are provided in the query string is only relevant if an Argument can take multiple values (in which
   * case they will be presented to the query in the order that they appear in the query string, regardless of any other arguments appearing
   * between them).
   * @return the declared arguments to the Pipeline.
   */
  @ArraySchema(
          arraySchema = @Schema(
                  description = """
                          <P>Declared arguments to the Pipeline.</P>
                          <P>
                          Pipelines can receive arguments via the HTTP query string.
                          Any arguments may be provided and may be processed by the templates of the pipeline, even if they are not
                          declared here.
                          Declare all arguments here, otherwise no-one will know that they exist unless they read the pipeline definition.
                          </P>
                          <P>
                          The order in which Arguments are defined here is relevant as it affects the order in which they will be displayed for
                          Interactive Pipelines.
                          The order in which Arguments are provided in the query string is only relevant if an Argument can take multiple values (in which
                          case they will be presented to the query in the order that they appear in the query string, regardless of any other arguments appearing
                          between them).
                          </P>
                          """
          )
          , schema = @Schema(
                  implementation = Argument.class
          )
          , minItems = 0
          , uniqueItems = true
  )
  public List<Argument> getArguments() {
    return arguments;
  }

  /**
   * The endpoints used by the sources in the pipeline as a List.
   * <P>
   * Endpoints are the actual providers of data to the Pipeline.
   * Most Sources (all except the TestSource) work through an Endpoint.
   * <P>
   * The segregation between Source and Endpoint allows a single Source to work with multiple Endpoints.
   * 
   * @return the endpoints used by the sources in the pipeline.
   */
  @Schema(
          type = "object"
          , description = """
                          <P>
                          The endpoints used by the sources in the pipeline.
                          </P>
                          <P>
                          Endpoints are the actual providers of data to the Pipeline.
                          Most Sources (all except the TestSource) work through an Endpoint.
                          </P>
                          <P>
                          The segregation between Source and Endpoint allows a single Source to work with multiple Endpoints.
                          </P>
                          """
  )
  public List<Endpoint> getSourceEndpoints() {
    return sourceEndpoints;
  }

  /**
   * The endpoints used by the sources in the pipeline as a Map.
   * <P>
   * This map is built in the constructor - endpoints are configured as a List.
   * 
   * @return the endpoints used by the sources in the pipeline as a Map.
   */
  @JsonIgnore
  public ImmutableMap<String, Endpoint> getSourceEndpointsMap() {
    return sourceEndpointsMap;
  }

  /**
   * Sub-Pipelines that can be run prior to the main Pipeline in order to generate more SourceEndpoints.
   * <p>
   * The expected use is for the source to query a database that contains connection strings (in vertx format, not JDBC format)
   * based on information contained in the request (usually extracted from a JWT).
   * In this way a single pipeline can support multiple databases based upon request content.
   * <p>
   * Most of the properties of the DynamicEndpointSource have default values and any fields that do not exist in the
   * results stream from the source pipeline will be silently ignored, so the DynamicEndpointSource usually requires minimal configuration.
   * <p>
   * If generated endpoints have a condition they will be silently dropped unless the condition is met.
   * All remaining endpoints generated by the DynamicEndpointSource will be added to the endpoints usable by the outer query in the order they are returned by the source.
   * If endpoints do not have unique keys this does mean that later ones will overwrite earlier ones.
   * <p>
   * The original endpoints that existed before the DynamicEndpointSource do not have special protection
   * , if the DynamicEndpointSource generates endpoints with the same key as existing endpoints they will be overwritten.
   * @return the sub-Pipelines that can be run prior to the main Pipeline in order to generate more SourceEndpoints.
   */
  @ArraySchema(
          arraySchema = @Schema(
                  description = """
                                  <P>Sub-Pipelines that can be run prior to the main Pipeline in order to generate more SourceEndpoints.</P>
                                  <P>
                                  The expected use is for the source to query a database that contains connection strings (in vertx format, not JDBC format)
                                  based on information contained in the request (usually extracted from a JWT).
                                  In this way a single pipeline can support multiple databases based upon request content.
                                  </P>
                                  <P>
                                  Most of the properties of the DynamicEndpointSource have default values and any fields that do not exist in the
                                  results stream from the source pipeline will be silently ignored, so the DynamicEndpointSource usually requires minimal configuration.
                                  </P>
                                  <P>
                                  If generated endpoints have a condition they will be silently dropped unless the condition is met.
                                  All remaining endpoints generated by the DynamicEndpointSource will be added to the endpoints usable by the outer query in the order they are returned by the source.
                                  If endpoints do not have unique keys this does mean that later ones will overwrite earlier ones.
                                  </P>
                                  <P>
                                  The original endpoints that existed before the DynamicEndpointSource do not have special protection
                                  , if the DynamicEndpointSource generates endpoints with the same key as existing endpoints they will be overwritten.
                                  </P>
                                  """
          )
          , schema = @Schema(
                  implementation = DynamicEndpoint.class
          )
          , minItems = 0
          , uniqueItems = true
  )
  public List<DynamicEndpoint> getDynamicEndpoints() {
    return dynamicEndpoints;
  }
    
  /**
   * Builder class.
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Builder class should result in all instances being immutable when object is built")
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class Builder extends SourcePipeline.Builder<Pipeline.Builder> {

    private String title;
    private String description;
    private Condition condition;
    private Duration cacheDuration;
    private List<RateLimitRule> rateLimitRules;
    private List<ArgumentGroup> argumentGroups;
    private List<Argument> arguments;
    private List<Endpoint> sourceEndpoints;
    private List<DynamicEndpoint> dynamicEndpoints;

    private Builder() {
    }
    
    /**
     * Create a new instance of the {@link Pipeline}.
     * @return a new instance of the {@link Pipeline}.
     */
    @Override
    public Pipeline build() {
      return new Pipeline(title, description, condition, cacheDuration, rateLimitRules, argumentGroups, arguments, sourceEndpoints, source, dynamicEndpoints, processors);
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
    
    /**
     * Set the {@link Pipeline#title} value in the builder.
     * @param value The value for the {@link Pipeline#title}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder title(final String value) {
      this.title = value;
      return this;
    }

    /**
     * Set the {@link Pipeline#description} value in the builder.
     * @param value The value for the {@link Pipeline#description}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder description(final String value) {
      this.description = value;
      return this;
    }

    /**
     * Set the {@link Pipeline#condition} value in the builder.
     * @param value The value for the {@link Pipeline#condition}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder condition(final Condition value) {
      this.condition = value;
      return this;
    }

    /**
     * Set the {@link Pipeline#cacheDuration} value in the builder.
     * @param value The value for the {@link Pipeline#cacheDuration}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder cacheDuration(final Duration value) {
      this.cacheDuration = value;
      return this;
    }

    /**
     * Set the {@link Pipeline#rateLimitRules} value in the builder.
     * @param value The value for the {@link Pipeline#rateLimitRules}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder rateLimitRules(final List<RateLimitRule> value) {
      this.rateLimitRules = value;
      return this;
    }

    /**
     * Set the {@link Pipeline#argumentGroups} value in the builder.
     * @param value The value for the {@link Pipeline#argumentGroups}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder argumentGroups(final List<ArgumentGroup> value) {
      this.argumentGroups = value;
      return this;
    }

    /**
     * Set the {@link Pipeline#arguments} value in the builder.
     * @param value The value for the {@link Pipeline#arguments}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder arguments(final List<Argument> value) {
      this.arguments = value;
      return this;
    }
    
    /**
     * Set the {@link Pipeline#sourceEndpoints} value in the builder.
     * @param value The value for the {@link Pipeline#sourceEndpoints}
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder sourceEndpoints(final List<Endpoint> value) {
      this.sourceEndpoints = value;
      return this;
    }

    /**
     * Set the {@link Pipeline#dynamicEndpoints} value in the builder.
     * @param value The value for the {@link Pipeline#dynamicEndpoints}
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder dynamicEndpoints(final List<DynamicEndpoint> value) {
      this.dynamicEndpoints = value;
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

  private Pipeline(String title, String description, Condition condition, Duration cacheDuration, List<RateLimitRule> rateLimitRules, List<ArgumentGroup> argumentGroups, List<Argument> arguments, List<Endpoint> sourceEndpoints, Source source, List<DynamicEndpoint> dynamicEndpoints, List<Processor> processors) {
    super(source, processors);
    this.title = title;
    this.description = description;
    this.condition = condition;
    this.cacheDuration = cacheDuration == null ? Duration.ZERO : cacheDuration;
    this.rateLimitRules = ImmutableCollectionTools.copy(rateLimitRules);
    this.argumentGroups = ImmutableCollectionTools.copy(argumentGroups);
    this.arguments = ImmutableCollectionTools.copy(arguments);
    this.sourceEndpoints = ImmutableCollectionTools.copy(sourceEndpoints);    
    this.sourceEndpointsMap = ImmutableCollectionTools.listToMap(sourceEndpoints, e -> e.getName());
    this.dynamicEndpoints = ImmutableCollectionTools.copy(dynamicEndpoints);
  }  
  
}
