package com.chaos.SOA;


import java.util.Map;
import javax.activation.MimetypesFileTypeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chaos.Context.SpringContextHolder;
import com.chaos.Util.HttpUtil;
import com.chaos.Util.ServiceUtil;
import com.chaos.Util.testUtil;
import com.google.gson.JsonObject;

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

@Service
public class HttpJSONSOAServerHandlerSpring extends SimpleChannelInboundHandler<FullHttpRequest>{

    private final String url;
    private final String IP;
    private final int port;
    @Autowired
    private ServiceUtil su;    //for services register and list
    private final Logger log = Logger.getLogger(getClass());
    
    
    public HttpJSONSOAServerHandlerSpring(String IP,int port,String url,ServiceUtil su) {
    	this.IP = IP;
        this.port = port;
    	this.url = url;
        this.su = su;
     
        //testUtil hs =(testUtil)SpringContextHolder.getApplicationContext().getBean("testUtil");
        
    }
    
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
            FullHttpRequest request) throws Exception {
    	

    	
    	
        if(!request.decoderResult().isSuccess())
        {
            sendError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }
       
        String uri = request.uri();         
        log.warn("request url:" + uri);
        
        if (uri.length() <= 1)
        {
        	 sendError(ctx, HttpResponseStatus.BAD_REQUEST);
             return;
        }
 
        
        // check index message 
        int position = uri.indexOf("index"); 
        
        if (position >0)
        {
        	welcomeMsg(ctx);
        	return;
        }
      //analysis the HttpMethod 
        if(request.method() != HttpMethod.GET )
        {
     	   
            log.warn("this is the Get Methond");
     	   if (uri.indexOf("?") > -1 ) uri = uri.substring(0, uri.indexOf("?"));  // avoid the parameters on URL
     	  
        }  	   
        else if (request.method() != HttpMethod.POST) 
        {
     	 
     	   log.warn("this is the Post Methond");
     	   if (uri.indexOf("?") > -1 ) uri = uri.substring(0, uri.indexOf("?"));   // avoid the parameters on URL
        }
        else
        {
                 sendError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
                 return;
        }
        //check unuseful message
       if (uri.equals("/favicon.ico")) return; //get the web icon by the browser
       if (uri.indexOf(".") ==-1 )   //this is one call  service name,we must get the detail URL from zookeeper,it will be redirected to new url.
       {
    	   if (uri.substring(0,1).equals("/")) uri=uri.substring(1);  //reduce the "/"
    	   String newurl=null;
    	   newurl = su.ZooKeeperGetServiceURL(uri);
    	   if (newurl == null || newurl.length()==0)  sendError(ctx, HttpResponseStatus.BAD_REQUEST);
    	   else
    		   {	   
    		   //added the parameter into the URL, maybe this is not good solution, i will fix it in the future
    		   HttpUtil hu= new HttpUtil(request);
    		   Map<String,String> ParameterInt= hu.parse();
    	       String parameter ="?";
    	        int i =0;
    	        for (Map.Entry<String, String> entry : ParameterInt.entrySet()) 
    	        {
    	        	parameter = parameter + entry.getKey()+"="+entry.getValue() +"&";	
    	        }		   
    		   sendRedirect(ctx,newurl+parameter);
    		  
    		   }
    	   return; 
       }
      
       
       
       
        /*
         * get class name from URL
         * create one instance by java reflected
         * call the run method by ancestor
         * 
         * 
         */
        
        
        //get parameter for post
        
        HttpUtil hu= new HttpUtil(request);
        Map<String,String> ParameterInt= hu.parse();
        String parameter[] =new String[ParameterInt.size()];
        int i =0;
        for (Map.Entry<String, String> entry : ParameterInt.entrySet()) 
        {  
        	parameter[i] = entry.getValue();
        	log.warn("parameter"+i+":"+parameter[i] );
        	i++;
        	
        }
        
        
     try 
     {
    	
    	//Class Constructed,maybe i need create one class to implement its
        String writecontent = ServiceUtil.callBaseServiceSpring(uri.substring(1), parameter);            
        JsonObject obj = new JsonObject();
        obj.addProperty("result", 0);
        obj.addProperty("content", writecontent);
        ByteBuf buffer = Unpooled.copiedBuffer(obj.toString(),CharsetUtil.UTF_8); 
        //construct the HTTP header
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        response.content().writeBytes(buffer);  
       
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE); 

     }
     catch (ClassNotFoundException ex) 
     {
    	 ex.printStackTrace();
    	 sendError(ctx, HttpResponseStatus.NOT_FOUND);
     }
        
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
    
    
    private  void welcomeMsg(ChannelHandlerContext ctx) throws Exception{
     
      
        StringBuilder buf = new StringBuilder();
        
        buf.append("<!DOCTYPE html>\r\n");
        buf.append("<html><head><title>");
       
        buf.append("Welcome to My SOA Server");
        buf.append("</title></head><body>\r\n");
        
        buf.append("<h3>");
        buf.append("Welcome to My SOA Server");
        buf.append("<BR>");
        buf.append("Please input your Service URL in the address bar or click the Service name from the below URLs.");
        buf.append("<BR>");
        buf.append("Any question,please don't hesitate to contact with me.");
        buf.append("</h3>\r\n");
        buf.append("<ul>");


    Map<String,Object>  keyList= su.ZooKeeperListService();
    
    
    if (keyList == null )
    {
    	buf.append("<h3>");
    	buf.append("System can't find the service from register server.");
    	buf.append("</h3>");
    }
    else
    {	
	    for (Map.Entry<String, Object> entry : keyList.entrySet()) {
		        	if (entry.getKey().length()>0)
			        	{
		        		
		        		        int i =0;
					            buf.append("<li>Service URL：<a href=\"http:\\\\");
					            //buf.append(hosturllist[i]);
					            buf.append(IP+":"+port+"\\"+entry.getKey());
					            buf.append("\">");
					            buf.append(entry.getKey());
					            buf.append("</a></li>\r\n");
					            i++;
		        		     
				        }
	        	} 

    }
        
        buf.append("</ul></body></html>\r\n");
        /*
         * 
         * public DefaultFullHttpResponse(HttpVersion version, HttpResponseStatus status, ByteBuf content) 
           byte[] bytes;
           ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
         * 
         */
        
        
        ByteBuf buffer = Unpooled.copiedBuffer(buf,CharsetUtil.UTF_8);  
        
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html;charset=UTF-8");
        response.headers().set(HttpHeaderNames.ORIGIN,"*");
        response.headers().set(HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        
        response.content().writeBytes(buffer);  
        buffer.release(); 
       
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE); 
    }

}