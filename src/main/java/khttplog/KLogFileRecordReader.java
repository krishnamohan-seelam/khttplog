package khttplog;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;


import com.krishna.hadoop.LogWritable;

public class KLogFileRecordReader extends RecordReader<LongWritable, LogWritable> {

	 LineRecordReader lineRecRdr  ; 
	 LogWritable lwValue;
	 
	
	 
	 //public static final String LOG_ENTRY_PATTERN = "^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(.+?)\" (\\d{3}) (\\d+)";
	 
	 public static final String LOG_ENTRY_PATTERN_URL ="^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"[^\\s]+ (.+?) HTTP[^\\s]+\" (\\d{3}) (\\d+)";
	 
	 
	 private static final SimpleDateFormat SDF_ISO_TIME=new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss Z");
	 private String userIP,request;
	 
	 private long timestamp;
	 private int  status,responsebytes;
	 @Override
		public void initialize(InputSplit inpSplit, TaskAttemptContext tskAttmptCntxt) throws IOException, InterruptedException {
			// TODO Auto-generated method stub
		 lineRecRdr = new LineRecordReader();
		 lineRecRdr.initialize(inpSplit, tskAttmptCntxt);
		}
	
	@Override
	public LongWritable getCurrentKey() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return lineRecRdr.getCurrentKey();
	}

	@Override
	public LogWritable getCurrentValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return lwValue;
	}

	@Override
	public float getProgress() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return lineRecRdr.getProgress();
	}

	

	@Override
	public boolean nextKeyValue() throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		if (!lineRecRdr.nextKeyValue())
		{
			return false;
		}
		Pattern p = Pattern.compile(LOG_ENTRY_PATTERN_URL);
		String logEntry_Str =lineRecRdr.getCurrentValue().toString();
		Matcher matcher = p.matcher(logEntry_Str);
		if (!matcher.matches()) {
			System.err.println("Bad Record : "+logEntry_Str);
			
			 return nextKeyValue();
		}
		userIP = matcher.group(1);
		 try {
				timestamp = SDF_ISO_TIME.parse(matcher.group(4)).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				timestamp =0L;
			}
		 request = "https://www.nasa.gov"+ matcher.group(5).trim();
		 status = isNumeric(matcher.group(6)) ?Integer.parseInt(matcher.group(6)):0;
		 responsebytes = isNumeric(matcher.group(7)) ?Integer.parseInt(matcher.group(7)):0;
		 
		 lwValue = new LogWritable(userIP,request,timestamp,status,responsebytes) ;
		return true;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		lineRecRdr.close();
	}

	public  boolean isNumeric(String inputData) {
		NumberFormat formatter = NumberFormat.getInstance();
		ParsePosition pos = new ParsePosition(0);
		formatter.parse(inputData, pos);
		return inputData.length() == pos.getIndex();
	}

}
