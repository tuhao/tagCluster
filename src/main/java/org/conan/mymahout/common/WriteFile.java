package org.conan.mymahout.common;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class WriteFile {

	public static void writeFile(String filename,String content){
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		PrintWriter pw = null;
		try {
			fos = new FileOutputStream(filename, true);
			osw = new OutputStreamWriter(fos,"utf-8");
			bw = new BufferedWriter(osw);
			pw = new PrintWriter(bw);
			pw.write(content);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			if (pw != null){
				pw.close();
			}
			if (bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (osw != null){
				try {
					osw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fos != null){
				try {
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
