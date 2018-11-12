package neu.coe.project.cloudapp.Controllers;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;
import com.amazonaws.services.sns.model.PublishResult;
import neu.coe.project.cloudapp.Model.UserData;
import neu.coe.project.cloudapp.Repository.UserDataRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
//import com.google.gson.JsonObject;
//import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
//import com.amazonaws.services.dynamodbv2.datamodeling.*;
//import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping
public class LoginController {
    private static int workload = 17;
    private static int post=0;
    private static int put=0;
    private static int delete=0;
    private static int get=0;
    private static int reset=0;
    //@Value("${aws.acccoutnId}") String accountId;
//    @Value("${aws.topicName}") String password_reset;
    @Value("${cloud.aws.path}")
    private String awsCredentialsPath;
    Logger logger = Logger.getLogger("MyLog");
    FileHandler fh;

    public LoginController() {
        try {
            fh = new FileHandler("/opt/tomcat/logs/csye6225.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages
            logger.info("My first log");
        }
catch(Exception e){
    logger.info("Exception");
}
    }

    public String[] retrieveParameters(String authorization) {
        String[] values = {};
        if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            values = credentials.split(":", 2);
        }
        return values;
    }


    @RequestMapping("/time")
    public @ResponseBody
    String time(@RequestHeader HttpHeaders httpRequest) {

        final String authorization = httpRequest.getFirst("Authorization");
        String[] values = retrieveParameters(authorization);
        Map<String,String> map= new HashMap<String,String>();

        if(values.length==0){
            map.put("message", "Login Unsuccessful. Please Enter Username and password in Basic Auth");
            return new JSONObject(map).toString();
        }

        String username = values[0];
        String password = values[1];

        if (Authenticate(username, password)) {

            SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

            map.put("time", ft.format(new Date()));
            map.put("message", "Login Successful");
            System.out.println(new JSONObject(map));
            return new JSONObject(map).toString();

        } else {
            map.put("message", "Login Unsuccessful. Please Check username and password");
            System.out.println(new JSONObject(map));
            return new JSONObject(map).toString();
        }

    }

    @RequestMapping("/times")
    public @ResponseBody
    String times() {


        Map<String,String> map= new HashMap<String,String>();


            SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");

            map.put("time", ft.format(new Date()));
            map.put("message", "Login Successful");
            System.out.println(new JSONObject(map));
            return new JSONObject(map).toString();


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

    public boolean findUsername(String username) {
        Iterable<UserData> list = userDataRepository.findAll();
        for (UserData u : list) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserDataRepository userDataRepository;

    @RequestMapping(method = POST, path = "/user/register") // Map ONLY GET Requests
    public @ResponseBody
    ResponseEntity<String> addNewUser(@RequestHeader HttpHeaders httpRequest) {
        final String authorization = httpRequest.getFirst("Authorization");

        String[] values = retrieveParameters(authorization);
        String username = values[0];
        String password = values[1];
        Map<String,String> map= new HashMap<String,String>();

        if(isValidEmailAddress(username)) {


            if (findUsername(username) == false) {

                String hashedPassword = hashPassword(password);
                UserData n = new UserData();
                n.setUsername(username);
                n.setPassword(hashedPassword);
                userDataRepository.save(n);
                addCloudMetrics("User","/user/register","Count",post++,"csye6225");
//                map.put("message", "User " + username + " created successfully");
//                return new JSONObject(map).toString();
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body("User "+username+" created successfully");
            } else {
//                map.put("message", "Username already exists");
//                return new JSONObject(map).toString();
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body("Username already exists");
            }
        } else{
           // map.put("message", "This is not a valid email address");
            //return new JSONObject(map).toString();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Please enter a valid email address");
        }
    }

    @GetMapping(path = "/user/all")
    public @ResponseBody
    Iterable<UserData> getAllUsers() {
        // This returns a JSON or XML with the users
        return userDataRepository.findAll();
    }

    @RequestMapping(path="user/reset",method=POST)
    public @ResponseBody
    ResponseEntity<String> resetPassword (@RequestHeader HttpHeaders httpRequest) {
        logger.info("Reset password called:");
        final String authorization = httpRequest.getFirst("Authorization");

        String[] values = retrieveParameters(authorization);
        String username = values[0];
        Map<String, String> map = new HashMap<String, String>();

        if (isValidEmailAddress(username)) {

            Iterable<UserData> allusers = userDataRepository.findAll();
            for (UserData user : allusers) {

                if (user.getUsername().equalsIgnoreCase(username)) {
                    //AWSCredentials credentialsProvider
                      //      = new  EnvironmentVariableCredentialsProvider().getCredentials();
                    AmazonSNSClient snsClient = new AmazonSNSClient();


                    //JSONObject jsonObject = new JSONObject();
                    //jsonObject.addProperty("username", username);

                    //jsonObject.put("username", username);
                    PublishRequest emailPublishRequest = new PublishRequest("arn:aws:sns:us-east-1:830173955131:password_reset", username);
                    PublishResult emailPublishResult = snsClient.publish(emailPublishRequest);
                    logger.info("topic published");
                    addCloudMetrics("User","/user/reset","Count",reset++,"csye6225");
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body("message published successfully");
                }
            }
        }

        else{
//                map.put("message", "Username already exists");
//                return new JSONObject(map).toString();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("not a valid user");
        }

          return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Please check your request");
    }

    public String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(workload);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        return (hashed_password);
    }

    public boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;
        System.out.println(stored_hash+""+password_plaintext);
        if (null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

        return (password_verified);
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    public void addCloudMetrics(String dimensionName, String dimensionValue, String metricName, int count, String nameSpace){

       // CloudWatchClient cw =  CloudWatchClient.builder().build() ;
        //CloudWatchClient cw = CloudWatchClient.create();
        final AmazonCloudWatch cw = AmazonCloudWatchClientBuilder.defaultClient();
        Dimension dimension = new Dimension()
                .withName(dimensionName)
                .withValue(dimensionValue);

        MetricDatum datum = new MetricDatum()
                .withMetricName(metricName)
                .withUnit(StandardUnit.Count)
                .withValue((double)count)
                .withDimensions(dimension).withStorageResolution(1);

        PutMetricDataRequest request = new PutMetricDataRequest()
                .withNamespace(nameSpace)
                .withMetricData(datum);

//        Dimension dimension = Dimension
//                .name("/user/register")
//                .value("URLS").build();
//
//        MetricDatum datum = MetricDatum
//                .metricName("Login")
//                .unit(StandardUnit.Count)
//                .value(12.0)
//                .dimensions(dimension).build();
//
//        PutMetricDataRequest request = PutMetricDataRequest
//                .namespace("csye6225")
//                .metricData(datum).build();

        //PutMetricDataResponse response = cw.putMetricData(request);
        PutMetricDataResult response = cw.putMetricData(request);

       logger.info("Successfully put data point metrics");

    }


}
