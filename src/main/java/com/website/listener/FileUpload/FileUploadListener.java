package com.website.listener.FileUpload;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.fastjavaframework.exception.ThrowPrompt;
import com.fastjavaframework.util.SecretUtil;

/**
 * 上传文件监听
 * 进入Controller前
 */
public class FileUploadListener extends CommonsMultipartResolver  {
	@Override
	protected MultipartParsingResult parseRequest(HttpServletRequest request) throws MultipartException {
		String encoding = determineEncoding(request);
		FileUpload fileUpload = prepareFileUpload(encoding);
		
		String md5 = "";
		String fileName = "";
		try {
			List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
			//获取文件的md5值
			for(FileItem fileItem : fileItems) {
				if("file".equals(fileItem.getFieldName())) {
					fileName = fileItem.getName();
					try {
						md5 = SecretUtil.md5ByFile((FileInputStream)fileItem.getInputStream(), fileItem.getSize())
								+ "." + fileItem.getContentType().split("/")[1];
					} catch (IOException e) {
						md5 = fileItem.getName();
					}
				}
			}

	        //设置监听器
			fileUpload.setProgressListener(new FileUploadProgressListener(request.getSession(),md5));
			MultipartParsingResult mpr= parseFileItems(fileItems, encoding);
			
			//md5值存入request中
			mpr.getMultipartParameterContentTypes().put(fileName, md5);
			return mpr;
		} catch (FileUploadBase.SizeLimitExceededException ex) {
			throw new ThrowPrompt("上传文件超出最大限制！");
		} catch (FileUploadException ex) {
			throw new MultipartException("Could not parse multipart servlet request", ex);
		}
	}
	
	@Override
	protected FileUpload newFileUpload(FileItemFactory fileItemFactory) {
		return super.newFileUpload(fileItemFactory);
	}
}
