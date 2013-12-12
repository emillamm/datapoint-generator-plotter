import java.io.*;

public class Gendata {

	public static void main(String[] args) {
		/*
		 * Do some error checking on arguments
		 */
		if (args.length != 2){
			throw new IllegalArgumentException("You must supply 2 arguments"); 
		}
		
		String arg0 = args[0];
		String arg1 = args[1];
			
		int m; 
		 if(arg0.equals("minute")){
			 m = 60; 
		 }else if(arg0.equals("hour")){
			 m = 60*60; 
		 }else if(arg0.equals("minute")){
			 m = 60*60*24;
		 }else{
			 throw new IllegalArgumentException("first argument must be either minute, hour or day"); 
		 }
		 
		 int n = 0; 
		 try{
			 n = Integer.parseInt(arg1); 
		 }catch (NumberFormatException e){
			 System.err.println("second argument must be an integer number");
			 e.printStackTrace(); 
		 }
		 if(!(n>0)){
			 throw new IllegalArgumentException("n must be greater than 0");
		 }
		 
		 /*
		  * Generate n datapoints with  'time' and 'value' 
		  * pass as arguments to the Collectdata program
		  */
		 double now = System.currentTimeMillis() / 1000; 
		 Process collecter = null;
		 for(int i=0; i<n; i++){
			 int data = (int) (Math.random()*100.0); 
			 double time_extra = i*m;  
		      try{
		          collecter = Runtime.getRuntime().exec("java Collectdata " + Double.toString(now+time_extra) + " " + Integer.toString(data));
		      }
		      catch(IOException e){
		         System.err.println("Error on executing Collectdata");
		         e.printStackTrace();  
		      }
		 }
	}

}
