package com.chaos.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.chaos.Annotation.ServiceMapping;
import com.chaos.Config.configer;
import com.chaos.Config.configerContextHolder;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import com.chaos.SOAService.*;



/*
 * 
 * the tools to get all classes for whole project
 * 

 * 
 * 
 */

public class ClassListUtil {
	
	
	
	
	
	public List<Class<?>> getAllClassess(String Path,String PackageName) throws Exception
	{
        String BasePackge=configerContextHolder.getProp("chaos.FCS.BasePackage");
        System.out.println("Basepackage:"+BasePackge);
		List<Class<?>> FSclasses=getAllFSClassess(Path,PackageName);
		List<Class<?>> Jarclasses=getAllJarClassess(BasePackge);
		
		List<Class<?>> classes=new ArrayList<Class<?>>();
		classes.addAll(FSclasses);
		classes.addAll(Jarclasses);
		return classes;
		
	}

	//only support the save classes in the folder, for it didn't support jar file.
    public  List<Class<?>> getAllFSClassess(String Path,String PackageName) throws Exception
    {
    	
    	if (Path == null || Path.length()==0)
    	{
    		Path=getClass().getResource("/").getFile().toString();
    		PackageName="";
		
    	}
    	//System.out.println("get path:" +Path);
    	
    	ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
    	File directory = new File(Path);
    	if (!directory.exists())  
    	{
    		System.out.println("unknown path:" +directory.getName());
    		return classes;  
    	}
    		
    	File[] files = directory.listFiles();  
    	for (File file : files) {  
           if (file.isDirectory()) classes.addAll(getAllFSClassess(file.getPath(),PackageName+"."+file.getName()));    //for path
           else if (file.getName().endsWith(".class") && file.getName().indexOf("$") == -1) 
           {    
        	   String classname=PackageName +"."+ file.getName().substring(0,file.getName().length() - 6);
        	   if (classname.substring(0,1).equals(".")) classname=classname.substring(1);
        	   //System.out.println("Class Name:"+classname);
        	   classes.add(Class.forName(classname));
            }  
        }   
 return classes;
    
    	
    	
    }
    
    
    public List<Class<?>> getAllJarClassess(String basePack) throws Exception {
    	//String basePack = configerContextHolder.getProp("com.chaos.SOAService");   //only can get the classes list from the jar file
    	ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        //String basePack = "java.io";
        Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(basePack.replace(".", "/"));
        while (urlEnumeration.hasMoreElements()) {
            URL url = urlEnumeration.nextElement();//得到的结果大概是：jar:file:/C:/Users/ibm/.m2/repository/junit/junit/4.12/junit-4.12.jar!/org/junit
            String protocol = url.getProtocol();//大概是jar
            if ("jar".equalsIgnoreCase(protocol)) {
                //转换为JarURLConnection
                JarURLConnection connection = (JarURLConnection) url.openConnection();
                if (connection != null) {
                    JarFile jarFile = connection.getJarFile();
                    if (jarFile != null) {
                        //得到该jar文件下面的类实体
                        Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                        while (jarEntryEnumeration.hasMoreElements()) {
                            /*entry的结果大概是这样：
                                    org/
                                    org/junit/
                                    org/junit/rules/
                                    org/junit/runners/*/
                            JarEntry entry = jarEntryEnumeration.nextElement();
                            String jarEntryName = entry.getName();
                            //这里我们需要过滤不是class文件和不在basePack包名下的类
                            if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/",".").startsWith(basePack)) {
                                String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                //Class cls = Class.forName(className);
                                //System.out.println(cls);
                                System.out.println("jarEntryName:"+jarEntryName);
                                System.out.println("className:"+className);
                                classes.add(Class.forName(className));
                            }
                        }
                    }
                }
            }
        }
        
        return classes;
    }
    
    
    
   	
    
    

	public static void main(String[] args) throws Exception {
		
		//ClassListUtil cl = new ClassListUtil(configer.DefaultHttpIP,configer.DefaultHttpPort);
		//cl.RedisServiceRegister();
		
		/*
		List<Class<?>> lc= cl.getAllClassess("", "");

		 for (Class a :  lc)
		{
        	try 
			{
        	System.out.println("Class Name:"+a.getName());	
			//JSONRPCBaseService instance1 = (JSONRPCBaseService) a.newInstance();    			
			//System.out.println(instance1.run("test message"));
			}
        	catch (Exception ex)
        	{
        		ex.printStackTrace();
        	}
			
		}
		*/
		ClassListUtil cl = new ClassListUtil();
		
		    String BasePackge="com.chaos.SOAService";
	        System.out.println("Basepackage:"+BasePackge);
			//List<Class<?>> Jarclasses=cl.getAllJarClassess(BasePackge);
			//System.out.println(Jarclasses);
			
			InputStream urlEnumeration = Thread.currentThread().getContextClassLoader().getResourceAsStream(BasePackge.replace(".", "/"));
			
			//Thread.currentThread().getContextClassLoader().ge
			
			 System.out.println(urlEnumeration.toString());
			 
	
	}
	

}
