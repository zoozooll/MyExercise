package com.tcl.manager.arithmetic.opetate;

import java.util.Collection;
import java.util.Map;

import com.tcl.manager.arithmetic.base.IOperate;
import com.tcl.manager.arithmetic.base.IOptimizeArithmectic;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.arithmetic.entity.OptimizeInfo;

/**
 * 优化
 * 
 * @author jiaquan.huang
 */
public class OptimizeArithmetic implements IOperate<Collection<AppScoreInfo>> {
    /** 网络优化项 **/
    Map<String, Boolean> optimizeNetList;
    /** 电量优化项 **/
    Map<String, Boolean> optimizeBatteryList;
    /** 电量默认优化分 **/
    public final static int DEFAULT_EB = 40;
    /** 流量默认优化分 **/
    public final static int DEFAULT_ED = 40;

    /** 初始化优化列表 **/
    public void init(Map<String, Boolean> optimizeNetList, Map<String, Boolean> optimizeBatteryList) {
        this.optimizeNetList = optimizeNetList;
        this.optimizeBatteryList = optimizeBatteryList;
    }

    /** 流量优化操作 */
    public static void optimizeNet(IOptimizeArithmectic optNet, boolean isOpenDataAccess) {
        optNet.setOpenDataAccess(isOpenDataAccess);
        if (!optNet.getIsRecord()) {
            return;
        }
        boolean isTooLow = optNet.getDataED() < DeductionArithmetic.LIMIT ? true : false;
        /** 得分太低 **/
        if (isTooLow) {
            optNet.setOptimizedDataED(DEFAULT_ED);
            optNet.setIsNetRisk(true);
        }
        /** 得分低还把开关打开，则需要优化 **/
        boolean isNeedOptimizedNet = isTooLow && isOpenDataAccess == true ? true : false;
        optNet.setIsNeedOptimizeNet(isNeedOptimizedNet);
    }

    /** 电量优化操作 **/
    public static void optimizeBattery(IOptimizeArithmectic optBattery, boolean isOpenAutoStart) {
        optBattery.setOpenAutoStart(isOpenAutoStart);
        if (!optBattery.getIsRecord()) {
            return;
        }
        boolean isTooLow = optBattery.getBatteryED() < DeductionArithmetic.LIMIT ? true : false;
        /** 得分太低 **/
        if (isTooLow) {
            optBattery.setOptimizedBatteryEB(DEFAULT_EB);
            optBattery.setIsBatteryRisk(true);
        }
        /** 得分低还把开关打开，则需要优化 **/
        boolean isNeedOptimizedBattery = isTooLow && isOpenAutoStart == true ? true : false;
        optBattery.setIsNeedOptimizeBattery(isNeedOptimizedBattery);
    }

    @Override
    public void operate(Collection<AppScoreInfo> appScoreInfos) {
        if (optimizeNetList != null) {
            for (AppScoreInfo appScoreInfo : appScoreInfos) {
                if (optimizeNetList.containsKey(appScoreInfo.pkgName)) {
                    optimizeNet(new OptimizeInfo(appScoreInfo), false);
                }
            }
        }
        if (optimizeBatteryList != null) {
            for (AppScoreInfo appScoreInfo : appScoreInfos) {
                if (optimizeBatteryList.containsKey(appScoreInfo.pkgName)) {
                    optimizeBattery(new OptimizeInfo(appScoreInfo), false);
                }
            }
        }

    }
}
