package com.chaos.FileSystemCluster;

import java.net.InetAddress;

import org.apache.zookeeper.ZooKeeper;

import com.chaos.Config.configer;
import com.chaos.Util.hashmapUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

class HttpNameNodeServer {
    private static final String DEFAULT_URL = "/src/";

    
    public void runNameService(final String IP,final int port, final String url)throws Exception{
    	
    	
    	/*
    	 * Zookeeper is responsible for file router and maintain the servers available
    	 * Hashmap is responsible for file cache
    	 * 
    	 */
    	
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ZooKeeper zk =new ZooKeeper(configer.ZooKeeperIp+":"+configer.ZooKeeperPort,2000,null); //the zookeeper connection will be remained until the program closed
        hashmapUtil  hmu = new hashmapUtil();  //for local file cache
        try{
     	
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(configer.FSCMaxUploadFileSize));    //request length
                    ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    ch.pipeline().addLast("compressor", new HttpContentCompressor());  
                    ch.pipeline().addLast("fileServerHandler", new HttpNameNodeServerHandler(url,hmu.getHM()));
                }
                
            });

          
            ChannelFuture f = b.bind(IP, port).sync();
            System.out.println("HTTP 文件服务器启动, 地址是： " + "http://"+ IP +":" + port + url);
            f.channel().closeFuture().sync();
            //f.channel().closeFuture().addListener(remover);

            
        }
        catch(Exception ex)
        {
        	ex.printStackTrace();
        }
        finally{
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    
    
    public static void main(String[] args) throws Exception {
    	String IP = configer.DefaultHttpIP;
    	int port = configer.FSCNameNodePort;
        if(args.length > 0)
        {
            try{
            	IP = args[0];
                port = Integer.parseInt(args[1]);
            }catch(NumberFormatException e){
            	IP = configer.DefaultHttpIP;
                port = configer.FSCNameNodePort;
            }
        }
       
        String url = "/index.html";
        
        
        
     
        new HttpNameNodeServer().runNameService(IP,port, url);
    }
}

