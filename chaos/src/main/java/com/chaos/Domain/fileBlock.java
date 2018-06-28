package com.chaos.Domain;

import java.io.Serializable;

public class fileBlock implements Serializable {

	private static final long serialVersionUID = 1708335628209602967L;
    
	

	private String blockId;
    private byte[] blockcontent;
	
	public String getBlockId() {
		return blockId;
	}

	public void setBlockId(String blockId) {
		this.blockId = blockId;
	}

	public byte[] getBlockcontent() {
		return blockcontent;
	}

	public void setBlockcontent(byte[] blockcontent) {
		this.blockcontent = blockcontent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    

	
	
	

}
