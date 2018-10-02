package neu.coe.project.cloudapp.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import neu.coe.project.cloudapp.Model.TransactionData;
import neu.coe.project.cloudapp.Model.UserData;
import neu.coe.project.cloudapp.Repository.TransactionDataRepository;
import neu.coe.project.cloudapp.Repository.UserDataRepository;
import org.hibernate.Transaction;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
@RequestMapping
public class TransactionController {
    Map<String,String> map= new HashMap<String,String>();
    @Autowired
    private TransactionDataRepository transactionRepository;

    @Autowired
    private UserDataRepository userDataRepository;
    LoginController loginController= new LoginController();

    @RequestMapping(method = GET, path ="/transactions/{transactionId}")
    public @ResponseBody
    ResponseEntity<String> getAllTransactionsById(@RequestHeader HttpHeaders httpRequest, @PathVariable(value = "transactionId") String id) throws Exception {
        final String authorization = httpRequest.getFirst("Authorization");

        String[] values = loginController.retrieveParameters(authorization);
        String username = values[0];
        String password = values[1];

        if (Authenticate(username, password)) {
            System.out.println("in auth");
            TransactionData t = getTransaction(id);

//            if(t==null){
//
//               // return new ResponseEntity(HttpStatus.BAD_REQUEST);
//
//                return ResponseEntity
//                        .status(HttpStatus.NO_CONTENT)
//                        .body("Transaction not found");
//                //return "Transaction not found";
//            }

           // System.out.println("out if"+t.getDescription());
            if(t!=null) {

                if (t.getId().equals(id)) {
                    if (t.getUserData().getUsername().equals(username)) {

                        ObjectMapper mapper = new ObjectMapper();
                        System.out.println("in if" + t.getDescription());
                        String jsonInString = mapper.writeValueAsString(t);
                       // return jsonInString;
                        return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(jsonInString);
                    } else {
                      //  map=new HashMap<>();
                      //  map.put("message", "You don't have access for this transaction");
                        //return new JSONObject(map).toString();
                       // return "You don't have access for this transaction";
                        return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body("You don't have access for this transaction");
                    }
                    }
                }
            else {
                map=new HashMap<>();
                map.put("message", "Transaction id not present");
                // return new JSONObject(map).toString();
//                    return ResponseEntity
//                            .status(HttpStatus.UNAUTHORIZED)
//                            .body("You don't have access for this transaction");
                //return "transaction id not present";
                return ResponseEntity
                        .status(HttpStatus.NO_CONTENT)
                        .body("Transaction not found");
            }

        }
        //System.out.println(transactionRepository.findById(id));
        //map.put("message", "check username and password");
        //return new JSONObject(map).toString();
        //return "check username and password";
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("You don't have access for this transaction");
    }

    //@PostMapping("/transactions/register")
    @RequestMapping(method = POST, path = "/transactions/register")
    public @ResponseBody ResponseEntity<String> createTransaction(@RequestHeader HttpHeaders httpRequest,@RequestParam String amount,@RequestParam String category,
                                             @RequestParam String date,@RequestParam String description,@RequestParam String merchant) {
        try {
            System.out.println(amount);
            TransactionData transactionData = new TransactionData();
            transactionData.setAmount(amount);
            transactionData.setCategory(category);
            transactionData.setDate(date);
            transactionData.setDescription(description);
            transactionData.setMerchant(merchant);
            System.out.println("sada " + transactionData.getCategory());
            final String authorization = httpRequest.getFirst("Authorization");
            String[] values = loginController.retrieveParameters(authorization);
            String username = values[0];
            String password = values[1];
            UserData user = new UserData();
            if (Authenticate(username, password)) {
//                String hashedPassword = loginController.hashPassword(password);
//                user.setUsername(username);
//                user.setPassword(hashedPassword);
//                userDataRepository.save(user);
//            } else {
                user = getUserData(username);

            transactionData.setUserData(user);
            transactionRepository.save(transactionData);
            ObjectMapper mapper = new ObjectMapper();

            String jsonInString = mapper.writeValueAsString(transactionData);
            //return jsonInString;
                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(jsonInString);
            }
            else{
                map.put("message", "check username and password");
                //return new JSONObject(map).toString();
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("You are unauthorized. Check username and password");
            }
        }
        catch (Exception ex){
//            map=new HashMap<>();
//            map.put("message", "Incorrect data to store in DB");
//            return new JSONObject(map).toString();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Please enter correct parameters");
           // return ""+ex.getMessage();
        }
    }



    @RequestMapping(method = PUT, path = "/transactions/{transactionId}")
    public @ResponseBody ResponseEntity<String> putTransaction(@RequestHeader HttpHeaders httpRequest, @PathVariable(value = "transactionId") String transactionId, @RequestParam String amount,@RequestParam String category,
                                 @RequestParam String date,@RequestParam String description,@RequestParam String merchant) {
        try {
            final String authorization = httpRequest.getFirst("Authorization");
            String[] values = loginController.retrieveParameters(authorization);
            String username = values[0];
            String password = values[1];
            //TransactionData transactionData= new TransactionData();

            System.out.println("put success");
            if (Authenticate(username, password)) {
                System.out.println("put success3");
                TransactionData transactionData = getTransaction(transactionId);
                transactionData.setAmount(amount);
                transactionData.setCategory(category);
                transactionData.setDate(date);
                transactionData.setDescription(description);
                transactionData.setMerchant(merchant);
                transactionData.setId(transactionId);

                System.out.println("put success2");
                if (transactionData != null) {
                    System.out.println(transactionData.getUserData().getUsername() + "    usrname    " + username);

                    if (transactionData.getUserData().getUsername().equals(username)) {
                        transactionRepository.findAll();
                        System.out.println("put start");
                        transactionRepository.save(transactionData);
                        System.out.println("put success");
                        ObjectMapper mapper = new ObjectMapper();

                        String jsonInString = mapper.writeValueAsString(transactionData);
                        return ResponseEntity
                                .status(HttpStatus.OK)
                                .body(jsonInString);
                    } else {
//                       map=new HashMap<>();
//                       map.put("message", "You don't have access for this transaction");
//                       return new JSONObject(map).toString();
                        return ResponseEntity
                                .status(HttpStatus.UNAUTHORIZED)
                                .body("You are unauthorized to edit this transaction");
                        //return "You don't have access for this transaction";
                    }

                } else {
                    System.out.println("DESC: " + transactionData.getDescription());
                    map = new HashMap<>();
                    map.put("message", "Updated Successfully\"");
                    // return new JSONObject(map).toString();
                    return ResponseEntity
                            .status(HttpStatus.NO_CONTENT)
                            .body("There is no content for this transaction");
                    // return "Updated Successfully";

                }
            } else {
//        map=new HashMap<>();
//        map.put("message", "Please Check your username and password\"");
//        return new JSONObject(map).toString();
                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("You are unauthorized. Check username and password");
                //return "Please Check your username and password";
            }
        }
        catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Please enter correct values");
        }

        }



    public UserData getUserData(String username) {
        Iterable<UserData> list = userDataRepository.findAll();

        for (UserData u : list) {

            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }

        }
        return null;
    }

    public TransactionData getTransaction(String id){
        System.out.println("trabs"+id);
        Iterable<TransactionData> list= new ArrayList<>();
        list= transactionRepository.findAll();
       // transactionRepository.findAll().forEach(TransactionData t){

        for (TransactionData t:list) {
            if(t.getId().equals(id)){
                System.out.println("inside get"+t.getCategory());
                return t;
            }
        }
        return null;
    }

    public boolean Authenticate(String username, String password) {
        System.out.println("inside Authrnetinscatie"+userDataRepository);
        Iterable<UserData> list = userDataRepository.findAll();
        System.out.println("inside Authrnetinscatie222222222"+userDataRepository);
        for (UserData u : list) {
            System.out.println("inside Authrnetinscatie33333333"+u);
            if (u.getUsername().equalsIgnoreCase(username) && checkPassword(password, u.getPassword())) {

                System.out.println("inside Authrnetinscatie4444"+u);
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
