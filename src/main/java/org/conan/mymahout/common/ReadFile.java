package org.conan.mymahout.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReadFile {
	
	public static List<String> readByLine(String filename,String charset){
		List<String> result = new LinkedList<String>();
		FileInputStream fis = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		String line = null;
		try {
			fis = new FileInputStream(filename);
			in = new InputStreamReader(fis,charset);
			br = new BufferedReader(in);
			while ((line = br.readLine()) != null){
				result.add(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static Map<String,Integer> readMapIntegerStringProcess(String filename,String charset){
		Map<String,Integer> map = new HashMap<String, Integer>();
		FileInputStream fis = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		String line = null;
		try {
			fis = new FileInputStream(filename);
			in = new InputStreamReader(fis,charset);
			br = new BufferedReader(in);
			while ((line = br.readLine()) != null){
				String[] temp = line.replace("\"","").split(",");
				try{
					String keyword = Simplify.toSimple(temp[0]);
					keyword = keyword.toLowerCase();
					if (map.get(keyword) == null){
						map.put(keyword, Integer.parseInt(temp[1]));
					}else{
						map.put(keyword, Integer.parseInt(temp[1]) + map.get(keyword));
					}
				}catch (NumberFormatException e) {
					// TODO: handle exception
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
	}
	
	
	public static Map<Integer,String> readHashcodeMap(String filename,String charset){
		Map<Integer,String> map = new HashMap<Integer, String>();
		FileInputStream fis = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		String line = null;
		try {
			fis = new FileInputStream(filename);
			in = new InputStreamReader(fis,charset);
			br = new BufferedReader(in);
			while ((line = br.readLine()) != null){
				String[] temp = line.replace("\"","").split(",");
				map.put(temp[0].hashCode(), temp[0]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
		
	}
	
	public static Map<Integer,String> readHashcodeMapProcess(String filename,String charset){
		Map<Integer,String> map = new HashMap<Integer, String>();
		FileInputStream fis = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		String line = null;
		try {
			fis = new FileInputStream(filename);
			in = new InputStreamReader(fis,charset);
			br = new BufferedReader(in);
			while ((line = br.readLine()) != null){
				String[] temp = line.replace("\"","").split(",");
				String keyword = Simplify.toSimple(temp[0]);
				keyword = keyword.toLowerCase();
				map.put(keyword.hashCode(), temp[0]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
		
	}
	
	public static Map<Integer,String> readMapIntegerString(String filename,String charset){
		Map<Integer,String> map = new HashMap<Integer, String>();
		FileInputStream fis = null;
		InputStreamReader in = null;
		BufferedReader br = null;
		String line = null;
		try {
			fis = new FileInputStream(filename);
			in = new InputStreamReader(fis,charset);
			br = new BufferedReader(in);
			while ((line = br.readLine()) != null){
				String[] temp = line.replace("\"","").split(",");
				StringBuffer sb = new StringBuffer();
				for (int i =1;i < temp.length;i++){
					sb.append(temp[i]).append(",");
				}
				try{
					map.put(Integer.parseInt(temp[0]), sb.toString());
				}catch (NumberFormatException e) {
					// TODO: handle exception
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (fis != null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (br != null){
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return map;
		
	}
	
	public static void main(String[] args) {
//		for (String item : readByLine(Constant.USER_TAGS_CSV,"gbk")){
//			String[] line = item.replace("\"","").split(",");
//			System.out.println(line);
//		}
	}

}
