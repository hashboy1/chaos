package com.chaos.FileSystemCluster;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;  
import io.netty.channel.ChannelFutureListener;  
import io.netty.channel.ChannelInitializer;  
import io.netty.channel.EventLoopGroup;  
import io.netty.channel.nio.NioEventLoopGroup;  
import io.netty.channel.socket.SocketChannel;  
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;

import com.chaos.Config.configer;  
  

  
public class DataNodeClient {  
    private final String host;  
    private final int port;  
  
    public DataNodeClient(String host, int port) {  
        this.host = host;  
        this.port = port;  
    }  
  
    public void start() throws Exception {  
        EventLoopGroup group = new NioEventLoopGroup();  
        try {  
            Bootstrap b = new Bootstrap();  
            b.group(group);  
            b.channel(NioSocketChannel.class);  
            b.remoteAddress(new InetSocketAddress(host, port));  
            b.handler(new ChannelInitializer<SocketChannel>() {  
  
                public void initChannel(SocketChannel ch) throws Exception {  
                    ch.pipeline().addLast(new DataNodeClientHandler());  
                }  
            });  
            ChannelFuture f = b.connect().sync();  
            f.addListener(new ChannelFutureListener() {       
                public void operationComplete(ChannelFuture future) throws Exception {  
                    if(future.isSuccess()){  
                        System.out.println("client connected");  
                    }else{  
                        System.out.println("server attemp failed");  
                        future.cause().printStackTrace();  
                    }            
                }  
            }); 
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   
            String currentTime= df.format(System.currentTimeMillis()); 
              
            //f.channel().write(currentTime);
            f.channel().writeAndFlush(Unpooled.copiedBuffer(currentTime+"&",CharsetUtil.UTF_8));
            f.channel().closeFuture().sync(); 
            f.channel().close();
        } finally {  
            group.shutdownGracefully().sync();  
        }  
    }  
  
    public static void main(String[] args) throws Exception {  
      
        DataNodeClient mc=new DataNodeClient(configer.DefaultHttpIP,configer.FSCDataNodePort );  
        mc.start();
      
    }  
}  