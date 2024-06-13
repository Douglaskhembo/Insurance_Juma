
package com.brokersystems.brokerapp.webservices.service;

import com.brokersystems.brokerapp.webservices.model.NTSADataObject;
import com.brokersystems.brokerapp.webservices.model.PolicyData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthBearerClientRequest;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Created by HP on 7/3/2018.
 */
@Service
public class NtsaService {

    public NTSADataObject getNtsaDetails(String regno){
        NTSADataObject object = new NTSADataObject();
        try {
            regno = regno.replaceAll("\\s","");

            OAuthClientRequest request = OAuthClientRequest
                    .tokenLocation("https://api.ndi.co.ke/oauth/token")
                    .setGrantType(GrantType.PASSWORD)
                    .setPassword("")
                    //.setClientId("3WuMGTDhIzmxl9MM2OgwhyIOb8yNOw0hWY49HCnb")
                    //.setClientSecret("eLtmBGcZ3wL26Ua0Z00ULQS22w1kVQ8QSGWC6xcrkeQ98ukuEVDGNKy2xMUFBo7ceAxkDAbT73qdncNRxwehzCxnMrSFiZqyg3yxdlRzl4pQcPeGBbhnyxBl2zxUlyfq")
                    .setUsername("pmugenya@korient.co.ke")
                    .buildQueryMessage();
            request.addHeader("Accept", "application/json");
            request.addHeader("Content-Type", "application/json");
            OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());

            OAuthAccessTokenResponse oauthResponse = oAuthClient.accessToken(request);
            String accessToken = oauthResponse.getAccessToken();

            System.out.println(accessToken);
//
//            PolicyData policyData = new PolicyData();
//            policyData.setApplicant_pin("A001203288Z");
//            policyData.setChassis_number("ACU300024782");
//            policyData.setCover_type("Comprehensive");
//            policyData.setExpiry_date("28-06-2019");
//            policyData.setInsurance("KOIL");
//            policyData.setValid_from("29-06-2018");
//            policyData.setRegistration_number("KBN590H");
//            policyData.setPolicy_number("HQS/0700/195933/2016");
//
//            reciprocateData(policyData,accessToken);

            String url = "https://api.ndi.co.ke/api/v1/get/2/integrations_accessid,regno/SYS-20170222144241005,"+regno;
            OAuthBearerClientRequest requestBuilder = new OAuthBearerClientRequest(url);
            OAuthClientRequest bearerRequest = requestBuilder.buildQueryMessage();
            bearerRequest.addHeader("Authorization", "Bearer "+accessToken);
            OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerRequest, OAuth.HttpMethod.GET, OAuthResourceResponse.class);
            String body = resourceResponse.getBody();
            Gson gson = new GsonBuilder().create();
             object =gson.fromJson((body).toString(), NTSADataObject.class);

        } catch (Exception exn) {
            exn.printStackTrace();
        }
        return object;
    }


    private void reciprocateData(PolicyData policyData, String accessToken) throws Exception{
        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
        String url = "https://api.ndi.co.ke/api/v1/get-ntsa-data";
        OAuthBearerClientRequest requestBuilder = new OAuthBearerClientRequest(url);
        OAuthClientRequest bearerRequest = requestBuilder.buildQueryMessage();
        bearerRequest.addHeader("Authorization", "Bearer "+accessToken);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("applicant_pin",policyData.getApplicant_pin());
        jsonObject.put("chassis_number",policyData.getChassis_number());
        jsonObject.put("cover_type",policyData.getCover_type());
        jsonObject.put("expiry_date",policyData.getExpiry_date());
        jsonObject.put("insurance",policyData.getInsurance());
        jsonObject.put("policy_number",policyData.getPolicy_number());
        jsonObject.put("valid_from",policyData.getValid_from());
        jsonObject.put("registration_number",policyData.getRegistration_number());
        bearerRequest.setBody(jsonObject.toString());
        OAuthResourceResponse resourceResponse = oAuthClient.resource(bearerRequest, OAuth.HttpMethod.POST, OAuthResourceResponse.class);
        String body = resourceResponse.getBody();
        System.out.println(body);
    }


}
