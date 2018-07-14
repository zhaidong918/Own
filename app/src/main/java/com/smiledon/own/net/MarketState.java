package com.smiledon.own.net;

/**
 * 当前市场状态实体
 *
 * @author zhaidong
 * @date   2018/5/12 11:40
 */

public class MarketState {

    /*
    {"period":86400,"last":"0","open":"0","close":"0","high":"0","low":"0","volume":"0","deal":"0"}
     */

    //市场周期
    private long period;
    //最新价格
    private String last;
    //开盘价
    private String open;
    //收盘价
    private String close;
    //最高价
    private String high;
    //最低价
    private String low;
    //总量（NEWG）
    private String volume;
    //成交易量（BTC）
    private String deal;
    //昨日收盘价
    private String last_close;


    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public String getLast() {
        return last == null ? "" : last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getOpen() {
        return open == null ? "" : open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close == null ? "" : close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getHigh() {
        return high == null ? "" : high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low == null ? "" : low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume == null ? "" : volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDeal() {
        return deal == null ? "" : deal;
    }

    public void setDeal(String deal) {
        this.deal = deal;
    }

    public String getLast_close() {
        return last_close == null ? "" : last_close;
    }

    public void setLast_close(String last_close) {
        this.last_close = last_close;
    }

    @Override
    public String toString() {
        return "MarketState{" +
                "period=" + period +
                ", last='" + last + '\'' +
                ", open='" + open + '\'' +
                ", close='" + close + '\'' +
                ", high='" + high + '\'' +
                ", low='" + low + '\'' +
                ", volume='" + volume + '\'' +
                ", deal='" + deal + '\'' +
                '}';
    }
}
