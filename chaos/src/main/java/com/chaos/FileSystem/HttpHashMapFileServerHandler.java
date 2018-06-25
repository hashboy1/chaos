package com.chaos.FileSystem;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.activation.MimetypesFileTypeMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;


public class HttpHashMapFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

    private final String url;
    private final Map<String, Object> handlerMap;
    
    public HttpHashMapFileServerHandler(String url,Map<String, Object> hmu1) {
        this.url = url;
        this.handlerMap = hmu1;
    }
    
    @Override
    protected void messageReceived(ChannelHandlerContext ctx,
            FullHttpRequest request) throws Exception {
    	/*
    	System.out.println("-----------------------------------");
    	System.out.println(request.toString());
    	System.out.println("-----------------------------------");
    	*/
    	
        if(!request.decoderResult().isSuccess())
        {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
        if(request.method() != HttpMethod.GET)
        {
            sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        
        final String uri = request.uri();         //raw value=/files/jixiebi.FBX
        //System.out.println("request url:" + uri);
 
        int position = uri.indexOf("index");
        
        if (position >0)
        {
        	welcomeMsg(ctx);
        	return;
        }
        
        if (uri.equals("/favicon.ico")) return; //unuseful message
        
        //get file from hashMap 
        byte[] writecontent=(byte[]) handlerMap.get(uri);
         
         if (writecontent == null || writecontent.length == 0  )
         {     System.out.println("can't find "+ uri +" from hashmap,it will be redirect to error page");
         	  sendError(ctx, HttpResponseStatus.NOT_FOUND);
         	  return;
         }        
         
         
        //construct the http header
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
        response.content().writeBytes(writecontent);  
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE); 
       
        
        //try to access:http://192.168.0.160:8888/files/jixiebi.FBX
        
       
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        cause.printStackTrace();
        if(ctx.channel().isActive())
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }
      
    private static void sendRedirect(ChannelHandlerContext ctx, String newUri){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);
        response.headers().set(HttpHeaderNames.ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, 
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
    private static void setContentTypeHeader(HttpResponse response){
        MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/octet-stream");
    }
    
    
    private  void welcomeMsg(ChannelHandlerContext ctx){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
      
        StringBuilder buf = new StringBuilder();
        
        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
       
        buf.append("Welcome to My Server");
        buf.append("</title></head><body>\r\n");
        
        buf.append("<h3>");
        buf.append("Welcome to My Server");
        buf.append("<BR>");
        buf.append("Please input your file URL in the address bar or click the file name from the below URLs.");
        buf.append("<BR>");
        buf.append("Any question,please don't hesitate to contact with me.");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        
        
        for (Map.Entry<String, Object> entry : handlerMap.entrySet()) {  
        	   String key = entry.getKey().toString();  
        	   if (key.length()>0)
        	   {
        	   buf.append("<li>Download URL：<a href=\"");
	            buf.append(key);
	            buf.append("\">");
	            buf.append(key);
	            buf.append("</a></li>\r\n");
        	   }
        
        } 


        
        buf.append("</ul></body></html>\r\n");
        
        ByteBuf buffer = Unpooled.copiedBuffer(buf,CharsetUtil.UTF_8);  
        response.content().writeBytes(buffer);  
        buffer.release(); 
        
       //System.out.println(response.toString()); 
       
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE); 
    }
}