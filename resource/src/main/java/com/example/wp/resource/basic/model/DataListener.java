package com.example.wp.resource.basic.model;

public interface DataListener {
	void dataStart();
	
	void dataStop();
	
	void dataOther(StatusInfo statusInfo);
}
