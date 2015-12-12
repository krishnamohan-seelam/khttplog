package khttplog;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import com.krishna.hadoop.LogWritable;

public class KLogInputFormat extends FileInputFormat<LongWritable, LogWritable> {

	@Override
	public RecordReader<LongWritable, LogWritable> createRecordReader(InputSplit inpSpilt, TaskAttemptContext tskAtmptCntxt)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		return new KLogFileRecordReader();
	}

}
