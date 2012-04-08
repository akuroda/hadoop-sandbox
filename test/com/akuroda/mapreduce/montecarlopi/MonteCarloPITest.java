package com.akuroda.mapreduce.montecarlopi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.apache.hadoop.mrunit.types.Pair;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.akuroda.mapreduce.montecarlopi.MonteCarloPI.MonteCarloPIMapper;
import com.akuroda.mapreduce.montecarlopi.MonteCarloPI.MonteCarloPIReducer;

public class MonteCarloPITest {

	MapDriver<LongWritable, Text, Text, LongWritable> mapDriver;
	ReduceDriver<Text, LongWritable, Text, LongWritable> reduceDriver;

	@Before
	public void setUp() throws Exception {
		MonteCarloPIMapper mapper = new MonteCarloPIMapper();
		MonteCarloPIReducer reducer = new MonteCarloPIReducer();

		mapDriver = new MapDriver<LongWritable, Text, Text, LongWritable>();
		mapDriver.setMapper(mapper);

		reduceDriver = new ReduceDriver<Text, LongWritable, Text, LongWritable>();
		reduceDriver.setReducer(reducer);
	}

	/*
	 * test using assertion method of JUnit
	 */
	@Test
	public void testMapper() throws IOException {
		mapDriver.setInput(new LongWritable(1), new Text("a"));
		List<Pair<Text, LongWritable>> result = mapDriver.run();
		assertThat(result.get(0).getSecond().toString(), anyOf(is("0"), is("1")));
	}

	/*
	 * test using method of MRUnit
	 */
	@Test
	public void testReducer() {
		List<LongWritable> list = new ArrayList<LongWritable>();
		reduceDriver.withInput(new Text("0"), list);
		reduceDriver.withOutput(new Text("0"), new LongWritable(0));
		reduceDriver.runTest();
	}
}
