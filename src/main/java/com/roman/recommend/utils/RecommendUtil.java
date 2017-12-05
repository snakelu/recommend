package com.roman.recommend.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RecommendUtil {

	public static void main(String[] args) {
		Map<String, Map<String, Double>> userPerfMap = new HashMap<String, Map<String, Double>>();
		Map<String, Double> pref1 = new HashMap<String, Double>();
		pref1.put("A", 3d);
		pref1.put("B", 4d);
		pref1.put("C", 3d);
		pref1.put("D", 5d);
		pref1.put("E", 1d);
		pref1.put("F", 4d);
		userPerfMap.put("p1", pref1);
		Map<String, Double> pref2 = new HashMap<String, Double>();
		pref2.put("A", 2d);
		pref2.put("B", 4d);
		pref2.put("C", 4d);
		pref2.put("D", 5d);
		pref2.put("E", 3d);
		pref2.put("F", 2d);
		userPerfMap.put("p2", pref2);
		Map<String, Double> pref3 = new HashMap<String, Double>();
		pref3.put("A", 3d);
		pref3.put("B", 5d);
		pref3.put("C", 4d);
		pref3.put("D", 5d);
		pref3.put("E", 2d);
		pref3.put("F", 1d);
		userPerfMap.put("p3", pref3);
		Map<String, Double> pref4 = new HashMap<String, Double>();
		pref4.put("A", 2d);
		pref4.put("B", 2d);
		pref4.put("C", 3d);
		pref4.put("D", 4d);
		pref4.put("E", 3d);
		pref4.put("F", 2d);
		userPerfMap.put("p4", pref4);
		Map<String, Double> pref5 = new HashMap<String, Double>();
		pref5.put("A", 4d);
		pref5.put("B", 4d);
		pref5.put("C", 4d);
		pref5.put("D", 5d);
		pref5.put("E", 1d);
		pref5.put("F", 0d);
		userPerfMap.put("p5", pref5);
		Map<String, Double> simUserSimMap = new HashMap<String, Double>();
		String output1 = "皮尔逊相关系数:";
		for (Entry<String, Map<String, Double>> userPerfEn : userPerfMap.entrySet()) {
			String userName = userPerfEn.getKey();
			if (!"p5".equals(userName)) {
				double sim = getUserSimilar(pref5, userPerfEn.getValue());
				output1 += "p5与" + userName + "之间的相关系数:" + sim + ",";
				simUserSimMap.put(userName, sim);
			}
		}
		System.out.println(output1);
		Map<String, Map<String, Double>> simUserObjMap = new HashMap<String, Map<String, Double>>();
		Map<String, Double> pobjMap1 = new HashMap<String, Double>();
		pobjMap1.put("一夜惊喜", 3d);
		pobjMap1.put("环太平洋", 4d);
		pobjMap1.put("变形金刚", 3d);
		simUserObjMap.put("p1", pobjMap1);
		Map<String, Double> pobjMap2 = new HashMap<String, Double>();
		pobjMap2.put("一夜惊喜", 5d);
		pobjMap2.put("环太平洋", 1d);
		pobjMap2.put("变形金刚", 2d);
		simUserObjMap.put("p2", pobjMap2);
		Map<String, Double> pobjMap3 = new HashMap<String, Double>();
		pobjMap3.put("一夜惊喜", 2d);
		pobjMap3.put("环太平洋", 5d);
		pobjMap3.put("变形金刚", 5d);
		simUserObjMap.put("p3", pobjMap3);
		System.out.println("根据系数推荐:" + getRecommend(simUserObjMap, simUserSimMap));
	}

	/**
	 * 计算两个用户皮尔逊相似度，相关系数的绝对值越大,相关度越大
	 * 
	 * @param pm1
	 *            用户1对item的喜好集合
	 * @param pm2
	 *            用户2对item的喜好集合
	 * @return 相似程度
	 * @author lyhcc
	 * @version 0.0.1 2017/12/3
	 */
	public static double getUserSimilar(Map<String, Double> pm1, Map<String, Double> pm2) {
		int n = 0;// 数量n
		double sxy = 0;// Σxy=x1*y1+x2*y2+....xn*yn
		double sx = 0;// Σx=x1+x2+....xn
		double sy = 0;// Σy=y1+y2+...yn
		double sx2 = 0;// Σx2=(x1)2+(x2)2+....(xn)2
		double sy2 = 0;// Σy2=(y1)2+(y2)2+....(yn)2
		for (Entry<String, Double> pme : pm1.entrySet()) {
			String key = pme.getKey();
			Double x = pme.getValue();
			Double y = pm2.get(key) == null ? 0d : pm2.get(key);
			if (x != null) {
				n++;
				sxy += x * y;
				sx += x;
				sy += y;
				sx2 += Math.pow(x, 2);
				sy2 += Math.pow(y, 2);
			}
		}
		// p=(Σxy-Σx*Σy/n)/Math.sqrt((Σx2-(Σx)2/n)(Σy2-(Σy)2/n));
		double sd = sxy - sx * sy / n;
		double sm = Math.sqrt((sx2 - Math.pow(sx, 2) / n) * (sy2 - Math.pow(sy, 2) / n));
		return Math.abs(sm == 0 ? 1 : sd / sm);
	}

	/**
	 * 根据相关系数得到推荐物品
	 * 
	 * @param simUserObjMap
	 *            相似用户的其他item喜好程度map
	 * @param simUserSimMap
	 *            相似用户的相似度map
	 * @return
	 * @author lyhcc
	 * @version 0.0.1 2017/12/3
	 */
	public static List<Entry<String, Double>> getRecommend(Map<String, Map<String, Double>> simUserObjMap,
			Map<String, Double> simUserSimMap) {
		Map<String, Double> objScoreMap = new HashMap<String, Double>();
		for (Entry<String, Map<String, Double>> simUserEn : simUserObjMap.entrySet()) {
			String user = simUserEn.getKey();
			double sim = simUserSimMap.get(user);
			for (Entry<String, Double> simObjEn : simUserEn.getValue().entrySet()) {
				double objScore = sim * simObjEn.getValue();
				String objName = simObjEn.getKey();
				if (objScoreMap.get(objName) == null) {
					objScoreMap.put(objName, objScore);
				} else {
					double totalScore = objScoreMap.get(objName);
					objScoreMap.put(objName, totalScore + objScore);
				}
			}
		}
		List<Entry<String, Double>> enList = new ArrayList<Entry<String, Double>>(objScoreMap.entrySet());
		Collections.sort(enList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				Double a = o1.getValue() - o2.getValue();
				if (a == 0) {
					return 0;
				} else if (a > 0) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		return enList;
	}
}