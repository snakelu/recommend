package com.roman.recommend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.roman.recommend.entity.ItemScore;
import com.roman.recommend.entity.UserAction;
import com.roman.recommend.mapper.ActionScoreMapper;
import com.roman.recommend.mapper.CommonConfigMapper;
import com.roman.recommend.mapper.ImeiMapper;
import com.roman.recommend.mapper.ItemScoreMapper;
import com.roman.recommend.mapper.UserActionMapper;
import com.roman.recommend.model.Response;
import com.roman.recommend.model.utils.CommonConstants;

/**
 * 用户行为service
 * 
 * @author lyhcc
 * @version 0.0.1 2017/12/3
 */
@Service
public class UserActionService {

	@Autowired
	private UserActionMapper userActionMapper;

	@Autowired
	private ItemScoreMapper itemScoreMapper;

	@Autowired
	private ActionScoreMapper actionScoreMapper;

	@Autowired
	private ImeiMapper imeiMapper;

	@Autowired
	private CommonConfigMapper commonConfigMapper;

	static Map<Short, Double> actionScoreMap = new HashMap<Short, Double>();

	@Transactional
	public Response<Boolean> batchUpload(List<UserAction> userActions) {
		userActionMapper.batchInsert(userActions);
		Integer addActionCount = userActions.size();
		commonConfigMapper.update(CommonConstants.ADD_ACTION_COUNT_COLUMN, addActionCount.toString());
		Map<String, Double> itemScoreMap = new HashMap<String, Double>();
		Map<String, String> userMap = new HashMap<String, String>();
		Set<String> imeiSet = new HashSet<String>();
		for (UserAction userAction : userActions) {
			imeiSet.add(userAction.getImei());
			String key = userAction.getImei() + "," + userAction.getItemId();
			if (itemScoreMap.get(key) == null) {
				itemScoreMap.put(key, getActionScore(userAction.getActionType()));
			} else {
				itemScoreMap.put(key, itemScoreMap.get(key) + getActionScore(userAction.getActionType()));
			}
			if (userAction.getUserId() != null) {
				userMap.put(key, userAction.getUserId());
			}
		}
		for (Entry<String, Double> entry : itemScoreMap.entrySet()) {
			String[] keys = entry.getKey().split(",");
			String userId = userMap.get(entry.getKey());
			ItemScore itemScore = itemScoreMapper.select(keys[0], keys[1]);
			if (itemScore == null) {
				itemScore = new ItemScore();
				itemScore.setImei(keys[0]);
				itemScore.setUserId(userId);
				itemScore.setItemId(keys[1]);
				itemScore.setScore(entry.getValue());
				itemScoreMapper.insert(itemScore);
			} else {
				itemScore.setScore(entry.getValue());
				itemScoreMapper.update(itemScore);
			}
		}
		List<String> imeiList = new ArrayList<String>();
		imeiList.addAll(imeiSet);
		List<String> selectImeis = imeiMapper.selectImeis(imeiList);
		imeiList.removeAll(selectImeis);
		if (!CollectionUtils.isEmpty(imeiList)) {
			imeiMapper.batchInsert(imeiList);
		}
		return new Response<Boolean>(true);
	}

	public Response<Boolean> clearAll() {
		userActionMapper.deleteAll();
		return new Response<Boolean>(true);
	}

	@Transactional
	public Response<Boolean> upload(UserAction userAction) {
		userActionMapper.insert(userAction);
		commonConfigMapper.update(CommonConstants.ADD_ACTION_COUNT_COLUMN, "1");
		String imei = userAction.getImei();
		ItemScore itemScore = itemScoreMapper.select(imei, userAction.getItemId());
		if (itemScore == null) {
			itemScore = new ItemScore();
			itemScore.setImei(imei);
			itemScore.setUserId(userAction.getUserId());
			itemScore.setItemId(userAction.getItemId());
			itemScore.setScore(getActionScore(userAction.getActionType()));
			itemScoreMapper.insert(itemScore);
		} else {
			itemScore.setScore(getActionScore(userAction.getActionType()));
			itemScoreMapper.update(itemScore);
		}
		if (imeiMapper.select(imei) == null) {
			imeiMapper.insert(imei);
		}
		return new Response<Boolean>(true);
	}

	private Double getActionScore(Short actionType) {
		if (actionScoreMap.get(actionType) == null) {
			actionScoreMap.put(actionType, actionScoreMapper.select(actionType));
		}
		return actionScoreMap.get(actionType);
	}
}
