package com.website.listener.FileUpload;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;

/**
 * 上传文件监听
 * 上传中
 */
public class FileUploadProgressListener implements ProgressListener {
	private HttpSession session;
	private String fileName;

	public FileUploadProgressListener(HttpSession session,String fileName) {
		this.session = session;
		this.fileName = fileName;
	}

	/**
	 * 监听上传进度
	 */
	public void update(long pBytesRead, long pContentLength, int pItems) {
        session.setAttribute(fileName, (int)Math.floor((double)pBytesRead/pContentLength*100));
	}
}
