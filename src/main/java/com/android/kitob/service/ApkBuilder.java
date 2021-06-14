package com.android.kitob.service;

import com.android.kitob.model.FileStorage;
import com.android.kitob.payload.ApiResponse;
import com.android.kitob.repository.FileStorageRepository;
import com.android.kitob.utils.Converter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.util.IOUtils;
import org.assertj.core.internal.Paths;
import org.hashids.Hashids;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.python.antlr.ast.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.script.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ApkBuilder {

    @Autowired
    private Converter converter;
    @Autowired
    private WordToPdf wordToPdf;
    private final Hashids hashids;
    public ApkBuilder() {
        this.hashids = new Hashids(getClass().getName(),6);
    }
    @Autowired
    private FileStorageRepository fileStorageRepository;
    public ApiResponse build(String name) throws IOException, InterruptedException, ScriptException {



        String s = null;

        try {

            // run the Unix "ps -ef" command
            // using the Runtime exec method:
//            Process p = Runtime.getRuntime().exec("python3 android/App-Automate-master/makeApp.py 1 my-release-key.keystore bekmirza bekmirza");
//            Runtime.getRuntime().exec("cd /src/main/resources/a/A/");
//            Runtime.getRuntime().exec("/usr/bin/open -a Terminal /path/to/the/executable");

            String[] args = new String[] {"/bin/bash", "-c", "cd src/main/resources/a/A;  python3 makeApp.py 1 my.keystore bekmirza bekmirza", "with", "args"};
            Process p = new ProcessBuilder(args).start();

//            Process p = Runtime.getRuntime().exec("python3 src/main/resources/a/A/makeApp.py 1 my.keystore bekmirza bekmirza");
//            Process p = Runtime.getRuntime().exec("python3 a/A/makeApp.py 1 my.keystore bekmirza bekmirza");

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            return converter.apiSuccess("src/main/resources/a/A/app/build/outputs/apk/release/"+name+".apk");


//            System.exit(0);
        }
        catch (IOException e) {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            return converter.apiError();
//            System.exit(-1);
        }


    }

    public ApiResponse file(MultipartFile[] files,String name){
        List<FileStorage> fileStorages=new ArrayList<>();
        try {
            if (files!=null&&files.length>0){
                if (files.length>1) return converter.apiError("Faqat 1 ta fayl yuklang!");

                for (MultipartFile multipartFile : files) {
                    if (!getEx(multipartFile.getOriginalFilename()).equals("doc") && !getEx(multipartFile.getOriginalFilename()).equals("docx")) return converter.apiError("Word (doc yoki docx) fayl yuklang!!!");
                    FileStorage fileStorage = FileStorage
                            .builder()
                            .name(multipartFile.getOriginalFilename())
                            .fileSize(multipartFile.getSize())
                            .contentType(multipartFile.getContentType())
                            .extension(getEx(multipartFile.getOriginalFilename()))
                            .build();
                    fileStorageRepository.save(fileStorage);


                    Date now = new Date();
                    //                this.uploadFolder+
                    File uploadFolder = new File(
                            (1900 + now.getYear()) +
                                    "/"
                                    + (1 + now.getMonth()) +
                                    "/"
                                    + now.getDate()
                    );

                    if (!uploadFolder.exists() && uploadFolder.mkdirs()) {
                        System.out.println("Papkalar yaratildi!");
                    }
                    fileStorage.setHashId(hashids.encode(fileStorage.getId()));
                    fileStorage.setUploadPath(
//                        this.uploadFolder+
                            (1900 + now.getYear()) +
                                    "/"
                                    + (1 + now.getMonth()) +
                                    "/"
                                    + now.getDate() +
                                    "/"
                                    + fileStorage.getHashId() +
                                    "."
                                    + fileStorage.getExtension()
                    );

                    uploadFolder = uploadFolder.getAbsoluteFile();
                    System.out.println(uploadFolder);
                    File file = new File(uploadFolder, fileStorage.getHashId() + "." + fileStorage.getExtension());
//                    File file = new File(uploadFolder, fileStorage.getHashId() + "." + "doc");

//
                    try {
                        multipartFile.transferTo(file);
                        fileStorages.add(fileStorageRepository.save(fileStorage));




                        try {
                            wordToPdf.wortToPdf(file.getPath(),
                                    "src/main/resources/a/A/app/src/main/assets/amaliyot.pdf");
                        }catch (Exception e){
                            e.printStackTrace();
                            return converter.apiError();
                        }

                        try {
                            setName(name);
                            ApiResponse response = this.build(name);
                            if (response.isSuccess()){
                                File fileApk = new File(response.getStatus());
                                FileInputStream input = new FileInputStream(fileApk);

                                MultipartFile result = new MockMultipartFile(name,
                                        fileApk.getName(), "application/vnd.android.package-archive", IOUtils.toByteArray(input));
                                FileStorage fileStorage1=FileStorage
                                        .builder()
                                        .name(result.getOriginalFilename())
                                        .fileSize(result.getSize())
                                        .uploadPath(response.getStatus())
                                        .contentType(result.getContentType())
                                        .extension(getEx(result.getOriginalFilename()))
                                        .build();
                                FileStorage save = fileStorageRepository.save(fileStorage1);
                                return converter.apiSuccess(save);
                            }
                            return response;
                        }catch (Exception e){
                            e.printStackTrace();
                            return converter.apiError();
                        }



                    } catch (IOException e) {
                        e.printStackTrace();
                        return converter.apiError();
                    }
                }
                return converter.apiSuccess(fileStorages);
            } return converter.apiError("Fayl bo'sh");
        }catch (Exception e){
            e.printStackTrace();
            return converter.apiError();
        }


    }



    public String getEx(String fileName){
        String ext=null;
        if (fileName!=null&&!fileName.isEmpty()){
            int dot=fileName.lastIndexOf(".");
            if (dot>0&&dot<=fileName.length()-2){
                ext=fileName.substring(dot+1);
            }
        }
        return ext;
    }


    public boolean setName(String name)  {

        try {


            String s = "[\n" +
                    "    {\n" +
                    "        \"id\": 1,\n" +
                    "        \"app_name\": \""+name+"\",\n" +
                    "        \"package_name\": \"com.ehsanfahad.googlegenius."+name+"\",\n" +
                    "        \"theme_color\": \"#0F9D58\",\n" +
                    "        \"app_icon\": \"app/src/main/res/drawable/app_icon.png\",\n" +
                    "        \"keystore_owner\": \"bekmirza\",\n" +
                    "        \"keystore_organizational_unit\": \"Coding Playground\",\n" +
                    "        \"keystore_organization\": \"bekmirza\",\n" +
                    "        \"keystore_country\": \"UZ\"\n" +
                    "    }\n" +
                    "]\n";
            //Read from file

//            String val_newer = jo.getString(key);
//            String val_older = root.getString(key);

            //Compare values
//            if(!val_newer.equals(val_older))
//            {
                //Update value in object
//                root.put(key,val_newer);

                //Write into the file
                FileWriter file = new FileWriter("src/main/resources/a/A/companyInfo.json");
                file.write(s);
                file.close();


//            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
