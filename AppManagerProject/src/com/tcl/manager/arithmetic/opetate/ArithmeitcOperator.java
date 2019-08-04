package com.tcl.manager.arithmetic.opetate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.tcl.manager.arithmetic.base.IDeductionArithmetic;
import com.tcl.manager.arithmetic.base.IOperate;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.arithmetic.entity.DeductionInfo;
import com.tcl.manager.firewall.FirewallManager;
import com.tcl.manager.optimize.AutoStartBlackList;

public class ArithmeitcOperator implements IOperate<Collection<AppScoreInfo>> {
    int appScore = 0;

    /** 获取总分 **/
    public int getTotalScore() {
        return appScore;
    }

    @Override
    public void operate(Collection<AppScoreInfo> data) {
        oprateAppScore(data);
        oprateTotalScore(data);
    }

    /** 计算单个App得分 **/
    private void oprateAppScore(Collection<AppScoreInfo> data) {
        ValueArithmetic value = new ValueArithmetic();
        ChangeScoreArithmetic change = new ChangeScoreArithmetic();
        value.operate(data);
        change.operate(data);

        /** 电量优化项的 **/
        Map<String, Boolean> batteryMap = AutoStartBlackList.getInstance().get();
        Map<String, Boolean> optimizeBattery = new HashMap<String, Boolean>();
        if (batteryMap != null) {
            for (Entry<String, Boolean> valuse : batteryMap.entrySet()) {
                if (valuse.getValue()) {
                    optimizeBattery.put(valuse.getKey(), true);
                }
            }
        }
        /** 流量优化项 ***/
        Map<String, ?> netMap = (Map<String, ?>) FirewallManager.getSingleInstance().getAllBlockPackages();
        Map<String, Boolean> optimizeNet = new HashMap<String, Boolean>();
        if (netMap != null) {
            for (Entry<String, ?> valuse : netMap.entrySet()) {
                if (Boolean.TRUE.equals(valuse.getValue())) {
                    optimizeNet.put(valuse.getKey(), true);
                }
            }
        }
        /** 优化算法 **/
        OptimizeArithmetic optimize = new OptimizeArithmetic();
        optimize.init(optimizeNet, optimizeBattery);
        optimize.operate(data);

    }

    /** 计算风险项然后扣分，得总分 **/
    private void oprateTotalScore(Collection<AppScoreInfo> data) {
        DeductionArithmetic totalAppScore = new DeductionArithmetic();
        List<IDeductionArithmetic> optimizeInfo = new ArrayList<IDeductionArithmetic>();
        for (AppScoreInfo appInfo : data) {
            DeductionInfo oprimizeInfo = new DeductionInfo(appInfo);
            /** 电量开关打开则需要参加扣分计算，反之说明是关闭的，不需要参加计算 ***/
            if (appInfo.isOpenAutoStart && appInfo.isRecord == true) {
                oprimizeInfo.setNeedOpreateBattery(true);
            } else {
                oprimizeInfo.setNeedOpreateBattery(false);
            }
            /** 流量开关打开则需要参加扣分计算，反之说明是关闭的，不需要参加计算 **/
            if (appInfo.isOpenDataAccess && appInfo.isRecord == true) {
                oprimizeInfo.setNeedOpreateNet(true);
            } else {
                oprimizeInfo.setNeedOpreateNet(false);
            }
            optimizeInfo.add(oprimizeInfo);
        }
        totalAppScore.operate(optimizeInfo);
        appScore = totalAppScore.getScore();
    }
}
