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

import java.util.regex.Pattern;

/**
 * Output the data stream in XML.
 * This format has no specific configuration options.
 *
 * @author jtalbut
 */
@JsonDeserialize(builder = FormatXml.Builder.class)
@Schema(description = """
                      Configuration for an output format of XML.
                      There are no formatting options for XML output.
                      """)
public class FormatXml implements Format {

  private final FormatType type;
  private final String name;
  private final String description;
  private final String extension;
  private final String filename;
  private final MediaType mediaType;
  private final boolean hidden;

  private final boolean xmlDeclaration;
  private final String encoding;
  private final boolean indent;
  private final boolean fieldsAsAttributes;
  private final String docName;
  private final String rowName;
  private final String fieldInitialLetterFix;
  private final String fieldInvalidLetterFix;
  private final String dateFormat;
  private final String dateTimeFormat;
  private final String timeFormat;

  private static final String NAME_START_REGEX_STR = "["
    + ":"
    + "A-Z"
    + "_"
    + "a-z"
    + "\\u00C0-\\u00D6"
    + "\\u00D8-\\u00F6"
    + "\\u00F8-\\u02ff"
    + "\\u0370-\\u037d"
    + "\\u037f-\\u1fff"
    + "\\u200c-\\u200d"
    + "\\u2070-\\u218f"
    + "\\u2c00-\\u2fef"
    + "\\u3001-\\ud7ff"
    + "\\uf900-\\ufdcf"
    + "\\ufdf0-\\ufffd"
    + "]";

  private static final String NAME_CHAR_REGEX_STR = "["
    + ":"
    + "A-Z"
    + "_"
    + "a-z"
    + "\\u00C0-\\u00D6"
    + "\\u00D8-\\u00F6"
    + "\\u00F8-\\u02ff"
    + "\\u0370-\\u037d"
    + "\\u037f-\\u1fff"
    + "\\u200c\\u200d"
    + "\\u2070-\\u218f"
    + "\\u2c00-\\u2fef"
    + "\\u3001-\\udfff"
    + "\\uf900-\\ufdcf"
    + "\\ufdf0-\\ufffd"
    + "\\-"
    + "\\."
    + "0-9"
    + "\\u00b7"
    + "\\u0300-\\u036f"
    + "\\u203f-\\u2040"
    + "]";

  /**
   * A compiled regular expression Pattern used to validate full names in an XML structure.
   * This pattern ensures that the entire string starts with a valid name-start character,
   * followed by other valid characters.
   */
  public static final Pattern NAME_START_REGEX = Pattern.compile("^" + NAME_START_REGEX_STR + NAME_CHAR_REGEX_STR + "*$");
  /**
   * A compiled regular expression Pattern used to validate names in an XML structure.
   * This pattern ensures that the entire string starts consists of valid following characters,
   */
  public static final Pattern NAME_CHAR_REGEX = Pattern.compile("^" + NAME_CHAR_REGEX_STR + "*$");

  /**
   * Constructor for FormatXml, using the Builder class for initialization.
   */
  private FormatXml(Builder builder) {
    this.type = builder.type;
    this.name = builder.name;
    this.description = builder.description;
    this.extension = builder.extension;
    this.filename = builder.filename;
    this.mediaType = builder.mediaType;
    this.hidden = builder.hidden;
    
    this.xmlDeclaration = builder.xmlDeclaration;
    this.encoding = builder.encoding;
    this.indent = builder.indent;
    this.fieldsAsAttributes = builder.fieldsAsAttributes;
    this.docName = builder.docName;
    this.rowName = builder.rowName;
    this.fieldInitialLetterFix = builder.fieldInitialLetterFix;
    this.fieldInvalidLetterFix = builder.fieldInvalidLetterFix;
    this.dateFormat = builder.dateFormat;
    this.dateTimeFormat = builder.dateTimeFormat;
    this.timeFormat = builder.timeFormat;
  }

  /**
   * Construct a new instance of the FormatXml.Builder class.
   * @return a new instance of the FormatXml.Builder class.
   */
  public static FormatXml.Builder builder() {
    return new FormatXml.Builder();
  }


  /**
   * Creates a new FormatXml instance with values replaced by defaults it they are not set.
   * @return a newly created FormatXml instance in which all fields have values.
   */
  public FormatXml withDefaults() {
    Builder builder = new Builder();
    builder.type(type);
    builder.name(name);
    builder.description(description);
    builder.extension(extension);
    builder.mediaType(mediaType);
    builder.xmlDeclaration(xmlDeclaration);
    builder.encoding(Strings.isNullOrEmpty(encoding) ? "utf-8" : encoding);
    builder.indent(indent);
    builder.fieldsAsAttributes(fieldsAsAttributes);
    builder.docName(Strings.isNullOrEmpty(docName) ? "data" : docName);
    builder.rowName(Strings.isNullOrEmpty(rowName) ? "row" : rowName);
    builder.fieldInitialLetterFix(fieldInitialLetterFix == null ? "" : fieldInitialLetterFix);
    builder.fieldInvalidLetterFix(fieldInvalidLetterFix == null ? "" : fieldInvalidLetterFix);
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
  @Schema(description = "The media type (e.g., application/xml).",
    maxLength = 100,
    defaultValue = "application/xml; charset=utf-8")
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
   * Check whether an XML declaration is included.
   *
   * @return true if an XML declaration should be included; false otherwise.
   */
  @Schema(description = "Whether the XML declaration should be included.",
    defaultValue = "true")
  public boolean isXmlDeclaration() {
    return xmlDeclaration;
  }

  /**
   * Get the encoding to be used for XML output.
   *
   * @return the character encoding to be used.
   */
  @Schema(description = "The character encoding (e.g., UTF-8) for the XML.",
    maxLength = 100,
    defaultValue = "utf-8")
  public String getEncoding() {
    return encoding;
  }

  /**
   * Check whether the XML output should be indented.
   *
   * @return true if indentation is enabled; false otherwise.
   */
  @Schema(description = "Whether the XML output should include indentation.",
    defaultValue = "false")
  public boolean isIndent() {
    return indent;
  }

  /**
   * Check whether the fields should be written as attributes.
   *
   * @return true if fields should be written as attributes; false otherwise.
   */
  @Schema(description = "Whether to write fields as attributes in the XML.",
    defaultValue = "false")
  public boolean isFieldsAsAttributes() {
    return fieldsAsAttributes;
  }

  /**
   * Get the document name to be used for XML structure.
   *
   * @return the XML document name.
   */
  @Schema(description = "The root document name in the XML output.",
    maxLength = 100,
    defaultValue = "data")
  public String getDocName() {
    return docName;
  }

  /**
   * Get the name for each row in the XML document structure.
   *
   * @return the row name.
   */
  @Schema(description = "The row element name.",
    maxLength = 100,
    defaultValue = "row")
  public String getRowName() {
    return rowName;
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
   * Builder class for creating instances of the FormatXml class.
   *
   * The Builder pattern allows for the incremental construction and customization
   * of the FormatXml class by setting various properties related to the format,
   * such as format type, name, extension, media type, and additional attributes specific to XML formatting.
   */
  @JsonPOJOBuilder(withPrefix = "")
  public static class Builder {

    private FormatType type = FormatType.XML;
    private String name = "xml";
    private String description;
    private String extension = "xml";
    private String filename = null;
    private MediaType mediaType = MediaType.parse("application/xml; charset=utf-8");
    private boolean hidden = false;

    private boolean xmlDeclaration = true;
    private String encoding;
    private boolean indent = false;
    private boolean fieldsAsAttributes = false;
    private String docName;
    private String rowName;
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
     * @param mediaType the media type (e.g., application/json).
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
     * Set whether an XML declaration should be included.
     *
     * @param xmlDeclaration true to include the declaration.
     * @return this Builder instance.
     */
    public Builder xmlDeclaration(boolean xmlDeclaration) {
      this.xmlDeclaration = xmlDeclaration;
      return this;
    }

    /**
     * Set the character encoding for XML output.
     *
     * @param encoding a valid character encoding (e.g., UTF-8).
     * @return this Builder instance.
     */
    public Builder encoding(String encoding) {
      this.encoding = encoding;
      return this;
    }

    /**
     * Set whether the XML output should be indented.
     *
     * @param indent true to enable indentation.
     * @return this Builder instance.
     */
    public Builder indent(boolean indent) {
      this.indent = indent;
      return this;
    }

    /**
     * Set whether fields should be written as attributes in the XML.
     *
     * @param fieldsAsAttributes true to treat fields as XML attributes.
     * @return this Builder instance.
     */
    public Builder fieldsAsAttributes(boolean fieldsAsAttributes) {
      this.fieldsAsAttributes = fieldsAsAttributes;
      return this;
    }

    /**
     * Set the document name for the XML structure.
     *
     * @param docName the root element's name in the XML.
     * @return this Builder instance.
     */
    public Builder docName(String docName) {
      this.docName = docName;
      return this;
    }

    /**
     * Set the name for each row in the XML document.
     *
     * @param rowName the row element's name.
     * @return this Builder instance.
     */
    public Builder rowName(String rowName) {
      this.rowName = rowName;
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
     * Build an instance of {@link FormatXml}.
     *
     * @return a new FormatXml instance.
     */
    public FormatXml build() {
      return new FormatXml(this);
    }
  }
}
