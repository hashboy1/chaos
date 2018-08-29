package com.chaos.SOA;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.annotation.Resource;

import org.apache.zookeeper.ZooKeeper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.chaos.Config.configer;
import com.chaos.Util.ServiceUtil;



/*
 * 
 * all services will be registered in zookeeper
 * 
 * 
 */
@Component
public class HttpJSONSOAServerSpring {
	
	static ApplicationContext ctx;
	//ZooKeeper zk;
	
    @Autowired
	ServiceUtil cl;
	
	 public HttpJSONSOAServerSpring() {
		 //ctx=new FileSystemXmlApplicationContext("classpath:applicationContext.xml"); 
		 //zk=(ZooKeeper)ctx.getBean("zookeeper"); 
		 //cl=(ServiceUtil) ctx.getBean("ServiceUtil");
		 //System.out.println(cl);
	  }
    
    public void run(final String IP,final int port, final String url)throws Exception{
    	//System.out.println(cl);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ZooKeeper zk =new ZooKeeper(configer.ZooKeeperIp+":"+configer.ZooKeeperPort,2000,null); //the zookeeper connection will be remained until the program closed
        try{
        	
          //register the temporary key in zookeeper, session will still exists until the session closed
            cl.init(IP,port,zk);
            cl.ZooKeeperServiceRegister();
	
    	    
    	    //web service startup
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                    ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                    ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                    ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
                    ch.pipeline().addLast("fileServerHandler", new HttpJSONSOAServerHandler(url,cl));
                }
            });              
            ChannelFuture f = b.bind(IP, port).sync();
            System.out.println("HTTP 文件服务器启动, 地址是： " + "http://"+ IP +":" + port + url);
            f.channel().closeFuture().sync();
            
        }finally{
        	zk.close();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    
    
}

