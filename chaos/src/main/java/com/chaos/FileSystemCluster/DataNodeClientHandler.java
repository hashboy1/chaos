package com.chaos.FileSystemCluster;

import io.netty.buffer.ByteBuf;  
import io.netty.buffer.ByteBufUtil;  
import io.netty.buffer.Unpooled;  
import io.netty.channel.ChannelHandlerContext;  
import io.netty.channel.SimpleChannelInboundHandler;  
import io.netty.channel.ChannelHandler.Sharable;  
import io.netty.util.CharsetUtil;  
  
@Sharable  
public class DataNodeClientHandler extends SimpleChannelInboundHandler<ByteBuf> {  
    /** 
     *此方法会在连接到服务器后被调用  
     * */  
    public void channelActive(ChannelHandlerContext ctx) {  
    	System.out.println("Client Actived");
        //ctx.write(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));  
    	
    	byte[] testing= "testing&".getBytes();
    	
    	
    	
        ctx.writeAndFlush(Unpooled.copiedBuffer(testing));
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!&", CharsetUtil.UTF_8));

    }  
    /** 
     *此方法会在接收到服务器数据后调用  
     * */  
    public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {  
        System.out.println("Client received: " + ByteBufUtil.hexDump(in.readBytes(in.readableBytes())));  
    }  
    /** 
     *捕捉到异常  
     * */  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  
        cause.printStackTrace();  
        ctx.close();  
    }
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		// TODO Auto-generated method stub
		  System.out.println("Client received: " + ByteBufUtil.hexDump(in.readBytes(in.readableBytes())));
		
	}  
  
}  