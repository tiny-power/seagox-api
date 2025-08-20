package com.seagox.lowcode.entity;

/**
 * icon数据
 */
public class SysIcon {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * font_class
     */
    private String font;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

}
