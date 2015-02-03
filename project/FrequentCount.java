package org.myorg;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class FrequentCount
{
	// Constants
	private final static String HDFS_APP_DIR_NAME = "FrequentCount";
	private final static String HDFS_RUN_DIR_NAME = "run";
	private final static String HDFS_INPUT_DIR_NAME = "input";
	private final static String HDFS_CANDIDATE_DIR_NAME = "candidate";
	private final static String HDFS_OUTPUT_DIR_NAME = "output";
	private final static String CANDIDATES_FILE_NAME = "candidates";
	private final static String RESULT_FILE_NAME = "result";
	
	private final static String ITEM_SEPARATOR = " ";
	private final static String OUTPUT_SEPARATOR = ";";
	
	private final static String THRESHOLD_VARIABLE_NAME = "THRESHOLD";
	
	
	// First Mapper Class
	public static class Mapper1 extends Mapper<LongWritable, Text, Text, NullWritable>
	{
		private Text frequentSet = new Text();
		private double thresholdPer;
		private int numOfBaskets;
		private ArrayList<HashSet<String>> baskets;
		
        @Override
	    protected void setup(Context context) throws IOException, InterruptedException
	    {
			super.setup(context);
			thresholdPer = context.getConfiguration().getDouble(THRESHOLD_VARIABLE_NAME, 0);
			baskets = new ArrayList<HashSet<String>>();
	    }
	    
        @Override
	    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
	    {
			String basket = value.toString().trim();

			if (!basket.isEmpty())
			{		
				numOfBaskets++;
				baskets.add(getItems(basket));
			}
		}
	    
        @Override
	    protected void cleanup(Context context)  throws IOException, InterruptedException
	    {
			// A-prior algorithm to generate frequent sets that are locally frequent in current sub file

	    	int threshold = (int)(Math.ceil(numOfBaskets * thresholdPer));
	    	ArrayList<String> frequents = new ArrayList<String>();
			Map<String, Integer> candidates = new HashMap<String, Integer>();
			int size = 1;
			
			do
			{
				candidates.clear();
				generateCandidates(frequents, candidates);
				countCandidates(candidates, size, baskets);
			
				frequents.clear();
				for (Map.Entry<String, Integer> entry : candidates.entrySet()) 
				{
					if (entry.getValue() >= threshold)
					{	
						String fs = entry.getKey();
						frequents.add(fs);
						
						frequentSet.set(fs);
						context.write(frequentSet, NullWritable.get());
					}
				}
					
				size++;
			} while (frequents.size() > 1);
	    }
	    
		private static void generateCandidates(ArrayList<String> previousFrequents, Map<String, Integer> candidates)
		{	
			if (previousFrequents == null || previousFrequents.isEmpty())
				return;
			
			for(int i = 0; i < previousFrequents.size() - 1; i++)
			{
				for (int j = i + 1; j < previousFrequents.size(); j++)
				{
					String candidate = createNewCandidate(previousFrequents.get(i), previousFrequents.get(j));
					
					// ToDo: check subset frequent
					
					if (candidate != null)
						candidates.put(candidate, 0);
				}
			}
		}
		
		private static String createNewCandidate(String frequent1, String frequent2)
		{
			String prefix1 = getPrefix(frequent1);
			String prefix2 = getPrefix(frequent2);
			
			if (!prefix1.equals(prefix2))
				return null;
			
			String last1 = getLast(frequent1);
			String last2 = getLast(frequent2);
			
			prefix1 = prefix1.isEmpty()? prefix1 : prefix1 + ITEM_SEPARATOR;
			
			if (Integer.parseInt(last1) <= Integer.parseInt(last2))
				return prefix1 + last1 + ITEM_SEPARATOR + last2;
				
			return prefix1 + last2 + ITEM_SEPARATOR + last1;
		}
		
		private static String getPrefix(String set)
		{
			int index = set.lastIndexOf(ITEM_SEPARATOR);
			return index != -1? set.substring(0, index) : "";
		}
		
		private static String getLast(String set)
		{
			return set.substring(set.lastIndexOf(ITEM_SEPARATOR) + 1);
		}
		
		private static void countCandidates(Map<String, Integer> candidates, int size, ArrayList<HashSet<String>> baskets) throws IOException, InterruptedException 
		{
			for(HashSet<String> bk : baskets)
			{
				if (size == 1)
				{
					for (String item : bk)
					{			
						if (candidates.containsKey(item))
						{
							candidates.put(item, candidates.get(item) + 1);
						}
						else
						{
							candidates.put(item, 1);
						}
					}
				}
				else
				{
					for (Map.Entry<String, Integer> entry : candidates.entrySet()) 
					{
						if (contains(bk, entry.getKey()))
						{
							entry.setValue(entry.getValue() + 1);
						}
					}
				}
			}
			
		}
		
		private static HashSet<String> getItems(String basket)
		{
			HashSet<String> result = new HashSet<String>();
			
			String[] items_temp = basket.split(ITEM_SEPARATOR);
			String addedItem = "";
			for (String item : items_temp)
			{
				addedItem = item.trim();
				if (!addedItem.isEmpty())
					result.add(addedItem);
			}
			
			return result;
		}
		
	    private static boolean contains(HashSet<String> basketItems, String candidateSet)
	    {
			List<String> cItems = Arrays.asList(candidateSet.split(ITEM_SEPARATOR));
			return basketItems.containsAll(cItems);
	    }
		
	}

	// First Reducer Class
	public static class Reducer1 extends Reducer<Text, NullWritable, Text, NullWritable>
	{
        @Override
	    public void reduce(Text key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException
	    {
			// output union of the frequents from each sub file
			context.write(key, NullWritable.get());
		}
	}
	
	// Second Mapper Class
	public static class Mapper2 extends Mapper<LongWritable, Text, Text, IntWritable>
	{
		private String candidatesStr = "";
		private Text frequentSet = new Text();
		private ArrayList<HashSet<String>> baskets;
		
        @Override
	    protected void setup(Context context) throws IOException, InterruptedException
	    {
			super.setup(context);
			candidatesStr = FileUtils.readFileToString(new File(CANDIDATES_FILE_NAME));
			baskets = new ArrayList<HashSet<String>>();
	    }
	    
        @Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException 
		{
        	// collect baskets
			String basket = value.toString().trim();
			if (!basket.isEmpty())
			{		
				baskets.add(getItems(basket));
			}
	    }

        @Override
	    protected void cleanup(Context context) throws IOException, InterruptedException
	    {
			// count support of each candidates in current sub file
			ArrayList<FrequentSet> candidates = readCandidates();
			for(HashSet<String> bk : baskets)
			{
				for (FrequentSet set : candidates) 
				{

					if (contains(bk, set.getItems()))
					{
						set.incrementSupport();
					}
				}
			}
			
			// output count result
			for (FrequentSet fs : candidates) 
			{		
				frequentSet.set(fs.getItems());
				context.write(frequentSet, new IntWritable(fs.getSupport()));
			}
	    }
		
	    private ArrayList<FrequentSet> readCandidates() throws IOException
	    {	
			ArrayList<FrequentSet> result = new ArrayList<FrequentSet>();
			BufferedReader br = new BufferedReader(new StringReader(candidatesStr));
			String line = null;
			while ((line = br.readLine()) != null)
			{
				result.add(new FrequentSet(line.trim(), 0));
			}
			br.close();
			
			return result;
	    }
	    
		private static HashSet<String> getItems(String basket)
		{
			HashSet<String> result = new HashSet<String>();
			
			String[] items_temp = basket.split(ITEM_SEPARATOR);
			String addedItem = "";
			for (String item : items_temp)
			{
				addedItem = item.trim();
				if (!addedItem.isEmpty())
					result.add(addedItem);
			}
			
			return result;
		}
	    
	    private static boolean contains(HashSet<String> basketItems, String candidateSet)
		{
			List<String> cItems = Arrays.asList(candidateSet.split(ITEM_SEPARATOR));
			return basketItems.containsAll(cItems);
		}
	}
	
	// Second Reducer Class
	public static class Reducer2 extends Reducer<Text, IntWritable, Text, IntWritable>
	{
	    private IntWritable count = new IntWritable();
		private int threshold;
	    
        @Override
	    protected void setup(Context context) throws IOException, InterruptedException
	    {
	    	threshold = context.getConfiguration().getInt(THRESHOLD_VARIABLE_NAME, 0);
	    }
	    
        @Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
		{
			// sum supports in each sub file and output those that are actually frequent
			int sum = 0;
			for (IntWritable val : values)
			{
				sum += val.get();
			}    
			  
			if (sum >= threshold)
			{
				count.set(sum);
				context.write(key, count);
			}
		}
	}
	

	public static class FrequentSet implements Comparable<FrequentSet>
    {
    	private String items;
    	private Integer support;
     
    	public FrequentSet(String items, Integer support)
    	{
			this.items = items;
			this.support = support;
    	}
     
		public String getItems(){ return this.items; }
		public Integer getSupport(){ return this.support; }
		public void incrementSupport() { this.support++; }

        @Override
        public int compareTo(FrequentSet o) 
        {
			// sort by support, then by item alphabetic
			int cmp = o.getSupport().compareTo(this.getSupport());
			return cmp == 0 ? this.getItems().compareTo(o.getItems()) : cmp;
        }
    }
	
	private static long[] processInput(String inputFile, int k, String hdfsInputDir) throws IOException
	{	
		// count total number of lines
		File input = new File(inputFile);
		long totalNumOfBaskets = 0;
		BufferedReader br = new BufferedReader(new FileReader(input));
		String line = null;
		while((line = br.readLine()) != null)
		{
			if (!line.trim().isEmpty())
				totalNumOfBaskets++;
		}
		br.close();
		
		// get size of sub file in order to split the file
		Long subFileSize = (Long)(input.length() / k);
		
		// copy to hdfs input directory
		FileSystem fs = FileSystem.get(new Configuration());
		fs.copyFromLocalFile(new Path(inputFile), new Path(hdfsInputDir));
		fs.close();
		
		
		System.out.println("Process Input File Finished. Total Number of Baskets=" + totalNumOfBaskets);
		return new long[] {totalNumOfBaskets, subFileSize};
	}
	
	private static void firstMR(String input, String output, double thresholdPer, long subFileSize) throws Exception
	{	
		Configuration conf = new Configuration();
		conf.setDouble(THRESHOLD_VARIABLE_NAME, thresholdPer);
		conf.set("mapreduce.output.textoutputformat.separator", OUTPUT_SEPARATOR);
		Job job = Job.getInstance(conf, "generate_candidates");
		job.setJarByClass(FrequentCount.class);
		job.setMapperClass(Mapper1.class);
		job.setReducerClass(Reducer1.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);
		
		// set the input split size to be the sub file size so that hadoop can split the input into k sub inputs
		FileInputFormat.setMaxInputSplitSize(job, subFileSize);
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		System.out.println("First MapReduce Started. Threshold percentage is " + thresholdPer);
		job.waitForCompletion(true);
		System.out.println("First MapReduce Finished.");
		
		// when there are more than one reducer
		// merge output files into a single file as the candidates
		
		FileSystem hdfs = FileSystem.get(conf);
		FileUtil.copyMerge(hdfs, new Path(output), hdfs, new Path(output + "/" + CANDIDATES_FILE_NAME), false, conf, null); 
	}
	
	private static void secondMR(String candidatesDir, String input, String output, int threshold, long subFileSize) throws Exception
	{
		Configuration conf = new Configuration();
		conf.setInt(THRESHOLD_VARIABLE_NAME, threshold);
		conf.set("mapreduce.output.textoutputformat.separator", OUTPUT_SEPARATOR);
		Job job = Job.getInstance(conf, "count_support");
		
		// add output of first MapReduce as cache
		String canadidates_output = candidatesDir + "/" + CANDIDATES_FILE_NAME +"#" + CANDIDATES_FILE_NAME;
		job.addCacheFile(new URI(canadidates_output));
		
		job.setJarByClass(FrequentCount.class);
		job.setMapperClass(Mapper2.class);
		job.setReducerClass(Reducer2.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
		
		// set the input split size to be the sub file size so that hadoop can split the input into k different sub inputs
		FileInputFormat.setMaxInputSplitSize(job, subFileSize);
		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));
		
		System.out.println("Second MapReduce Started. Threshold is " + threshold);
		job.waitForCompletion(true);
		System.out.println("Second MapReduce Finished.");
		
		// merge output files into a single file as the final result
		FileSystem hdfs = FileSystem.get(conf);
		FileUtil.copyMerge(hdfs, new Path(output), hdfs, new Path(output + "/" + RESULT_FILE_NAME), false, conf, null); 
	}
	
	private static void processOutput(String resultDir) throws IOException
	{
		ArrayList<FrequentSet> result = new ArrayList<FrequentSet>();
		
		// Writer to local result file
		BufferedWriter bw = new BufferedWriter(new FileWriter(RESULT_FILE_NAME));
		
		// Reader to hdfs result file
		Configuration conf = new Configuration();
		String filePath = resultDir + "/" + RESULT_FILE_NAME;
		Path path = new Path(filePath);
		FileSystem hdfs = path.getFileSystem(conf);
		FSDataInputStream inputStream = hdfs.open(path);
		
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
		String line = null;
		while((line = br.readLine()) != null)
		{
			String[] kv = line.split(OUTPUT_SEPARATOR);
			result.add(new FrequentSet(kv[0].trim(), Integer.parseInt(kv[1].trim())));
		}
		
		Collections.sort(result);
		 
		int size = result.size();
		System.out.println(size);
		bw.write(size + "");
		bw.newLine();
		
		String record = "";
		for (FrequentSet kv: result)
		{
			record = String.format("%-20s%-10s\n", kv.getItems(), "(" + kv.getSupport() + ")");
			System.out.println(record);
			bw.write(record);
			bw.newLine();
		}
		System.out.println();
		
		bw.close();
		br.close();
		hdfs.close();
	}
	
	// Main
	public static void main(String[] args) throws Exception
	{
		// create directories to store files during executing
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String homeDir = FileSystem.get(new Configuration()).getHomeDirectory().toString();
		String runDir = homeDir + "/" + HDFS_APP_DIR_NAME + "/" + HDFS_RUN_DIR_NAME + "_" + dateFormat.format(date);
		String inputDir = runDir + "/" + HDFS_INPUT_DIR_NAME;
		String candidatesDir = runDir + "/" + HDFS_CANDIDATE_DIR_NAME;
		String outputDir = runDir + "/" + HDFS_OUTPUT_DIR_NAME;
		
		// execute
		int subFileNum = Integer.parseInt(args[1]);
		long[] inputInfo = processInput(args[0], subFileNum, inputDir);
		
		double threshold = Double.parseDouble(args[2]);
		int support_threshold = (int)Math.ceil((threshold * inputInfo[0]));
		
		firstMR(inputDir, candidatesDir, threshold, inputInfo[1]);										// using threshold percentage
		secondMR(candidatesDir, inputDir, outputDir, support_threshold, inputInfo[1]);					// using support threshold
		processOutput(outputDir);
	}
}