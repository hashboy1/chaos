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
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.chaos.Util.ServiceUtil;


/*
 * 
 * all services will be registered in zookeeper
 * 
 * 
 */
@Component
public class HttpJSONSOAServerSpring {

    @Autowired
	private ServiceUtil cl;


	 public HttpJSONSOAServerSpring() {
	  }
    
    public void run(final String IP,final int port, final String url,final ZooKeeper zk)throws Exception{
    	//System.out.println(cl);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
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
                    ch.pipeline().addLast("fileServerHandler", new HttpJSONSOAServerHandlerSpring(url,cl));
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

