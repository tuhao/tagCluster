package org.conan.mymahout;

import org.conan.mymahout.cluster08.KmeansHadoop;
import org.conan.mymahout.recommendation.CFGenerate;
import org.conan.mymahout.recommendation.CalculateScore;
import org.conan.mymahout.recommendation.ItemCFHadoop;

public class Main {
	
	public static void main(String[] args) throws Exception{
//		CalculateScore.itemsFile();
		CalculateScore.calculate();
//		ItemCFHadoop.itemCF(args);
//		CFGenerate.generate();
//		KmeansHadoop.kmeans(args);
	}	
}