package com.quailstreetsoftware.newsreader.common;

public enum SoundEnum {

	GENERIC_BEEP("META-INF/sounds/00000001.wav");
	
	private final String fileLoc;

    private SoundEnum(final String fileLoc) {
        this.fileLoc = fileLoc;
    }
    
    public String fileLoc(){
        return this.fileLoc;
     }
}
