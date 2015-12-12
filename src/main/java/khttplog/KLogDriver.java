package khttplog;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.krishna.hadoop.LogWritable;
import com.krishna.hadoop.TextPair;



/*
hadoop jar klogmr.jar khttplog.KLogDriver -libjars ${LIBJARS}  hdfs:///user/cloudera/http_log/access_log_Jul95 /user/cloudera/http_log_users

This example  implements  user defined input format, record reader ,multiple outputs
output 01 :userIp <<tab separator>> requestURL <<tab separator>> requestcount
output 02:
CREATE  EXTERNAL TABLE nasa_log ( userip String,requrl String,reqcnt int)
LOCATION '/user/cloudera/nasa/httplog/';

*/




public class KLogDriver extends Configured implements Tool {

	@Override
	public int run(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		if(args.length !=2)
		{
			 System.err.printf("Usage: %s [generic options] <input> <output>\n",
			          getClass().getSimpleName());
			 ToolRunner.printGenericCommandUsage(System.err);
			return -1;
		}
		
		Job ldJob = Job.getInstance(getConf(), "httplog");
		ldJob.setJarByClass(getClass());
		
		ldJob.setInputFormatClass(KLogInputFormat.class);
		FileInputFormat.addInputPath(ldJob, new Path(args[0]));
		FileOutputFormat.setOutputPath(ldJob, new Path(args[1]));
		
		ldJob.setNumReduceTasks(1);
		
		ldJob.setMapperClass(KLogMapper.class);
		ldJob.setMapOutputKeyClass(LogWritable.class);
		ldJob.setMapOutputValueClass(LongWritable.class);
		
		ldJob.setReducerClass(KLogReducer.class);
		ldJob.setOutputKeyClass(TextPair.class);
		ldJob.setOutputValueClass(LongWritable.class);
		
		ldJob.setSortComparatorClass(KLogComparator.class);
		MultipleOutputs.addNamedOutput(ldJob, "requestcount", TextOutputFormat.class,TextPair.class, LongWritable.class);
		MultipleOutputs.addNamedOutput(ldJob, "logger", TextOutputFormat.class,LogWritable.class, NullWritable.class);
		return ldJob.waitForCompletion(true) ?0 :-1;
		
	}

	
	
	public static void main(String args[]) throws Exception
	{
		 int exitCode = ToolRunner.run(new KLogDriver(), args);
		 System.exit(exitCode);
	}
}
