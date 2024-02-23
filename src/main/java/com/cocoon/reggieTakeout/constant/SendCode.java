package com.cocoon.reggieTakeout.constant;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "message")
public class SendCode {
    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateCode;

    @SneakyThrows
    public Client createClient(String accessKeyId, String accessKeySecret) {
        Config config = new Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(accessKeyId)
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        return new Client(config);
    }

    /** 选择手机号发送短信验证码 **/
    @SneakyThrows
    public void sendMessage(String phoneNumber, String code) {
        Client client = createClient(accessKeyId, accessKeySecret);
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setPhoneNumbers(phoneNumber)
                .setTemplateParam("{\"code\":\"" + code + "\"}");

        // 发送短信异常响应处理
        RuntimeOptions runtime = new RuntimeOptions();
        SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);
        SendSmsResponseBody body = response.getBody();
        if (!body.getCode().equals("OK")) {
            throw new RuntimeException(body.getMessage());
        }
    }
}