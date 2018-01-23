package com.roman.recommend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.ReloadFromJDBCDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.roman.recommend.entity.ItemScore;
import com.roman.recommend.entity.UserAction;
import com.roman.recommend.mapper.CommonConfigMapper;
import com.roman.recommend.mapper.ExcludeItemMapper;
import com.roman.recommend.mapper.ImeiMapper;
import com.roman.recommend.mapper.ItemMapper;
import com.roman.recommend.mapper.ItemScoreMapper;
import com.roman.recommend.mapper.SimpleItemMapper;
import com.roman.recommend.mapper.UserActionMapper;
import com.roman.recommend.model.Response;
import com.roman.recommend.model.utils.CommonConstants;
import com.roman.recommend.utils.ItemCfUtil;
import com.roman.recommend.utils.RecommendUtil;
import com.roman.recommend.utils.UserCfUtil;

/**
 * 获取推荐活动service
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/4
 */
@Service
public class RecommendService {

	private static MySQLJDBCDataModel jdbcDataModel;

	private static ReloadFromJDBCDataModel realModel;

	@Value("${spring.datasource.host}")
	private String host;

	@Value("${spring.datasource.dbport}")
	private int dbport;

	@Value("${spring.datasource.username}")
	private String username;

	@Value("${spring.datasource.password}")
	private String password;

	@Value("${spring.datasource.name}")
	private String databasename;

	@Autowired
	private ItemScoreMapper itemScoreMapper;

	@Autowired
	private SimpleItemMapper simpleItemMapper;
	
	@Autowired
	private ItemMapper itemMapper;

	@Autowired
	private ImeiMapper imeiMapper;

	@Autowired
	private ExcludeItemMapper excludeItemMapper;

	@Autowired
	private CommonConfigMapper commonConfigMapper;

	@Autowired
	private UserActionMapper userActionMapper;

	@Autowired
	private RedisService redisService;

	/**
	 * 基于item推荐，通过推荐引擎获取推荐结果，排除已经推荐过的，不够的通过退化搜索补充
	 * 
	 * @param imei
	 * @param userId
	 * @param size
	 * @param exclude
	 * @param cateid 
	 * @return 推荐结果
	 * @throws Exception
	 */
	@Transactional
	public Response<List<ItemScore>> getRecommendByItem(String imei, String userId, int size, String exclude,
			String cateid, Integer isExclude) throws Exception {
		Long imeiId = imeiMapper.select(imei);
		if (imeiId == null) {
			// imeiId为空认为是新用户
			imeiMapper.insert(imei);
			redisService.set(imei + "index", size);
			return new Response<List<ItemScore>>(userActionMapper.topItemScore(0, size));
		} else {
			List<UserAction> userActions = userActionMapper.selectByImei(imei);
			if (CollectionUtils.isEmpty(userActions)) {
				Integer from = 0;
				Object object = redisService.get(imei + "index");
				if (object != null) {
					from = Integer.valueOf(object.toString());
				}
				return new Response<List<ItemScore>>(userActionMapper.topItemScore(from, size));
			}

		}
		List<ItemScore> recommendList = new ArrayList<ItemScore>();
		if (isExclude == null || isExclude.intValue() == 0) {
			excludeItemMapper.deleteByImei(imei);
		}
		// 需要排除的itemId
		List<String> excludeItemIds = getExcludeItemIds(imei, userId, exclude);
		List<ItemScore> originRecommendList = ItemCfUtil.getRecommend(getDataModel(), imeiId);
		filterList(size, recommendList, excludeItemIds, originRecommendList, cateid);
		// if (recommendList.size() < size) {
		// // 通过退化搜索补充结果
		// fillRecommendList(imei, recommendList, size, excludeItemIds);
		// }
		if (!CollectionUtils.isEmpty(recommendList)) {
			for (ItemScore itemScore : recommendList) {
				redisService.lPush(imei, itemScore.getItemId());
				if (StringUtils.isNotBlank(userId)) {
					redisService.lPush(userId, itemScore.getItemId());
				}
			}
		}
		return new Response<List<ItemScore>>(recommendList);
	}

	/**
	 * 基于user推荐，通过推荐引擎获取推荐结果，排除已经推荐过的，不够的通过退化搜索补充
	 * 
	 * @param imei
	 * @param userId
	 * @param size
	 * @param exclude
	 * @param cateid 
	 * @return 推荐结果
	 * @throws Exception
	 */
	@Transactional
	public Response<List<ItemScore>> getRecommendByUser(String imei, String userId, int size, String exclude,
			String cateid, Integer isExclude) throws Exception {
		Long imeiId = imeiMapper.select(imei);
		if (imeiId == null) {
			// imeiId为空认为是新用户
			imeiMapper.insert(imei);
			redisService.set(imei + "index", size);
			return new Response<List<ItemScore>>(userActionMapper.topItemScore(0, size));
		} else {
			List<UserAction> userActions = userActionMapper.selectByImei(imei);
			if (CollectionUtils.isEmpty(userActions)) {
				Integer from = 0;
				Object object = redisService.get(imei + "index");
				if (object != null) {
					from = Integer.valueOf(object.toString());
				}
				redisService.set(imei + "index", from + size);
				return new Response<List<ItemScore>>(userActionMapper.topItemScore(from, size));
			}
		}
		List<ItemScore> recommendList = new ArrayList<ItemScore>();
		if (isExclude == null || isExclude.intValue() == 0) {
			excludeItemMapper.deleteByImei(imei);
		}
		List<String> excludeItemIds = getExcludeItemIds(imei, userId, exclude);
		List<ItemScore> originRecommendList = UserCfUtil.getRecommend(getDataModel(), imeiId);
		filterList(size, recommendList, excludeItemIds, originRecommendList,cateid);
		// if (recommendList.size() < size) {
		// fillRecommendList(imei, recommendList, size, excludeItemIds);
		// }
		if (!CollectionUtils.isEmpty(recommendList)) {
			for (ItemScore itemScore : recommendList) {
				redisService.lPush(imei, itemScore.getItemId());
				if (StringUtils.isNotBlank(userId)) {
					redisService.lPush(userId, itemScore.getItemId());
				}
			}
			// excludeItemMapper.batchInsert(imei, userId, recommendList);
		}
		return new Response<List<ItemScore>>(recommendList);
	}

	private void filterList(int size, List<ItemScore> recommendList, List<String> excludeItemIds,
			List<ItemScore> originRecommendList,String cateid) {
		if (CollectionUtils.isEmpty(excludeItemIds)) {
			int endSize = size;
			if (originRecommendList.size() < size) {
				endSize = originRecommendList.size();
			}
			for (int i = 0; i < endSize; i++) {
				String itemId = originRecommendList.get(i).getItemId();
				if (StringUtils.isNotBlank(cateid)) {
					String cateId = null;
					Object obj = redisService.get(itemId);
					if (obj == null) {
						cateId = itemMapper.selectCateId(itemId);
						redisService.set(itemId, cateId);
					} else {
						cateId = obj.toString();
					}
					if (!cateid.equals(cateId)) {
						continue;
					}
				}
				recommendList.add(originRecommendList.get(i));
				excludeItemIds.add(itemId);
			}
		} else {
			int endSize = 0;
			for (ItemScore itemScore : originRecommendList) {
				if (endSize >= size) {
					break;
				}
				String itemId = itemScore.getItemId();
				if (!excludeItemIds.contains(itemId)) {
					if (StringUtils.isNotBlank(cateid)) {
						String cateId = null;
						Object obj = redisService.get(itemId);
						if (obj == null) {
							cateId = itemMapper.selectCateId(itemId);
							redisService.set(itemId, cateId);
						} else {
							cateId = obj.toString();
						}
						if (!cateid.equals(cateId)) {
							continue;
						}
					}
					recommendList.add(itemScore);
					excludeItemIds.add(itemId);
					endSize++;
				}
			}
		}
	}

	public Response<List<ItemScore>> getRecommendByUser1(String imei, String userId, int size, String exclude) {
		List<ItemScore> itemScores = new ArrayList<ItemScore>();
		// 该用户有过用户行为的所有活动
		List<ItemScore> items = null;
		// 所有活动的总喜好得分
		Double totalScore = null;
		if (StringUtils.isBlank(userId)) {
			items = itemScoreMapper.selectByImei(imei);
			totalScore = itemScoreMapper.sumScoreByImei(imei);
		} else {
			items = itemScoreMapper.selectByUserId(userId);
			totalScore = itemScoreMapper.sumScoreByUserId(userId);
		}
		if (CollectionUtils.isEmpty(items) || totalScore == null) {
			return new Response<List<ItemScore>>(itemScores);
		}
		// 所有活动的平均喜好得分
		double avgScore = totalScore / items.size();
		Map<String, Double> favoriteItemMap = new HashMap<String, Double>();
		List<String> favoriteItemIds = new ArrayList<String>();
		for (ItemScore item : items) {
			// 认为超过平均得分的活动为用户喜爱的活动
			if (item.getScore() >= avgScore) {
				String itemId = item.getItemId();
				if (favoriteItemMap.get(itemId) == null) {
					favoriteItemMap.put(item.getItemId(), item.getScore());
				} else {
					favoriteItemMap.put(item.getItemId(), favoriteItemMap.get(itemId) + item.getScore());
				}
				favoriteItemIds.add(itemId);
			}
		}
		if (!CollectionUtils.isEmpty(favoriteItemIds)) {
			// 查询出所有与该用户有共同爱好的其他用户
			List<ItemScore> otherItemScores = itemScoreMapper.selectInItemIds(favoriteItemIds);
			if (!CollectionUtils.isEmpty(otherItemScores)) {
				// 给其他所有itemScore按照用户分组
				Map<String, Map<String, Double>> otherFavoriteMap = new HashMap<String, Map<String, Double>>();
				for (ItemScore otherItemScore : otherItemScores) {
					String otherImei = otherItemScore.getImei();
					if (imei.equals(otherImei)
							|| StringUtils.isNotBlank(userId) && userId.equals(otherItemScore.getUserId())) {
						continue;
					}
					Map<String, Double> otherFavoriteItemMap = otherFavoriteMap.get(otherImei);
					if (otherFavoriteItemMap == null) {
						otherFavoriteItemMap = new HashMap<String, Double>();
						otherFavoriteMap.put(otherImei, otherFavoriteItemMap);
					}
					otherFavoriteItemMap.put(otherItemScore.getItemId(), otherItemScore.getScore());
				}
				// 其他用户和该用户的相似度
				Map<String, Double> simUserSimMap = new HashMap<String, Double>();
				Map<String, Map<String, Double>> simUserItemMap = new HashMap<String, Map<String, Double>>();
				for (Entry<String, Map<String, Double>> otherFavorite : otherFavoriteMap.entrySet()) {
					String otherImei = otherFavorite.getKey();
					// 其他用户喜欢的其他的活动
					List<ItemScore> otherOtherFavoriteItems = itemScoreMapper.selectNotInItemIds(otherImei,
							favoriteItemIds);
					if (!CollectionUtils.isEmpty(otherOtherFavoriteItems)) {
						// 上面已经过滤了
						// if (!imei.equals(otherImei)) {
						double userSimilar = RecommendUtil.getUserSimilar(favoriteItemMap, otherFavorite.getValue());
						simUserSimMap.put(otherImei, userSimilar);
						// }
						Map<String, Double> simUserItemScoreMap = new HashMap<String, Double>();
						for (ItemScore itemScore : otherOtherFavoriteItems) {
							simUserItemScoreMap.put(itemScore.getItemId(), itemScore.getScore());
						}
						simUserItemMap.put(otherImei, simUserItemScoreMap);
					}
				}
				List<Entry<String, Double>> recommendList = RecommendUtil.getRecommend(simUserItemMap, simUserSimMap);
				int count = 0;
				for (int i = recommendList.size() - 1;; i--) {
					String itemId = recommendList.get(i).getKey();
					if (StringUtils.isBlank(exclude) || !exclude.contains(itemId)) {
						ItemScore itemScore = new ItemScore();
						itemScore.setItemId(itemId);
						itemScore.setScore(recommendList.get(i).getValue());
						itemScores.add(itemScore);
						count++;
					}
					if (i == 0 || count == size) {
						break;
					}
				}
			}
		}
		return new Response<List<ItemScore>>(itemScores);
	}

	private List<String> getExcludeItemIds(String imei, String userId, String exclude) {
		List<String> excludeItemIds = new ArrayList<String>();
		if (StringUtils.isNotBlank(userId)) {
			excludeItemIds = redisService.lRangeAll(userId);
		} else {
			excludeItemIds = redisService.lRangeAll(imei);
		}
		if (StringUtils.isNotBlank(exclude)) {
			String[] excludeIds = exclude.split(",");
			for (String excludeId : excludeIds) {
				excludeItemIds.add(excludeId);
			}
		}
		return excludeItemIds;
	}

	private ReloadFromJDBCDataModel getDataModel() throws TasteException {
		Integer addActionCount = Integer.valueOf(commonConfigMapper.select(CommonConstants.ADD_ACTION_COUNT_COLUMN));
		Integer minusCount = -addActionCount;
		if (jdbcDataModel == null || addActionCount > 100) {// 暂定新增用户行为数量大于100，重新加载用户行为数据
			commonConfigMapper.update(CommonConstants.ADD_ACTION_COUNT_COLUMN, minusCount.toString());
			MysqlDataSource dataSource = new MysqlDataSource();
			dataSource.setServerName(host);
			dataSource.setPort(dbport);
			dataSource.setUser(username);
			dataSource.setPassword(password);
			dataSource.setDatabaseName(databasename);
			jdbcDataModel = new MySQLJDBCDataModel(dataSource, "rm_item_score", "imei_id", "item_id", "score", null);
		}
		if (realModel == null || addActionCount > 100) {
			// 利用ReloadFromJDBCDataModel包裹jdbcDataModel,可以把输入加入内存计算，加快计算速度。
			realModel = new ReloadFromJDBCDataModel(jdbcDataModel);
		}
		return realModel;
	}

	/*
	 * 通过用户行为中最喜爱的分类，退化搜索补充推荐结果
	 */
	private void fillRecommendList(String imei, List<ItemScore> recommendList, int count, List<String> excludeItemIds) {
		// 查询用户最喜爱的3个标签，有的用户可能没有3个
		List<String> favoriteTags = itemScoreMapper.selectTop3TagByImei(imei);
		int start = 0;
		int size = count < 8 ? 8 : count;
		int tagIndex = 0;
		if (!CollectionUtils.isEmpty(favoriteTags) && null != favoriteTags.get(0)) {
			while (recommendList.size() < count) {
				// 根据最喜爱的标签查询活动，按照发布时间倒序排列
				List<String> itemIds = simpleItemMapper.select(favoriteTags.get(tagIndex), start, size);
				if (!CollectionUtils.isEmpty(itemIds)) {
					for (String itemId : itemIds) {
						// 如果该活动没被推荐过，加入推荐列表
						if (!excludeItemIds.contains(itemId)) {
							ItemScore itemScore = new ItemScore();
							itemScore.setItemId(itemId);
							recommendList.add(itemScore);
							// 推荐列表达到数量，直接返回
							if (recommendList.size() == count) {
								return;
							}
						}
					}
				}
				// 本次根据标签查询出来的活动数量不够size数量，说明该标签的活动已经查完了
				if (itemIds.size() < size) {
					// 换下一个用户喜欢的标签（按照用户的喜欢程度向下）
					tagIndex++;
					// 如果用户喜欢的标签都查询完了，退出循环
					if (tagIndex > favoriteTags.size() - 1) {
						break;
					}
				}
				// 查询下一批size的活动
				start += size;
				size = size << 1;
			}
		} else {
			// 用户没有喜欢的标签（冷启动），按照发布时间顺序，查询最新的活动给用户
			List<String> itemIds = simpleItemMapper.selectRecentItem(size);
			if (!CollectionUtils.isEmpty(itemIds)) {
				for (String itemId : itemIds) {
					// 如果该活动没被推荐过，加入推荐列表
					if (!excludeItemIds.contains(itemId)) {
						ItemScore itemScore = new ItemScore();
						itemScore.setItemId(itemId);
						recommendList.add(itemScore);
					}
				}
			}
		}
	}
}
