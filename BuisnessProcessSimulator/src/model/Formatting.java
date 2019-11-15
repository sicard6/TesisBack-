package model;

public class Formatting {

	private String alignment;
	private String fontName;
	private int sizeFont;
	private boolean bold;
	private boolean italic;
	private boolean strikeout;
	private boolean underline;
	private int colorFont;
	
	public Formatting(String alignment, String fontName, int sizeFont, boolean bold, boolean italic, boolean strikeout,
			boolean underline, int colorFont) {
		super();
		this.alignment = alignment;
		this.fontName = fontName;
		this.sizeFont = sizeFont;
		this.bold = bold;
		this.italic = italic;
		this.strikeout = strikeout;
		this.underline = underline;
		this.colorFont = colorFont;
	}

	public String getAlignment() {
		return alignment;
	}

	public void setAlignment(String alignment) {
		this.alignment = alignment;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getSizeFont() {
		return sizeFont;
	}

	public void setSizeFont(int sizeFont) {
		this.sizeFont = sizeFont;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public boolean isStrikeout() {
		return strikeout;
	}

	public void setStrikeout(boolean strikeout) {
		this.strikeout = strikeout;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	public int getColorFont() {
		return colorFont;
	}

	public void setColorFont(int colorFont) {
		this.colorFont = colorFont;
	}

}
