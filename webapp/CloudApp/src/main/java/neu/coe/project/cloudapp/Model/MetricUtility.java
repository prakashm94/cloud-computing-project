package neu.coe.project.cloudapp.Model;

import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.cloudwatch.model.*;

public class MetricUtility {

    public static int get=0;
    public static int put=0;
    public static int post=0;
    public static int delete=0;

    public static void addCloudMetrics(String dimensionName, String dimensionValue, String metricName, int count, String nameSpace){

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



    }

}
