package com.chaos.ClassLoader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;

public class RemoteClassLoader extends ClassLoader {
	
	
   
	public Class<?> loadClassByHTTPFile(String Name)  throws ClassNotFoundException
    {
    	
    	try {
    		String[] result = Name.split("\\.");
    		
    		//for (String a:result) System.out.println(a);
			
    		byte data[]=loadHTTPClassFile(result[result.length-1]);
    		
			//System.out.println(data);
			return super.defineClass(Name,data, 0, data.length);
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	
    	return null;
    	
    }
    
    public static byte[] loadHTTPClassFile(String ClassName) throws Exception
    {
    	
    	//"http://192.168.0.160:8080/examples/JSONRPC.Service.PrintService.class"
    	String[] result=ClassName.split("\\.");
    	String   name  = result[result.length-1];
    	URL url=new URL("http://192.168.0.160:8080/examples/" + name + ".class");
    	URLConnection urlConn = url.openConnection();
    	InputStream input = urlConn.getInputStream();       //input stream
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();  //output stream
        byte[] data = new byte[1024];
        int len = 0 ;
        while ((len = input.read(data)) != -1) bos.write(data,0,len);
    	byte[] returnVal = bos.toByteArray();
    	return returnVal;
    	
    }
    
	
	
	public static void main(String[] args) throws Exception {
		
		


		//call class model from remote URL without any ancestor
		
		
		/*
		RemoteClassLoader rc =new RemoteClassLoader();
		Class<?> cl=rc.loadFile("model");
        Method[] meth =   cl.getMethods();
          for (Method m : meth)
          {
        	  //System.out.println(m);
        	  
        	  if (m.toString().indexOf("print") >-1) m.invoke(cl.newInstance(), null);
        	  
          }
          */
	     
		
		RemoteClassLoader rc =new RemoteClassLoader();
		Class<?> cl=rc.loadClassByHTTPFile("JSONRPC.Service.PrintService");
		
		//call method by ancestor
		/*
		JSONRPCBaseService n1=(JSONRPCBaseService)cl.newInstance();
		System.out.println(n1.run("Parameter1", "Parameter2"));
		*/
		//call method by reflect
		Method[] meth =   cl.getDeclaredMethods();
        for (Method m : meth)
        {
      	  System.out.println("function name:"+m);
      	  System.out.println("function return type:"+m.getReturnType());
      	  
      	  if (m.toString().indexOf("run") >-1)
      	  {
      		  String result1 = (String) m.invoke(cl.newInstance(),"Parameter1", "Parameter2");
      		  System.out.println("function result:"+result1);
      		  
      	  }
        }
		
		
		//System.out.println(new String(RemoteClassLoader.loadHTTPClassFile("PrintService"),"UTF-8"));
		
    	
    	/* call remote class by URLClassLoader, but it is failed solution
		   URL url = new URL("http://192.168.0.160:8080/examples/PrintService.class");
	       URLClassLoader loader = new URLClassLoader (new URL[] {url});
	       Class cl = Class.forName ("JSONRPC.Service.PrintService", true, loader);
	       JSONRPCBaseService foo = (JSONRPCBaseService) cl.newInstance();
	       System.out.println(foo.run("Parameter1", "Parameter2"));
	       loader.close ();
*/
	}

}
