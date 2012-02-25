package com.akuroda.mapreduce.montecarlopi;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MonteCarloPI {

	static class MonteCarloPIMapper
		extends Mapper<LongWritable, Text, Text, LongWritable> {

		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {

			double x = Math.random() * 2 - 1;
			double y = Math.random() * 2 - 1;
			
			if (Math.hypot(x, y) <= 1.0) {
				context.write(new Text("1"), key);
			} else {
				context.write(new Text("0"), key);
			}

		}
	}

	static class MonteCarloPIReducer
		extends Reducer<Text, LongWritable, Text, LongWritable> {

		@Override
		protected void reduce(Text key, Iterable<LongWritable> values, Context context)
				throws IOException, InterruptedException {
			
			long counter = 0;
			
			for (LongWritable value : values) {
				counter++;
			}
			context.write(key, new LongWritable(counter));
		}		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) {
			System.err.println("Usage: MonteCarloPI <input path> <output path>");
			System.exit(-1);
		}
		
		Job job = new Job();
		job.setJarByClass(MonteCarloPI.class);
		
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		job.setMapperClass(MonteCarloPIMapper.class);
		job.setReducerClass(MonteCarloPIReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
