package com.mn.tiger.upload;

public interface IUploadStrategy 
{
	void upload(TGUploadParams uploadParams);
	
	void shutdown();
}
