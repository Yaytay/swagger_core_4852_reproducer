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
import com.google.common.base.Strings;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.regex.Pattern;

/**
 * Specification of colours used in XLSX output.
 * 
 * Colours should be specified as 3 or 4 pairs of hexadecimal digits.
 * 
 * A colour specification consists of two colours - a foreground colour and a background colour.
 * 
 * <P>
 * Some examples:
 * <UL>
 * <LI>FFFFFF: White
 * <LI>999999: Grey
 * <LI>990000: Red
 * <LI>000099: Blue
 * <LI>0A5F42: I hope to get around to making these colours more descriptive!
 * </UL>
 * 
 * @author jtalbut
 */
@JsonDeserialize(builder = FormatXlsxColours.Builder.class)
@Schema(description = """
                      Specification of colours used in XLSX output.
                      <P>
                      Colours should be specified as 3 or 4 pairs of hexadecimal digits.
                      <P>
                      A colour specification consists of two colours - a foreground colour and a background colour.
                      <P>
                      Some examples:
                      <UL>
                      <LI>FFFFFF: White
                      <LI>999999: Grey
                      <LI>990000: Red
                      <LI>000099: Blue
                      <LI>0A5F42: I hope to get around to making these colours more descriptive!
                      </UL>
                      """
)
public class FormatXlsxColours {
  
  /**
   * Regular expression matching valid colour names (three or four pairs of hexadecimal digits).
   */
  public static final Pattern VALID_COLOUR = Pattern.compile("[0-9A-F]{6}([0-9A-F]{2})?");  
  
  private final String fgColour;
  private final String bgColour;

  /**
   * Get the foreground colour to use.
   * @return the foreground colour to use.
   */
  @Schema(description = """
                        <P>The foreground colour to use.</P>
                        <P>
                        Colours must be expressed as 6 or 8 uppercase hexadecimal digits.
                        </P>
                        <P>
                        Some examples:
                        <UL>
                        <LI><font style="color: #FFFFFF">FFFFFF</font>
                        <LI><font style="color: #999999">999999</font>
                        <LI><font style="color: #990000">990000</font>
                        <LI><font style="color: #000099">000099</font>
                        <LI><font style="color: #0A5F42">0A5F42</font>
                        </UL>
                        </P>
                        """
          , defaultValue = "000000"
          , maxLength = 8
  )
  public String getFgColour() {
    return fgColour;
  }


  /**
   * Get the background colour to use.
   * @return the background colour to use.
   */
  @Schema(description = """
                        <P>The background colour to use.</P>
                        <P>
                        Colours must be expressed as 6 or 8 uppercase hexadecimal digits.
                        </P>
                        <P>
                        Some examples:
                        <UL>
                        <LI><font style="background-color: #000000">000000</font>
                        <LI><font style="background-color: #999999">999999</font>
                        <LI><font style="background-color: #990000">990000</font>
                        <LI><font style="background-color: #000099">000099</font>
                        <LI><font style="background-color: #0A5F42">0A5F42</font>
                        </UL>
                        </P>
                        """
          , defaultValue = "FFFFFF"
          , maxLength = 8
  )
  public String getBgColour() {
    return bgColour;
  }
  
  /**
   * Builder class for FormatXlsxColours.
   */
  @SuppressFBWarnings(value = {"EI_EXPOSE_REP2"}, justification = "Builder class should result in all instances being immutable when object is built")
  @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
  public static class Builder {

    private String fgColour;
    private String bgColour;

    private Builder() {
    }

    /**
     * Set the {@link FormatXlsxColours#fgColour} value in the builder.
     * @param value The value for the {@link FormatXlsxColours#fgColour}, must be 6 or 8 hexadecimal characters ([0-9A-Z]).
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder fgColour(final String value) {
      this.fgColour = value;
      return this;
    }

    /**
     * Set the {@link FormatXlsxColours#bgColour} value in the builder.
     * @param value The value for the {@link FormatXlsxColours#bgColour}, must be 6 or 8 hexadecimal characters ([0-9A-Z]).
     * @return this, so that this builder may be used in a fluent manner.
     */
    public Builder bgColour(final String value) {
      this.bgColour = value;
      return this;
    }

    /**
     * Construct a new instance of the FormatXlsxColours class.
     * @return a new instance of the FormatXlsxColours class.
     */
    public FormatXlsxColours build() {
      return new uk.co.spudsoft.query.defn.FormatXlsxColours(fgColour, bgColour);
    }
  }

  /**
   * Construct a new instance of the FormatXlsxColours.Builder class.
   * @return a new instance of the FormatXlsxColours.Builder class.
   */
  public static FormatXlsxColours.Builder builder() {
    return new FormatXlsxColours.Builder();
  }

  private FormatXlsxColours(final String fgColour, final String bgColour) {
    this.fgColour = fgColour;
    this.bgColour = bgColour;
  }
  
}
