package com.roman.recommend.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.JDBCDataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;

import com.roman.recommend.entity.ItemScore;

/**
 * 基于物品推荐
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/4
 */
public class ItemCfUtil {

	private static int SIZE = 1000;

	public static List<ItemScore> getRecommend(JDBCDataModel dataModel, long imeiId) throws Exception {
		// 利用ReloadFromJDBCDataModel包裹jdbcDataModel,可以把输入加入内存计算，加快计算速度。
		ReloadFromJDBCDataModel model = new ReloadFromJDBCDataModel(dataModel);

		ItemSimilarity item = new EuclideanDistanceSimilarity(model);

		Recommender r = new GenericItemBasedRecommender(model, item);
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