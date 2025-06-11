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
import com.google.common.net.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.format.DateTimeFormatter;

/**
 * Output the data stream in JSON.
 * This format has no specific configuration options.
 * @author jtalbut
 */
@JsonDeserialize(builder = FormatJson.Builder.class)
@Schema(description = """
                      Configuration for an output format of JSON.
                      There are no formatting options for JSON output.
                      """)
public class FormatJson implements Format {

  private final FormatType type;
  private final String name;
  private final String description;
  private final String extension;
  private final String filename;
  private final MediaType mediaType;
  private final boolean hidden;
  
  private final String dataName;
  private final String metadataName;
  private final boolean compatibleTypeNames;
  private final String dateFormat;
  private final String dateTimeFormat;
  private final String timeFormat;
  private final String decimalFormat;
  private final String booleanFormat;
  
  
  @Override
  public FormatType getType() {
    return type;
  }

  /**
   * Get the name of the format, as will be used on query string parameters.
   * No two formats in a single pipeline should have the same name.
   * @return the name of the format, as will be used on query string parameters.
   */
  @Override
  @Schema(description = """
                        <P>The name of the format.</P>
                        <P>
                        The name is used to determine the format based upon the '_fmt' query string argument.
                        </P>
                        <P>
                        It is an error for two Formats to have the same name.
                        This is different from the other Format determinators which can be repeated, the name is the
                        ultimate arbiter and must be unique.
                        This ensures that all configured Formats can be used.
                        </P>
                        """
          , maxLength = 100
          , defaultValue = "json"
  )
  public String getName() {
    return name;
  }

  /**
   * Get the description of the format, optional value to help UI users choose which format to use.
   * @return the description of the format.
   */
  @Schema(description = """
                        <P>The description of the format.</P>
                        <P>
                        The description is used in UIs to help users choose which format to use.
                        </P>
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @Override
  public String getDescription() {
    return description;
  }

  /**
   * Get the extension of the format.
   * The extension is used to determine the format based upon the URL path and also to set the default filename for the content-disposition header.
   * If multiple formats have the same extension the first in the list will be used.
   * @return the extension of the format.
   */
  @Override
  @Schema(description = """
                        <P>The extension of the format.</P>
                        <P>
                        The extension is used to determine the format based upon the URL path and also to set the default filename for the content-disposition header.
                        If multiple formats have the same extension the first in the list will be used.
                        </P>
                        """
          , maxLength = 20
          , defaultValue = "json"
  )
  public String getExtension() {
    return extension;
  }

    /**
   * Get the filename to use in the Content-Disposition header.
   * 
   * If not specified then the leaf name of the pipeline (with extension the value of {@link #getExtension()} appended) will be used.
   *
   * @return the filename of the format.
   */
  @Schema(description = """
                        <P>The filename to specify in the Content-Disposition header.</P>
                        <P>
                        If not specified then the leaf name of the pipeline (with extension the value of {@link #getExtension()} appended) will be used.
                        </P>
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  @Override
  public String getFilename() {
    return filename;
  }

  /**
   * Get the media type of the format.
   * The media type is used to determine the format based upon the Accept header in the request.
   * If multiple formats have the same media type the first in the list will be used.
   * The media type will also be set as the Content-Type header in the response.
   * @return the media type of the format.
   */
  @Override
  @Schema(description = """
                        <P>The media type of the format.</P>
                        <P>
                        The media type is used to determine the format based upon the Accept header in the request.
                        If multiple formats have the same media type the first in the list will be used.
                        </P>
                        <P>
                        The media type will also be set as the Content-Type header in the response.
                        </P>
                        """
          , defaultValue = "application/json"
          , implementation = String.class
  )
  public MediaType getMediaType() {
    return mediaType;
  }

  @Schema(description = """
                        <P>Whether the format should be removed from the list when presented as an option to users.
                        <P>
                        This has no effect on processing and is purely a UI hint.
                        <P>
                        When hidden is true the format should removed from any UI presenting formats to the user.
                        </P>
                        """
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
          , defaultValue = "false"
  )
  @Override
  public boolean isHidden() {
    return hidden;
  }

  /**
   * The name of the parent data element in the output JSON.
   * <P>
   * JSON output consists of an array of objects, with an object for each row of the output.
   * <P>
   * By default this is the only contents in the output (the root of the JSON will be an array).
   * <P>
   * If dataName is set the output will instead be an object containing the array.
   * 
   * @return name of the parent data element in the output JSON.
   */
  @Schema(description = """
                        The name of the parent data element in the output JSON.
                        <P>
                        JSON output consists of an array of objects, with an object for each row of the output.
                        <P>
                        By default this is the only contents in the output (the root of the JSON will be an array).
                        <P>
                        If dataName is set the output will instead be an object containing the array.
                        </P>
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  public String getDataName() {
    return dataName;
  }

  /**
   * The name of the metadata element in the output JSON.
   * <P>
   * JSON output consists of an array of objects, with an object for each row of the output.
   * <P>
   * By default this is the only contents in the output (the root of the JSON will be an array) and there will be no information about the field types in the output.
   * <P>
   * If dataName and metadataName are both set the output will instead be an object containing the array and there will be an object containing a description of the output.
   * The metadata will contain two elements:
   * <UL>
   * <LI>The name of the feed.
   * <LI>An object describing the type of each field in the output.
   * </UL>
   * 
   * @return name of the parent data element in the output JSON.
   */
  @Schema(description = """
                        The name of the metadata element in the output JSON.
                        <P>
                        JSON output consists of an array of objects, with an object for each row of the output.
                        <P>
                        By default this is the only contents in the output (the root of the JSON will be an array) and there will be no information about the field types in the output.
                        <P>
                        If dataName and metadataName are both set the output will instead be an object containing the array and there will be an object containing a description of the output.
                        The metadata will contain two elements:
                        <UL>
                        <LI>The name of the feed.
                        <LI>An object describing the type of each field in the output.
                        </UL>
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  public String getMetadataName() {
    return metadataName;
  }

  /**
   * When set to true the types output in the metadata will be recorded in lowercase and with Boolean shortened to bool.
   * <P>
   * By default, no metadata is output at all, if both metadataName and dataName are set then a separate metadata list will be output.
   * This will list all the fields in the pipeline along with their data types.
   * <P>
   * By default the types will be given as the names from the {@link DataType} enum.
   * If compatibleTypeNames is true the type names will all be in lower case and boolean will be shortened to bool.
   * <p>
   * The default is to not output metadata at all, and to not change the case of type names if metadata is output.
   * 
   * @return true if the types output in the metadata structure should be in lowercase.
   */
  @Schema(description = """
                        When set to true the types output in the metadata will be recorded in lowercase and with Boolean shortened to bool.
                        <P>
                        By default, no metadata is output at all, if both metadataName and dataName are set then a separate metadata list will be output.
                        This will list all the fields in the pipeline along with their data types.
                        <P>
                        By default the types will be given as the names from the {@link DataType} enum.
                        If compatibleTypeNames is true the type names will all be in lower case and boolean will be shortened to bool.
                        <p>
                        The default is to not output metadata at all, and to not change the case of type names if metadata is output.
                        """
          , defaultValue = "false"
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  public boolean isCompatibleTypeNames() {
    return compatibleTypeNames;
  }

  /**
   * Get the Java format to use for date fields.
   * <P>
   * To be processed by a Java {@link DateTimeFormatter}.
   * 
   * @return the Java format to use for date fields.
   */
  @Schema(description = """
                        The Java format to use for date fields.
                        <P>
                        This value will be used by the Java DateTimeFormatter to format dates.
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
          , defaultValue = "yyyy-mm-dd"
  )
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * Get the Java format to use for date/time columns.
   * <P>
   * To be processed by a Java {@link DateTimeFormatter}, this can either be a DateTimeFormatter pattern or one of the predefined formats.
   * <table class="striped" style="text-align:left">
   * <caption>Predefined Formatters</caption>
   * <thead>
   * <tr>
   * <th scope="col">Formatter</th>
   * <th scope="col">Description</th>
   * <th scope="col">Example</th>
   * </tr>
   * </thead>
   * <tbody>
   * <tr>
   * <th scope="row"> {@link DateTimeFormatter#BASIC_ISO_DATE}</th>
   * <td>Basic ISO date </td> <td>'20111203'</td>
   * </tr>
   * <tr>
   * <th scope="row"> {@link DateTimeFormatter#ISO_LOCAL_DATE}</th>
   * <td> ISO Local Date </td>
   * <td>'2011-12-03'</td>
   * </tr>
   * <tr>
   * <th scope="row"> {@link DateTimeFormatter#ISO_DATE}</th>
   * <td> ISO Date with or without offset </td>
   * <td> '2011-12-03+01:00'; '2011-12-03'</td>
   * </tr>
   * <tr>
   * <th scope="row"> {@link DateTimeFormatter#ISO_LOCAL_TIME}</th>
   * <td> Time without offset </td>
   * <td>'10:15:30'</td>
   * </tr>
   * <tr>
   * <th scope="row"> {@link DateTimeFormatter#ISO_TIME}</th>
   * <td> Time with or without offset </td>
   * <td>'10:15:30+01:00'; '10:15:30'</td>
   * </tr>
   * <tr>
   * <th scope="row"> {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME}</th>
   * <td> ISO Local Date and Time </td>
   * <td>'2011-12-03T10:15:30'</td>
   * </tr>
   * <tr>
   * <th scope="row"> {@link DateTimeFormatter#ISO_ORDINAL_DATE}</th>
   * <td> Year and day of year </td>
   * <td>'2012-337'</td>
   * </tr>
   * <tr>
   * <th scope="row"> {@link DateTimeFormatter#ISO_WEEK_DATE}</th>
   * <td> Year and Week </td>
   * <td>'2012-W48-6'</td>
   * </tr>
   * <tr>
   * <th scope="row"> EPOCH_SECONDS</th>
   * <td> Seconds since the epoch (1970-01-01)</td>
   * <td>1684158330L</td>
   * </tr>
   * <tr>
   * <th scope="row"> EPOCH_MILLISECONDS</th>
   * <td> Milliseconds since the epoch (1970-01-01)</td>
   * <td>1684158330120L</td>
   * </tr>
   *  
   * </tbody></table>
   * 
   * <table class="striped" style="text-align:left">
   * <caption>The following predefined formats all require zone/offset data that will be assumed to be UTC.</caption>
   * <thead>
   * <tr>
   * <th scope="col">Formatter</th>
   * <th scope="col">Description</th>
   * <th scope="col">Example</th>
   * </tr>
   * </thead>
   * <tbody>
   *  
   * <tr>
   * <th scope="row"> ISO_OFFSET_DATE</th>
   * <td> ISO Date with offset </td>
   * <td>'2023-05-15Z'</td>
   * </tr>
   *  
   * <tr>
   * <th scope="row"> ISO_OFFSET_TIME</th>
   * <td> Time with offset </td>
   * <td>'13:45:30.12Z'</td>
   * </tr>
   *  
   * <tr>
   * <th scope="row"> ISO_OFFSET_DATE_TIME</th>
   * <td> Date Time with Offset </td>
   * <td>'2023-05-15T13:45:30.12Z'</td>
   * </tr>
   *  
   * <tr>
   * <th scope="row"> ISO_ZONED_DATE_TIME</th>
   * <td> Zoned Date Time </td>
   * <td>'2023-05-15T13:45:30.12Z'</td>
   * </tr>
   *  
   * <tr>
   * <th scope="row"> ISO_DATE_TIME</th>
   * <td> Date and time with ZoneId </td>
   * <td>'2023-05-15T13:45:30.12Z'</td>
   * </tr>
   *  
   * <tr>
   * <th scope="row"> ISO_INSTANT</th>
   * <td> Date and Time of an Instant </td>
   * <td>'2023-05-15T13:45:30.120Z'</td>
   * </tr>
   *  
   * <tr>
   * <th scope="row"> RFC_1123_DATE_TIME</th>
   * <td> RFC 1123 / RFC 822 </td>
   * <td>'Mon, 15 May 2023 13:45:30 GMT'</td>
   * </tr>                      
   *  
   * </tbody>
   * </table>
   * <p>
   * The default output (when the format is not set) is that of {@link java.time.LocalDateTime#toString()} method, specifically, the output will be one of the following ISO-8601 formats:
   * <ul>
   * <li>uuuu-MM-dd'T'HH:mm
   * <li>uuuu-MM-dd'T'HH:mm:ss
   * <li>uuuu-MM-dd'T'HH:mm:ss.SSS
   * <li>uuuu-MM-dd'T'HH:mm:ss.SSSSSS
   * <li>uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS
   * </ul>
   * The format used will be the shortest that outputs the full value of the time where the omitted parts are implied to be zero.
   * 
   * @return the Java format to use for date/time columns.
   */
  @Schema(description = """
                        The Java format to use for date/time columns.
                        <P>
                        This value will be used by the Java DateTimeFormatter to format datetimes.
                        <P>
                        To value may be either a DateTimeFormatter pattern or one of the predefined formats:
                        <table class="striped" style="text-align:left">
                        <caption>Predefined Formatters</caption>
                        <thead>
                        <tr>
                        <th scope="col">Formatter</th>
                        <th scope="col">Description</th>
                        <th scope="col">Example</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                        <th scope="row"> BASIC_ISO_DATE</th>
                        <td>Basic ISO date </td> <td>'20111203'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_LOCAL_DATE</th>
                        <td> ISO Local Date </td>
                        <td>'2011-12-03'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_LOCAL_TIME</th>
                        <td> Time without offset </td>
                        <td>'10:15:30'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_TIME</th>
                        <td> Time with or without offset </td>
                        <td>'10:15:30+01:00'; '10:15:30'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_LOCAL_DATE_TIME</th>
                        <td> ISO Local Date and Time </td>
                        <td>'2011-12-03T10:15:30'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_ORDINAL_DATE</th>
                        <td> Year and day of year </td>
                        <td>'2012-337'</td>
                        </tr>
                        <tr>
                        <th scope="row"> EPOCH_SECONDS</th>
                        <td> Seconds since the epoch (1970-01-01)</td>
                        <td>1684158330L</td>
                        </tr>
                        <tr>
                        <th scope="row"> EPOCH_MILLISECONDS</th>
                        <td> Milliseconds since the epoch (1970-01-01)</td>
                        <td>1684158330120L</td>
                        </tr>
                        <tr colspan="3"><td>
                        The following predefined formats all require zone/offset data that will be assumed to be UTC.
                        </td></tr>
                        <tr>
                        <th scope="row"> ISO_OFFSET_DATE</th>
                        <td> ISO Date with offset </td>
                        <td>'2023-05-15Z'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_OFFSET_TIME</th>
                        <td> Time with offset </td>
                        <td>'13:45:30.12Z'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_OFFSET_DATE_TIME</th>
                        <td> Date Time with Offset </td>
                        <td>'2023-05-15T13:45:30.12Z'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_ZONED_DATE_TIME</th>
                        <td> Zoned Date Time </td>
                        <td>'2023-05-15T13:45:30.12Z'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_DATE_TIME</th>
                        <td> Date and time with ZoneId </td>
                        <td>'2023-05-15T13:45:30.12Z'</td>
                        </tr>
                        <tr>
                        <th scope="row"> ISO_INSTANT</th>
                        <td> Date and Time of an Instant </td>
                        <td>'2023-05-15T13:45:30.120Z'</td>
                        </tr>
                        <tr>
                        <th scope="row"> RFC_1123_DATE_TIME</th>
                        <td> RFC 1123 / RFC 822 </td>
                        <td>'Mon, 15 May 2023 13:45:30 GMT'</td>
                        </tr>
                        </table>
                        <P>
                        The predefined formatters have capabilities that the pattern formatting does not, specifically, if you want to output an ISO8601
                        date time with fractional seconds but only showing signficant figures in the fractional seconds, use ISO_LOCAL_DATE_TIME.
                        <P>
                        The default output (when the format is not set) is that of the java LocalDateTime.toString() method, specifically, the output will be one of the following ISO-8601 formats:
                        <ul>
                        <li>uuuu-MM-dd'T'HH:mm
                        <li>uuuu-MM-dd'T'HH:mm:ss
                        <li>uuuu-MM-dd'T'HH:mm:ss.SSS
                        <li>uuuu-MM-dd'T'HH:mm:ss.SSSSSS
                        <li>uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS
                        </ul>
                        The format used will be the shortest that outputs the full value of the time where the omitted parts are implied to be zero.
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
  )
  public String getDateTimeFormat() {
    return dateTimeFormat;
  }

  /**
   * Get the Java format to use for time columns.
   * <P>
   * To be processed by a Java {@link DateTimeFormatter}.
   * 
   * @return the Java format to use for time columns.
   */
  @Schema(description = """
                        The Java format to use for time columns.
                        <P>
                        This value will be used by the Java DateTimeFormatter to format times.
                        """
          , maxLength = 100
          , requiredMode = Schema.RequiredMode.NOT_REQUIRED
          , defaultValue = "hh:mm:ss"
  )
  public String getTimeFormat() {
    return timeFormat;
  }

  /**
   * Get the Java {@link java.text.DecimalFormat} to use for float and double columns.
   * <P>
   * If not set the default toString() method will be called, which will result in a format equivalent to "0.0" 
   * (i.e. it will include at least one digit after the decimal point).
   * 
   * @return the Java format to use for floating point columns.
   */
  @Schema(description = """
                        The Java format to use for float and double columns.
                        <P>
                        This value will be used by the Java DecimalFormat to format floating point values.
                        <P>
                        If not set the default toString() method will be called, which will result in a format equivalent to "0.0"
                        (i.e. it will include at least one digit after the decimal point).
                        """
          , maxLength = 100
  )
  public String getDecimalFormat() {
    return decimalFormat;
  }

  /**
   * Get the format to use for Boolean columns.
   * <P>
   * This must be a <A href="https://commons.apache.org/proper/commons-jexl/" target="_blank">JEXL</A> expression that evaluates to
   * an array of two string values - the first being true and the second being false.
   * These strings will be inserted into the output stream as is, and thus must be valid JSON, specifically they can be:
   * <UL>
   * <LI>true
   * <LI>false
   * <LI>A numeric value
   * <LI>A string value
   * </UL>
   * The following are all examples of valid expressions:
   * <UL>
   * <LI>['true', 'false']
   * Valid, but pointless, because this is the default behaviour.
   * <LI>['1', '0']
   * Output a numeric 1 or 0.
   * <LI>['"1"', '"0"']
   * Output a quoted "1" or "0".
   * <LI>['"yes"', '"no"']
   * Output a quoted "yes" or "no".
   * </UL>
   * <P>
   * Validation is carried out on the output from the expression, but this validation is not perfect and it is possible to produce invalid JSON with a bad format.
   * 
   * If not set Boolean values will be output as standard JSON Boolean values.
   * 
   * @return the format to use for Boolean columns.
   */
  @Schema(description = """
                        Get the format to use for Boolean columns.
                        <P>
                        This must be a <A href="https://commons.apache.org/proper/commons-jexl/" target="_blank">JEXL</A> expression that evaluates to
                        an array of two string values - the first being true and the second being false.
                        These strings will be inserted into the output stream as is, and thus must be valid JSON; specifically they can be:
                        <UL>
                        <LI>true
                        <LI>false
                        <LI>A numeric value
                        <LI>A string value
                        </UL>
                        The following are all examples of valid expressions:
                        <UL>
                        <LI>['true', 'false']
                        Valid, but pointless, because this is the default behaviour.
                        <LI>['1', '0']
                        Output a numeric 1 or 0.
                        <LI>['"1"', '"0"']
                        Output a quoted "1" or "0".
                        <LI>['"yes"', '"no"']
                        Output a quoted "yes" or "no".
                        </UL>
                        <P>
                        Validation is carried out on the output from the expression, but this validation is not perfect and it is possible to produce invalid JSON with a bad format.
                        <P>
                        If not set Boolean values will be output as standard JSON Boolean values.
                        """
          , maxLength = 100
  )
  public String getBooleanFormat() {
    return booleanFormat;
  }
  
  /**
   * Builder class for FormatJson.
   */
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class Builder {

    private FormatType type = FormatType.JSON;
    private String name = "json";
    private String description;
    private String extension = "json";
    private String filename = null;
    private MediaType mediaType = MediaType.parse("application/json");
    private boolean hidden = false;

    private String dataName;
    private String metadataName;
    private boolean compatibleTypeNames;
    private String dateFormat;
    private String dateTimeFormat;
    private String timeFormat;
    private String decimalFormat;
    private String booleanFormat;    
    
    private Builder() {
    }

    /**
     * Set the {@link FormatJson#type} value in the builder.
     * @param value The value for the {@link FormatJson#type}, must be {@link FormatType#JSON}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder type(final FormatType value) {
      this.type = value;
      return this;
    }

    /**
     * Set the {@link FormatJson#name} value in the builder.
     * @param value The value for the {@link FormatJson#name}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder name(final String value) {
      this.name = value;
      return this;
    }

    /**
     * Set the description of the format.
     *
     * @param description the description of the format.
     * @return this Builder instance.
     */
    public Builder description(String description) {
      this.description = description;
      return this;
    }

    /**
     * Set the {@link FormatJson#extension} value in the builder.
     * @param value The value for the {@link FormatJson#extension}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder extension(final String value) {
      this.extension = value;
      return this;
    }

    /**
     * Set the filename for the format.
     *
     * @param filename the default filename for the format.
     * @return this Builder instance.
     */
    public Builder filename(String filename) {
      this.filename = filename;
      return this;
    }

    /**
     * Set the {@link FormatJson#mediaType} value in the builder.
     * @param value The value for the {@link FormatJson#mediaType}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder mediaType(final String value) {
      this.mediaType = MediaType.parse(value);
      return this;
    }

    /**
     * Set the hidden property of the format.
     *
     * @param hidden the {@link Format#isHidden()} property of the format.
     * @return this Builder instance.
     */
    public Builder hidden(final boolean hidden) {
      this.hidden = hidden;
      return this;
    }

    /**
     * Set the {@link FormatJson#dataName} value in the builder.
     * @param value The value for the {@link FormatJson#dataName}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder dataName(final String value) {
      this.dataName = value;
      return this;
    }

    /**
     * Set the {@link FormatJson#metadataName} value in the builder.
     * @param value The value for the {@link FormatJson#metadataName}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder metadataName(final String value) {
      this.metadataName = value;
      return this;
    }

    /**
     * Set the {@link FormatJson#compatibleTypeNames} value in the builder.
     * @param value The value for the {@link FormatJson#compatibleTypeNames}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder compatibleTypeNames(final Boolean value) {
      this.compatibleTypeNames = value == null ? false : value;
      return this;
    }
    
    /**
     * Set the {@link FormatJson#dateFormat} value in the builder.
     * @param value The value for the {@link FormatJson#dateFormat}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder dateFormat(final String value) {
      this.dateFormat = value;
      return this;
    }

    /**
     * Set the {@link FormatJson#dateTimeFormat} value in the builder.
     * @param value The value for the {@link FormatJson#dateTimeFormat}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder dateTimeFormat(final String value) {
      this.dateTimeFormat = value;
      return this;
    }

    /**
     * Set the {@link FormatJson#timeFormat} value in the builder.
     * @param value The value for the {@link FormatJson#timeFormat}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder timeFormat(final String value) {
      this.timeFormat = value;
      return this;
    }

    /**
     * Set the {@link FormatJson#decimalFormat} value in the builder.
     * @param value The value for the {@link FormatJson#decimalFormat}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder decimalFormat(final String value) {
      this.decimalFormat = value;
      return this;
    }

    /**
     * Set the {@link FormatJson#booleanFormat} value in the builder.
     * @param value The value for the {@link FormatJson#booleanFormat}.
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder booleanFormat(final String value) {
      this.booleanFormat = value;
      return this;
    }

    /**
     * Construct a new instance of the FormatJson class.
     * @return a new instance of the FormatJson class.
     */
    public FormatJson build() {
      return new uk.co.spudsoft.query.defn.FormatJson(type, name, description, extension, filename, mediaType
              , hidden, dataName, metadataName, compatibleTypeNames
              , dateFormat, dateTimeFormat, timeFormat, decimalFormat, booleanFormat
      );
    }
  }

  /**
   * Construct a new instance of the FormatJson.Builder class.
   * @return a new instance of the FormatJson.Builder class.
   */
  public static FormatJson.Builder builder() {
    return new FormatJson.Builder();
  }

  private FormatJson(final FormatType type, final String name, final String description
          , final String extension, final String filename, final MediaType mediaType
          , final boolean hidden, String dataName, String metadataName, boolean compatibleTypeNames
          , final String dateFormat, final String dateTimeFormat, final String timeFormat, final String decimalFormat, final String booleanFormat
  ) {
    validateType(FormatType.JSON, type);
    this.type = type;
    this.name = name;
    this.description = description;
    this.extension = extension;
    this.filename = filename;
    this.mediaType = mediaType;
    this.hidden = hidden;
    this.dataName = dataName;
    this.metadataName = metadataName;
    this.compatibleTypeNames = compatibleTypeNames;
    this.dateFormat = dateFormat;
    this.dateTimeFormat = dateTimeFormat;
    this.timeFormat = timeFormat;
    this.decimalFormat = decimalFormat;
    this.booleanFormat = booleanFormat;
  }
  
}
