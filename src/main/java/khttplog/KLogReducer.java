package khttplog;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import com.krishna.hadoop.LogWritable;
import com.krishna.hadoop.TextPair;

public class KLogReducer extends Reducer<LogWritable , LongWritable , TextPair, LongWritable> {
	
	private LongWritable reducerValue =new LongWritable();
	private TextPair textPairKey = new TextPair();
	private MultipleOutputs mouts;

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		mouts = new MultipleOutputs(context);
	}

	@Override
	protected void reduce(LogWritable logKey, Iterable<LongWritable> logValues,
			   Context context) throws IOException, InterruptedException 
	{
		   long sum_of_Values=0L;
		   for(LongWritable logValue:logValues)
		   {
			   sum_of_Values += logValue.get();
			   
		   }
		   
		   textPairKey.set(logKey.getUserIP() ,logKey.getRequest());
		   reducerValue.set(sum_of_Values);
		   
		   mouts.write("requestcount",textPairKey,reducerValue);
		   mouts.write("logger", logKey, NullWritable.get());
		   
		   
	}
	@Override
	public void cleanup(Context context) throws IOException,
			InterruptedException {
		mouts.close();
	}

}
