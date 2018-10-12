package neu.coe.project.cloudapp.Controllers;



import com.fasterxml.jackson.databind.ObjectMapper;
import neu.coe.project.cloudapp.Model.Attachment;
import neu.coe.project.cloudapp.Model.TransactionData;
import neu.coe.project.cloudapp.Model.UserData;
import neu.coe.project.cloudapp.Repository.AttachmentRepository;
import neu.coe.project.cloudapp.Repository.TransactionDataRepository;
import neu.coe.project.cloudapp.Repository.UserDataRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Profile("!dev")
@Controller
@RequestMapping

public class LocalAttachmentController {


    Map<String,String> map= new HashMap<String,String>();

    @Autowired
    private UserDataRepository userDataRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TransactionDataRepository transactionRepository;

    @Autowired
    private LoginController loginController;

    @Autowired
    private  TransactionController transactionController;

    @Value("${image.filepath}")
    private String filePath;



    @RequestMapping(method = POST, path ="/transactions/{transactionId}/attachments")
    public @ResponseBody
    ResponseEntity<String> createAttachment(@RequestHeader HttpHeaders httpRequest, @PathVariable(value = "tid") String transactionId, @RequestParam("url") MultipartFile file) throws Exception {
        final String authorization = httpRequest.getFirst("Authorization");
        String[] values = loginController.retrieveParameters(authorization);
        String username = values[0];
        String password = values[1];

        System.out.println("put success");
        if (Authenticate(username, password)) {
            Attachment a = new Attachment();

            TransactionData t = transactionController.getTransaction(transactionId);
            File convFile = new File(file.getOriginalFilename());
            String ext = FilenameUtils.getExtension(convFile.getPath());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png")){
                if (t != null) {
                    List<Attachment> as = t.getAttachments();

                    try {
                        // Get the file and save it somewhere
                        byte[] bytes = file.getBytes();
                        Path path = Paths.get(filePath + a.getId() + "." + ext);
                        Files.write(path, bytes);
                        a.setUrl(path.toString());

                    } catch (IOException e) {
                        e.printStackTrace();
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Please give the path correctly. File not able to copy");
                    }

                    as.add(a);
                    t.setAttachments(as);
                    transactionRepository.save(t);
                    ObjectMapper mapper = new ObjectMapper();

                    String jsonInString = mapper.writeValueAsString(t);
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(jsonInString);
                } else {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("Please check transaction Id");
                }
            }
            else{
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("This is not a image");
            }
        }else{
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Not Authorised");
        }
    }



    @RequestMapping(method = GET, path ="/transactions/{transactionId}/attachments")
    public @ResponseBody
    ResponseEntity<String> getAllAttachments(@RequestHeader HttpHeaders httpRequest, @PathVariable(value = "transactionId") String tid) {
        try {
            final String authorization = httpRequest.getFirst("Authorization");
            String[] values = loginController.retrieveParameters(authorization);
            String username = values[0];
            String password = values[1];
            if (Authenticate(username, password)) {
                TransactionData t = transactionController.getTransaction(tid);

                if (t != null) {
                    if (t.getId().equals(tid)) {
                        if (t.getUserData().getUsername().equals(username)) {

                            List<Attachment> at = transactionController.getTransaction(tid).getAttachments();
                            ObjectMapper mapper = new ObjectMapper();

                            String jsonInString = mapper.writeValueAsString(at);

                            return ResponseEntity
                                    .status(HttpStatus.OK)
                                    .body(jsonInString);
                        } else {
                            return ResponseEntity
                                    .status(HttpStatus.UNAUTHORIZED)
                                    .body("Unauthorized for this Transaction");
                        }
                    }
                } else {

                    return ResponseEntity
                            .status(HttpStatus.NO_CONTENT)
                            .body("Transaction not found");
                }
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Not Authorised");
            }
        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(e.getMessage());
        }
        return null;
    }

    @RequestMapping(method = PUT, path = "/transactions/{transactionId}/attachments/{aid}")
    public @ResponseBody ResponseEntity<String> putTransaction(@RequestHeader HttpHeaders httpRequest, @PathVariable(value = "transactionId") String transactionId, @PathVariable(value = "aid") String aid, @RequestParam("url") MultipartFile file) {
        try {
            final String authorization = httpRequest.getFirst("Authorization");
            String[] values = loginController.retrieveParameters(authorization);
            String username = values[0];
            String password = values[1];
            TransactionData t = transactionController.getTransaction(transactionId);
            if (Authenticate(username, password)) {
                Attachment a = null;
                for (Attachment at : t.getAttachments()) {
                    if (aid.equals(at.getId())) {
                        a = at;
                        break;
                    }
                }
                File convFile = new File(file.getOriginalFilename());
                String ext = FilenameUtils.getExtension(convFile.getPath());
                FileOutputStream fos = new FileOutputStream(convFile);
                fos.write(file.getBytes());
                fos.close();
                if (ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("png")) {
                    if (t != null) {
                        if (t.getUserData().getUsername().equals(username))
                        {
                            List<Attachment> as = t.getAttachments();

                            try {
                                File file1 = new File(a.getUrl());
                                System.out.println(a.getUrl());
                                System.out.println(file1.getAbsolutePath());
                                if (file1.delete()) {
                                    System.out.println("File update started successfully");

                                } else {
                                    System.out.println("Failed to update the file");
                                }
                                t.getAttachments().remove(a);
                                byte[] bytes = file.getBytes();
                                System.out.println("File bytes " + file.getContentType());
                                Path path = Paths.get(filePath + a.getId() + "." + ext);
                                Files.write(path, bytes);
                                a.setUrl(path.toString());

                            } catch (IOException e) {
                                e.printStackTrace();
                                return ResponseEntity
                                        .status(HttpStatus.BAD_REQUEST)
                                        .body("Please give the path correctly. File not able to copy");
                            }

                            t.setAttachments(as);
                            transactionRepository.save(t);
                            return ResponseEntity
                                    .status(HttpStatus.OK)
                                    .body("File is updated");
                        }
                        else
                        {
                            return ResponseEntity
                                    .status(HttpStatus.UNAUTHORIZED)
                                    .body("Unauthorized for this transaction");
                        }
                    } else {
                        return ResponseEntity
                                .status(HttpStatus.BAD_REQUEST)
                                .body("Please check transaction Id");
                    }
                } else {
                    return ResponseEntity
                            .status(HttpStatus.BAD_REQUEST)
                            .body("This is not a image");
                }
            } else {
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Not Authorised");
            }
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("transaction");
        }
    }




    @RequestMapping(method = DELETE, path = "/transactions/{transactionId}/attachments/{aid}")
    public @ResponseBody ResponseEntity<String> deleteAttachment(@RequestHeader HttpHeaders httpRequest, @PathVariable(value = "transactionId") String transactionId, @PathVariable(value = "aid") String aid) {
        final String authorization = httpRequest.getFirst("Authorization");
        String[] values = loginController.retrieveParameters(authorization);
        String username = values[0];
        String password = values[1];
        if (Authenticate(username, password)) {
            TransactionData t = transactionController.getTransaction(transactionId);

            if(t!=null){
                if(t.getUserData().getUsername().equals(username))
                {
                    List<Attachment> attachments = t.getAttachments();
                    Attachment a = null;

                    for (Attachment at : attachments) {
                        if (aid.equals(at.getId())) {
                            a = at;
                            break;
                        }
                    }
                    File file = new File(a.getUrl());
                    System.out.println(a.getUrl());
                    System.out.println(file.getAbsolutePath());
                    if (file.delete()) {
                        System.out.println("File deleted successfully");
                    } else {
                        System.out.println("Failed to delete the file");
                    }
                    t.getAttachments().remove(a);
                    attachmentRepository.delete(a);
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body("Deleted Successfully");
                }
                else {
                    return ResponseEntity
                            .status(HttpStatus.UNAUTHORIZED)
                            .body("Unauthorized for this Transaction");
                }
            }

            else{
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("No transaction found");
            }
        }
        else{
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Unauthorized");
        }

    }



    public boolean Authenticate(String username, String password) {
        Iterable<UserData> list = userDataRepository.findAll();
        for (UserData u : list) {
            if (u.getUsername().equalsIgnoreCase(username) && checkPassword(password, u.getPassword())) {
                return true;
            }
        }
        return false;
    }
    public boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;
        System.out.println(stored_hash+""+password_plaintext);
        if (null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return (password_verified);
    }
}
