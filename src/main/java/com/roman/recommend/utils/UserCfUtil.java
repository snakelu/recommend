package com.roman.recommend.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.roman.recommend.entity.ItemScore;

/**
 * 基于用户推荐
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/4
 */
public class UserCfUtil {

	private static int NEIGHBORHOOD_NUM = 50; // 用户邻居数量

	private static int SIZE = 1000;

	public static List<ItemScore> getRecommend(ReloadFromJDBCDataModel model, long imeiId) throws Exception {
		// 基于用户的协同过滤算法，基于物品的协同过滤算法
		UserSimilarity user = new EuclideanDistanceSimilarity(model); // 计算欧式距离，欧式距离来定义相似性，用s=1/(1+d)来表示，范围在[0,1]之间，值越大，表明d越小，距离越近，则表示相似性越大
		NearestNUserNeighborhood neighbor = new NearestNUserNeighborhood(NEIGHBORHOOD_NUM, user, model);

		// 构建基于用户的推荐系统
		Recommender r = new GenericUserBasedRecommender(model, neighbor, user);
		// 获取推荐结果
		List<RecommendedItem> list = r.recommend(imeiId, SIZE);
		List<ItemScore> resultList = new ArrayList<ItemScore>();
		// 遍历推荐结果
		for (RecommendedItem ritem : list) {
			ItemScore itemScore = new ItemScore();
			itemScore.setItemId(ritem.getItemID() + "");
			itemScore.setScore(ritem.getValue());
			resultList.add(itemScore);
		}
		return resultList;
	}
}