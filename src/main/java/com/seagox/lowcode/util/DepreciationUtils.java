package com.seagox.lowcode.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DepreciationUtils {
	
	/**
	 * 平均年限法
	 * 
	 * @param originalValue 原值
	 * @param usefulLife 使用年限
	 * @param netSalvage 净残值
	 */
	public static BigDecimal averageAgeMethod(BigDecimal originalValue, int usefulLife, BigDecimal netSalvage) {
		// 年折旧额
		BigDecimal yearDepreciationAmount = originalValue.subtract(netSalvage).divide(BigDecimal.valueOf(usefulLife), 2, RoundingMode.HALF_EVEN);
		// 年折旧率
		// BigDecimal yearDepreciationRate = yearDepreciationAmount.divide(originalValue, 2, RoundingMode.HALF_EVEN);
		// 月折旧额
		BigDecimal monthDepreciationAmount = yearDepreciationAmount.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
		// 月折旧率
		// BigDecimal monthDepreciationRate = yearDepreciationRate.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
		return monthDepreciationAmount;
	}
	
	/**
	 * 工作量法
	 * 
	 * @param originalValue 原值
	 * @param totalWorkload 总工作量
	 * @param netSalvage 净残值
	 */
	public static BigDecimal unitsOfProductionMethod(BigDecimal originalValue, BigDecimal totalWorkload, BigDecimal netSalvage) {
		// 单位工作量折旧额‌ 
		BigDecimal unitDepreciationAmount = originalValue.subtract(netSalvage).divide(totalWorkload, 2, RoundingMode.HALF_EVEN);
		return unitDepreciationAmount;
	}
	
	/**
	 * 双倍余额递减法
	 * 
	 * @param originalValue 原值
	 * @param usefulLife 使用年限
	 * @param netSalvage 净残值
	 * @param remainingYear 尚可使用年限
	 */
	public static BigDecimal doubleDecliningBalance(BigDecimal originalValue, int usefulLife, BigDecimal netSalvage, int remainingYear) {
		// 年折旧率
		BigDecimal yearDepreciationRate = BigDecimal.valueOf(2).divide(BigDecimal.valueOf(usefulLife), 2, RoundingMode.HALF_EVEN);
		// 累计折旧额
		BigDecimal cumulativeAmount = BigDecimal.valueOf(0);
		BigDecimal yearDepreciationAmount = BigDecimal.valueOf(0);
		for (int i = usefulLife; i > 0; i--) {
			// 年折旧额
			if(i > 2) {
				yearDepreciationAmount = originalValue.subtract(cumulativeAmount).multiply(yearDepreciationRate);
				cumulativeAmount = cumulativeAmount.add(yearDepreciationAmount);
			} else {
				// 最后两年每年折旧额
				yearDepreciationAmount = originalValue.subtract(cumulativeAmount).subtract(netSalvage).divide(BigDecimal.valueOf(2), 2 , RoundingMode.HALF_EVEN);
			}
			if(i == remainingYear) {
				break;
			}
		}
		// 月折旧额
		BigDecimal monthDepreciationAmount = yearDepreciationAmount.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
		return monthDepreciationAmount;
	}
	
	/**
	 * 年数总和法
	 * 
	 * @param originalValue 原值
	 * @param usefulLife    使用年限
	 * @param netSalvage    净残值
	 * @param remainingYear 尚可使用年限
	 */
	public static BigDecimal syd(BigDecimal originalValue, int usefulLife, BigDecimal netSalvage, int remainingYear) {
		// 年限总和
		int liftTotal = 0;
		for (int i = usefulLife; i > 0; i--) {
			liftTotal += i;
		}
		// 年折旧额
		BigDecimal yearDepreciationAmount = originalValue.subtract(netSalvage).multiply(BigDecimal.valueOf(remainingYear)).divide(BigDecimal.valueOf(liftTotal), 2, RoundingMode.HALF_EVEN);
		// 月折旧额
		BigDecimal monthDepreciationAmount = yearDepreciationAmount.divide(BigDecimal.valueOf(12), 2, RoundingMode.HALF_EVEN);
		return monthDepreciationAmount;
	}
}
