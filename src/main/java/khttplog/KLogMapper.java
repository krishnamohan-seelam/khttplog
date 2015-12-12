package khttplog;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Mapper;

import com.krishna.hadoop.LogWritable;
import com.krishna.hadoop.TextPair;

public class KLogMapper extends Mapper<LongWritable, LogWritable, LogWritable, LongWritable> {
	
	
	 private LongWritable mapValue = new LongWritable(1L);
	enum RecordTypeCounter{BAD}
	@Override
	protected void map(LongWritable key, LogWritable value, Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		 
		 context.write(value, mapValue);
		
	}
	
	

}
