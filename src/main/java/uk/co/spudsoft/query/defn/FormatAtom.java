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

package uk.co.spudsoft.query.defn;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.base.Strings;
import com.google.common.net.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;

import static uk.co.spudsoft.query.defn.FormatXml.NAME_CHAR_REGEX;
import static uk.co.spudsoft.query.defn.FormatXml.NAME_START_REGEX;

/**
 * Output the data stream in Atom.
 * This format has no specific configuration options.
 *
 * @author jtalbut
 */
@JsonDeserialize(builder = FormatAtom.Builder.class)
@Schema(description = """
                      Configuration for an output format of Atom.
                      There are no formatting options for Atom output.
                      """)
public class FormatAtom implements Format {

  private final FormatType type;
  private final String name;
  private final String description;
  private final String extension;
  private final String filename;
  private final MediaType mediaType;
  private final boolean hidden;

  private final String fieldInitialLetterFix;
  private final String fieldInvalidLetterFix;
  private final String dateFormat;
  private final String dateTimeFormat;
  private final String timeFormat;

  /**
   * Constructor for FormatAtom, using the Builder class for initialization.
   */
  private FormatAtom(Builder builder) {
    this.type = builder.type;
    this.name = builder.name;
    this.description = builder.description;
    this.extension = builder.extension;
    this.filename = builder.filename;
    this.mediaType = builder.mediaType;
    this.hidden = builder.hidden;
    this.fieldInitialLetterFix = builder.fieldInitialLetterFix;
    this.fieldInvalidLetterFix = builder.fieldInvalidLetterFix;
    this.dateFormat = builder.dateFormat;
    this.dateTimeFormat = builder.dateTimeFormat;
    this.timeFormat = builder.timeFormat;
  }

  /**
   * Construct a new instance of the FormatAtom.Builder class.
   * @return a new instance of the FormatAtom.Builder class.
   */
  public static FormatAtom.Builder builder() {
    return new FormatAtom.Builder();
  }


  /**
   * Creates a new FormatAtom instance with values replaced by defaults it they are not set.
   * @return a newly created FormatAtom instance in which all fields have values.
   */
  public FormatAtom withDefaults() {
    Builder builder = new Builder();
    builder.type(type);
    builder.name(name);
    builder.description(description);
    builder.extension(extension);
    builder.mediaType(mediaType);
    builder.fieldInitialLetterFix(fieldInitialLetterFix == null ? "F" : fieldInitialLetterFix);
    builder.fieldInvalidLetterFix(fieldInvalidLetterFix == null ? "_" : fieldInvalidLetterFix);
    builder.dateFormat(dateFormat);
    builder.dateTimeFormat(dateTimeFormat);
    builder.timeFormat(timeFormat);
    
    return builder.build();
  }

  /**
   * Get the type of the format.
   *
   * @return the {@link FormatType} of this format.
   */
  @Override
  @Schema(description = "The type of the format.")
  public FormatType getType() {
    return type;
  }

  /**
   * Get the name of the format, as will be used on query string parameters.
   * No two formats in a single pipeline should have the same name.
   *
   * @return the name of the format, as will be used on query string parameters.
   */
  @Override
  @Schema(description = """
                        <p>The name of the format.</p>
                        <p>The name is used to determine the format based upon the '_fmt' query
                        string argument.</p>
                        <p>It is an error for two Formats to have the same name. This is different
                        from the other Format determinators which can be repeated; the name is the
                        ultimate arbiter and must be unique.</p>
                        """,
    maxLength = 100,
    defaultValue = "XML")
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
   *
   * @return the file extension used for this format.
   */
  @Schema(description = """
                          <p>The extension of the format.</p>
                          <p>This is used to determine the file extension for output files and
                          for URL paths.</p>
                          """,
    maxLength = 100,
    defaultValue = ".xml")
  @Override
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
   * Get the media type of this format.
   *
   * @return the {@link MediaType}, which maps to Content-Type in HTTP headers.
   */
  @Schema(description = "The media type (e.g., application/atom+xml; charset=utf-8).",
    maxLength = 100,
    defaultValue = "application/atom+xml; charset=utf-8")
  @Override
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
   * Get the field initial letter fix to adjust XML field names.
   *
   * @return the initial letter fix strategy.
   */
  @Schema(description = "Fix applied to the initial letter of a field's name.",
    maxLength = 100,
    defaultValue = "F"
  )
  public String getFieldInitialLetterFix() {
    return fieldInitialLetterFix;
  }

  /**
   * Get the fix strategy for invalid letters in XML field names.
   *
   * @return the fix strategy for invalid letters.
   */
  @Schema(description = "Fix applied to invalid letters in field names.",
    maxLength = 100,
    defaultValue = "_")
  public String getFieldInvalidLetterFix() {
    return fieldInvalidLetterFix;
  }

  /**
   * Get the Java format to use for date fields.
   * <P>
   * To be processed by a Java {@link java.time.format.DateTimeFormatter}.
   * <P>
   * The default behaviour is to use {@link java.time.LocalDate#toString()}, which will output in accordance with ISO 8601.
   * 
   * @return the Java format to use for date fields.
   */
  @Schema(description = """
                        The Java format to use for date fields.
                        <P>
                        This value will be used by the Java DateTimeFormatter to format dates.
                        <P>
                        The default behaviour is to use java.time.LocalDate#toString(), which will output in accordance with ISO 8601.
                        """
          , maxLength = 100
          , defaultValue = "yyyy-mm-dd"
  )
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * Get the Java format to use for date/time columns.
   * <P>
   * To be processed by a Java {@link java.time.format.DateTimeFormatter}.
   * <P>
   * The default behaviour is to use {@link java.time.LocalDateTime#toString()}, which will output in accordance with ISO 8601..
   * 
   * @return the Java format to use for date/time columns.
   */
  @Schema(description = """
                        The Java format to use for date/time columns.
                        <P>
                        This value will be used by the Java DateTimeFormatter to format datetimes.
                        <P>
                        The default behaviour is to use java.time.LocalDateTime#toString(), which will output in accordance with ISO 8601.
                        """
          , maxLength = 100
          , defaultValue = "yyyy-mm-ddThh:mm:ss"
  )
  public String getDateTimeFormat() {
    return dateTimeFormat;
  }

  /**
   * Get the Java format to use for time columns.
   * <P>
   * To be processed by a Java {@link java.time.format.DateTimeFormatter}.
   * <P>
   * The default behaviour is to use {@link java.time.LocalTime#toString()}, which will output in accordance with ISO 8601.
   * 
   * @return the Java format to use for time columns.
   */
  @Schema(description = """
                        The Java format to use for time columns.
                        <P>
                        This value will be used by the Java DateTimeFormatter to format times.
                        <P>
                        The default behaviour is to use java.time.LocalTime#toString(), which will output in accordance with ISO 8601.
                        """
          , maxLength = 100
          , defaultValue = "hh:mm:ss"
  )
  public String getTimeFormat() {
    return timeFormat;
  }

  /**
   * Builder class for creating instances of the FormatAtom class.
   *
   * The Builder pattern allows for the incremental construction and customization
   * of the FormatAtom class by setting various properties related to the format,
   * such as format type, name, extension, media type, and additional attributes specific to XML formatting.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private FormatType type = FormatType.Atom;
    private String name = "Atom";
    private String description;
    private String extension = "xml";
    private String filename = null;
    private MediaType mediaType = MediaType.parse("application/atom+xml; charset=utf-8");
    private boolean hidden = false;

    private String fieldInitialLetterFix;
    private String fieldInvalidLetterFix;
    private String dateFormat;
    private String dateTimeFormat;
    private String timeFormat;

    /**
     * Default constructor.
     */
    public Builder() {
    }

    /**
     * Set the type of the format.
     *
     * @param type the {@link FormatType}.
     * @return this Builder instance.
     */
    public Builder type(FormatType type) {
      this.type = type;
      return this;
    }

    /**
     * Set the name of the format.
     *
     * @param name the name of the format (used in query parameters).
     * @return this Builder instance.
     */
    public Builder name(String name) {
      this.name = name;
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
     * Set the file extension for the format.
     *
     * @param extension the file extension (e.g., json, xml).
     * @return this Builder instance.
     */
    public Builder extension(String extension) {
      this.extension = extension;
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
     * Set the media type of the format.
     *
     * @param mediaType the media type (e.g., application/atom+xml; charset=utf-8).
     * @return this Builder instance.
     */
    public Builder mediaType(MediaType mediaType) {
      this.mediaType = mediaType;
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
     * Set the strategy for fixing the initial letter of field names.
     *
     * @param fieldInitialLetterFix the initial letter fix strategy.
     * @return this Builder instance.
     */
    public Builder fieldInitialLetterFix(String fieldInitialLetterFix) {
      this.fieldInitialLetterFix = fieldInitialLetterFix;
      return this;
    }

    /**
     * Set the strategy for handling invalid letters in field names.
     *
     * @param fieldInvalidLetterFix the invalid letter fix strategy.
     * @return this Builder instance.
     */
    public Builder fieldInvalidLetterFix(String fieldInvalidLetterFix) {
      this.fieldInvalidLetterFix = fieldInvalidLetterFix;
      return this;
    }

    /**
     * Set the Java format to use for formatting Date values.
     *
     * @param dateFormat the Java format to use for formatting Date values.
     * @return this Builder instance.
     */
    public Builder dateFormat(String dateFormat) {
      this.dateFormat = dateFormat;
      return this;
    }
    
    /**
     * Set the Java format to use for formatting DateTime values.
     *
     * @param dateTimeFormat the Java format to use for formatting DateTime values.
     * @return this Builder instance.
     */
    public Builder dateTimeFormat(String dateTimeFormat) {
      this.dateTimeFormat = dateTimeFormat;
      return this;
    }
    
    /**
     * Set the Java format to use for formatting Time values.
     *
     * @param timeFormat the Java format to use for formatting Time values.
     * @return this Builder instance.
     */
    public Builder timeFormat(String timeFormat) {
      this.timeFormat = timeFormat;
      return this;
    }
    
    /**
     * Build an instance of {@link FormatAtom}.
     *
     * @return a new FormatAtom instance.
     */
    public FormatAtom build() {
      return new FormatAtom(this);
    }
  }
}
