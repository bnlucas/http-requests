package org.datoin.net.http.methods;

import org.apache.http.entity.ContentType;
import org.datoin.net.http.RequestHeaderFields;
import org.datoin.net.http.Requests;
import org.datoin.net.http.Response;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PostTest {

    @Test
    public void testExecute() throws Exception {
        String url = String.format("http://localhost:%s/%s/", RequestsTestSuite.PORT, RequestsTestSuite.CONTEXT);
        String headerValue = "text/plain";
        String userAgentValue = "mytest/1.0";
        Response response = Requests.post(url)
                .accept(headerValue)
                .userAgent(userAgentValue)
                .execute();
        InputStream input= new ByteArrayInputStream(response.getContentAsByteArray());
        Properties props = new Properties();
        props.load(input);
        System.out.print(props);
        Object o = props.get(RequestHeaderFields.USER_AGENT.getName());
        Assert.assertEquals("UserAgent didnt match", userAgentValue, o.toString().trim());
        Object o1 = props.get(RequestHeaderFields.ACCEPT.getName());
        Assert.assertEquals("Accept header didnt match", headerValue, o1.toString().trim());
        Assert.assertEquals("Method didnt match", Methods.POST.getMethod(), props.get("Method").toString().trim());

    }

    @Test
    public void testPostContent() throws Exception {
        String url = String.format("http://localhost:%s/%s/", RequestsTestSuite.PORT, RequestsTestSuite.CONTEXT);
        String testText = "this is a test text";
        InputStream is = new ByteArrayInputStream(testText.getBytes());
        Response response = Requests.post(url)
                .setHeader(RequestHeaderFields.CONTENT_TYPE.getName(), ContentType.TEXT_PLAIN.getMimeType())
                .setContent(is, ContentType.TEXT_PLAIN).execute();

        InputStream input= new ByteArrayInputStream(response.getContentAsByteArray());
        Properties props = new Properties();
        props.load(input);
        System.out.print(props);
        Assert.assertEquals("Method didnt match", ContentType.TEXT_PLAIN.getMimeType(),
                                props.get(RequestHeaderFields.CONTENT_TYPE.getName()).toString().trim());
        Assert.assertEquals("Method didnt match", "POST", props.get("Method").toString().trim());
        Assert.assertEquals("content text didnt match", testText, props.get("content").toString().trim());

    }

}