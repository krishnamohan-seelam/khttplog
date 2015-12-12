package khttplog;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

import com.krishna.hadoop.LogWritable;
/*
 * Log comparator class to  compare logs based on user ip , requestedURL,timestamp
 */
public class KLogComparator extends WritableComparator {
	
	protected KLogComparator()
	{
		super(LogWritable.class,true);
	}
	
	@Override
	public int compare(WritableComparable thisWC,WritableComparable otherWC )
	{
		LogWritable thislw = (LogWritable) thisWC;
		LogWritable otherlw = (LogWritable) otherWC;
		   int comparsion = thislw.getUserIP().compareTo(otherlw.getUserIP()) ;
		    
		  
		    	if(comparsion == 0) 
		    	{ 
		    		  comparsion = thislw.getRequest().compareTo(otherlw.getRequest()) ; 
		    		      if(comparsion ==0)
		    		      {
		    		    	  comparsion = thislw.getTimestamp().compareTo(otherlw.getTimestamp()) ; 
		    		      }
		    		     
		    		
		    	}
		    	
		    	return comparsion;
		    	 
		    	
		   
		    
		    	
		   
		    		
	
		
		
	}

}
