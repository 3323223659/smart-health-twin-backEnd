package com.smarthealth.common.Utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;

import java.io.InputStream;

public class AliOssUtil {

    //这三个是固定的，我们提取到工具类方法之外，改的话也好改
    //Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    private static final String ENDPOINT = "https://oss-cn-hangzhou.aliyuncs.com";
    //填写Bucket名称，例如examplebucket。
    private static final String BUCKET_NAME = "tlias399";
    //填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。
    private static final String REGIN = "cn-hangzhou";


    //将来我们上传成功文件之后，需要返回文件的访问地址，所以返回值String类型

                                      // 上传到阿里云的名字     输入流
    public static String uploadFile(String objectName, InputStream in) throws Exception {


        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();


        //这个是你将文件上传到阿里云中的名字，是一个动态的，我们可以当一个参数传进方法里面来
//        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
//        String objectName = "001.png";


        // 创建OSSClient实例。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(ENDPOINT)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(REGIN)
                .build();

        //空字符串，来设置所拼接的url
        String url="";

        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objectName, in);

            // 上传文件，将上面创建的PutObjectRequest对象传进去
            PutObjectResult result = ossClient.putObject(putObjectRequest);

            //在这里，如果上传成功，这里可以将文件的地址（因为是固定的）这里我们直接拼接，设置到 url 里面，然后再进行返回
            url="http://"+BUCKET_NAME+"." + ENDPOINT.substring(ENDPOINT.lastIndexOf("/")+1)+"/"+objectName;


        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        return url;
    }
}