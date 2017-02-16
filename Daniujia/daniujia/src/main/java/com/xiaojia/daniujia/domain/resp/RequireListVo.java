package com.xiaojia.daniujia.domain.resp;

import java.util.List;

/**
 * Created by Administrator on 2017/1/5.
 */
public class RequireListVo {
    private List<RequireListEntity> items;//	array	需求列表
    private int totalCount;//	int	所有数量
    private int itemCount	;//int	当前获取的需求数量
    private int nextCursor	;//int	下页游标, 0表示下页无数据
    private int searchingChanged;//	int	寻找中列表状态存在未被查看需求 , true:显示小红点, false:则不显示
    private int confirmedChanged;//	int	已确定列表状态存在未被查看需求 , true:显示小红点, false:则不显示

    public int getSearchingChanged() {
        return searchingChanged;
    }

    public void setSearchingChanged(int searchingChanged) {
        this.searchingChanged = searchingChanged;
    }

    public int getConfirmedChanged() {
        return confirmedChanged;
    }

    public void setConfirmedChanged(int confirmedChanged) {
        this.confirmedChanged = confirmedChanged;
    }

    public List<RequireListEntity> getItems() {
        return items;
    }

    public void setItems(List<RequireListEntity> items) {
        this.items = items;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(int nextCursor) {
        this.nextCursor = nextCursor;
    }
}
