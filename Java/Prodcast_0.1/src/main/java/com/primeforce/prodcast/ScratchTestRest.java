package com.primeforce.prodcast;

import com.primeforce.prodcast.dto.AdminDTO;
import com.primeforce.prodcast.dto.LoginDTO;
import org.apache.tomcat.jni.File;
import org.apache.tomcat.util.http.fileupload.FileUpload;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Created by sarathan732 on 3/16/2017.
 */
@Named
@Path("/uploadpoc/")
public class ScratchTestRest {

    @POST
    @Path("saveProfile")
    @Consumes({MediaType.MULTIPART_FORM_DATA, MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    public AdminDTO<String> saveProfile(@FormDataParam("name") String name, @FormDataParam("city") String city, @FormDataParam("profilePic") InputStream profilePicInputStream, @FormDataParam("profilePic") FormDataContentDisposition profilePicMetaData ) throws IOException, ServletException {
        System.out.println("Name="+name );
        System.out.println("City="+city );

        String uploadedFileName = profilePicMetaData.getFileName();

        String fileName = UUID.randomUUID().toString();
        String baseDir = "c:\\temp\\";
        int indexOfDot = uploadedFileName.lastIndexOf(".");
        String extension = uploadedFileName.substring(indexOfDot );
        fileName = fileName+extension;
        java.io.File file = new java.io.File( baseDir , fileName       );
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.copy(profilePicInputStream , fos );
        fos.flush();
        fos.close();

        System.out.println("Uploading to File "+fileName);

        return new AdminDTO<String>();



    }

}