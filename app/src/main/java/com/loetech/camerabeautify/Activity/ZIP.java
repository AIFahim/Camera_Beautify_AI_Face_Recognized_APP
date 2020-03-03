package com.loetech.camerabeautify.Activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class ZIP {
    public ZIP(){

    }


    public static void unzipAStickPaperPackages(String dirStickPaperPathName)
    {
        try {

            ZIP.UnZipFolder(dirStickPaperPathName+".zip",dirStickPaperPathName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void UnZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                // get the folder name of the widget
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                if(folder.exists()) continue;
                folder.mkdirs();
            } else {

                File file = new File(outPathString + File.separator + szName);
                if(file.exists()) continue;
                file.createNewFile();

                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];

                while ((len = inZip.read(buffer)) != -1) {

                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        inZip.close();
    }







    public static InputStream UpZip(String zipFileString, String fileString)throws Exception {
        ZipFile zipFile = new ZipFile(zipFileString);
        ZipEntry zipEntry = zipFile.getEntry(fileString);
        return zipFile.getInputStream(zipEntry);
    }


    public static List<File> GetFileList(String zipFileString, boolean bContainFolder, boolean bContainFile)throws Exception {
        List<File> fileList = new ArrayList<File>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(szName);
                if (bContainFolder) {
                    fileList.add(folder);
                }

            } else {
                File file = new File(szName);
                if (bContainFile) {
                    fileList.add(file);
                }
            }
        }
        inZip.close();
        return fileList;
    }
}