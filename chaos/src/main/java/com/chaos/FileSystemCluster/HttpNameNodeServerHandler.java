package com.chaos.FileSystemCluster;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import javax.activation.MimetypesFileTypeMap;

import com.chaos.Config.configer;
import com.chaos.Util.HttpUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder.ErrorDataDecoderException;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.handler.codec.http.multipart.MemoryFileUpload;
import io.netty.handler.codec.http.multipart.FileUpload; 
import io.netty.util.CharsetUtil;


public class HttpNameNodeServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

    private final String url;
    private final Map<String, Object> handlerMap;
    
    private HttpPostRequestDecoder decoder;  

    public HttpNameNodeServerHandler(String url,Map<String, Object> hmu1) {
        this.url = url;
        this.handlerMap = hmu1;
    }
    
    
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    	 System.out.println("Client Register");
    	super.channelRegistered(ctx);
       
    }
    
    /**
	 * 客户端连接到服务端后进行
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("Client Active");
			
			super.channelActive(ctx);
			System.out.println("ChannelHandlerContext:"+ctx.toString());
			/*
			String string = "to Server";
			ByteBuf buf = Unpooled.buffer(string.length());
			buf.writeBytes(string.getBytes());
			ctx.writeAndFlush(buf);
			*/
	}
    
	

	/**
	 * 客户端读取服务端信息
	 */
	
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
	
			System.out.println("Client Data Send");
			super.channelRead(arg0, arg1);
			/*
			ByteBuf buf = (ByteBuf) arg1;
			byte[] bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
			System.out.println(new String(bytes));
			*/
	}

    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
            FullHttpRequest request) throws Exception {
    	/*
    	System.out.println("-----------------------------------");
    	System.out.println(request.toString());
    	System.out.println("-----------------------------------");
    	*/
    	
    	boolean successflag = false;
    	
    	
        if(!request.decoderResult().isSuccess())
        {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }      
        
        final String uri = request.uri();         
        //System.out.println("request url:" + uri);
    int position = uri.indexOf("index");
        
        if (position >0)
        {
        	welcomeMsg(ctx);
        	return;
        }
        
        if (uri.equals("/favicon.ico")) return; //unuseful message
        
        
        
        
        
        /* parameter parser for normal parameter
        HttpUtil hu= new HttpUtil(request);
        Map<String,String> ParameterInt= hu.parse();
        int i =0;
        for (Map.Entry<String, String> entry : ParameterInt.entrySet()) 
        {   
        	System.out.println("parameter"+i+":"+entry.getKey());
        	System.out.println("parameter"+i+":"+entry.getValue());
        	i++;
        }
        */
        
        
        
        /* url区分的入口在这里 */  
        if (!uri.startsWith("/upload"))  {  
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);  
            return;  
        } else {  
            
        	//analysis the file
            decoder = new HttpPostRequestDecoder(new DefaultHttpDataFactory(false),request);
            List< InterfaceHttpData> datas = decoder.getBodyHttpDatas();
            for (InterfaceHttpData interfaceHttpData : datas) {
            
            	if (interfaceHttpData.getHttpDataType().toString().equals("FileUpload"))   //check whether it can find the file from request
            	{
            		
            	System.out.println("type:"+interfaceHttpData.getHttpDataType());
                System.out.println("data:"+interfaceHttpData.toString());
            	MemoryFileUpload attribute =(MemoryFileUpload) interfaceHttpData;
            	System.out.println("content length:"+attribute.length());
            	ByteBuf filecontent=attribute.getChunk((int) attribute.length()); 
            	byte[] byteArray = new byte[filecontent.capacity()];  
            	filecontent.readBytes(byteArray); 
            	
            	
            	
            	//for Debug
           
                FileOutputStream fos = new FileOutputStream("d:\\log.txt",false);
        		DataOutputStream out=new DataOutputStream(fos);
                out.write(byteArray);
                out.close();
    
                
                //Debug End
                
                //save file to block 
                
                
                successflag =true;
            	}
            	else
            	{
            		System.out.println("unsupport type:"+interfaceHttpData.getHttpDataType());
                	System.out.println("unsupport data:"+interfaceHttpData.toString());
                	successflag=false;   	
                	sendError(ctx, HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE);  
                    return;  
                	
               }

            
           } 
           
        
        }
 

        StringBuilder buf = new StringBuilder();
        
        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
       
        buf.append("Welcome to My Server");
        buf.append("</title></head><body>\r\n");
        
        buf.append("<h3>");
        buf.append("Welcome to My Server");
        buf.append("<BR>");
        if (successflag) buf.append("The file has been uploaded successful");
        else  buf.append("The file has been uploaded with some errors");
        ByteBuf buffer = Unpooled.copiedBuffer(buf,CharsetUtil.UTF_8);    
        buf.append("<BR>");
        buf.append("Any question,please don't hesitate to contact with me.");
        buf.append("</h3>\r\n");
        buf.append("<ul>");
        
        
        
         
        //construct the http header
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        response.content().writeBytes(buffer);   
        buffer.release(); 
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE); 
        

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