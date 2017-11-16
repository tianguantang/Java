package com.flying.sparrow.helper;

import com.flying.sparrow.bean.Param;
import com.flying.sparrow.framework.bean.FileParam;
import com.flying.sparrow.framework.bean.FormParam;
import com.flying.sparrow.util.CollectionUtil;
import com.flying.sparrow.util.FileUtil;
import com.flying.sparrow.util.StreamUtil;
import com.flying.sparrow.util.StringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 文件上传助手类
 * Created by wangjianchun on 2017/11/15.
 */
public final class UploadHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadHelper.class);


    /**
     * Apache Commons FileUpload 提供的 Servlet 文件上传对象
     */
    private static ServletFileUpload servletFileUpload;

    public static void init(ServletContext servletContext){
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        servletFileUpload = new ServletFileUpload(new DiskFileItemFactory(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD, repository));
        int uploadLimit = ConfigHelper.getAppUploadLimit();
        if(uploadLimit != 0){
            servletFileUpload.setFileSizeMax(uploadLimit*1024*1024);
        }
    }

    /**
     * 判断请求是否为 multipart 类型
     * @param request
     * @return
     */
    public static boolean isMultipart(HttpServletRequest request){
        return ServletFileUpload.isMultipartContent(request);
    }

    /**
     * 创建请求参数对象
     * @param request
     * @return
     * @throws IOException
     */
    public static Param createParam(HttpServletRequest request) throws IOException{
        List<FormParam> formParamList = new ArrayList<FormParam>();
        List<FileParam> fileParamList = new ArrayList<FileParam>();
        try {
            Map<String, List<FileItem>> fileItemListMap = servletFileUpload.parseParameterMap(request);
            if(CollectionUtil.isNotEmpty(fileItemListMap)){
                for(Map.Entry<String, List<FileItem>> fileItemListEntry: fileItemListMap.entrySet()){
                    String fieldName = fileItemListEntry.getKey();
                    List<FileItem> fileItemList = fileItemListEntry.getValue();
                    if(CollectionUtil.isNotEmpty(fileItemList)){
                        for(FileItem fileItem: fileItemList){
                            if(fileItem.isFormField()){
                                String fieldValue = fileItem.getString("UTF-8");
                                formParamList.add(new FormParam(fieldName, fieldValue));
                            }else{
                                String fileName = FileUtil.getRealFileName(new String(fileItem.getName().getBytes(), "UTF-8"));
                                if(StringUtils.isNotEmpty(fileName)){
                                    long fileSize = fileItem.getSize();
                                    String contentType = fileItem.getContentType();
                                    InputStream inputStream = fileItem.getInputStream();
                                    fileParamList.add(new FileParam(fieldName, fileName, fileSize, contentType, inputStream));
                                }
                            }
                        }
                    }
                }
            }
        } catch (FileUploadException e) {
            LOGGER.error("create param failure", e);
            throw new RuntimeException(e);
        }
        return new Param(formParamList, fileParamList);
    }

    /**
     * 上传文件
     * @param basePath
     * @param fileParam
     */
    public static void uploadFile(String basePath, FileParam fileParam){
        if(fileParam != null){
            String filePath = basePath+fileParam.getFileName();
            FileUtil.createFile(filePath);
            try {
                InputStream inputStream = new BufferedInputStream(fileParam.getInputStream());
                OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
                StreamUtil.copyStream(inputStream, outputStream);
            } catch (FileNotFoundException e) {
                LOGGER.error("upload file failure", e);
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 批量上传文件
     * @param basePath
     * @param fileParamList
     */
    public static void uploadFile(String basePath, List<FileParam> fileParamList){
        try {
            if(CollectionUtil.isNotEmpty(fileParamList)){
                for(FileParam fileParam: fileParamList){
                    uploadFile(basePath, fileParam);
                }
            }
        } catch (Exception e) {
            LOGGER.error("upload file failure", e);
            throw new RuntimeException(e);
        }
    }

}
