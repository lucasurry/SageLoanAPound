package com.fairsail.loan;

import com.fairsail.utils.GlobalSettings;

public class SecuredLoan extends Loan{	
	// If this is true then work out minimum deposit as a percentage oof the asset value
	// If this is false then use a fixed minimum value
	private boolean minDepositIsPercent = false;
	private double minimumDeposit = 0.0;

	public SecuredLoan() {}

	public boolean isMinDepositIsPercent() {
		return minDepositIsPercent;
	}

	public void setMinDepositIsPercent(boolean minDepositIsPercent) {
		this.minDepositIsPercent = minDepositIsPercent;
	}

	public double getMinimumDeposit() {
		return minimumDeposit;
	}

	public void setMinimumDeposit(double minimumDeposit) {
		if(minDepositIsPercent) {
			this.minimumDeposit = GlobalSettings.formatPercentage(minimumDeposit);
		}else {
			this.minimumDeposit = GlobalSettings.formatValue(minimumDeposit);
		}
	}
	
}
