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


public class DataNodeServerHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

    
    private final Map<String, Object> handlerMap;
    
    private HttpPostRequestDecoder decoder;  

    public DataNodeServerHandler(Map<String, Object> hmu1) {
        
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
			
	}
    
	

	/**
	 * 客户端读取服务端信息
	 */
	
	@Override
	public void channelRead(ChannelHandlerContext arg0, Object arg1) throws Exception {
	
			System.out.println("Client Data Send");

			ByteBuf buf = (ByteBuf) arg1;
			byte[] bytes = new byte[buf.readableBytes()];
			buf.readBytes(bytes);
			System.out.println("Get Message:"+new String(bytes));
		
	}


	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		// TODO Auto-generated method stub
		
	}

    
}