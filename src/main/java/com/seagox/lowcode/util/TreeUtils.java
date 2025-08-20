package com.seagox.lowcode.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TreeUtils {
	
	/**
     * 将JSONArray数组转为树状结构
     *
     * @param arr   需要转化的数据
     * @param key   数据唯一的标识键值
     * @param child 子节点键值
     * @return JSONArray
     */
    public static JSONArray listToTree(JSONArray arr, String key, String child) {
        JSONArray r = new JSONArray();
        JSONObject hash = new JSONObject();
        // 将数组转为Object的形式，key为数组中的id
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = (JSONObject) arr.get(i);
            hash.put(json.getString(key), json);
        }
        // 遍历结果集
        for (int j = 0; j < arr.size(); j++) {
            // 单条记录
            JSONObject aVal = (JSONObject) arr.get(j);
            // 在hash中取出key为单条记录中pid的值
            String curCode = aVal.get(key).toString();
            int digit = 2;
            if (curCode.length() >= 9) {
                digit = 3;
            }
            String curKey = curCode.substring(0, curCode.length() - digit);
            JSONObject hashVP = (JSONObject) hash.get(curKey);
            // 如果记录的pid存在，则说明它有父节点，将她添加到孩子节点的集合中
            if (hashVP != null) {
                // 检查是否有child属性
                if (hashVP.get(child) != null) {
                    JSONArray ch = (JSONArray) hashVP.get(child);
                    ch.add(aVal);
                    hashVP.put(child, ch);
                } else {
                    JSONArray ch = new JSONArray();
                    ch.add(aVal);
                    hashVP.put(child, ch);
                }
            } else {
                r.add(aVal);
            }
        }
        return r;
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> categoryTreeHandle(List<Map<String, Object>> treeList,
                                                               String parentNodeName, Long pid) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        // 获取顶级元素集合
        for (Map<String, Object> treeNode : treeList) {
            Long parentId = 0L;
            if (treeNode.get(parentNodeName) != null) {
                parentId = Long.valueOf(treeNode.get(parentNodeName).toString());
            }
            if (parentId.equals(pid)) {
                resultList.add(treeNode);
            }
        }
        // 获取每个顶层元素的子数据集合
        for (Map<String, Object> map : resultList) {
            List<Map<String, Object>> temp = categoryTreeHandle(treeList, parentNodeName,
                    Long.valueOf(map.get("id").toString()));
            if (temp.size() > 0) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) map.get("children");
                if (children != null) {
                    for (Map<String, Object> tempMap : temp) {
                        children.add(tempMap);
                    }
                } else {
                    map.put("children", temp);
                }
            }
        }
        return resultList;
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> categoryTreeHandleString(List<Map<String, Object>> treeList,
                                                                     String parentNodeName, String pid) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        // 获取顶级元素集合
        for (Map<String, Object> treeNode : treeList) {
            String parentId = "0";
            if (treeNode.get(parentNodeName) != null) {
                parentId = treeNode.get(parentNodeName).toString();
            }
            if (parentId.equals(pid)) {
                resultList.add(treeNode);
            }
        }
        // 获取每个顶层元素的子数据集合
        for (Map<String, Object> map : resultList) {
            List<Map<String, Object>> temp = categoryTreeHandleString(treeList, parentNodeName,
                    map.get("id").toString());
            if (temp.size() > 0) {
                List<Map<String, Object>> children = (List<Map<String, Object>>) map.get("children");
                if (children != null) {
                    for (Map<String, Object> tempMap : temp) {
                        children.add(tempMap);
                    }
                } else {
                    map.put("children", temp);
                }
            }
        }
        return resultList;
    }

    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> companyDeptLevel(List<Map<String, Object>> treeList, String parentNodeName,
                                                             Long pid, List<Map<String, Object>> lowerList) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();

        // 获取顶级元素集合
        for (Map<String, Object> treeNode : treeList) {
            Long parentId = 0L;
            if (treeNode.get(parentNodeName) != null) {
                parentId = Long.valueOf(treeNode.get(parentNodeName).toString());
            }
            if (parentId.equals(pid)) {
                resultList.add(treeNode);
            }
            for (int j = 0; j < lowerList.size(); j++) {
                Map<String, Object> itemDept = lowerList.get(j);
                if (treeNode.get("id").toString().equals(itemDept.get("companyId").toString())) {
                    List<Map<String, Object>> childrenList = new ArrayList<>();
                    if (treeNode.containsKey("children")) {
                        childrenList = (List<Map<String, Object>>) treeNode.get("children");
                    }
                    childrenList.add(itemDept);
                    treeNode.put("children", childrenList);
                }
            }
        }

        // 获取每个顶层元素的子数据集合
        for (Map<String, Object> map : resultList) {
            List<Map<String, Object>> temp = categoryTreeHandle(treeList, parentNodeName,
                    Long.valueOf(map.get("id").toString()));
            if (map.containsKey("children")) {
                if (temp.size() > 0) {
                    List<Map<String, Object>> childrenList = (List<Map<String, Object>>) map.get("children");
                    childrenList.addAll(temp);
                    map.put("children", childrenList);
                }
            } else {
                if (temp.size() > 0) {
                    map.put("children", temp);
                }
            }
        }

        return resultList;
    }

    /**
     * 将JSONArray数组转为树状结构
     *
     * @param arr   需要转化的数据
     * @param key   数据唯一的标识键值
     * @param child 子节点键值
     * @return JSONArray
     */
    public static JSONArray listToTreeByRule(JSONArray arr, String key, String child, String rule) {
        String[] ruleArray = rule.split("");
        List<Integer> ruleList = new ArrayList<>();
        JSONObject ruleMap = new JSONObject();
        for (int i = 0; i < ruleArray.length; i++) {
            if (i == 0) {
                ruleList.add(Integer.valueOf(ruleArray[i]));
                ruleMap.put(ruleArray[i], i);
            } else {
                ruleList.add(ruleList.get(i - 1) + Integer.valueOf(ruleArray[i]));
                ruleMap.put(String.valueOf(ruleList.get(i - 1) + Integer.valueOf(ruleArray[i])), i);
            }
        }

        JSONArray r = new JSONArray();
        JSONObject hash = new JSONObject();
        // 将数组转为Object的形式，key为数组中的id
        for (int i = 0; i < arr.size(); i++) {
            JSONObject json = (JSONObject) arr.get(i);
            hash.put(json.getString(key), json);
        }
        // 遍历结果集
        for (int j = 0; j < arr.size(); j++) {
            // 单条记录
            JSONObject aVal = (JSONObject) arr.get(j);
            // 在hash中取出key为单条记录中pid的值
            String curCode = aVal.get(key).toString();

            int index = ruleMap.getIntValue(String.valueOf(curCode.length()));

            String curKey = null;
            if (index != 0) {
                curKey = curCode.substring(0, ruleList.get(index - 1));
            }
            JSONObject hashVP = (JSONObject) hash.get(curKey);
            // 如果记录的pid存在，则说明它有父节点，将她添加到孩子节点的集合中
            if (hashVP != null) {
                // 检查是否有child属性
                if (hashVP.get(child) != null) {
                    JSONArray ch = (JSONArray) hashVP.get(child);
                    ch.add(aVal);
                    hashVP.put(child, ch);
                } else {
                    JSONArray ch = new JSONArray();
                    ch.add(aVal);
                    hashVP.put(child, ch);
                }
            } else {
                r.add(aVal);
            }
        }
        return r;
    }
    
    public static JSONArray jsonArrayToTree(JSONArray data, String key) {
    	JSONArray result = new JSONArray();
    	JSONObject map = new JSONObject();
    	for (int i = 0; i < data.size(); i++) {
    		JSONObject node = data.getJSONObject(i);
    		String nodeId = node.getString(key);
    		map.put(nodeId, node);
    	}
    	for (int i = 0; i < data.size(); i++) {
    		JSONObject node = data.getJSONObject(i);
    		String nodeId = node.getString(key);
    		JSONObject parent = new JSONObject();
    		if(nodeId.contains("_")) {
    			parent = map.getJSONObject(nodeId.substring(0, nodeId.length() - 5));
    		}
            if(!parent.isEmpty()) {
            	JSONArray children = new JSONArray();
            	if (parent.containsKey("children")) {
            		children = parent.getJSONArray("children");
            	}
            	children.add(node);
            	parent.put("children", children);
            } else {
                result.add(node);
            }
    	}
        return result;
    }

}